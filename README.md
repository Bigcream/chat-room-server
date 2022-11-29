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
// cau lenh hay dung devops
docker run --name mysql --network spring-boot -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=learn_web -d mysql:8.0.31
docker network create spring-boot
docker run --network spring-boot --name backend-container -p 8080:8080 -d backend:v1.0.1
docker run --name mysqldb1 --network spring-boot -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=learn_web -e MYSQL_USER=sa -e MYSQL_PASSWORD=123456 -p 3307:3306 -d mysql:8.0.31
docker exec -it bash
mysql -uroot -p123456
show databases;
docker network inspect bridge

set up jenkins
#!/usr/bin/env bash
mkdir -p /home/jenkins/data
cd /home/jenkins
docker run -u 0 -v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):$(which docker) -v `pwd`/data:/var/jenkins_home  -p 8085:8080 --name jenkins-server -d jenkins/jenkins:lts
vi /etc/ssh/sshd_config
202.92.4.207
sudo docker run --privileged  -d --name rancher_server --restart=unless-stopped -p 80:80 -p 443:443 rancher/rancher
//xoa het config khi tao cluster moi
sudo rm -rf /etc/kubernetes/ /var/lib/kubelet/ /var/lib/etcd/
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker rmi $(docker images -q)