
echo "Shutting the AWS network down"


echo "Please enter the stack name"
read stackname

if 
	[ "$instanceName" == null ]; then
	echo "The name doesn't exist"
	exit 0
else
	echo "The name entered is"
	echo $stackname
	echo "Delete in 5s"
fi


sleep 5

jsonFileName="$stackname-informationJSON.json"
echo $jsonFileName

instanceId=$(/usr/bin/jq '.instanceId' "$jsonFileName" | tr -d '"')
echo $instanceId
groupId=$(/usr/bin/jq '.groupId' "$jsonFileName" | tr -d '"')
routeTableId=$(/usr/bin/jq '.routeTableId' "$jsonFileName" | tr -d '"')
gatewayId=$(/usr/bin/jq '.gatewayId' "$jsonFileName" | tr -d '"')
vpcId=$(/usr/bin/jq '.vpcId' "$jsonFileName" | tr -d '"')
keyName="$stackname-key"

echo
echo "Deleting the Route Table"
aws ec2 delete-route-table --route-table-id "$routeTableId"&&

echo
echo "Detaching the Internet Gateway from the VPC"
aws ec2 detach-internet-gateway --internet-gateway-id "$gatewayId" --vpc-id "$vpcId"&&

echo
echo "Deleting the Internet Gateway"
aws ec2 delete-internet-gateway --internet-gateway-id "$gatewayId"&&

echo
echo "Deleting the VPC"
aws ec2 delete-vpc --vpc-id "$vpcId"&&

echo
echo "Delete finished"

