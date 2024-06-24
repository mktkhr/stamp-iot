### eksデプロイ手順(既にクラスターをdestroyしていた場合)
1. `terraform apply`
2. `aws eks update-kubeconfig --name stamp-iot-cluster --region ap-northeast-1`
3. `helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx`
4. `helm upgrade -i ingress-nginx ingress-nginx/ingress-nginx --version 4.10.1 --namespace kube-system`
5. `helm repo add metrics-server https://kubernetes-sigs.github.io/metrics-server/`
6. `helm upgrade --install metrics-server metrics-server/metrics-server`
7. `kubectl -n kube-system rollout status deployment ingress-nginx-controller`
8. `kubectl get deployment -n kube-system ingress-nginx-controller`
9. `secret.yaml` の内，RDSとElasticCacheのエンドポイントをBase64エンコードで更新
10. `kubectl apply -f secret.yaml`
11. `kubectl apply -f ingress/ingress-manifest.yaml`
12. `kubectl apply -f java/java-manifest.yaml`
13. `kubectl apply -f nginx/nginx-manifest.yaml`
14. acmの検証
    1. AWSマネジメントコンソールでterraform側で既に生成している証明書を確認
       1. CNAME名とCNAME値をメモ
    2. お名前ドットコムにログインし，証明書を紐づけたいドメインのDNSレコードに `CNAME` でレコード追加
       1. ホスト名側にACMの `CNAME名からドメイン文字列を抜いた値` を設定し，VALUEにACMの `CNAME値から末尾の.を抜いた値` を設定して更新
    3. 多少時間がかかるが検証が完了して発行済みになる
15. CLBのリスナー更新
    1. CLBの詳細画面に遷移し，`リスナーを管理` ボタンからリスナーを追加する
        *  リスナープロトコル `SSL`，ポート `443`，インスタンスのポート `80`，証明書に先ほど発行した証明書を選択して追加する
          * 既存の `443` のリスナーは削除する
16. CLBのDNSをお名前ドットコム側に設定
    1.  お名前ドットコムにログインし，紐づけたいドメインのDNSレコードに `CNAME` でレコード追加
        1.  ホスト名は任意で，`VALUE` にCLBのDNS名を指定する