module "vpc" {
  source = "./vpc"

  cluster_name         = var.cluster_name
  vpc_cidr             = var.vpc_cidr
  public_subnet_cidrs  = var.public_subnet_cidrs
  private_subnet_cidrs = var.private_subnet_cidrs
  azs                  = var.azs
}

module "iam" {
  source = "./iam"

  cluster_name = var.cluster_name
}

module "sg" {
  source = "./sg"

  cluster_name = var.cluster_name
  vpc_id       = module.vpc.vpc_id
  vpc_cidr     = var.vpc_cidr
}

module "eks" {
  source = "./eks"

  cluster_name            = var.cluster_name
  cluster_version         = var.cluster_version
  backend_iam_policy_arns = var.backend_iam_policy_arns
  enable_pod_identity     = var.enable_pod_identity
  vpc_id                  = module.vpc.vpc_id
  public_subnet_ids    = module.vpc.public_subnet_ids
  private_subnet_ids   = module.vpc.private_subnet_ids

  cluster_role_arn = module.iam.cluster_role_arn
  node_role_arn    = module.iam.node_role_arn

  node_instance_types = var.node_instance_types
  node_desired_size   = var.node_desired_size
  node_max_size       = var.node_max_size
  node_min_size       = var.node_min_size
}

module "ecr" {
  source = "./ecr"

  cluster_name = var.cluster_name
  nametag      = var.nametag
}

module "rds" {
  source = "./rds"

  cluster_name       = var.cluster_name
  vpc_id             = module.vpc.vpc_id
  vpc_cidr           = var.vpc_cidr
  private_subnet_ids = module.vpc.private_subnet_ids

  db_name     = var.db_name
  db_username = var.db_username
  db_password = var.db_password
}