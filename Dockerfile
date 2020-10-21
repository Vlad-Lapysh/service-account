FROM openjdk:11
VOLUME /tmp
EXPOSE 8090
ADD build/libs/service-account-1.0.0.jar service-account.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/service-account.jar"]