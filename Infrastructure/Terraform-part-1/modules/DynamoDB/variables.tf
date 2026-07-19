variable "table_name" {
  description = "DynamoDB table name"
  type        = string
}

variable "hash_key" {
  description = "Partition key"
  type        = string
}

variable "billing_mode" {
  description = "Billing mode"
  type        = string
  default     = "PAY_PER_REQUEST"
}

variable "manufacturer" {
  description = "Car manufacturer"
  type        = string
}

variable "make" {
  description = "Car model"
  type        = string
}

variable "year" {
  description = "Manufacturing year"
  type        = number
}

variable "vin" {
  description = "Vehicle Identification Number"
  type        = string
}