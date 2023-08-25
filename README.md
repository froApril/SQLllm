<center><h1>ELEC5620-G17</h1></center>

<center><h3>SQL-LLM</h3></center>

------

Our project's primary goal is to develop a user-friendly web application that seamlessly translates natural language into SQL query commands. 

The core concept revolves around enabling users, even those without a technical background, to effortlessly connect our application to their database. Through this connection, they can freely retrieve the required data using simple and intuitive human language commands. 

Our system boasts an interactive user interface designed to facilitate the translation process. This interface efficiently channels all commands to a Language Model (LLM), which takes charge of transforming the natural language queries into precise SQL commands. Once the SQL commands are generated, we seamlessly facilitate the retrieval of results and promptly present them through the front-end interface.

------



## Technical stack

SpringBoot 2.7.2      

MySQL

------

## Team

| Name     | Unikey   |
| -------- | -------- |
| Yifei Xu | yixu5881 |
|          |          |
|          |          |
|          |          |
|          |          |

------

## Install

Open with IDEA and import as a Maven project

Set up your personal MySQL database and update the info in config/jdbc.properties as following

```
spring.datasource.default.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.default.jdbc-url=jdbc:mysql://localhost:3306/databaseName?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=UTF-8
spring.datasource.default.username= your username
spring.datasource.default.password= your password
```

  