#!/bin/bash

echo "Enter your stack name"
read stackname

aws cloudformation delete-stack --stack-name ${stackname}&&
aws cloudformation wait stack-delete-complete --stack-name ${stackname}
echo done
