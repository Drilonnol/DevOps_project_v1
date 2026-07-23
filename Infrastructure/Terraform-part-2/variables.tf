variable "region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "EKS cluster name, used for tagging and subnet discovery"
  type        = string
}

variable "cluster_version" {
  description = "Kubernetes version for the EKS cluster"
  type        = string
  default     = "1.31"
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

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "azs" {
  description = "Availability zones to use"
  type        = list(string)
}

variable "public_subnet_cidrs" {
  description = "CIDR blocks for public subnets"
  type        = list(string)
}

variable "private_subnet_cidrs" {
  description = "CIDR blocks for private subnets"
  type        = list(string)
}

variable "nametag" {
  description = "Name tag for resources"
  type        = string
  nullable    = false
}

variable "eks_admin_principal_arns" {
  description = "List of IAM user or role ARNs that will have admin access to the EKS cluster"
  type        = list(string)
  default     = []
}

variable "node_instance_types" {
  description = "List of EC2 instance types for the EKS worker nodes"
  type        = list(string)
  default     = ["t3.medium"]
}

variable "node_desired_size" {
  description = "Desired number of worker nodes"
  type        = number
  default     = 2
}

variable "node_max_size" {
  description = "Maximum number of worker nodes"
  type        = number
  default     = 3
}

variable "node_min_size" {
  description = "Minimum number of worker nodes"
  type        = number
  default     = 1
}

variable "db_name" {
  description = "Database name for PostgreSQL RDS"
  type        = string
  default     = "bookstore"
}

variable "db_username" {
  description = "Database master username"
  type        = string
  default     = "postgres"
}

variable "db_password" {
  description = "Database master password"
  type        = string
  sensitive   = true
  default     = "changemeinprod123!"
}