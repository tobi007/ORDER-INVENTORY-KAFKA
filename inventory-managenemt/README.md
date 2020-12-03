# inventory-managenemt service

![inventory-management-kafka-topology](./inventory-management-kafka-topology.png?raw=true "inventory-management-kafka-topology")

# REST API

The REST API to the inventory-managenemt service app is described below.

        server.port: 9000

## Create a new Product Item

### Request

`POST /api/v1/product/create`

    curl -d '{"name": "Phone", "description": "Random desaaa", "price": 23.3}' -H "Content-Type: application/json" -X POST http://localhost:9000/api/v1/product/create

### Response
    
    {
        "status": true,
        "description": "",
        "data": null
    }

## Update a Product Item

### Request

`POST /api/v1/product/update`

    curl -d '{"name": "Phone", "description": "Random desaaa", "price": 98.65}' -H "Content-Type: application/json" -X POST http://localhost:9000/api/v1/product/create

### Response

    {
        "status": true,
        "description": "",
        "data": null
    }

## Get all Products Item

### Request

`GET /api/v1/product/all`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9000/api/v1/product/all
    
### Response

    {
        "status": true,
        "description": "",
        "data": [
            {
                "name": "Garri",
                "description": "Hear all thoings",
                "price": 999.0, 
                "isAvailable": true
            },
            {
                "name": "Phone",
                "description": "Random desaaa",
                "price": 23.3,
                "isAvailable": false
            },
            {
                "name": "Soap",
                "description": "Baff Wella",
                "price": 23.0,
                "isAvailable": true
            }
        ]
    }
    
    
## Get all Available Products Item

### Request

`GET /api/v1/product/available`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9000/api/v1/product/available
    
### Response

    {
        "status": true,
        "description": "",
        "data": [
            {
                "name": "Garri",
                "description": "Hear all thoings",
                "price": 999.0, 
                "isAvailable": true
            },
            {
                "name": "Soap",
                "description": "Baff Wella",
                "price": 23.0,
                "isAvailable": true
            }
        ]
    }
    

## Get Product Item by name

### Request

`GET /api/v1/product?name=Soap`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9000/api/v1/product?name=Soap    
### Response

    {
        "status": true,
        "description": "",
        "data":{
            "name": "Soap",
            "description": "Baff Wella",
            "price": 23.0,
            "isAvailable": true
        }
    }
    
    

## Get all Inventories Item

### Request

`GET /api/v1/inventory/all`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9000/api/v1/inventory/all
    
### Response

    {
        "status": true,
        "description": "",
        "data": [
            {
                "productName": "Garri",
                "quantity": 31,
                "created": "2020-12-02T10:22:26.461+00:00",
                "updated": "2020-12-02T14:41:21.394+00:00"
            },
            {
                "productName": "Phone",
                "quantity": 0,
                "created": "2020-12-02T19:06:19.216+00:00",
                "updated": "2020-12-02T19:06:19.216+00:00"
            },
            {
                "productName": "Soap",
                "quantity": 8,
                "created": "2020-12-02T10:22:05.384+00:00",
                "updated": "2020-12-02T10:42:14.250+00:00"
            }
        ]
    }
    
    
## Get inventory Item by product name

### Request

`GET /api/v1/inventory?productName=Soap`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9000/api/v1/inventory?productName=Soap    
### Response

    {
        "status": true,
        "description": "",
        "data": {
            "productName": "Soap",
            "quantity": 8,
            "created": "2020-12-02T10:22:05.384+00:00",
            "updated": "2020-12-02T10:42:14.250+00:00"
        }
    }
    

## Update inventory Quantity by product name

### Request

`POST /api/v1/inventory/update`

    curl -d '{"productName": "Soap", "quantity": 10, "isRestock": true}' -H "Content-Type: application/json" -X POST http://localhost:9000/api/v1/inventory/update

### Response 

    {
        "status": true,
        "description": "",
        "data": null
    }

## Create a new Order Item

### Request

`POST /api/v1/order/create`

    curl -d '{"productName": "Garri", "productQuantity": 1, "customerEmail": "ekayode700@gmail.com"}' -H "Content-Type: application/json" -X POST http://localhost:9000/api/v1/order/create

### Response 

     {
         "status": true,
         "description": "",
         "data": null
     }