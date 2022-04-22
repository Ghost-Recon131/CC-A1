# About
API Gateway API endpoints


### Backend
- Localhost: http://localhost:8080/
- AWS EC2 (appmicroservice): http://ec2-23-20-223-11.compute-1.amazonaws.com:8080
- AWS EC2 (transactionmicroservice): http://ec2-23-20-223-11.compute-1.amazonaws.com:8081


# Account
## AccountControllerPublic
| METHOD | Spring Boot endpoint                                                                          | API Gateway Endpoint                                                                              |
|--------|-----------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/RegisterLogin                        | https://0xq8werjoh.execute-api.us-east-1.amazonaws.com/live/RegisterLogin                         |
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/RegisterLogin/login                  | https://0xq8werjoh.execute-api.us-east-1.amazonaws.com/live/RegisterLogin/login                   |
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/RegisterLogin/resetforgottenpassword | https://0xq8werjoh.execute-api.us-east-1.amazonaws.com/live/RegisterLogin/resetforgottenpassword  |


## AccountInfoController
| METHOD | Spring Boot endpoint                                                                   | API Gateway Endpoint                                                                                  |
|--------|----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/AccountInfo/updateAccountInfo | https://bjge6rs3se.execute-api.us-east-1.amazonaws.com/AccountInfo/api/AccountInfo/getAccountInfo     |
| GET    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/AccountInfo/getAccountInfo    | https://bjge6rs3se.execute-api.us-east-1.amazonaws.com/AccountInfo/api/AccountInfo/updateaccountinfo  |


## ItemListingController
| METHOD    | Spring Boot endpoint                                                                            | API Gateway Endpoint                                                                              |
|-----------|-------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| GET       | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/viewAllListings           | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/api/viewAllListings           |
| GET       | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/viewListingByID           | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/api/viewListingByID           |
| POST      | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/newItemListing/{id}       | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/newitemlisting/{id}           |
| POST      | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/addImageToListing/{id}    | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/api/addImageToListing/{id}    |
| GET       | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/getListingImageLinks/{id} | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/api/getListingImageLinks/{id} |
| PUT       | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/modifyItemListing/{id}    | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/api/modifyItemListing/{id}    |
| DELETE    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8080/api/itemListings/deleteItemListing/{id}    | https://ji1fy3w7p2.execute-api.us-east-1.amazonaws.com/item-listing/api/deleteItemListing/{id}    |


## TransactionController
| METHOD | Spring Boot endpoint                                                                    | API Gateway Endpoint                                                                                  |
|--------|-----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| POST   | http://ec2-23-20-223-11.compute-1.amazonaws.com:8081/api/Transactions/createPayment     | https://i5lunowrqh.execute-api.us-east-1.amazonaws.com/transactions/api/Transactions/createPayment    |
| PUT    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8081/api/Transactions/cancelPayment     | https://i5lunowrqh.execute-api.us-east-1.amazonaws.com/transactions/api/Transactions/cancelPayment    |
| PUT    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8081/api/Transactions/successPayment    | https://i5lunowrqh.execute-api.us-east-1.amazonaws.com/transactions/api/Transactions/successPayment   |
| GET    | http://ec2-23-20-223-11.compute-1.amazonaws.com:8081/api/Transactions/getUserPurchases  | https://i5lunowrqh.execute-api.us-east-1.amazonaws.com/transactions/api/Transactions/getUserPurchases |


