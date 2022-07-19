package com.example.onlineshopmvc.appController.managementAppLogic;

import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.Product;

import java.util.ArrayList;

public interface ManagementApp {

    /**
     * Login into the management panel
     * @param password password to enter the panel
     * @return True if succeeded
     *         False if failed
     * @throws PasswordException if password not correct
     */
    boolean login(String password) throws PasswordException;

    /**
     * logout from the management panel
     * @return True if succeeded
     *         False if failed
     */
    boolean logout();

    /**
     * change the password for the management panel
     * @param oldPassword the old password, that should be changed
     * @param newPassword the new password
     * @return True if succeeded
     *         False if failed
     * @throws PasswordException if the new password not allowed
     */
    boolean changePassword(String oldPassword,String newPassword) throws PasswordException;

    /**
     * to add new article or update the stock number if the item is already exists
     * @param product product to be added
     * @return the added Product
     * @throws IncorrectInputsException if some of the inputs not correct
     */
    Product addProduct(Product product) throws IncorrectInputsException;

    /**
     * to remove an article
     * @param productNr product number of the product to remove it
     * @return True if succeeded
     *         False if failed
     */
    boolean deleteProduct(int productNr);

    /**
     * clear all the data
     * @return True if succeeded
     *         False if failed
     * @throws NoItemsException if the database already empty
     */
    boolean clearProductsList() throws NoItemsException;

    /**
     * display all products
     * @return an arraylist contains the products
     */
    ArrayList<Product> getAllProducts();

    /**
     * update the product number
     * @param productID the product to update
     * @param newProductID the new product number
     * @return the updated Product
     *         null if failed
     * @throws ProductExistsException if a product with this productID already exists
     */
    Product updateProductId(int productID, int newProductID) throws ProductExistsException;

    /**
     * update the product title
     * @param productID the product to update
     * @param title the new product title
     * @return the updated Product
     *         null if failed
     */
    Product updateProductTitle(int productID, String title);

    /**
     * update the product description
     * @param productID the product to update
     * @param description the new product description
     * @return the updated Product
     *         null if failed
     */
    Product updateProductDescription(int productID, String description);

    /**
     * update the product pries
     * @param productID the product to update
     * @param pries the new product pries
     * @return the updated Product
     *         null if failed
     */
    Product updateProductPries(int productID, double pries);

    /**
     * update number of items
     * @param productID the product to update
     * @param stock the current number of items
     * @return the updated Product
     *         null if failed
     */
    Product updateProductStockNr(int productID, int stock);

    /**
     * display all Orders
     * @return an arraylist contains the orders
     */
    ArrayList<Order> getAllOrders();

    /**
     * change the state of an order
     * @param order Order whose state is to be changed
     * @param state the current state
     * @return current order state
     */
    OrderState changeOrderState(Order order, OrderState state);

    /**
     * to delete an order from list
     * @param order order is to be removed
     * @return True if succeeded
     *         False if failed
     */
    boolean deleteOrder(Order order);

    /**
     * clear the order list
     * @return True if succeeded
     *         False if failed
     * @throws NoItemsException if the database already empty
     */
    boolean clearOrdersList() throws NoItemsException;

    /**
     * Return an order
     * @param orderNr The order number of the desired order
     * @return the desired order if found
     * @throws IncorrectInputsException if order not found
     */
    Order getOrder(int orderNr) throws IncorrectInputsException;

    /**
     * get the product by ProductID
     * @param productNr product number to search
     * @return the product
     */
    Product getProduct(int productNr);
}