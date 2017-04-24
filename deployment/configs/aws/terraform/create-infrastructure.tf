terraform {
    required_version = ">= 0.8.7"
}

provider "aws" {
    access_key = "${var.AWS_ACCESS_KEY_ID}"
    secret_key = "${var.AWS_SECRET_ACCESS_KEY}"
    region = "${var.AWS_DEFAULT_REGION}"
}

/*
* Calling modules who create the initial AWS VPC / AWS ELB
* and AWS IAM Roles for Kubernetes Deployment
*/

module "aws-vpc" {
  source = "modules/vpc"

  aws_cluster_name = "${var.aws_cluster_name}"
  aws_vpc_cidr_block = "${var.aws_vpc_cidr_block}"
  aws_avail_zone="${var.aws_avail_zone}"

}


/*
* Create Elastic BeanStalk Infrastructure
*
*/


resource "aws_s3_bucket" "aws_s3_metanome" {
  bucket = "metanome.deployment.bucket"
}

resource "aws_s3_bucket_object" "aws_s3_object" {
  bucket = "${aws_s3_bucket.aws_s3_metanome.id}"
  key    = "${var.aws_java_war_name}"
  source = "${format("%s%s",var.aws_java_war_path,var.aws_java_war_name)}"
}


resource "aws_elastic_beanstalk_application" "aws_beanstalk" {
  name        = "${var.aws_cluster_name}"
  description = "${var.aws_cluster_descr}"
}


resource "aws_elastic_beanstalk_application_version" "aws_beanstalk_version" {
  name        = "${var.aws_cluster_name}-version"
  application = "${aws_elastic_beanstalk_application.aws_beanstalk.name}"
  description = "${var.aws_cluster_descr}"
  bucket      = "${aws_s3_bucket.aws_s3_metanome.id}"
  key         = "${aws_s3_bucket_object.aws_s3_object.id}"
}


resource "aws_elastic_beanstalk_environment" "tfenvtest" {
  name                = "${var.aws_cluster_name}-environment"
  application         = "${aws_elastic_beanstalk_application_version.aws_beanstalk_version.name}"
  solution_stack_name = "${var.aws_solution_stack}"

  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = "${module.aws-vpc.aws_vpc_id}"
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = "${module.aws-vpc.aws_subnet_id_public}"
  }

  setting {
      namespace = "aws:elasticbeanstalk:environment"
      name      = "EnvironmentType"
      value     = "SingleInstance"
  }

  setting {
      namespace = "aws:autoscaling:launchconfiguration"
      name      = "InstanceType"
      value     = "${var.aws_beanstalk_size}"
  }

 setting {
      namespace = "aws:autoscaling:launchconfiguration"
      name      = "EC2KeyName"
      value     = "${var.AWS_SSH_KEY_NAME}"
    }

 setting {
      namespace = "aws:autoscaling:launchconfiguration"
      name      = "SecurityGroups"
      value     = "${module.aws-vpc.aws_security_group}"
    }

 tags {
         Name = "aws-${var.aws_cluster_name}-Elastic-Bean-Stalk"
         Cluster = "${var.aws_cluster_name}"
         Role = "Elastic-Beanstalk"
     }


}




