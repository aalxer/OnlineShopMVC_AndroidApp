package com.example.onlineshopmvc.appModel;

import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;

public interface OrdersModel_SaleApp {

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
     * Get the Order of the OrderNr
     * @param orderNr Order Number
     * @return Order if found or null
     */
    Order getOrder(int orderNr);

    /**
     * update the state of the order
     * @param order Order whose status is to be updated
     * @param state the current state
     * @return True if succeeded
     *         False if failed
     */
    boolean updateState(Order order, OrderState state);
}
