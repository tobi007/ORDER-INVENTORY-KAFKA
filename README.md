# PRODUCT ORDER INVENTORY PROCESSOR API WITH KAFKA

This is a basic application for managing product inventory, creating order based on inventory and generating report.

The entire application is divided into two application services `inventory-managenemt` and `reporting-service`.

it contains a `docker-compose.yml` file for starting the kafka server to handle messaging between two applications

## Services

- **[inventory-managenemt](./inventory-managenemt/README.md)**
- **[reporting-service](./reporting-service/README.md)**