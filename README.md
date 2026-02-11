# Customer On-boarding 

A end user register the application and processing officer will notify or take the decision to approve/reject/pending the application

---

## 📦 Features

- ✅ Customer On boarding with file upload (image/ pdf)
- ✅ Send notification 
- ✅ State change of the application by processing officer

---

## 🛠️ Technologies Used

- Angular
- Spring Boot 
- PostgreSQL
- Websocket (for notification)
- Kafka

---

## 📂 Project Structure

/src

├── onboarding / Angular Application

└── customer / Spring Boot application

---

## ⚙️ Getting Started

### Prerequisites

- Java 17
- node 20 
- Angular 14

### Back-end Setup
#### Kafka server setup for messaging queue
##### start zookeeper and kafka server
 - .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
 - .\bin\windows\kafka-server-start.bat .\config\server.properties
 
##### create topics 
 - .\bin\windows\kafka-topics.bat --create --topic applicant-registration --bootstrap-server localhost:9092 --partitions 1 ^--replication-factor 1
 - .\bin\windows\kafka-topics.bat --create --topic applicant-submission-event --bootstrap-server localhost:9092 --partitions 1 ^--replication-factor 1
 
#### Database setup
- create database onboarding (check application.properties file and update the database property accordingly)

### Front-end setup
#### use below command to run the project
- navigate to parent directory onboarding and run 
- ``npm install``
- once the npm install completed then use
- ``npm start``
#### navigation and user cred
- open the browser and navigate to ``http:\\localhost:4200``

- to login as as processing officer click on the login buttion and enter any validemail and any password to store the data in session
- example user id and password 
- userId: ``a@a.com``, password: ``123``




 
