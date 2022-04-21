# About
API Gateway API endpoints


### Backend
- Localhost: http://localhost:8080/
- AWS EC2 (appmicroservice): http://ec2-23-20-223-11.compute-1.amazonaws.com:8080
- AWS EC2 (transactionmicroservice): http://ec2-23-20-223-11.compute-1.amazonaws.com:8081

# Account
## AccountControllerPublic

|     |     |     |
|-----|-----|-----|
|     |     |     |
|     |     |     |
|     |     |     |

| METHOD | Spring Boot endpoint                                                                          | API Gateway Endpoint                                                                              |
|--------|-----------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/RegisterLogin                        | https://0xq8werjoh.execute-api.us-east-1.amazonaws.com/live/RegisterLogin                         |
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/RegisterLogin/login                  | https://0xq8werjoh.execute-api.us-east-1.amazonaws.com/live/RegisterLogin/login                   |
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/RegisterLogin/resetforgottenpassword | https://0xq8werjoh.execute-api.us-east-1.amazonaws.com/live/RegisterLogin/resetforgottenpassword  |

## AccountInfoController
| METHOD | Spring Boot endpoint                                                                   | API Gateway Endpoint                                                                                  |
|--------|----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/AccountInfo/updateAccountInfo | https://219fucattd.execute-api.us-east-1.amazonaws.com/AccountInfo/api/AccountInfo/getaccountinfo     |
| GET    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/AccountInfo/getAccountInfo    | https://219fucattd.execute-api.us-east-1.amazonaws.com/AccountInfo/api/AccountInfo/updateaccountinfo  |



## ItemListingController
| METHOD | Spring Boot endpoint                                                                     | API Gateway Endpoint                                                                                  |
|--------|------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| GET    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/viewAllListings    | https://219fucattd.execute-api.us-east-1.amazonaws.com/AccountInfo/api/AccountInfo/getaccountinfo     |
| GET    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/viewListingByID    | https://219fucattd.execute-api.us-east-1.amazonaws.com/AccountInfo/api/AccountInfo/updateaccountinfo  |






