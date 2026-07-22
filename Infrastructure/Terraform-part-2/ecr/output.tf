output "ecr_repository_url" {
  description = "URL to push/pull Docker images"
  value       = aws_ecr_repository.ecr_repo.repository_url
}

output "ecr_repository_arn" {
  description = "ARN of the ECR repository"
  value       = aws_ecr_repository.ecr_repo.arn
}

output "ecr_repository_name" {
  description = "Name of the ECR repository"
  value       = aws_ecr_repository.ecr_repo.name
}

output "ecr_registry_id" {
  description = "Registry ID of the ECR repository"
  value       = aws_ecr_repository.ecr_repo.registry_id
}