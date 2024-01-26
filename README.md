# trtlmail-email-service
Microservice that is responsible for sending emails and updating email data

## Trtlmail-email-service
API's responsible for handling REST processes for trtlmail-web

### To run docker:
1) Run `mvn clean package`
2) Run `docker build -t trtlmail-email:latest .`
3) Run `docker run --env-file ./.env -p 5000:5000 trtlmail-email:latest`
- You can also run `docker run --env-file ./.env -p 5000:5000  514832027284.dkr.ecr.us-east-1.amazonaws.com/trtmail-email:latest` to run the aws image

### To deploy to ECS:
1) mvn clean package
2) Open Dockerhub
3) Run the deploy.sh script
4) Check that service in ECS has at least 1 desirable task
