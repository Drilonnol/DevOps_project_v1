#!/bin/bash

echo "Checking required tools..."

MISSING=0
TOOLS="git docker kubectl terraform aws helm"

for tool in $TOOLS; do
  command -v "$tool" >/dev/null 2>&1
  FOUND=$?

  case $FOUND in
    0)
      case $tool in
        git)
          echo "$tool: OK - $(git --version)"
          ;;
        docker)
          echo "$tool: OK - $(docker --version)"
          ;;
        kubectl)
          echo "$tool: OK - $(kubectl version --client)"
          ;;
        terraform)
          echo "$tool: OK - $(terraform version)"
          ;;
        aws)
          echo "$tool: OK - $(aws --version)"
          ;;
        helm)
          echo "$tool: OK - $(helm version --short)"
          ;;
      esac
      ;;
    *)
      echo "$tool: MISSING"
      MISSING=1
      ;;
  esac
done

case $MISSING in
  1)
    echo "ERROR: Some required tools are missing. Please install them first."
    exit 1
    ;;
esac

echo "All tools found. Creating folders..."

mkdir -p app docker terraform k8s .github monitoring

echo "Done. Folders created."