# stamp-iot

---

## システム構成図

  ![システム構成図](document/systemConstitution/system_prod.svg)

---
## コンセプト(未達成も含む)

- マイクロコントローラーおよび各種センサを用いて大気・土壌(主に作土層)環境を可視化
  - マイクロコントローラーはコンセントのない野外でも動作可能なように省電力であること
- マイクロコントローラーを用いてセンサより測定した値はローカル(microSD カード)だけでなく，サーバー上に保存される
- マイクロコントローラーに紐づくユーザーは測定した値をブラウザ上で表示できる(グラフ化，変動予測等)

---
## マイクロコントローラー, 基板概要

- 給電方法は下記の通りである
  - USB Type-C(5V)
  - 2 芯ケーブル(7V ~ 26V)
    - ソーラーパネルを使用する場合は，バッテリーの過放電・過充電を防止するため，チャージャーを介した給電を推奨する
- 対応しているセンサは下記の通りである

  - SDI-12(下記以外のセンサも測定は可能)

    - WD-5 WET([A・R・P](https://www.arp-id.co.jp/))
    - WD-5 WTA([A・R・P](https://www.arp-id.co.jp/))
    - WD-5 WT([A・R・P](https://www.arp-id.co.jp/))
    - Digital TDT Soil Moisture Sensor([Acclima](https://acclima.com/))
    - Digital True TDR-315H Sensor([Acclima](https://acclima.com/))
    - Digital True TDR-315L Sensor([Acclima](https://acclima.com/))
    - TEROS-21(旧 MPS-2, MPS-6)([METER Environment](https://www.metergroup.co.jp/))
    - TEROS-12([METER Environment](https://www.metergroup.co.jp/))
    - TEROS-11([METER Environment](https://www.metergroup.co.jp/))

  - I2C
    - ENV.Ⅱ([M5STACK](https://m5stack.com/))
    - ENV.Ⅲ([M5STACK](https://m5stack.com/))
    - SGP30([M5STACK](https://m5stack.com/))
    - BH1750FVI-TR([M5STACK](https://m5stack.com/))

- 基板概要図
![基板概要図](document/systemConstitution/StampBoard4.PNG)

---

## 動作確認済環境

### OS

- macOS Ventura v.14.2
- Windows10 64bit

### IDE

- VSCode
- IntelliJ IDEA CE
- Arduino IDE v.2.2.1

---

## 環境構築手順

### Docker

1. `stamp-iot/docker` へ移動
2. `docker-compose up -d` を実行
3. `docker ps -a` を実行し，起動済みであることを確認

### フロントエンド

1. `stamp-iot/frontend` へ移動
2. `npm install` を実行
3. `npm run serve` を実行
4. ブラウザで `localhost:8080` へアクセスする

### バックエンド

1. Java21 をインストール ([Oracle](https://www.oracle.com/jp/java/technologies/downloads/#java21), [Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html))
2. `stamp-iot/backend` へ移動
3. IntelliJ で `BackendApplication` を実行

### データベース
1. 初期テーブルはjpaにより自動で生成されるため，作成する必要はなし

### API仕様書の更新
1. バックエンドを起動
2. `localhost:8082/api-docs.yaml` にアクセスし，`api-docs.yaml` をダウンロード
3. `/document/API仕様/api-docs.yaml` を置換
