resource "helm_release" "kube_prometheus_stack" {
  count            = var.enabled ? 1 : 0
  name             = var.release_name
  repository       = "https://prometheus-community.github.io/helm-charts"
  chart            = "kube-prometheus-stack"
  namespace        = var.namespace
  create_namespace = true
  version          = var.chart_version
  timeout          = 600
  wait             = true
  cleanup_on_fail  = true
  force_update     = true

  values = [
    file(var.values_file_path != "" ? var.values_file_path : "${path.module}/../../K8s/monitoring/values.yaml")
  ]
}

resource "kubectl_manifest" "bookstore_alerts" {
  count      = var.enabled ? 1 : 0
  depends_on = [helm_release.kube_prometheus_stack]

  yaml_body = file("${path.module}/../../K8s/monitoring/alerts/bookstore-alerts.yaml")
}

locals {
  dashboard_json = file("${path.module}/../../K8s/monitoring/dashboards/dashboard-1784827714048.json")
}

resource "kubectl_manifest" "bookstore_dashboard" {
  count      = var.enabled ? 1 : 0
  depends_on = [helm_release.kube_prometheus_stack]

  yaml_body = yamlencode({
    apiVersion = "v1"
    kind       = "ConfigMap"
    metadata = {
      name      = "bookstore-grafana-dashboard"
      namespace = var.namespace
      labels = {
        grafana_dashboard = "1"
      }
    }
    data = {
      "bookstore-dashboard.json" = local.dashboard_json
    }
  })
}