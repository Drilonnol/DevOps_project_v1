variable "bucket_name" {
  description = "Name of the S3 bucket"
  type        = string
}

variable "force_destroy" {
  description = "Delete bucket even if it contains objects"
  type        = bool
  default     = true
}

variable "environment" {
  description = "Environment tag"
  type        = string
  default     = "Dev"
}

variable "managed_by" {
  description = "Managed by tag"
  type        = string
  default     = "Terraform"
}