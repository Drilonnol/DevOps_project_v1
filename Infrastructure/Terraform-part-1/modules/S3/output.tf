output "bucket_name" {
  description = "S3 bucket name"
  value       = aws_s3_bucket.bucket_example.bucket
}

output "bucket_arn" {
  description = "S3 bucket ARN"
  value       = aws_s3_bucket.bucket_example.arn
}

output "bucket_id" {
  description = "S3 bucket ID"
  value       = aws_s3_bucket.bucket_example.id
}

output "bucket_region" {
  description = "AWS region of the bucket"
  value       = aws_s3_bucket.bucket_example.region
}