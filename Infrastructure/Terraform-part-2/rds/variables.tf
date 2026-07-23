variable "cluster_name" {
  description = "Cluster name prefix for resource naming and tagging"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID where RDS will be deployed"
  type        = string
}

variable "vpc_cidr" {
  description = "VPC CIDR block for security group access rules"
  type        = string
}

variable "private_subnet_ids" {
  description = "List of private subnet IDs for the RDS DB subnet group"
  type        = list(string)
}

variable "db_name" {
  description = "PostgreSQL database name"
  type        = string
  default     = "bookstore"
}

variable "db_username" {
  description = "Master username for PostgreSQL database"
  type        = string
  default     = "postgres"
}

variable "db_password" {
  description = "Master password for PostgreSQL database"
  type        = string
  sensitive   = true
}

variable "allocated_storage" {
  description = "Allocated storage in GB (Free tier: up to 20 GB)"
  type        = number
  default     = 20
}

variable "instance_class" {
  description = "RDS DB Instance Class (Free tier: db.t3.micro)"
  type        = string
  default     = "db.t3.micro"
}
