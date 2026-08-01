output "nlb_hostname_command" {
  value       = "kubectl get svc -n ingress-nginx ingress-nginx-controller -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'"
  description = "Command to get the hostname of the NLB"
}