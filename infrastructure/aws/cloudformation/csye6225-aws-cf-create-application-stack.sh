#!/bin/bash

echo "Enter Your Stack Name:"
read stackname
instanceName="$stackname-csye6225-instance"

vpcId=$(aws ec2 describe-vpcs --filters "Name=cidr-block-association.cidr-block,Values=10.0.0.0/16" --query "Vpcs[0].VpcId" --output text)
echo $vpcId

subnetId=$(aws ec2 describe-subnets --filters "Name=cidrBlock,Values=10.0.0.0/24" --query "Subnets[0].SubnetId" --output text)
echo $subnetId

webpolicy=$(aws ec2 describe-security-groups --filters Name=tag-key,Values="Web" --query "SecurityGroups[0].GroupId" --output text)
echo $webpolicy

dbpolicy=$(aws ec2 describe-security-groups --filters Name=tag-key,Values="db" --query "SecurityGroups[0].GroupId" --output text)
echo $dbpolicy

loadbalancerpolicy=$(aws ec2 describe-security-groups --filters Name=tag-key,Values="loadbalancer" --query "SecurityGroups[0].GroupId" --output text)
echo $loadbalancerpolicy

subnetgroupname=$(aws rds describe-db-subnet-groups --query "DBSubnetGroups[1].DBSubnetGroupName" --output text)
echo $subnetgroupname

awslambdaservice=$(aws lambda get-function --function-name "csye6225" --query "Configuration.FunctionArn" --output text)
echo $awslambdaservice

Subnet1=$(aws ec2 describe-subnets --filters "Name=cidrBlock,Values=10.0.0.0/24" --query "Subnets[0].SubnetId" --output text)
echo $Subnet1

Subnet2=$(aws ec2 describe-subnets --filters "Name=cidrBlock,Values=10.0.3.0/24" --query "Subnets[0].SubnetId" --output text)
echo $Subnet2

Subnet3=$(aws ec2 describe-subnets --filters "Name=cidrBlock,Values=10.0.4.0/24" --query "Subnets[0].SubnetId" --output text)
echo $Subnet3


aws cloudformation create-stack --template-body file://./csye6225-cf-application.json --stack-name ${stackname} --capabilities "CAPABILITY_NAMED_IAM" --parameters ParameterKey=InstanceName,ParameterValue=$instanceName ParameterKey=SubnetId,ParameterValue=$subnetId ParameterKey=WebPolicy,ParameterValue=$webpolicy ParameterKey=DBPolicy,ParameterValue=$dbpolicy ParameterKey=DBSubnetGroup,ParameterValue=$subnetgroupname ParameterKey=awslambdaservice,ParameterValue=$awslambdaservice ParameterKey=Subnet1,ParameterValue=$Subnet1 ParameterKey=Subnet2,ParameterValue=$Subnet2 ParameterKey=Subnet3,ParameterValue=$Subnet3 ParameterKey=LoadBalancerPolicy,ParameterValue=$loadbalancerpolicy ParameterKey=VpcId,ParameterValue=$vpcId ParameterKey=ImageId,ParameterValue=ami-66506c1c ParameterKey=InstanceType,ParameterValue=t2.micro ParameterKey=CoolDown,ParameterValue=60 ParameterKey=MinSize,ParameterValue=3 ParameterKey=MaxSize,ParameterValue=7 ParameterKey=DesiredCapacity,ParameterValue=3 ParameterKey=DomainName,ParameterValue=csye6225-spring2018-huziy.me. ParameterKey=EngineVersion,ParameterValue=5.6.37 ParameterKey=DBInstanceClass,ParameterValue=db.t2.medium ParameterKey=MasterUsername,ParameterValue=csye6225master ParameterKey=MasterUserPassword,ParameterValue=csye6225password ParameterKey=S3BucketName,ParameterValue=s3.csye6225-spring2018-huziy.me

aws cloudformation wait stack-create-complete --stack-name ${stackname} 
echo done
