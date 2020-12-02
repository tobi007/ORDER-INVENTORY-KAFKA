# reporting-service

# REST API

The REST API to the reporting-service app is described below.

    server.port: 9001

## Get all Orders Item

### Request

`GET /api/v1/order/all`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9001/api/v1/order/all
    
### Response

    [
        {
            "id": "466388b3-7558-4066-a1fd-9cd63a5bfee0",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 1,
            "orderedOn": "2020-12-02T11:50:01.320+00:00",
            "isProcessed": true
        },
        {
            "id": "55792884-7ab5-429e-8cde-cfbf8874e2f6",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Soap",
            "productPrice": 23.0,
            "productQuantity": 1,
            "orderedOn": "2020-12-02T10:41:53.560+00:00",
            "isProcessed": true
        },
        {
            "id": "75a72178-4d8a-41a1-bbeb-15b300e429fa",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 2,
            "orderedOn": "2020-12-02T14:41:17.283+00:00",
            "isProcessed": true
        },
        {
            "id": "808cc2fa-e031-4880-9117-f7e66d1b6617",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 1,
            "orderedOn": "2020-12-02T19:44:00.462+00:00",
            "isProcessed": true
        },
        {
            "id": "93951b08-cc03-40c0-9daa-c08dc53852cc",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 1,
            "orderedOn": "2020-12-02T10:38:36.377+00:00",
            "isProcessed": true
        },
        {
            "id": "9d59f296-89c6-43c9-9a23-65ee7101eda6",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 5,
            "orderedOn": "2020-12-02T10:29:19.002+00:00",
            "isProcessed": true
        },
        {
            "id": "a1dd309e-f534-498a-a782-11821e6bd8c7",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 5,
            "orderedOn": "2020-12-02T10:23:31.568+00:00",
            "isProcessed": true
        },
        {
            "id": "b9a783cf-ee04-4d72-ac5c-14ead35f8480",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Garri",
            "productPrice": 999.0,
            "productQuantity": 5,
            "orderedOn": "2020-12-02T10:27:56.378+00:00",
            "isProcessed": true
        },
        {
            "id": "fcd6eb81-3139-4252-8f18-37380a8606b0",
            "customerEmail": "ekayode700@gmail.com",
            "productName": "Soap",
            "productPrice": 23.0,
            "productQuantity": 1,
            "orderedOn": "2020-12-02T10:41:15.136+00:00",
            "isProcessed": true
        }
    ]
    
    
    
    
## Get all Orders Item Group By Date

### Request

`GET /api/v1/order/group/date/all`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9001/api/v1/order/group/date/all
    
### Response

    {
        "totalOrderCount": 9,
        "totalOrderAmount": 20026.0,
        "orders": {
            "2020-12-02": [
                {
                    "id": "466388b3-7558-4066-a1fd-9cd63a5bfee0",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T11:50:01.320+00:00",
                    "isProcessed": true
                },
                {
                    "id": "55792884-7ab5-429e-8cde-cfbf8874e2f6",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Soap",
                    "productPrice": 23.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T10:41:53.560+00:00",
                    "isProcessed": true
                },
                {
                    "id": "75a72178-4d8a-41a1-bbeb-15b300e429fa",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 2,
                    "orderedOn": "2020-12-02T14:41:17.283+00:00",
                    "isProcessed": true
                },
                {
                    "id": "808cc2fa-e031-4880-9117-f7e66d1b6617",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T19:44:00.462+00:00",
                    "isProcessed": true
                },
                {
                    "id": "93951b08-cc03-40c0-9daa-c08dc53852cc",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T10:38:36.377+00:00",
                    "isProcessed": true
                },
                {
                    "id": "9d59f296-89c6-43c9-9a23-65ee7101eda6",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 5,
                    "orderedOn": "2020-12-02T10:29:19.002+00:00",
                    "isProcessed": true
                },
                {
                    "id": "a1dd309e-f534-498a-a782-11821e6bd8c7",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 5,
                    "orderedOn": "2020-12-02T10:23:31.568+00:00",
                    "isProcessed": true
                },
                {
                    "id": "b9a783cf-ee04-4d72-ac5c-14ead35f8480",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 5,
                    "orderedOn": "2020-12-02T10:27:56.378+00:00",
                    "isProcessed": true
                },
                {
                    "id": "fcd6eb81-3139-4252-8f18-37380a8606b0",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Soap",
                    "productPrice": 23.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T10:41:15.136+00:00",
                    "isProcessed": true
                }
            ]
        }
    }
    
    
## Get all Orders Item By Date Range and Group By Date

### Request

`GET /api/v1/order/group/daterange`

    curl -H "Content-Type: application/json" -X GET  http://localhost:9001/api/v1/order/group/daterange?from=2020-12-01&to=2020-12-03
    
### Response

    {
        "totalOrderCount": 9,
        "totalOrderAmount": 20026.0,
        "orders": {
            "2020-12-02": [
                {
                    "id": "466388b3-7558-4066-a1fd-9cd63a5bfee0",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T11:50:01.320+00:00",
                    "isProcessed": true
                },
                {
                    "id": "55792884-7ab5-429e-8cde-cfbf8874e2f6",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Soap",
                    "productPrice": 23.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T10:41:53.560+00:00",
                    "isProcessed": true
                },
                {
                    "id": "75a72178-4d8a-41a1-bbeb-15b300e429fa",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 2,
                    "orderedOn": "2020-12-02T14:41:17.283+00:00",
                    "isProcessed": true
                },
                {
                    "id": "808cc2fa-e031-4880-9117-f7e66d1b6617",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T19:44:00.462+00:00",
                    "isProcessed": true
                },
                {
                    "id": "93951b08-cc03-40c0-9daa-c08dc53852cc",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T10:38:36.377+00:00",
                    "isProcessed": true
                },
                {
                    "id": "9d59f296-89c6-43c9-9a23-65ee7101eda6",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 5,
                    "orderedOn": "2020-12-02T10:29:19.002+00:00",
                    "isProcessed": true
                },
                {
                    "id": "a1dd309e-f534-498a-a782-11821e6bd8c7",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 5,
                    "orderedOn": "2020-12-02T10:23:31.568+00:00",
                    "isProcessed": true
                },
                {
                    "id": "b9a783cf-ee04-4d72-ac5c-14ead35f8480",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Garri",
                    "productPrice": 999.0,
                    "productQuantity": 5,
                    "orderedOn": "2020-12-02T10:27:56.378+00:00",
                    "isProcessed": true
                },
                {
                    "id": "fcd6eb81-3139-4252-8f18-37380a8606b0",
                    "customerEmail": "ekayode700@gmail.com",
                    "productName": "Soap",
                    "productPrice": 23.0,
                    "productQuantity": 1,
                    "orderedOn": "2020-12-02T10:41:15.136+00:00",
                    "isProcessed": true
                }
            ]
        }
    }