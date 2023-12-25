FROM eclipse-temurin:17

LABEL maintainer="jackmu@umich.edu"

WORKDIR /app

COPY target/trtlmail-email.jar /app/trtlmail-email.jar

ENTRYPOINT ["java", "-jar", "trtlmail-email.jar"]