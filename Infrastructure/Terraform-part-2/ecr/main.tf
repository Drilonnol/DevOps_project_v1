resource "aws_ecr_repository" "ecr_repo" {
  name                 = var.repository_name != "" ? var.repository_name : "${var.cluster_name}-${var.nametag}-ecr-repo"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Name        = "${var.cluster_name}-${var.nametag}-ecr-repo"
    Environment = var.nametag
  }
}