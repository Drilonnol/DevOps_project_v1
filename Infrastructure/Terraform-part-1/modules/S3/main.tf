resource "aws_s3_bucket" "bucket_example" {
  bucket = var.bucket_name

  force_destroy = var.force_destroy

  tags = {
    Environment = var.environment
    ManagedBy   = var.managed_by
  }
}