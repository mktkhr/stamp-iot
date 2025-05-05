# 環境設定
RELEASE_NAME ?= stamp-iot
CHART_DIR ?= ./helm
NAMESPACE ?= stamp-iot
VALUES_FILE ?= ./helm/values.yaml
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

get-values:
	KUBECONFIG=$(KUBECONFIG) helm get values $(RELEASE_NAME) --namespace $(NAMESPACE) -o yaml

get-pod:
	kubectl --kubeconfig=$(KUBECONFIG) get pods -o wide -n ${NAMESPACE}

get-node:
	kubectl --kubeconfig=$(KUBECONFIG) get nodes -o wide

log:
	kubectl logs ${POD} -n ${NAMESPACE}