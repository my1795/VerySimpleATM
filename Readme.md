# VerySimpleATM
## _An ATM Simulator with balance check and withdraw features_


- Jacoco for test coverage report
- Docker for containerization- 
- Maven for installing and packaing the App
- Swagger is enabled 
## Features

-	User (assume any rest client – curl, postman, browser) should be able to request a balance check along with maximum withdrawal amount (if any),
-	User should be able to request a withdrawal. If successful - details of the notes that would be dispensed along with remaining balance,
-	If anything goes wrong, user should receive meaningful message, and there should be no changes in user’s account,

## Constarints 
-	should initialize with €1500 made up of 10 x €50s, 30 x €20s, 30 x €10s and 20 x €5s
-	should not dispense funds if the pin is incorrect,
-	cannot dispense more money than it holds,
-	cannot dispense more funds than customer have access to
-	should not expose the customer balance if the pin is incorrect,
-	should only dispense the exact amounts requested,
-	should dispense the minimum number of notes per withdrawal,

## 
## Clone the project
```sh
git clone git@github.com:my1795/VerySimpleATM.git 
cd VerySimpleATM
```
## Installation And Run 

Install the the app by maven command , after cloning the project.

```sh
mvn install
```

To run from an executable jar 

```sh
java -jar /target/VerySimpleATM.jar
```
Swagger UI is enabled after startup visit http://localhost:9500/swagger-ui.html 
Please check Controller Structure and Resource Types via swagger. Then play with the API.

To access database(h2) visit http://localhost:9500/h2-console 
put jdbc url as "jdbc:h2:mem:atm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;"
## Tests and Coverage Report
```sh
mvn test
```
To view test report in browser
```sh
cd /target/site/jacoco
open index.hmtl
```
## Docker
Dockerfile is provided. You can run the app inside of a container 
To do this you need to build the image first:
```sh
docker build -t verysimpleatm:0.1 . 
```
if image is built successfully Run container: 
```sh
docker run -d  -p 9500:9500 verysimpleatm:0.1 
```
Visit pages : http://localhost:9500/swagger-ui.html , http://localhost:9500/h2-console



