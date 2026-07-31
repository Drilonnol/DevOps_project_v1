terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "~> 1.22.0"
    }
  }
}

provider "aws" {
  region = var.region
}

provider "postgresql" {
  host            = module.rds.db_instance_address
  port            = module.rds.db_instance_port
  database        = var.db_name
  username        = var.db_username
  password        = var.db_password
  sslmode         = "require"
  connect_timeout = 15
}

data "aws_caller_identity" "current" {}