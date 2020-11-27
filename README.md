![Publishing the Docker image](https://github.com/AMTeam-Heig/Gamification-Engine/workflows/Publishing%20the%20Docker%20image/badge.svg)

# Gamification

To run the program use the following command:
```bash
docker run -p 8080:8080 ghcr.io/amteam-heig/gamification-engine/spring/gamification:latest
```
 Or 
 
After cloning the repository you can use maven to build and run the REST API implementation from the command line. After invoking the following maven goal, the Spring Boot server will be up and running, listening for connections on port 8080.

```
cd gamification-impl/
mvn spring-boot:run
```

You can then access:

* the [API documentation](http://localhost:8080/swagger-ui.html), generated from annotations in the code
* the [API endpoint](http://localhost:8080/), accepting GET and POST requests


# Test the gamification  microservice by running the executable specification

You can use the Cucumber project to validate the API implementation. Do this when the server is running.

```
cd gamification-specs/
mvn clean test
```
You will see the test results in the console, but you can also open the file located in `./target/cucumber`

