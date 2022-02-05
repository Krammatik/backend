resource "aws_elastic_beanstalk_application" "application" {
  name = "krammatik-backend"

}

resource "aws_elastic_beanstalk_application_version" "version" {
  application = aws_elastic_beanstalk_application.application.name
  bucket      = aws_s3_bucket.dist.bucket
  key         = aws_s3_bucket_object.dist_item.id
  name        = "backend-${var.environment}-${var.app_version}"
}

resource "aws_elastic_beanstalk_environment" "environment" {
  name                = "backend-${var.environment}"
  application         = aws_elastic_beanstalk_application.application.name
  solution_stack_name = "64bit Amazon Linux 2 v3.2.11 running Corretto 11"
  version_label       = aws_elastic_beanstalk_application_version.version.name

  setting {
    name      = "PORT"
    namespace = "aws:elasticbeanstalk:application:environment"
    value     = "8080"
  }

  setting {
    name      = "InstanceTypes"
    namespace = "aws:ec2:instances"
    value     = "t4g.micro"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = "krammatikBackend"
  }
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "ENCRYPT_SECRET"
    value     = var.encrypt_secret
  }
}
