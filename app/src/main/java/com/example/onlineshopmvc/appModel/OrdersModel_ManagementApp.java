package com.example.onlineshopmvc.appModel;

import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;

import java.util.ArrayList;

public interface OrdersModel_ManagementApp {

    /**
     * Add new order to Database
     * @param order the order to be added
     * @return True if succeeded
     *         False if failed
     */
    boolean add(Order order);

    /**
     * Delete an order from database
     * @param orderID the order to be removed
     * @return True if succeeded
     *         False if failed
     */
    boolean remove(int orderID);

    /**
     * clear all the data
     * @return True if succeeded
     *         False if failed
     */
    boolean clear();

    /**
     * Display all orders
     * @return an arraylist contains the orders
     */
    ArrayList<Order> getAll();

    /**
     * update the state of the order
     * @param order Order whose status is to be updated
     * @param state the current state
     * @return True if succeeded
     *         False if failed
     */
    boolean updateState(Order order, OrderState state);
}
