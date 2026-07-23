variable "repository_name" {
  description = "Name of the ECR repository"
  type        = string
  default     = ""
}

variable "nametag" {
  description = "Name tag for resources"
  type        = string
}

variable "cluster_name" {
  description = "EKS cluster name, used for tagging and naming resources"
  type        = string
}