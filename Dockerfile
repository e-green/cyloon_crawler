FROM centos:centos7

# Prerequisites.

RUN yum -y update
RUN yum -y install wget tar

# Oracle Java 7

WORKDIR /opt

RUN wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/7u71-b14/server-jre-7u71-linux-x64.tar.gz
RUN tar xzf server-jre-7u71-linux-x64.tar.gz
RUN rm -rf server-jre-7u71-linux-x64.tar.gz
RUN alternatives --install /usr/bin/java java /opt/jdk1.7.0_71/bin/java 1

# App
WORKDIR /data
#USER daemon
VOLUME /data/cyloon
# This copies to local fat jar inside the image
ADD  . ./


RUN chmod +x ./gradlew
RUN ./gradlew crawler:fatJar

# What to run when the container starts
ENTRYPOINT [ "java", "-jar", "/data/crawler/crawlerapp/build/libs/crawlerapp-1.0-SNAPSHOT.jar" ]

# Ports used by the app
EXPOSE 5000