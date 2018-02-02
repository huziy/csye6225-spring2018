aws ec2 create-vpc --cidr-block 10.0.0.0/16 &&
#get VpcId

aws ec2 create-internet-gateway &&
#

aws ec2 attach-internet-gateway --vpc-id XXX --internet-gateway-id XXX &&

aws ec2 create-route-table --vpc-id XXX &&

aws ec2 create-route --route-table-id XXX --destination-cidr-block 0.0.0.0/0 --gateway-id XXX &&

aws ec2 describe-route-tables --route-table-id XXX &&