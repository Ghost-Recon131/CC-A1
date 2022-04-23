# CC Assignment 1
Jingxuan Feng s3843790

# About
Backend source code for Cloud Computing COSC2626 A1

Frontend at: https://github.com/Ghost-Recon131/CC-A1-FrontEnd

## Docker 
- Compile backend with mvn package
```
docker build -t <name>microservice .
```
- Run docker containers locally
```
docker run -it -p  externalPort:dockerPort imageName
```

## Created User accounts

| Accounts | fullName        | username                      | password               | secretQuestion                                  | secretQuestionAnswer  |
|----------|-----------------|-------------------------------|------------------------|-------------------------------------------------|-----------------------|
| 1        | Tom Black       | tomblack@gmail.com            | password               | What is the name of your first dog              | Ella                  |
| 2        | John Doe        | johndoe@gmail.com             | password               | What is your favorite fruit                     | Apple                 |                                     
| 3        | Lavina Woodward | lavinawoodward@protonmail.com | lavinawoodward123      | What is your favourite song                     | Irony                 |
| 4        | Irina Qindove   | irina@outlook.com             | irinaQindove           | What is the most annoying task in C languages   | Memory management     |
Note: #4 Will be created during demo

## References
* [1] Java Techie, Amazon API Gateway | Access Your Spring Boot Microservice | JavaTechie, (Aug. 22, 2020). Accessed: Apr. 23, 2022. [Online Video]. Available: https://www.youtube.com/watch?v=3iwItfr4N38
* [2] Upasana, ‘AWS Java SDK v2 - S3 File upload & download’. https://www.javacodemonk.com/aws-java-sdk-2-s3-file-upload-download-spring-boot-c1a3e072 (accessed Apr. 23, 2022).
* [3] Be A Better Dev, How to Deploy a Docker App to AWS using Elastic Container Service (ECS), (Sep. 07, 2020). Accessed: Apr. 23, 2022. [Online Video]. Available: https://www.youtube.com/watch?v=zs3tyVgiBQQ
* [4] N. Arif, ‘PayPal Integration with Spring Boot’, Medium, Jun. 10, 2021. https://najeebarif.medium.com/paypal-integration-with-spring-boot-f1e297d76336 (accessed Apr. 23, 2022).
* [5] ‘Random Name Generator &mdash; Easy Random Name Picker’, Random Word Generator. https://randomwordgenerator.com/name.php (accessed Apr. 23, 2022).