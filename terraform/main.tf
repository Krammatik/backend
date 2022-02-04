terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.74.0"
    }
  }
  backend "s3" {
    bucket = "krammatik-tf"
    key    = "terraform.tfstate"
    region = "eu-central-1"
  }
  required_version = ">=1.1.4"
}

provider "aws" {
  region = "eu-central-1" # Frankfurt
}
