aws ec2 delete-route-table --route-table-id XXX &&

aws ec2 detach-internet-gateway --internet-gateway-id XXX --vpc-id XXX &&

aws ec2 delete-internet-gateway --internet-gateway-id XXX &&

aws ec2 delete-vpc --vpc-id XXX