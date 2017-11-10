# PostsTask

Create a Java REST based web application leveraging Spring Boot. The application must expose CRUD (Create, Read, Update, Delete) operations mapped onto HTTP methods. The application to create is a single author blog, it should have CRUD operations on Posts. A single Post comprises Title, Content, Tags. Basic-Authentication must be in place for one user. Unit-tests should cover application’s crucial parts. As a database an “in memory db” should be used e.g. h2.
No front end is needed, however, the endpoints must be listed in application documentation alongside with the way how to run and work with the application.
Source code should be available on a public git repository.

List of endpoints:

["GET /api/endpoints","PUT /api/posts/{id}","DELETE /api/posts/{id}","GET /api/posts/{id}","GET /api/posts","POST /api/posts","GET /error","GET /error"]

in order to reach endpoint which contains data about all endpoints try http://localhost:8080/endpoints

Basic-Authentication is used for each of them. You need to use 
username: user and 
password: password. 

How to start
just type in terminal mvn spring-boot:run
or run main method 
Tomcat started on port(s): 8080 (http)

but you can change it in a file application.properties part server.port

please note that database is not empty. Just for your convenience there are few inserts

You can also use postman to view and try all endpoints 




