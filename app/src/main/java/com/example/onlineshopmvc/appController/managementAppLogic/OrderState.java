package com.example.onlineshopmvc.appController.managementAppLogic;

public enum OrderState {
    PENDING,
    AWAITING_PAYMENT,
    AWAITING_FULFILLMENT,
    AWAITNG_PACKAGING,
    AWAITING_SHIPMENT,
    SHIPPED,
    DELIVERED,
    CANCELLD,
    DECLINED,
    REFUNDED,
}
