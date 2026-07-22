resource "aws_ecrpublic_repository" "ecr_repo" {
  repository_name = "${var.cluster_name}-${var.nametag}-ecr-repo"

  catalog_data {
    description = "bookstore application image"
  }
}