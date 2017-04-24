
resource "aws_vpc" "cluster-vpc" {
    cidr_block = "${var.aws_vpc_cidr_block}"

    #DNS Related Entries
    enable_dns_support = true
    enable_dns_hostnames = true

    tags {
        Name = "aws-${var.aws_cluster_name}-vpc"
    }
}



resource "aws_internet_gateway" "cluster-vpc-internetgw" {
  vpc_id = "${aws_vpc.cluster-vpc.id}"

  tags {
      Name = "aws-${var.aws_cluster_name}-internetgw"
  }
}

resource "aws_subnet" "cluster-vpc-subnet-public" {
    vpc_id = "${aws_vpc.cluster-vpc.id}"
    availability_zone = "${var.aws_avail_zone}"
    cidr_block = "${var.aws_vpc_cidr_block}"

    tags {
        Name = "aws-${var.aws_cluster_name}-${var.aws_avail_zone}-public"
    }
}


#Routing in VPC

resource "aws_route_table" "aws-public" {
    vpc_id = "${aws_vpc.cluster-vpc.id}"
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = "${aws_internet_gateway.cluster-vpc-internetgw.id}"
    }
    tags {
        Name = "aws-${var.aws_cluster_name}-routetable-public"
    }
}

resource "aws_route_table_association" "aws-public" {
    subnet_id = "${aws_subnet.cluster-vpc-subnet-public.*.id}"
    route_table_id = "${aws_route_table.aws-public.id}"

}



#Kubernetes Security Groups

resource "aws_security_group" "aws" {
    name = "aws-${var.aws_cluster_name}-securitygroup"
    vpc_id = "${aws_vpc.cluster-vpc.id}"

    tags {
        Name = "aws-${var.aws_cluster_name}-securitygroup"
    }
}

resource "aws_security_group_rule" "allow-all-ingress" {
    type = "ingress"
    from_port = 0
    to_port = 65535
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    security_group_id = "${aws_security_group.aws.id}"
}

resource "aws_security_group_rule" "allow-all-egress" {
    type = "egress"
    from_port = 0
    to_port = 65535
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    security_group_id = "${aws_security_group.aws.id}"
}