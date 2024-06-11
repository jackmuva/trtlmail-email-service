#!/bin/sh

echo "Pre-Build Steps:"
echo "authenticating with AWS ECR"
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 514832027284.dkr.ecr.us-east-1.amazonaws.com
echo "Maven clean and package:"
mvn clean package

echo "Build Steps:"
echo "building image..."
docker build -t 514832027284.dkr.ecr.us-east-1.amazonaws.com/trtlmail-email:latest .

echo "updating AWS ECS service..."
aws ecs update-service --cluster trtlmail-rest-cluster --service tm-email-sv --force-new-deployment

echo "Post-Build steps:"
echo "pushing image to AWS ECR"
#docker push --all-tags 514832027284.dkr.ecr.us-east-1.amazonaws.com/trtlmail-rest
docker push 514832027284.dkr.ecr.us-east-1.amazonaws.com/trtlmail-email:latest
