![Publishing the Docker image](https://github.com/AMTeam-Heig/Gamification-Engine/workflows/Publishing%20the%20Docker%20image/badge.svg)

# Gamification
The version pushed on Docker for the second project is on branch ```master```.
Link to the 2nd project : 
```
https://github.com/AMTeam-Heig/project_2
```

To run the program use the following steps:
 
Kindly refer to this *[version](https://github.com/AMTeam-Heig/Gamification-Engine/tree/Cucumber-Tests)* if you prefer to run only the gamification engine .

After cloning the repository you can use maven to build and run the REST API implementation from the command line. After invoking the following maven goal, the Spring Boot server will be up and running, listening for connections on port 9081.
```
cd gamification-impl/
mvn spring-boot:run
```
or 
```bash
docker run -p 8080:8080 ghcr.io/amteam-heig/gamification-engine/spring/gamification:latest
```
Note that this version is used mainly to integrate the gamification engine with the web application. 
You can then access:

* the [API documentation](http://localhost:9081/swagger-ui.html), generated from annotations in the code
* the [API endpoint](http://localhost:9081/), accepting GET and POST requests


# Test the gamification  microservice by running the executable specification
### Cucumber :
A dedicated branche of Cumcumber tests was created to avoid all congestion with project 3 :
Kindly use this branche : 
[Cumcumber tests](https://github.com/AMTeam-Heig/Gamification-Engine/tree/Cucumber-Tests)

```
cd gamification-specs/
mvn clean test
```
You will see the test results in the console, but you can also open the file located in `./target/cucumber`
We tested all possible behaviour of an application creation and retrieve ,also some tests for badges .
### Jmeter:
The gamification engine was tested by this load test tools .We tested a heavy load with  multiple connections from multiple applications and users .
the flux of events was tested also .
