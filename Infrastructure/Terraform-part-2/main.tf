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

  cluster_name      = var.cluster_name
  oidc_provider_arn = module.eks.oidc_provider_arn
  oidc_issuer_url   = module.eks.oidc_issuer_url
  rds_resource_id   = module.rds.db_instance_resource_id
  aws_region        = var.region
  account_id        = data.aws_caller_identity.current.account_id
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
  public_subnet_ids       = module.vpc.public_subnet_ids
  private_subnet_ids      = module.vpc.private_subnet_ids

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

 module "nlb" {
   source     = "./nlb"
   depends_on = [module.eks]
 }

# module "monitoring" {
#   source     = "./monitoring"
#   depends_on = [module.eks]
# }



resource "aws_eks_access_entry" "github_actions" {
  depends_on    = [module.eks]
  cluster_name  = var.cluster_name
  principal_arn = module.iam.github_actions_role_arn
  type          = "STANDARD"
}

resource "aws_eks_access_policy_association" "github_actions" {
  depends_on    = [module.eks, aws_eks_access_entry.github_actions]
  cluster_name  = var.cluster_name
  policy_arn    = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
  principal_arn = module.iam.github_actions_role_arn

  access_scope {
    type = "cluster"
  }
}