output "vpc_id" {
  description = "VPC ID"
  value       = module.vpc.vpc_id
}

output "public_subnet_ids" {
  description = "Public subnet IDs"
  value       = module.vpc.public_subnet_ids
}

output "private_subnet_ids" {
  description = "Private subnet IDs"
  value       = module.vpc.private_subnet_ids
}

output "cluster_name" {
  description = "EKS cluster name"
  value       = module.eks.cluster_name
}

output "cluster_endpoint" {
  description = "EKS cluster Kubernetes API endpoint"
  value       = module.eks.cluster_endpoint
}

output "cluster_ca_certificate" {
  description = "EKS cluster CA certificate data"
  value       = module.eks.cluster_ca_certificate
  sensitive   = true
}

output "ecr_repository_url" {
  description = "ECR Repository URL for Docker images"
  value       = module.ecr.ecr_repository_url
}

output "rds_endpoint" {
  description = "RDS PostgreSQL connection endpoint"
  value       = module.rds.db_instance_endpoint
}

output "rds_address" {
  description = "RDS PostgreSQL hostname address"
  value       = module.rds.db_instance_address
}

output "rds_port" {
  description = "RDS PostgreSQL port"
  value       = module.rds.db_instance_port
}

output "configure_kubectl" {
  description = "Command to configure kubectl to connect to the new EKS cluster"
  value       = "aws eks update-kubeconfig --region ${var.region} --name ${module.eks.cluster_name}"
}
