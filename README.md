# checkout-api

Project is built using Java 17, Spring boot and Maven.

<b>Build :</b><br />
mvn clean install

<b>Start MySQL : </b><br />
docker-compose up -d

<b>To start the application run : </b>  
`CheckoutApiApplication.java` in "local" profile

For API documentation visit the below link after starting the server,<br/>
http://localhost:8080/swagger-ui/index.html
<br/>
 




<b>Approach</b> : <br/>
Identified the core functionalities and entities in the project. <br/>
Multi module maven project with checkout-api-data and checkout-api-service. <br/>
Followed basic TDD approach of red -> green -> refactor -> repeat. Started off with WebMvcTest and took it from there through service and repo layer. <br/>
Tests cover both integration and unit tests. <br/>







