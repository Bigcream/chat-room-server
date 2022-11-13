FROM maven:3.6.3-jdk-11-slim as build-hapi
WORKDIR /tmp

ARG HAPI_FHIR_STARTER_URL=https://github.com/hapifhir/hapi-fhir-jpaserver-starter/
ARG HAPI_FHIR_STARTER_BRANCH=master

RUN apt-get update && apt-get install git -y
RUN git clone --branch ${HAPI_FHIR_STARTER_BRANCH} ${HAPI_FHIR_STARTER_URL} starter-clone

WORKDIR /tmp/hapi-fhir-jpaserver-starter

COPY pom.xml .
RUN mvn -ntp dependency:go-offline

RUN mv /tmp/starter-clone/src/ /tmp/hapi-fhir-jpaserver-starter/src/
RUN mvn clean install -DskipTests

FROM tomcat:9.0.38-jdk11-openjdk-slim-buster

RUN mkdir -p /data/hapi/lucenefiles && chmod 775 /data/hapi/lucenefiles
COPY --from=build-hapi /tmp/hapi-fhir-jpaserver-starter/target/*.war /usr/local/tomcat/webapps/ROOT.tmp

ENV BASE_URI "/ROOT"

EXPOSE 8080

CMD mv /usr/local/tomcat/webapps/ROOT.tmp /usr/local/tomcat/webapps${BASE_URI}.war && exec catalina.sh run