FROM adoptopenjdk/openjdk11

#EXPOSE 8080

COPY build/libs/newPlace-0.0.1-SNAPSHOT.war newPlace-0.0.1-SNAPSHOT.war

ENTRYPOINT ["java", "-jar", "newPlace-0.0.1-SNAPSHOT.war"]

##태그 명령어는 무조건 소문자
## docker build -t newplace-backend .
