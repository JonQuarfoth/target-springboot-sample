# target-springboot-sample
Sample Spring Boot Rest app for Target Interview

## This sample application is intended to demonstrate the following:
* Spring Boot REST interface
* Java 8 Skills
* Knowledge of Spock Testing Framework
* Build application as Docker container via Gradle

### To build docker image:
`gradle build buildDocker`

Note: There will be an error if you are not authorized to push to the `jonquarfoth` docker repository. The image should still have been built successfully. change the `group` in the `build.gradle` file if you would like to push the image to a different repository.

The Docker image built by this command is available here:
https://hub.docker.com/r/jonquarfoth/springboot-sample/

### To run docker image
docker run -p 8080:8080 -t jonquarfoth/springboot-sample:latest

This should start up the application listening on port 8080
