output "beanstalk_ip" {
    value = "${join("\n", aws_instance.bastion-server.*.public_ip)}"
}