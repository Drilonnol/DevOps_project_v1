# Bookstore Project

![CI](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/ci.yml/badge.svg)
![CD-Docker](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/cd-docker.yml/badge.svg)
![CD-ECR](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/ecr-ci.yml/badge.svg)
![CD-EKS](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/cd-eks.yaml/badge.svg)

```mermaid
flowchart TD

A[Developer] --> B[GitHub Repository]
B --> C[GitHub Actions CI/CD]
C --> D[Docker Image Build]
D --> E[Amazon ECR]
E --> F[AWS EKS Cluster]
F --> G[Helm Deployment]

G --> H[Application]

G --> I[Prometheus]
I --> J[Grafana]
