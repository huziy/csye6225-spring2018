

echo "Enter your stack name"
read stackname

aws cloudformation delete-stack --stack-name ${stackname}&&
echo done