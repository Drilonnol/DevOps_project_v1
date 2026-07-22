module "vpc" {
  source = "./vpc"

  vpc_cidr             = var.vpc_cidr
  azs                  = var.azs
  public_subnet_cidrs  = var.public_subnet_cidrs
  private_subnet_cidrs = var.private_subnet_cidrs
  cluster_name         = var.cluster_name
}

module "ecr" {
  source = "./ecr"

  cluster_name = var.cluster_name
  nametag      = var.nametag
}