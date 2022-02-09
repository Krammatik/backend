# ecs.tf

resource "aws_ecs_cluster" "main" {
  name = "krammatik-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "krammatik-app-task"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.fargate_cpu
  memory                   = var.fargate_memory
  container_definitions    = jsonencode(
    [
      {
        name             = "krammatik-app",
        image            = var.app_image,
        cpu              = var.fargate_cpu,
        memory           = var.fargate_memory,
        networkMode      = "awsvpc",
        logConfiguration = {
          logDriver = "awslogs",
          options   = {
            awslogs-group         = "/ecs/krammatik-app",
            awslogs-region        = "eu-central-1",
            awslogs-stream-prefix = "ecs"
          }
        },
        portMappings = [
          {
            containerPort = var.app_port,
            hostPort      = var.app_port
          }
        ],
        environment = [
          {
            name  = "ENCRYPT_SECRET",
            value = var.encrypt_secret
          },
          {
            name  = "SSL_CERT",
            value = var.ssl_cert
          },
          {
            name  = "SSL_KEY",
            value = var.ssl_key
          }
        ]
      }
    ]
  )
}

resource "aws_ecs_service" "main" {
  name            = "krammatik-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = [aws_security_group.ecs_tasks.id]
    subnets          = aws_subnet.private.*.id
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.app.id
    container_name   = "krammatik-app"
    container_port   = var.app_port
  }

  depends_on = [aws_alb_listener.front_end, aws_iam_role_policy_attachment.ecs_task_execution_role]
}

