### Kubernetesクラスター構築資料
---
### ローカルでの実験
#### 前提
- macOSを使用していること
- colimaを使用していること

#### 準備
- `brew install kubectl`
- `colima start --with-kubernetes`
  - <details><summary>実行時ログ</summary><div>

    ```
    INFO[0000] starting colima                              
    INFO[0000] runtime: docker+k3s                          
    INFO[0000] preparing network ...                         context=vm
    INFO[0000] starting ...                                  context=vm
    INFO[0022] provisioning ...                              context=docker
    INFO[0023] starting ...                                  context=docker
    INFO[0028] provisioning ...                              context=kubernetes
    INFO[0028] downloading and installing ...                context=kubernetes
    INFO[0049] loading oci images ...                        context=kubernetes
    INFO[0053] starting ...                                  context=kubernetes
    INFO[0056] updating config ...                           context=kubernetes
    INFO[0057] Switched to context "colima".                 context=kubernetes
    INFO[0057] done
    ```
- [optional] [kind(Kubernetes IN Docker)](https://kind.sigs.k8s.io/) を使ってローカル環境でKubernetes環境を構築&破棄することができるそうだ

#### Kubernetesのダッシュボードを有効化
- `kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml`
- `kubectl proxy &`
- 任意のディレクトリに下記の `admin-user.yaml` を作成
  ```yaml
  apiVersion: v1
  kind: ServiceAccount
  metadata:
    name: admin-user
    namespace: kubernetes-dashboard
  ---
  apiVersion: rbac.authorization.k8s.io/v1
  kind: ClusterRoleBinding
  metadata:
    name: admin-user
  roleRef:
    apiGroup: rbac.authorization.k8s.io
    kind: ClusterRole
    name: cluster-admin
  subjects:
  - kind: ServiceAccount
    name: admin-user
    namespace: kubernetes-dashboard
  ```
- サービスアカウントを作成
  - `kubectl apply -f admin-user.yaml`
- 認証トークンを取得
  - `kubectl -n kubernetes-dashboard create token admin-user`
  - トークンをコピー
- ブラウザから `http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/` にアクセス
  - 認証方式としてトークンを選択し，コピーしたトークンを貼り付け
  - 閲覧

#### 各種基本的なコマンド
* nodeの確認
  * `kubectl get nodes`
* namespaceの確認
  * `kubectl get namespace`
* deployment の 操作ログ確認
  * `kubectl rollout history deployment hogehoge`
* deployment の 指定の revision の内容確認
  * `kubectl rollout history deployment hodehode --revision=1`
* deployment の rollback
  * `kubectl rollout undo deployment hogehoge`

#### サービスの特徴
* ClusterIP Service
  * デフォルト
  * クラスタ内部IPにServiceを公開する
    * PodからPodへの通信はでき，Service名で名前解決できるが，外部からはアクセスできない
* NodePort Service
  * ClusterIPの特徴に加えて，各nodeからアクセスするためのグローバルなポートを開けている
* LoadBalancer Service
  * クラウドサービスとの連携用
    * あまり使わない？
* ExternalName Service
  * クラスタ内から外部のホストを名前解決するためのエイリアスを作成する

#### Ingress
* ServiceはL4層
  * IngressはL7層
    * イメージはnginx

---
### 実際のサーバーへの適用
#### kubespray でのクラスタ構築実験
* kubespray(on macOS)
  * `curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py`
  * `python get-pip.py`
  * 全サーバーで下記実行
    * `sudo vim /etc/sysctl.conf`
      * net.ipv4.ip_forward=1
    * `sudo sysctl -w net.ipv4.ip_forward=1`
  * `brew install ansible`
  * `git clone https://github.com/kubernetes-sigs/kubespray.git`
  * `cd kubespray`
  * `git checkout release-2.24`
  * `cp -rfp inventory/sample inventory/myCluster`
  * ansible用のyaml生成
    * `python3 -m venv venv`
      * vscode上で作った方が楽
      * kubespray配下の requirements.txt を全て呼んで パッケージをインストールしてくれる
    * `source venv/bin/activate`
    * `pip install -r requirements.txt`
    * `declare -a IPS=(192.168.0.140 192.168.0.111 192.168.0.222)`
    * `CONFIG_FILE=inventory/mycluster/hosts.yaml python3 contrib/inventory_builder/inventory.py ${IPS[@]}`
    * `deactivate`
  * `host.yaml` が生成されるので，適宜変更
    * ansible_ssh_user はデフォルトで root になっているため，必要に応じて変更が必要
    
    ```yaml
    all:
      vars:
        ansible_become: true
        ansible_become_method: sudo
        ansible_become_pass: <パスワード指定>
        ansible_ssh_user: <ユーザー指定>
        ansible_ssh_pass: <パスワード指定>

      hosts:
        node1:
          ansible_host: 192.168.0.140
          ip: 192.168.0.140
          access_ip: 192.168.0.140
        node2:
          ansible_host: 192.168.0.111
          ip: 192.168.0.111
          access_ip: 192.168.0.111
        node3:
          ansible_host: 192.168.0.222
          ip: 192.168.0.222
          access_ip: 192.168.0.222
          
      children:
        kube_control_plane:
          hosts:
            node1:
        kube_node:
          hosts:
            node1:
            node2:
            node3:
        etcd:
          hosts:
            node1:
            node2:
            node3:
        k8s_cluster:
          children:
            kube_control_plane:
            kube_node:
        calico_rr:
          hosts: {}
    ```
  * `kubespray/inventory/myCluster/group_vars/k8s_cluster/k8s-cluster.yml` の設定を変更
  
    ```diff
    ## admin.confを取得
    - kubeconfig_localhost: false
    + kubeconfig_localhost: yes
    ## MetalLB
    - kube_proxy_strict_arp: false
    + kube_proxy_strict_arp: true
    ## ラズパイでのエラー回避
    - enable_nodelocaldns: true
    + enable_nodelocaldns: false
    ```

  * `ansible-playbook --flush-cach -i inventory/myCluster/hosts.yaml -K --become --become-user=root cluster.yml`
    * etcd3 のバックアップにかなり時間がかかったのでやり直した
      * `RUNNING HANDLER [etcd : Backup etcd v3 data]`
    * 発生したエラー(解決済み)
      * worker2つにエラーが発生
      
        ```yaml
        fatal: [worker1]: FAILED! => {"changed": false, "msg": "modprobe: FATAL: Module dummy not found in directory /lib/modules/6.5.0-1005-raspi\n", "name": "dummy", "params": "numdummies=0", "rc": 1, "state": "present", "stderr": "modprobe: FATAL: Module dummy not found in directory /lib/modules/6.5.0-1005-raspi\n", "stderr_lines": ["modprobe: FATAL: Module dummy not found in directory /lib/modules/6.5.0-1005-raspi"], "stdout": "", "stdout_lines": []}
        fatal: [worker2]: FAILED! => {"changed": false, "msg": "modprobe: FATAL: Module dummy not found in directory /lib/modules/5.15.0-1049-raspi\n", "name": "dummy", "params": "numdummies=0", "rc": 1, "state": "present", "stderr": "modprobe: FATAL: Module dummy not found in directory /lib/modules/5.15.0-1049-raspi\n", "stderr_lines": ["modprobe: FATAL: Module dummy not found in directory /lib/modules/5.15.0-1049-raspi"], "stdout": "", "stdout_lines": []}
        ```
        * `kubespray/inventory/myCluster/group_vars/k8s_cluster/k8s-cluster.yml` の設定を `enable_nodelocaldns: false` に変更で解決
    * masterにsshしてclusterが作成されているかを確認(解決済み)
      * `kubectl get nodes`
        * エラー発生
          
          ```
          couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
          The connection to the server localhost:8080 was refused - did you specify the right host or port?
          ```
        * `su root` で実行して解決

    * クラスターリセット方法(ただし，master のネットワークが切れたり副作用があった)
      * `sudo ansible-playbook -i inventory/myCluster/hosts.yml reset.yml -K --become --become-user=root`

#### kubeadmでのクラスタ構築実験

* [自宅サーバー構築手順ログ.md](./自宅サーバー構築手順ログ.md)の手順のうち，必要な手順を実行

* https://kubernetes.io/ja/docs/setup/production-environment/tools/kubeadm/install-kubeadm/
  * まずはControl Plane
    * `sudo apt-get update`
    * `sudo apt-get install -y apt-transport-https ca-certificates curl gpg`
    * `curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg`
    * `echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list`
    * `sudo apt-get update`
    * `sudo apt-get install -y kubelet kubeadm kubectl`
    * `sudo apt-mark hold kubelet kubeadm kubectl`
    * `sudo swapoff -a`
      * swap無効化
    * `sudo vim /etc/fstab`
      * swap 無効の恒久化

        ```diff
        - /swap.img     none    swap    sw      0       0
        + # /swap.img     none    swap    sw      0       0
        ```
    * ipv4forwardingを有効化
    
    ```
    cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
    overlay
    br_netfilter
    EOF

    sudo modprobe overlay
    sudo modprobe br_netfilter

    cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
    net.bridge.bridge-nf-call-iptables  = 1
    net.bridge.bridge-nf-call-ip6tables = 1
    net.ipv4.ip_forward                 = 1
    EOF

    // モジュールの読み込みを確認
    lsmod | grep br_netfilter
    lsmod | grep overlay

    // ip forwardingの設定が効いていることを確認
    sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward
    ```

    * containerdの設定(systemd cgroupドライバー設定)
      * containerdインストール時のほぼ空のconfigが `/etc/containerd/config.toml` に存在するので新たに作成する
      * `sudo containerd config default | sudo tee /etc/containerd/config.toml`
      * `sudo vim /etc/containerd/config.toml`

        ```diff
        [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
        ...
        - SystemdCgroup = false
        + SystemdCgroup = true
        ```
      * `sudo systemctl restart containerd`
    * CNIプラグインのインストール
      * `wget https://github.com/containernetworking/plugins/releases/download/v1.4.1/cni-plugins-linux-amd64-v1.4.1.tgz`
      * `sudo mkdir -p /opt/cni/bin`
      * `sudo tar Cxzvf /opt/cni/bin cni-plugins-linux-amd64-v1.4.1.tgz`
    * kubeadm init のconfig
      * `kubeadm config print init-defaults`
        * 設定例を表示

        ```yaml
        apiVersion: kubeadm.k8s.io/v1beta3
        bootstrapTokens:
        - groups:
          - system:bootstrappers:kubeadm:default-node-token
          token: abcdef.0123456789abcdef
          ttl: 24h0m0s
          usages:
          - signing
          - authentication
        kind: InitConfiguration
        localAPIEndpoint:
          advertiseAddress: 1.2.3.4
          bindPort: 6443
        nodeRegistration:
          criSocket: unix:///var/run/containerd/containerd.sock
          imagePullPolicy: IfNotPresent
          name: node
          taints: null
        ---
        apiServer:
          timeoutForControlPlane: 4m0s
        apiVersion: kubeadm.k8s.io/v1beta3
        certificatesDir: /etc/kubernetes/pki
        clusterName: kubernetes
        controllerManager: {}
        dns: {}
        etcd:
          local:
            dataDir: /var/lib/etcd
        imageRepository: registry.k8s.io
        kind: ClusterConfiguration
        kubernetesVersion: 1.30.0
        networking:
          dnsDomain: cluster.local
          serviceSubnet: 10.96.0.0/12
        scheduler: {}
        ```

        * 今回は以下に設定
        
          ```yaml
          apiVersion: kubeadm.k8s.io/v1beta3
          bootstrapTokens:
          - groups:
            - system:bootstrappers:kubeadm:default-node-token
            # 適宜変更すること
            token: abcdef.0123456789abcdef
            ttl: 24h0m0s
            usages:
            - signing
            - authentication
          kind: InitConfiguration
          localAPIEndpoint:
            advertiseAddress: 192.168.0.140
            bindPort: 6443
          nodeRegistration:
            criSocket: unix:///var/run/containerd/containerd.sock
            imagePullPolicy: IfNotPresent
            name: kube-controlplane-01
            taints: null
            kubeletExtraArgs:
              node-ip: 192.168.0.140
          ---
          apiServer:
            timeoutForControlPlane: 4m0s
          apiVersion: kubeadm.k8s.io/v1beta3
          certificatesDir: /etc/kubernetes/pki
          clusterName: kubernetes
          controllerManager: {}
          dns: {}
          etcd:
            local:
              dataDir: /var/lib/etcd
          imageRepository: registry.k8s.io
          kind: ClusterConfiguration
          kubernetesVersion: 1.30.0
          networking:
            dnsDomain: cluster.local
            serviceSubnet: 10.96.0.0/12
            podSubnet: 192.168.0.50/16
          scheduler: {}
          ```

        * `sudo kubeadm init --config ~/server/stamp-iot/kubeadm/init-config.yaml`
          * tokenとdiscovery-tokenのハッシュ値が発行されるので，メモる
            * `kubeadm token list` でtokenを確認可能
            * **[注意]** tokenはデフォルトで24時間後に無効化されるので，24時間を超えた場合は `kubeadm token create` で再生成する
    * root以外でもkubectlを実行できるように変更
      * `mkdir -p $HOME/.kube`
      * `sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config`
      * `sudo chown $(id -u):$(id -g) $HOME/.kube/config`
    * control-planeが生成されていることを確認する
      * `kubectl get node`
        * 出力例
          ```
          NAME                   STATUS     ROLES           AGE     VERSION
          kube-controlplane-01   NotReady   control-plane   6m45s   v1.30.0 
          ```
      * admin.conf を作業用PCにコピーすれば，configを指定してそちらからも確認可能
        * `kubectl --kubeconfig ./admin.conf get nodes`
  * 次にWorker
* `mkdir ~/kubeadm && cd ~/kubeadm`
* `kubeadm config print join-defaults`
 
  ```yaml
  apiVersion: kubeadm.k8s.io/v1beta3
  caCertPath: /etc/kubernetes/pki/ca.crt
  discovery:
    bootstrapToken:
      apiServerEndpoint: 192.168.0.140:6443
      token: <適宜変更する>
      caCertHashes:
        - sha256:<適宜変更する>
      unsafeSkipCAVerification: true
    timeout: 5m0s
    tlsBootstrapToken: <適宜変更する>
  kind: JoinConfiguration
  nodeRegistration:
    criSocket: unix:///var/run/containerd/containerd.sock
    imagePullPolicy: IfNotPresent
    name: <好きな名前に変更>
    taints: null
    kubeletExtraArgs:
      node-ip: 192.168.0.111
  ```

* `sudo kubeadm join --config ~/kubeadm/join-config.yaml`
* CNIのインストール
  * `kubectl create -f https://raw.githubusercontent.com/projectcalico/calico/v3.27.3/manifests/tigera-operator.yaml`
  * `mkdir -p ~/home/server/stamp-iot/colico && cd ~/home/server/stamp-iot/colico`
  * `wget https://projectcalico.docs.tigera.io/manifests/custom-resources.yaml`
  * `sudo vim custom-resources.yaml`

    ```diff
    - cidr: 192.168.0.0/16
    # kubeadm init で podSubnet として指定した値
    + cidr: 192.168.0.50/16
    ```
  * `kubectl apply -f /home/ems/server/stamp-iot/calico/custom-resources.yaml`
  * `kubectl get pod -n calico-system -o wide`
    * podが作られていることを確認
* サービスアカウントの作成
  * `mkdir -p /home/ems/server/stamp-iot/service-account`
  * `sudo vim /home/ems/server/stamp-iot/service-account/admin-service-account.yaml`

    ```yaml
    apiVersion: v1
    kind: ServiceAccount
    metadata:
      name: ems-admin
      namespace: kube-system
    ---
    apiVersion: rbac.authorization.k8s.io/v1
    kind: ClusterRoleBinding
    metadata:
      name: ems-admin
    roleRef:
      apiGroup: rbac.authorization.k8s.io
      kind: ClusterRole
      name: cluster-admin
    subjects:
    - kind: ServiceAccount
      name: ems-admin
      namespace: kube-system            
    ```

  * `kubectl apply -f /home/ems/server/stamp-iot/service-account/admin-service-account.yaml`
  * `sudo vim /home/ems/server/stamp-iot/service-account/admin-service-account-token.yaml`
  
    ```yaml
    apiVersion: v1
    kind: Secret
    metadata:
      name: admin-service-account-token
      namespace: kube-system
      annotations:
      kubernetes.io/service-account.name: ems-admin
    type: kubernetes.io/service-account-token
    ```

  * `kubectl apply -f /home/ems/server/stamp-iot/service-account/admin-service-account-token.yaml`
  * `kubectl describe secrets admin-service-account-token -n kube-system`
    * tokenを表示
* ダッシュボードを入れてみる
  * `kubectl --kubeconfig ./admin.conf apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml`
  * `kubectl --kubeconfig ./admin.conf proxy`
    * `http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login` でログイン画面表示