# reddit-clone

Using [mailtrap](https://mailtrap.io/) as SMTP server.

Api's will be available at http://localhost:8080/swagger-ui.html#/

Following [tutorial](https://www.youtube.com/watch?v=DKlTBBuc32c)

Using Rabbit-mq as pub-sub to send email.
![Rabbit mq](https://user-images.githubusercontent.com/29348796/132297619-325013c4-d722-4697-adc9-06cf002d7c48.png)

# Steps to setup - 
1. Install Jdk8, intellij, docker, mysql, postman.
2. Import project as maven project.
3. Use ```mvn clean install``` to build the project.
4. Run ```docker-compose up``` to run rabbit-mq locally.
5. Create an account on [mailtrap](https://mailtrap.io/) for email smtp server.
6. Replace username/password with your's in properties.xml file.
7. Replace mysql properties with your's in properties.xml file.

# How to use this application
1. Run the application.
2. All the endpoints will be accessible at ```http://localhost:8080/swagger-ui.html```
3. User can signup using signup uri. A mail will get triggred to mailtrap for user verification. After verifying user, account will get activated.
4. Jwt token will be created once user is logged in. Same jwt token can be used for subsequent requests.