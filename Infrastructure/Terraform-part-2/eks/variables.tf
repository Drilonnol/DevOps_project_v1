variable "cluster_name" {
  type = string
}

variable "cluster_version" {
  type    = string
  default = "1.33"
}

variable "backend_iam_policy_arns" {
  description = "Additional IAM policy ARNs to attach to the backend IAM role"
  type        = list(string)
  default     = []
}

variable "enable_pod_identity" {
  description = "Enable EKS pod identity support"
  type        = bool
  default     = false
}

variable "vpc_id" {
  type = string
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "cluster_role_arn" {
  type = string
}

variable "node_role_arn" {
  type = string
}

variable "node_instance_types" {
  type    = list(string)
  default = ["t3.medium"]
}

variable "node_desired_size" {
  type    = number
  default = 2
}

variable "node_max_size" {
  type    = number
  default = 3
}

variable "node_min_size" {
  type    = number
  default = 1
}