variable "enabled" {
  description = "Enable deployment of monitoring stack"
  type        = bool
  default     = true
}

variable "namespace" {
  description = "Kubernetes namespace for monitoring"
  type        = string
  default     = "monitoring"
}

variable "release_name" {
  description = "Helm release name for kube-prometheus-stack"
  type        = string
  default     = "prometheus"
}

variable "chart_version" {
  description = "Helm chart version for kube-prometheus-stack"
  type        = string
  default     = "56.6.0"
}

variable "values_file_path" {
  description = "Path to the custom values.yaml for kube-prometheus-stack"
  type        = string
  default     = ""
}
