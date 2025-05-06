# 環境設定
RELEASE_NAME ?= grafana
CHART_DIR ?= .
NAMESPACE ?= monitoring
VALUES_FILE ?= values.yaml
KUBECONFIG ?= ${HOME}/.kube/config
POD ?= 

.PHONY: all lint template install upgrade uninstall rollback diff status get-values

all: lint template

lint:
	KUBECONFIG=$(KUBECONFIG) helm lint $(CHART_DIR)

template:
	KUBECONFIG=$(KUBECONFIG) helm template $(RELEASE_NAME) $(CHART_DIR) \
		--namespace $(NAMESPACE) \
		--values $(VALUES_FILE)

install:
	KUBECONFIG=$(KUBECONFIG) helm install $(RELEASE_NAME) $(CHART_DIR) \
		--namespace $(NAMESPACE) \
		--create-namespace \
		--values $(VALUES_FILE)

upgrade:
	KUBECONFIG=$(KUBECONFIG) helm upgrade $(RELEASE_NAME) $(CHART_DIR) \
		--namespace $(NAMESPACE) \
		--values $(VALUES_FILE)

upgrade-prom:
	KUBECONFIG=$(KUBECONFIG) helm upgrade kube-prometheus prometheus-community/kube-prometheus-stack \       
		--namespace monitoring \
		--values values.yaml

upgrade-loki:
	KUBECONFIG=$(KUBECONFIG) helm upgrade loki grafana/loki-stack \
		--namespace monitoring \
		--values values.yaml

upgrade-alloy:
	KUBECONFIG=$(KUBECONFIG) helm upgrade alloy grafana/alloy \        
  --namespace monitoring \
  --values values.yaml

uninstall:
	KUBECONFIG=$(KUBECONFIG) helm uninstall $(RELEASE_NAME) --namespace $(NAMESPACE)

rollback:
	KUBECONFIG=$(KUBECONFIG) helm rollback $(RELEASE_NAME)

diff:
	KUBECONFIG=$(KUBECONFIG) helm diff upgrade $(RELEASE_NAME) $(CHART_DIR) \
		--namespace $(NAMESPACE) \
		--values $(VALUES_FILE)

status:
	KUBECONFIG=$(KUBECONFIG) helm status $(RELEASE_NAME) --namespace $(NAMESPACE)

dep-build:
	KUBECONFIG=$(KUBECONFIG) helm dependency build

values:
	KUBECONFIG=$(KUBECONFIG) helm get values $(RELEASE_NAME) --namespace $(NAMESPACE) -o yaml

pod:
	kubectl --kubeconfig=$(KUBECONFIG) get pods -o wide -n ${NAMESPACE}

node:
	kubectl --kubeconfig=$(KUBECONFIG) get nodes -o wide

log:
	kubectl logs ${POD} -n ${NAMESPACE}

gen-dashboard:
	kubectl create configmap stamp-iot-dashboard --from-file=./provisioning/dashboard.json --namespace monitoring --dry-run=client -o yaml > ./templates/config-map-dashboard.yaml