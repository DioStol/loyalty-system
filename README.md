# Welcome to loyalty system

- ## Descrption

The loyalty system web service has been created to meet the needs of rewarding the users of a bank with points for each transaction. More specific a user can earn money for each inside transaction.

- 1 pending point for every euro until 5000 euro value of transaction
- 2 pending points for every euro from 5001 euro to 7500 euro value of transaction
- 3 pending points from 7501 euro value of transaction

New pending points become  available points for use at the end of every week if:

-       The customer has spent at least 500 euro that week

-       At least one transaction exists for that customer on every day of the week

-       A user will lose all the points if no transaction was made in the last 5 weeks

A customer can use available points to invest in stocks, forex, or any other investment that would like. In addition, a customer can request for historical data such as cutomer's transactions, available points, pending points, investments and other.


- ## Usage

First of all you need to clone the project, and run it!


#### Create customer

POST http://localhost:8080/v1/customers/register

*Request body:*
```json
{
    "name":"Your name",
    "balance":10000
}
```
*Response:* customer id


#### Customer info

GET http://localhost:8080/v1/customers?id=0

*Request param: customer id* 

*Response:*
```json    
{
    "id": 0,
    "name": "Your name",
    "balance": 1001020.0
}
```
or if you have made some transactions
```json
{
    "id": 0,
    "name": "Dionysios STOLIS",
    "balance": 986020.0,
    "points": [
        {
            "status": "PENDING",
            "amount": 5000,
            "date": "2020-10-16",
            "earnings": 50.0,
            "transactionId": 0
        },
        {
            "status": "PENDING",
            "amount": 5000,
            "date": "2020-10-16",
            "earnings": 50.0,
            "transactionId": 1
        },
        {
            "status": "PENDING",
            "amount": 5000,
            "date": "2020-10-16",
            "earnings": 50.0,
            "transactionId": 2
        }
    ]
}
```

#### Add income

POST http://localhost:8080/v1/customers/income?id=0&income=20

*Request param: customer id, income*

*Response:* 
Customer info

#### Make transaction

POST http://localhost:8080/v1/customers/register

*Request body:*
```json
{
    "senderId":0,
    "recipientId":1,
    "amount":5000
}
```
*Response:* 
```json
{
    "id": 2,
    "date": "2020-10-16",
    "senderId": 0,
    "recipientId": 1,
    "amount": 5000.0
}
```
