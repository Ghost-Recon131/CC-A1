# Student Info Package

### About
Contains code for generating 'StudentInfo' objects and data manipulation methods. 

### Model
StudentInfo: Defines and creates the 'StudentInfo' object + details that will be saved to database

All Model objects that needs to be saved to database requires an @Entity annotation

### Repository
StudentInfoRepository: Extends JPA repository, allows program to interact with 'StudentInfo' objects with methods such as 
.getById() and .save() without writing SQL statements manually. Also handles getting and saving objects to SQL database.

Repositories require a @Repository annotation. 

### Requests
StudentInfoRequest: Takes in @RequestBody requests from the 'StudentInfoController' and generates an object containing 
data as needed. 

### Services
StudentInfoService: Most of the program logic for 'StudentInfo', used by ''StudentInfoController' to perform actions on 
'StudentInfo' data such as creating, editing etc. All changes then saved to 'StudentInfoRepository'.

Services that the controller class uses must be annotated with @Service.