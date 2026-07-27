# Bookstore Project

![CI](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/ci.yml/badge.svg)
![CD-Docker](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/cd-docker.yml/badge.svg)
![CD-ECR](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/ecr-ci.yml/badge.svg)
![CD-EKS](https://github.com/drilonnol/DevOps_project_v1/actions/workflows/cd-eks.yaml/badge.svg)

# Bookstore Project

## Architecture

This project implements a cloud-native architecture on AWS using Terraform, Kubernetes, Helm, and GitHub Actions.

Terraform is used to provision AWS infrastructure, including VPC, EKS Cluster, IAM, ECR, Security Groups, and RDS.

GitHub Actions handles CI/CD by running tests, building Docker images, pushing images to Amazon ECR, and deploying the application to Kubernetes.

The application is deployed to AWS EKS using Helm charts.

Prometheus and Grafana are used for monitoring application and cluster metrics.2

# VPC

![Architecture Diagram](docs/architecture.png)

## Infrastructure

Infrastructure is managed with Terraform.

Created resources:

- VPC
- EKS Cluster
- Node Groups
- IAM Roles
- Security Groups
- Amazon ECR
- RDS Database

## Kubernetes Deployment

The application is deployed using Helm charts.

Helm manages:

- Backend deployment
- Frontend deployment
- Services
- ConfigMaps
- Secrets
- Ingress
- ServiceMonitor

## CI/CD Pipeline

GitHub Actions automates:

1. Code checkout and testing
2. Docker image build
3. Push image to Amazon ECR
4. Deploy application to EKS

## Monitoring

Monitoring is implemented using:

- Prometheus
- Grafana

Metrics include:

- Application metrics
- CPU and memory usage
- Kubernetes pod status

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
