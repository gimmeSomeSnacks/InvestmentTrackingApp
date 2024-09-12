FROM gradle:8.7-jdk as build

COPY --chown=gradle:gradle . /project

WORKDIR /project

RUN gradle build --no-daemon -x test


FROM openjdk:23

WORKDIR /app

COPY --from=build /project/build/libs/DB-Service-0.0.1-SNAPSHOT.jar /app/DBService.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "DBService.jar"]

#FROM openjdk:23
#
#WORKDIR /app
#
#COPY /build/libs/DB-Service-0.0.1-SNAPSHOT.jar /app/DBService.jar
#
#EXPOSE 8081
#
#ENTRYPOINT ["java", "-jar", "DBService.jar"]