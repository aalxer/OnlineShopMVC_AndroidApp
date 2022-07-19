package com.example.onlineshopmvc.appModel;

import java.util.ArrayList;

public interface ProductsModel_SaleApp {

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
     * update number of items
     * @param productID the product to update
     * @param stock the current number of items
     * @return True if succeeded
     *         False if failed
     */
    boolean updateStockNr(int productID, int stock);
}
