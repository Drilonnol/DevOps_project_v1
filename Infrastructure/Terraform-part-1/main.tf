module "s3" {
  source = "./modules/S3"
  bucket_name  = var.bucket_name
  force_destroy = var.force_destroy
  environment = var.environment
  managed_by  = var.managed_by
}


module "dynamodb" {
  source = "./modules/DynamoDB"
  table_name   = var.table_name
  hash_key     = var.hash_key
  billing_mode = var.billing_mode
  manufacturer = var.manufacturer
  make         = var.make
  year         = var.year
  vin          = var.vin
}