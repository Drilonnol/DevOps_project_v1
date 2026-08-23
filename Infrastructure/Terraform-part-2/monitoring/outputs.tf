output "release_name" {
  description = "Helm release name for monitoring"
  value       = try(helm_release.kube_prometheus_stack[0].name, "")
}

output "namespace" {
  description = "Namespace where monitoring is installed"
  value       = try(helm_release.kube_prometheus_stack[0].namespace, "")
}

output "chart_version" {
  description = "Chart version of kube-prometheus-stack"
  value       = try(helm_release.kube_prometheus_stack[0].version, "")
}
