# https://oneuptime.com/blog/post/2026-02-23-crds-custom-resources-terraform/view
resource "helm_release" "nginx_ingress_aws" {

  name       = "ingress-nginx"
  repository = "https://kubernetes.github.io/ingress-nginx"
  chart      = "ingress-nginx"
  namespace  = "ingress-nginx"
  create_namespace = true
  version = "4.9.0"

  values = [
    yamlencode({
      controller = {
        replicaCount = 2

        service = {
          type = "LoadBalancer"

          annotations = {
            "service.beta.kubernetes.io/aws-load-balancer-type" = "nlb"
            "service.beta.kubernetes.io/aws-load-balancer-scheme" = "internet-facing"

          }
        }
      }
    })
  ]
}
#https://oneuptime.com/blog/post/2026-02-23-ingress-controllers-terraform/view
