variable "ecr_repository_name" {
  description = "Name of the ECR repository"
  type        = string
  default     = "my-ecr-repository"
}

variable "nametag" {
  description = "Name tag for resources"
  nullable    = false
}

variable "cluster_name" {
  description = "EKS cluster name, used for tagging and naming resources"
  type        = string
}