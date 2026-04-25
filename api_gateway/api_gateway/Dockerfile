FROM eclipse-temurin:17-jdk as build

LABEL maintainer="Amir Zhaboev <Zhaboev7029@gmail.com>"

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

FROM eclipse-temurin:17-jre

VOLUME /tmp

ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","/app:/app/lib/*","com.zhaboev.microservices.api_gateway.ApiGatewayApplication"]