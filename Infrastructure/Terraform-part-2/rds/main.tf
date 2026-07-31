resource "aws_db_subnet_group" "this" {
  name       = "${var.cluster_name}-rds-subnet-group"
  subnet_ids = var.private_subnet_ids

  tags = {
    Name = "${var.cluster_name}-rds-subnet-group"
  }
}

resource "aws_security_group" "rds" {
  name        = "${var.cluster_name}-rds-sg"
  description = "Security group for RDS PostgreSQL instance"
  vpc_id      = var.vpc_id

  ingress {
    description = "PostgreSQL access from VPC"
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.cluster_name}-rds-sg"
  }
}

resource "aws_db_instance" "this" {
  identifier                          = "${var.cluster_name}-postgres"
  allocated_storage                   = var.allocated_storage
  storage_type                        = "gp2"
  engine                              = "postgres"
  engine_version                      = "16.13"
  instance_class                      = var.instance_class
  db_name                             = var.db_name
  username                            = var.db_username
  password                            = var.db_password
  iam_database_authentication_enabled = true
  db_subnet_group_name                = aws_db_subnet_group.this.name
  vpc_security_group_ids              = [aws_security_group.rds.id]

  publicly_accessible = false
  skip_final_snapshot = true

  tags = {
    Name = "${var.cluster_name}-postgres-rds"
  }
}

resource "postgresql_role" "backend_app" {
  name  = "backend_app"
  login = true
  roles = ["rds_iam"]

  depends_on = [aws_db_instance.this]
}

resource "postgresql_grant" "schema_public" {
  database    = aws_db_instance.this.db_name
  role        = postgresql_role.backend_app.name
  schema      = "public"
  object_type = "schema"
  privileges  = ["CREATE", "USAGE"]
}

resource "postgresql_grant" "tables_public" {
  database    = aws_db_instance.this.db_name
  role        = postgresql_role.backend_app.name
  schema      = "public"
  object_type = "table"
  privileges  = ["ALL"]
}

resource "postgresql_default_privileges" "default_tables" {
  database    = aws_db_instance.this.db_name
  role        = postgresql_role.backend_app.name
  schema      = "public"
  owner       = aws_db_instance.this.username
  object_type = "table"
  privileges  = ["ALL"]
}
