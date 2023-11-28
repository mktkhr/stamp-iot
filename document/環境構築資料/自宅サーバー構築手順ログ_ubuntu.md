### 自宅サーバー構築手順ログ_ubuntu

---
#### 必要なパッケージのインストール
- `sudo apt-get update`
- vim
  - `sudo apt install vim`
- docker
  - `sudo apt-get install ca-certificates curl gnupg`
  - `sudo install -m 0755 -d /etc/apt/keyrings`
  - `curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg`
  - `sudo chmod a+r /etc/apt/keyrings/docker.gpg`
  - `echo "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null`
  - `sudo apt-get update`
  - `sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin`
  - ``

---
#### サーバーのローカル IP を固定
- `ip a`
  - 自分のネットワークアダプタ名を確認 
    - `enp0s25`
- `cd /etc/netplan/`
- `sudo mv 01-network-manager-all.yaml 01-network-manager-all.yaml.bak`
  - 既存の設定ファイルをバックアップとして作成
- `sudo vim 100-enp0s25-fixed.yaml`
  - **注意** 拡張子が .yml だとネットワークに接続できなくなった
  - 下記を記載
  
    ```yaml
    network:
    version: 2
    renderer: networkd
    ethernets:
      enp0s25:
        dhcp4: false
        addresses: 
          - 192.168.0.100/24 # 任意の値
        nameservers:
          addresses: 
            - 192.168.0.1
        routes:
          - to: default
            via: 192.168.0.1
    ```

- `sudo netplan apply`
- `ip a`
  - 変更を確認

---
#### Github Actions self-hosted runner
- `mkdir runner && cd runner`
- `sudo vim docker-compose.yaml`
  - 下記を記載

    ```yaml
    version: '3.9'

    services:
      runner:
        image: myoung34/github-runner
        container_name: runner
        environment:
          RUNNER_NAME: runner_ubuntu_22.04
          RUNNER_SCOPE: repo
          REPO_URL: https://github.com/mktkhr/stamo-iot
          LABELS: ubuntu
          RUNNER_TOKEN: <runnerのアクセストークン>
        volumes:
          - type: bind
            source: /var/run/docker.sock
            target: /var/run/docker.sock
        restart: always
    ```

- `sudo docker compose up -d`
- `sudo docker ps -a`
  - コンテナが正常に起動していることを確認
- Github の管理画面にてランナーが登録されていることを確認

---
#### ssh
- サーバー側で作業
  - `sudo apt install openssh-server`
  - `sudo vim /etc/ssh/sshd_config`
    - 下記に一時的に
      - `PasswordAuthentication yes`
  - `sudo service sshd restart`
    - sshd を再起動
  - `mkdir ~/.ssh`
    - .ssh ディレクトリがない場合に実行

- クライアント側で作業
  - `ssh-keygen -t rsa`
    - 任意の場所にキーを生成(.ssh配下など)
  - `scp ~/.ssh/<ユーザー名>/id_rsa.pub  <サーバーのユーザー名>@<サーバーのIP>:~/.ssh/`
    - 作成した公開鍵をサーバー側にコピー

- サーバー側で作業
  - `cd ~/.ssh && sudo ls -la`
    - コピーした公開鍵が存在することを確認
  - `cat <公開鍵ファイル名> >> authorized_keys`
  - `rm <公開鍵ファイル名>`
  - `cd ~`
  - `chmod 700 .ssh`
  - `chmod 600 .ssh/authorized_keys`
  - `sudo vim /etc/ssh/sshd_config`
    - 下記に変更
      - `PasswordAuthentication no`
  - `sudo service sshd restart`

- クライアント側で作業
  - `vim ~/.ssh/config`
    - ホストの情報を追記
      - IdentityFile のパス指定を行うこと
- 現状はLAN外から操作することはないので，LAN内での接続までとする

---
#### NAT転送
- [サーバーのローカル IP を固定](#サーバーのローカル-ip-を固定) セクションで設定した固定IPメモする
- ルーターの管理画面にログインする
  - `192.168.0.1`
- NAT転送設定に設定した固定IPを追加する
  - デバイスのIPアドレス
  - 外部ポート
  - 内部ポート

---
#### DDNS
- `sudo vim /home/<任意の場所>/cron_ddns.sh`
  - 下記のshellスクリプトを作成

    ```sh
    #!/bin/bash

    gip=$(curl inet-ip.info)
    domip=$(dig <変更するAレコード> +short)

    if [ "${gip}" != "${domip}" ]; then
      {
        echo "LOGIN"
        echo "USERID:<ユーザーID>"
        echo "PASSWORD:<パスワード>"
        echo "."
        echo "MODIP"
        echo "HOSTNAME:<ホスト名>"
        echo "DOMNAME:<ドメイン名>"
        echo "IPV4:$gip"
        echo "."
        echo "LOGOUT"
        echo "."
      } > input.txt
      openssl s_client -connect ddnsclient.onamae.com:65010 -quiet < input.txt
      wait $!
      echo "$(date) DDNS UPDATED: ${domip} -> ${gip}"
    else
      echo "$(date) DDNS NO UPDATE NECESSARY"
    fi

    rm input.txt

    exit 0
    ```

- 実行結果サンプル

  ```log
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                  Dload  Upload   Total   Spent    Left  Speed
  100    14  100    14    0     0    200      0 --:--:-- --:--:-- --:--:--   202
  depth=2 OU = GlobalSign Root CA - R3, O = GlobalSign, CN = GlobalSign
  verify return:1
  depth=1 C = BE, O = GlobalSign nv-sa, CN = GlobalSign GCC R3 DV TLS CA 2020
  verify return:1
  depth=0 CN = *.onamae.com
  verify return:1
  000 COMMAND SUCCESSFUL
  .
  000 COMMAND SUCCESSFUL
  .
  000 COMMAND SUCCESSFUL
  .
  000 COMMAND SUCCESSFUL
  .
  ```

- cron 実行
  - `sudo vim /etc/cron.d/ddns`
    - 下記を記載
    
      ```
      */5 * * * * root /usr/bin/sh /home/<任意の場所>/cron_ddns.sh >> /home/<>任意の場所>/cron.log
      ```
  - `sudo chmod 600 /home/<任意の場所/cron.log`
    - ログの権限を絞る
  - `sudo chmod 700 /home/<任意の場所>/cron_ddns.sh`
    - スクリプトの権限を絞る

#### ファイアウォール設定
- `sudo ufw status`
  - 設定されているファイアウォールを確認
  - 初期設定の場合は `非アクティブ` が表示されるため，下記を実行
    - `sudo ufw enable`
- 80，443 を開放する
  - `sudo ufw allow 80`
  - `sudo ufw allow 443`
  - `sudo ufw reload`
- 適用を確認する
  - `sudo ufw status`


#### 証明書取得
- [ファイアウォール設定](#ファイアウォール設定)を行った後に実行
- `nginx.conf` の 443 側の設定をコメントアウトし，nginx の設定をリロード
  - pem が存在しないことによるエラー回避のため
- `docker-compose -f /home/ems/deploy/docker/docker-compose-prod.yaml run --rm certbot certonly --webroot -w /usr/share/nginx/dist -d www.ems-engineering.jp`
  - ログの例
  
    <details><summary>実行ログ</summary><div>

      ```
      $ docker-compose -f /home/ems/deploy/docker/docker-compose-prod.yaml run --rm certbot certonly --webroot -w /usr/share/nginx/dist -d www.ems-engineering.jp
      Saving debug log to /var/log/letsencrypt/letsencrypt.log
      Plugins selected: Authenticator webroot, Installer None
      Enter email address (used for urgent renewal and security notices)
      (Enter 'c' to cancel): hogehoge@example.com

      - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      Please read the Terms of Service at
      https://letsencrypt.org/documents/LE-SA-v1.3-September-21-2022.pdf. You must
      agree in order to register with the ACME server at
      https://acme-v02.api.letsencrypt.org/directory
      - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      (A)gree/(C)ancel: A

      - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      Would you be willing, once your first certificate is successfully issued, to
      share your email address with the Electronic Frontier Foundation, a founding
      partner of the Let's Encrypt project and the non-profit organization that
      develops Certbot? We'd like to send you email about our work encrypting the web,
      EFF news, campaigns, and ways to support digital freedom.
      - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
      (Y)es/(N)o: Y
      Obtaining a new certificate
      Performing the following challenges:
      http-01 challenge for www.ems-engineering.jp
      Using the webroot path /usr/share/nginx/dist for all unmatched domains.
      Waiting for verification...
      Cleaning up challenges
      Subscribe to the EFF mailing list (email: hogehoge@example.com).

      IMPORTANT NOTES:
      - Congratulations! Your certificate and chain have been saved at:
        /etc/letsencrypt/live/www.ems-engineering.jp/fullchain.pem
        Your key file has been saved at:
        /etc/letsencrypt/live/www.ems-engineering.jp/privkey.pem
        Your cert will expire on 2023-08-12. To obtain a new or tweaked
        version of this certificate in the future, simply run certbot
        again. To non-interactively renew *all* of your certificates, run
        "certbot renew"
      - Your account credentials have been saved in your Certbot
        configuration directory at /etc/letsencrypt. You should make a
        secure backup of this folder now. This configuration directory will
        also contain certificates and private keys obtained by Certbot so
        making regular backups of this folder is ideal.
      - If you like Certbot, please consider supporting our work by:

        Donating to ISRG / Let's Encrypt:   https://letsencrypt.org/donate
        Donating to EFF:                    https://eff.org/donate-le
      ```
      </div></details>

- `nginx.conf` の 443 側の設定をのコメントアウトを復帰させ，nginx の設定をリロード