output "ecr_repository_url" {
  description = "URL to push/pull Docker images"
  value       = aws_ecrpublic_repository.ecr_repo.repository_uri
}

output "ecr_repository_arn" {
  value = aws_ecrpublic_repository.ecr_repo.arn
}

output "ecr_repository_name" {
  value = aws_ecrpublic_repository.ecr_repo.repository_name
}

output "ecr_registry_id" {
  value = aws_ecrpublic_repository.ecr_repo.registry_id
}