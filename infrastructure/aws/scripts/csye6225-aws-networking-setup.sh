

echo "Setting the AWS network"

echo "Please enter the stack name"
read stackname

serverZone="us-east-1"
vpcName="$stackname-csye6225-vpc"
gatewayName="$stackname-csye6225-InternetGateway"
routeTableName="$stackname-csye6225-route-table"
instanceName="$stackname-csye6225-Instance"
jsonFileName="$stackname-informationJSON"
keyName="$stackname-key"

vpcCidrBlock="10.0.0.0/16"
subNetCidrBlock1="10.0.0.0/24"
subNetCidrBlock2="10.0.1.0/24"
destinationCidrBlock="0.0.0.0/0"

echo "Creating the VPC"
aws_response=$(aws ec2 create-vpc --cidr-block "$vpcCidrBlock") &&
vpcId=$(echo -e "$aws_response" | /usr/bin/jq '.Vpc.VpcId' | tr -d '"') &&
if [ -z "$vpcId" ];then 
	echo "Failed creating the VPC"
    exit 1
else echo "Succeed creating the VPC"
fi

aws ec2 create-tags --resources "$vpcId" --tags Key=Name,Value=$vpcName&&

echo

echo "Creating the Internet Gateway"
gateway_response=$(aws ec2 create-internet-gateway)&&
gatewayId=$(echo -e "$gateway_response" | /usr/bin/jq '.InternetGateway.InternetGatewayId' | tr -d '"') &&
if [ -z "$gatewayId" ];then 
	echo "Failed creating the Internet Gateway"
    exit 1
else echo "Succeed creating the Internet Gateway"
fi

aws ec2 create-tags --resources "$gatewayId" --tags Key=Name,Value=$gatewayName&&

echo
echo "Attaching the Internet Gateway to the VPC"
aws ec2 attach-internet-gateway --vpc-id "$vpcId" --internet-gateway-id "$gatewayId"&&

echo
echo "Creating the public Route Table for vpc"
route_table_response=$(aws ec2 create-route-table --vpc-id "$vpcId")&&
routeTableId=$(echo -e "$route_table_response" | /usr/bin/jq '.RouteTable.RouteTableId' | tr -d '"')&&
if [ -z "$routeTableId" ];then 
	echo "Failed creating the public Route Table"
    exit 1
else echo "Succeed creating the public Route Table"
fi

echo
aws ec2 create-tags --resources "$routeTableId" --tags Key=Name,Value=$routeTableName&&

echo
echo "Adding Route for the internet gateway"
route_response=$(aws ec2 create-route --route-table-id "$routeTableId" --destination-cidr-block "$destinationCidrBlock" --gateway-id "$gatewayId")&&
if [ -z "$route_response" ];then 
	echo "Failed"
    exit 1
else echo "Succeed"
fi

echo
echo "Checking the Route status"
isActive_response=$(aws ec2 describe-route-tables --route-table-id "$routeTableId")&&
if [ -z "$isActive_response" ];then 
	echo "The Route is active"
    exit 1
else echo "The Route is inactive"
fi

echo "Creating the information JSON file"
cat >./"$jsonFileName".json <<EOF
{
	"instanceId": "$instanceId",
	"groupId": "$groupId",
	"routeTableId": "$routeTableId",
	"gatewayId": "$gatewayId",
	"vpcId": "$vpcId"
}
EOF

echo
echo "Create finished"



