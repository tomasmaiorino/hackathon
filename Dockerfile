# Ubuntu 16.04
# Oracle Java 1.8.0_101 64 bit
# Maven 3.3.9

FROM maven:3-jdk-8-slim
ARG branch_name

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

# update dpkg repositories
RUN apt-get update

# install git
RUN apt-get -y install git

# remove download archive files
RUN apt-get clean

RUN mkdir /app
WORKDIR /app
RUN git clone https://github.com/tomasmaiorino/hackathon
WORKDIR /app/hackathon
RUN git checkout master
RUN git pull origin
RUN mvn clean install

EXPOSE 8080

CMD ["mvn"]
