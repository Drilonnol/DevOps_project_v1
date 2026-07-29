output "db_instance_endpoint" {
  description = "Connection endpoint for the RDS PostgreSQL instance"
  value       = aws_db_instance.this.endpoint
}

output "db_instance_address" {
  description = "Hostname address of the RDS PostgreSQL instance"
  value       = aws_db_instance.this.address
}

output "db_instance_port" {
  description = "Database connection port"
  value       = aws_db_instance.this.port
}

output "db_name" {
  description = "Database name"
  value       = aws_db_instance.this.db_name
}

output "db_security_group_id" {
  description = "ID of the RDS security group"
  value       = aws_security_group.rds.id
}

output "db_instance_resource_id" {
  value = aws_db_instance.this.resource_id
}