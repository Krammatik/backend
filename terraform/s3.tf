## Jar file

data "archive_file" "backend_dist_zip" {
  type        = "zip"
  source_file = "${path.root}/../build/libs/backend.jar"
  output_path = "${path.root}/backend.jar.zip"
}

resource "aws_s3_bucket" "dist" {
  bucket = "krammatik"
  acl    = "private"
}

resource "aws_s3_bucket_object" "dist_item" {
  bucket = aws_s3_bucket.dist.id
  key    = "${var.environment}/${uuid()}.zip"
  source = data.archive_file.backend_dist_zip.output_path
  etag   = data.archive_file.backend_dist_zip.output_md5
}