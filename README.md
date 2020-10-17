# Welcome to loyalty system

- ## Descrption

The loyalty system web service has been created to meet the needs of rewarding the users of a bank with points for each transaction. More specific a user can earn money for each bank-internal transaction.

- 1 pending point for every euro until 5000 euro value of transaction
- 2 pending points for every euro from 5001 euro to 7500 euro value of transaction
- 3 pending points from 7501 euro value of transaction

New pending points become  available points for use at the end of every week if:

-       The customer has spent at least 500 euro that week

-       At least one transaction exists for that customer on every day of the week

-       A user will lose all the points if no transaction was made in the last 5 weeks

-       Every point is worth 1 eurocent

Customers can use available points to invest in stocks, forex, or any other investment that they would like. In addition, a customer can request for historical data such as cutomer's transactions, available points, pending points, investments and other.


- ## Usage

First of all you need to clone the project, and run it!


#### Create customer

POST http://localhost:8080/v1/customers/register

*Request body:*
```json
{
    "id":0,
    "name":"Jane Doe",
    "balance":10000.0
}
```
*Response: customer id* 


#### Customer info

GET http://localhost:8080/v1/customers?id=0

*Request param: customer id* 

*Response:*
```json    
{
    "id": 0,
    "name": "Jane Doe",
    "balance": 1001020.0
}
```
or if you have made some transactions
```json
{
    "id": 0,
    "name": "Jane Doe",
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

PUT http://localhost:8080/v1/customers/income?id=0&income=20

*Request param: customer id, income*

*Response:* 
Customer info

#### Make transaction

POST http://localhost:8080/v1/transactions

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

#### Get all transactions

GET http://localhost:8080/v1/transactions?id=0

*Request body: custmoer id*

*Response:* 
```json
{
    "transactions": [
        {
            "id": 0,
            "date": "2020-10-16",
            "senderId": 0,
            "recipientId": 1,
            "amount": 5000.0
        },
        {
            "id": 1,
            "date": "2020-10-16",
            "senderId": 0,
            "recipientId": 1,
            "amount": 5000.0
        },
        {
            "id": 2,
            "date": "2020-10-16",
            "senderId": 0,
            "recipientId": 1,
            "amount": 8000.0
        }
    ],
    "total": 18000.0
}
```

#### Use earnings from points

POST http://localhost:8080/v1/customers/invest

*Request body:*
```json
{
    "customerId":0,
    "description": "Invest in stocks",
    "balance":5000
}

*Response:* 
```json
{
    "id": 0,
    "description": "Invest in stocks",
    "balance": 5000.0,
    "customerId": 0,
    "date": "2020-10-16"
}
```

#### Get investments

GET http://localhost:8080/v1/customers/invest?id=0

*Request body: customer id*

*Response:* 
```json
{
    "investments": [
        {
            "id": 0,
            "description": "Invest in bitcoin",
            "balance": 500.0,
            "customerId": 0,
            "date": "2020-10-16"
        },
        {
            "id": 1,
            "description": "Invest in ethereum",
            "balance": 250.0,
            "customerId": 0,
            "date": "2020-10-16"
        }
    ],
    "total": 750.0
}
```

#### Get pending points

GET http://localhost:8080/v1/customers/pending/points?id=0

*Request parameter: customer id*

*Response:* 
```json
{
    "points": [
        {
            "status": "PENDING",
            "amount": 11500,
            "date": "2020-10-16",
            "earnings": 115.0,
            "transactionId": 7
        },
        {
            "status": "PENDING",
            "amount": 11500,
            "date": "2020-10-16",
            "earnings": 115.0,
            "transactionId": 8
        },
        {
            "status": "PENDING",
            "amount": 11500,
            "date": "2020-10-16",
            "earnings": 115.0,
            "transactionId": 9
        },
        {
            "status": "PENDING",
            "amount": 11500,
            "date": "2020-10-16",
            "earnings": 115.0,
            "transactionId": 10
        }
    ],
    "total": 460.0
}
```

#### Get available points

GET http://localhost:8080/v1/customers/available/points?id=0

*Request parameter: customer id*

*Response:* 
```json
{
    "points": [
        {
            "status": "AVAILABLE",
            "amount": 11500,
            "date": "2020-10-16",
            "earnings": 55.0,
            "transactionId": 6
        }
    ],
    "total": 55.0
}
```


