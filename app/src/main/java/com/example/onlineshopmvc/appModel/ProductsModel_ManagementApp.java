package com.example.onlineshopmvc.appModel;

import java.util.ArrayList;

public interface ProductsModel_ManagementApp {
    /**
     * to add new article
     * @param product object to add
     * @return True if succeeded
     *         False if failed
     */
    boolean add(Product product);

    /**
     * to remove an article
     * @param productNr product number of the product to remove it
     * @return True if succeeded
     *         False if failed
     */
    boolean remove(int productNr);

    /**
     * clear all the data
     * @return True if succeeded
     *         False if failed
     */
    boolean clear();

    /**
     * get the product by ProductID
     * @param productNr product number to search
     * @return the product
     */
    Product getByNr(int productNr);

    /**
     * get products by product title
     * @param title title to search it
     * @return the products
     */
    ArrayList<Product> getByName(String title);

    /**
     * get all products
     * @return all products
     */
    ArrayList<Product> getAll();

    /**
     * update the product number
     * @param productID the product to update
     * @param newProductID the new product number
     * @return True if succeeded
     *         False if failed
     */
    boolean updateProductId(int productID, int newProductID);

    /**
     * update the product title
     * @param productID the product to update
     * @param title the new product title
     * @return True if succeeded
     *         False if failed
     */
    boolean updateTitle(int productID, String title);

    /**
     * update the product description
     * @param productID the product to update
     * @param description the new product description
     * @return True if succeeded
     *         False if failed
     */
    boolean updateDescription(int productID, String description);

    /**
     * update the product pries
     * @param productID the product to update
     * @param pries the new product pries
     * @return True if succeeded
     *         False if failed
     */
    boolean updatePries(int productID, double pries);

    /**
     * update number of items
     * @param productID the product to update
     * @param stock the current number of items
     * @return True if succeeded
     *         False if failed
     */
    boolean updateStockNr(int productID, int stock);
}