output "dev_ec2_ip" {
    value = aws_instance.mtc_ec2.public_ip
}