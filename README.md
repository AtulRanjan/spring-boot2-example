# Restful API with proper automated API documentation



## Build Project

##### To build the project, Run following command
	mvn clean install

## Running the Project

##### To run the project, Run following command
	mvn spring-boot:run

##### Testing the API using swagger
Click on the link to access the swagger ui.

[Swagger-UI](http://localhost:7772/spring-boot2/swagger-ui.html "Swagger-UI") http://localhost:7772/spring-boot2/swagger-ui.html

### Creating static API Documentation
To create static document in pdf and html format, Run following command

	mvn test
	
Three files will be created in **\src\main\resources\asciidoc** directory

1.  api-doc.adoc

2.  api-doc.html

3.  api-doc.pdf


* PDF Sample : 
[Generated pdf document](https://github.com/AtulRanjan/spring-boot2/tree/master/src/main/resources/asciidoc/api-doc.pdf "Generated pdf document") 


* HTML Sample : 
[Generated html document](https://github.com/AtulRanjan/spring-boot2/tree/master/src/main/resources/asciidoc/api-doc.html "Generated html document") 