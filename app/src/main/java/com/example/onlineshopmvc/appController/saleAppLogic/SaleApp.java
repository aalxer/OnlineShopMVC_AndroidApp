package com.example.onlineshopmvc.appController.saleAppLogic;

import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.Product;

import java.util.ArrayList;

import javax.mail.MessagingException;

public interface SaleApp {

    /**
     * display all products
     * @return an arraylist contains the products
     */
    ArrayList<Product> displayAllProducts();

    /**
     * get the product by ProductID
     * @param productNr product number to search
     * @return the product
     */
    Product getProductByNr(int productNr);

    /**
     * get products by product title
     * @param title title to search it
     * @return the products
     */
    ArrayList<Product> getProductByName(String title);

    /**
     * to add an item to the shopping basket
     * @param product the product to be added
     * @return the added product
     * @throws OperationFailedException if the add failed
     */
    Product addToShoppingCart(Product product) throws OperationFailedException;

    /**
     * to add an order to the Orders Database as a first step to confirm the order
     * default state for the first step : AWAITING_PAYMENT
     * @param
     * @return the ordered Order, if succeeded
     * @throws ProductSoldOutException if one of the Products sold out
     * @throws OperationFailedException if the add or update an order failed
     */
    Order toOrder(String firstname, String surename, String email, String firstLineAddress, String city, int postcode) throws ProductSoldOutException, OperationFailedException;

    /**
     * After the Payment-API confirm the Payment, this method will change the order state and confirms the customer's order by email
     * @param order order which has been paid
     * @return true if succeeded
     *         false if field
     * @throws MessagingException if the confirm email cannot be sent
     */
    boolean pay(Order order) throws MessagingException;

    /**
     * to cancel an order
     * @param orderNr the order to be canceled
     * @param email the email address from the order
     * @param customerName Name of the customer who ordered the order
     * @return true if succeeded
     *         false if field
     * @throws OrderStateException if the order cannot be canceled anymore
     * @throws IncorrectInputsException if the order information not correct
     * @throws MessagingException if the confirm email cannot be sent
     */
    boolean cancelOrder(int orderNr, String email, String customerName) throws OrderStateException, IncorrectInputsException, MessagingException;

    /**
     * Show the current state of the order
     * @param orderNr Order Number
     * @return current order state
     * @throws IncorrectInputsException if the order number not correct
     */
    OrderState getOrderState(int orderNr) throws IncorrectInputsException;

    /**
     * return all products that have been added to the shopping cart
     * @return liste of products
     */
    ArrayList<Product> showShoppingCart();
}