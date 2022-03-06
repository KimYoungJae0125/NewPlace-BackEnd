FROM adoptopenjdk/openjdk11

EXPOSE 8080

ADD build/libs/newPlace-0.0.1-SNAPSHOT.war newPlace-0.0.1-SNAPSHOT.war

ENTRYPOINT ["java", "-jar", "newPlace-0.0.1-SNAPSHOT.war"]