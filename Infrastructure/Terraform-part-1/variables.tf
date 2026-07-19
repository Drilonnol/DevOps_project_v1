variable "aws_region" {
  description = "AWS region"
  type        = string
}

variable "bucket_name" {
  description = "S3 bucket name"
  type        = string
}

variable "force_destroy" {
  description = "Destroy bucket with objects"
  type        = bool
  default     = true
}

variable "environment" {
  description = "Environment"
  type        = string
}

variable "managed_by" {
  description = "Managed by"
  type        = string
}

variable "table_name" {
  description = "DynamoDB table name"
  type        = string
}

variable "hash_key" {
  description = "DynamoDB partition key"
  type        = string
}

variable "billing_mode" {
  description = "DynamoDB billing mode"
  type        = string
  default     = "PAY_PER_REQUEST"
}

variable "manufacturer" {
  type = string
}

variable "make" {
  type = string
}

variable "year" {
  type = number
}

variable "vin" {
  type = string
}

