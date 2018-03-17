The main focus on this projects was to create an api base for many other api's.

All the new rest api's need to do is to extend the base classes and to implement some interfaces, by doing that, it would be provide to that to than a base CRUD operations.


## Create docker image
$ docker build -t <image_name> .

## Create docker container
$ docker run -it --rm --link <container_image_name> -p 8080:8080 --name <container_name> <image_name>  mvn spring-boot:run -Drun.arguments="--spring.profiles.active=local"

## Used Technologies

**1. Java version 8.**

**2. MYSQL:** Mapping persistent entities in domains objects.

## Additional Technologies

**Tests:** The tests are defined as use case of the Junit. The tests have been made available in the structure: src/test/java.

**Integration Tests:** The integration tests are defined as use case of the Junit. The tests have been made available in the structure: src/it/java.


**Maven:** Life cycle management and project build.

**Docker** Used container manager to create an application image and the containers.

## Considerations


## Usage In Local Machine

###### Pré-requisitos

JDK - Java version 1.8.

Docker latest version. (For docker installation)

Maven for build and dependecies. (For not docker installation)

### Using Docker

1 - To install mysql container.  
```$
docker pull mysql:5.5
```  
2 - To create container.  
```$
docker run -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=skip -d mysql:5.5
```  
3 - To access the mysql container.
```$
docker exec -it mysql /bin/bash
```  
4 - To mysql and to create the database skip.  
```$
mysql -h localhost -pinitdb
```  
```$
create database english_skip;
```    

create an user:   
```$
GRANT ALL PRIVILEGES ON skip.* TO 'skip'@'%' IDENTIFIED BY 'skip';
```  
```$
quit
```      
5 - To exit from container.  
```$
quit
```    
6 - To create application image. (This steps excute the mvn clean install automatically)  
```$
docker build -t eng --build-arg  .
```  
7 - To create application container and start it.  
```$
docker run --rm -it --link postgres -p 8080:8080 --name eng eng mvn spring-boot:run -Drun.arguments="--spring.profiles.active=container"
```    
###### To run the integrations tests through docker run this command:
```$
docker run --rm -it --link mysql -p 8080:8080 --name skip skip mvn spring-boot:run
```
    
```$
docker run --rm -it --name skip_it eng mvn verify -DskipItTest=false -Drun.arguments="--spring.profiles.active=it"
```


### Not using Docker

Any Java IDE with support Maven.

Maven for build and dependecies.

###### After download the fonts from [link github](http://github.com/tomasmaiorino), to install the application and test it execute the maven command:
```$
mvn clean instal
```

###### To only test the application execute the maven command:
```$
mvn clean test
```

###### To run the integrations tests excute the maven command:
```$
mvn verify -DskipItTest=false  -P it 
```

###### To run the application the maven command:
```$
mvn spring-boot:run
```

###### Service's call examples:

#### Create a client.
```$
curl -i -H "Contencurl -i -H "Content-Type:application/json" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/customers -d "{\"password\": \"123123\",\"email\": \"user@domain.com\",\"name\": \"Jean Gray\",\"status\":\"ACTIVE\"}"t-Type:application/json"  -H "AT: cecadbd7-e07c-48b2-b11d-038f7aaab4f6" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/cards-type -d "{\"name\": \"Kicthen\",\"imgUrl\": \"assets/img/cards/kitchen/kitchen.jpg\",\"status\":\"ACTIVE\"}"
```