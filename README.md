# target-springboot-sample
Sample Spring Boot Rest app for Target Interview

## This sample application is intended to demonstrate the following:
* Spring Boot REST interface
* Java 8 Skills
* Knowledge of Spock Testing Framework
* Build application as Docker container via Gradle

### Application
This is a tiny Spring Boot Application which manages a Todo list. Requests and responses are expected to be JSON.

#### Endpoints
* POST    /todos : create a todo item
* GET     /todos : List all items in the todo lists
* GET     /todos?query=text : list all items with tasks containing the given search term
* PUT     /todos/{id} : update an existing todo item
* GET     /todos/{id} : get an existing todo item
* DELETE  /todos/{id} : delete an existing todo item

Example JSON:
```
{
  "id" : 1,
  "task" : "Watch Star Wars",
  "complete" : "true"
}
```

### To Run app
`gradle bootRun`

### To Run Spock Tests
`gradle test`

### To build docker image:
`gradle build buildDocker`

Note: There will be an error if you are not authorized to push to the `jonquarfoth` docker repository. The image should still have been built successfully. change the `group` in the `build.gradle` file if you would like to push the image to a different repository.

The Docker image built by this command is available here:
https://hub.docker.com/r/jonquarfoth/springboot-sample/

### To run docker image
`docker run -p 8080:8080 -t jonquarfoth/springboot-sample:latest`

This should start up the application listening on port 8080
