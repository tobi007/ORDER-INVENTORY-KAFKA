package com.tobi.order.processor.commons.constant;

public class MessagingConstant {
    public static final String INVENTORY_MANAGEMENT_GROUP_ID = "order_processor_inventory_management_group_id";
    public static final String REPORTING_SERVICE_GROUP_ID = "order_processor_reporting_service_group_id";


    public static final String PRODUCT_CREATION_TOPIC_NAME = "product_creation";
    public static final String PRODUCT_UPDATE_TOPIC_NAME = "product_update";
    public static final String PRODUCT_CREATION_SOURCE_NAME = "product_creation_source";
    public static final String PRODUCT_UPDATE_SOURCE_NAME = "product_update_source";
    public static final String PRODUCT_CREATION_PROCESSOR_NAME = "product_creation_processor";
    public static final String PRODUCT_UPDATE_PROCESSOR_NAME = "product_update_processor";
    public static final String PRODUCTS_K_TABLE_NAME = "nopain-products-k-table";


    public static final String INVENTORY_UPDATE_TOPIC_NAME = "inventory_update";
    public static final String INVENTORY_UPDATE_SOURCE_NAME = "inventory_update_source";
    public static final String INVENTORY_CREATION_PROCESSOR_NAME = "inventory_creation_processor";
    public static final String INVENTORY_UPDATE_PROCESSOR_NAME = "inventory_update_processor";
    public static final String INVENTORIES_K_TABLE_NAME = "nopain-inventories-k-table";

    public static final String ORDER_CREATION_TOPIC_NAME = "order_creation";
    public static final String ORDER_CREATION_SOURCE_NAME = "order_creation_source";
    public static final String ORDER_CREATION_PROCESSOR_NAME = "order_creation_processor";
    public static final String ORDERS_K_TABLE_NAME = "nopain-orders-k-table";

    public static final String ORDER_REPORT_CREATION_TOPIC_NAME = "order_report_creation";
    public static final String ORDER_REPORT_CREATION_SOURCE_NAME = "order_report_creation_source";
    public static final String ORDER_REPORT_CREATION_PROCESSOR_NAME = "order_report_creation_processor";
    public static final String ORDERS_REPORT_K_TABLE_NAME = "nopain-orders-report-k-table";

}
