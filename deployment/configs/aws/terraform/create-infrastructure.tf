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
  aws_avail_zone="${var.aws_avail_zones}"

  aws_cidr_subnet_public="${var.aws_cidr_subnet_public}"

}


/*
* Create Elastic BeanStalk Infrastructure
*
*/

resource "aws_elastic_beanstalk_application" "aws_beanstalk" {
  name        = "${var.aws_cluster_name}"
  description = "${var.aws_cluster_descr}"
}


resource "aws_elastic_beanstalk_environment" "tfenvtest" {
  name                = "${var.aws_cluster_name}-environment"
  application         = "${aws_elastic_beanstalk_application.aws_beanstalk.name}"
  solution_stack_name = "${var.aws_solution_stack}"

  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = "vpc-xxxxxxxx"
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = "subnet-xxxxxxxx"
  }
}

resource "aws_instance" "k8s-worker" {
    ami = "${var.aws_cluster_ami}"
    instance_type = "${var.aws_beanstalk}"

    count = "1"

    availability_zone  = "${var.aws_avail_zone}"
    subnet_id = "${module.aws-vpc.aws_subnet_id_public}"

    vpc_security_group_ids = [ "${module.aws-vpc.aws_security_group}" ]

    key_name = "${var.AWS_SSH_KEY_NAME}"


    tags {
        Name = "aws-${var.aws_cluster_name}-Elastic-Bean-Stalk"
        Cluster = "${var.aws_cluster_name}"
        Role = "Elastic-Beanstalk"
    }

}




