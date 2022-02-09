# logs.tf

# Set up CloudWatch group and log stream and retain logs for 30 days
resource "aws_cloudwatch_log_group" "krammatik_log_group" {
  name              = "/ecs/krammatik-app"
  retention_in_days = 30

  tags = {
    Name = "krammatik-log-group"
  }
}

resource "aws_cloudwatch_log_stream" "krammatik_log_stream" {
  name           = "krammatik-log-stream"
  log_group_name = aws_cloudwatch_log_group.krammatik_log_group.name
}

