# spring-security-demo
This is a sample project that shows how to implement and configure Spring Security on any Spring Boot REST Web Service application

© 2025 Daniel Pinheiro Maia All Rights Reserved<br>
(see Copyright© License at the end of this text).

[**Description of this repository**]<br>
This is a sample project that shows how to implement and configure Spring Security on any Spring Boot REST Web Service application. It basically allows the sign up and sign in (log in) of any number of users into the system, demanding authentication for granting access to secured endpoint resources, while letting non-secured resources available for general access. The user DB is managed through a JPA CRUD, using an H2 DBMS persisted into a local file (not an in-memory DB). The authentication is done through JWT tokens, generated and validated by the same system - at different requests. You first send a request to log in, in order to get a valid token from the system, then you send another request - with the valid token at the header - so that the system can validate the token and grant you privilege access to some of the resources, only available to registered members. You can easily define, or modify, which resources you wanna secure, by simply setting which endpoints are accessible only by previous authentication (login).   

[**Content and Run**]<br>
Source code available at github.com, through the following link:<br>
[https://github.com/danielpm1982/spring-security-demo](https://github.com/danielpm1982/spring-security-demo)<br>

This app has been developed and run with OpenJDK 21. You can run this app as any other Java REST app. The focus here is not the deploy, but the source code itself... how to use Spring Security when implementing secured REST applications. You can simply see and run the code at your IDE, for example, at IntelliJ. For testing the endpoints, after having the server started, you may use the Swagger interface, at your browser, or WS testing suites as Postman. For more, read the comments at each project class, starting from the main class: SpringSecurityDemoApplication.

[**Printscreen samples**]<br>

![console.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/console.png)

![browser1.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/browser1.png)

![swagger1.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/swagger1.png)

![swagger2.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/swagger2.png)

![postman1.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/postman1.png)

![postman2.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/postman2.png)

![postman3.png](https://raw.githubusercontent.com/danielpm1982/spring-security-demo/refs/heads/master/img/postman3.png)

[**Support**]<br>
If you have any suggestion or correction about the content of this repository, please, feel free to open an issue at the project issues' section:<br>
https://github.com/danielpm1982/spring-security-demo/issues

[**Copyright© License**]<br>
© 2025 Daniel Pinheiro Maia All Rights Reserved<br>
This GitHub repository - and all code (software) available inside - is exclusively for academic and individual learning purposes, and is **NOT AVAILABLE FOR COMMERCIAL USE**, nor has warranty of any type. You're authorized to fork, clone, run, test, modify, branch and merge it, at your own risk and using your own GitHub account, for individual learning purposes only, but you're **NOT ALLOWED to distribute, sublicense and/or sell copies of the whole or of parts of it** without explicit and written consent from its owner / author. You can fork this repository to your individual account at GitHub, clone it to your personal notebook or PC, analyse, run and test its code, modify and extend it locally or remotely (exclusively at your own GitHub account and as a forked repository), as well as send issues or pull-requests to this parent (original) repository for eventual approval. GitHub is in charge of explicitly showing whom this respository has been forked from. **If you wish to use any of this repository content in any way other than what is expressed above, or publish it anyway or anywhere other than as a forked repository at your own GitHub account, please contact this repository owner / author** using GitHub or the contact info below. For the meaning of the technical terms used at this license, please refer to GitHub documentation, at https://help.github.com/en/github .

[**Owner and Author of this GitHub Repository**]<br>
Daniel Pinheiro Maia<br>
[danielpm1982.com](https://www.danielpm1982.com)<br>
danielpm1982@gmail.com<br>
[linkedin.com/in/danielpm1982](https://www.linkedin.com/in/danielpm1982)<br>
Brazil<br>
.
