package com.example.onlineshopmvc.appController.managementAppLogic;

import android.content.Context;

import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.OrdersModel_ManagementApp;
import com.example.onlineshopmvc.appModel.OrdersModelImpl;
import com.example.onlineshopmvc.appModel.Product;
import com.example.onlineshopmvc.appModel.ProductsModel_ManagementApp;
import com.example.onlineshopmvc.appModel.ProductsModelImpl;

import java.util.ArrayList;

public class ManagementAppImpl implements ManagementApp {

    private static ManagementApp instance;

    // Access program models :
    private final OrdersModel_ManagementApp ordersModel;
    private final ProductsModel_ManagementApp productsModel;

    // Save Models Data :
    private ArrayList<Product> productsList;
    private ArrayList<Order> ordersList;

    // Login Info :
    private String PASS = "123";
    int counter = 0;

    // -----------------------------------
    // Singleton Pattern

    public static ManagementApp getAppManagementInstance(Context context) {

        if (ManagementAppImpl.instance==null) {
            ManagementAppImpl.instance = new ManagementAppImpl(context);
        }
        return ManagementAppImpl.instance;
    }

    private ManagementAppImpl(Context context) {

        this.ordersModel = new OrdersModelImpl(context);
        this.productsModel = new ProductsModelImpl(context);
        this.productsList = new ArrayList<>();
        this.ordersList = new ArrayList<>();
    }

    // -----------------------------------
    // Login/Logout

    @Override
    public boolean login(String password) throws PasswordException {

        if (this.counter == 3) {
            throw new PasswordException();
        }

        if (password.equals(this.PASS)) {
            this.counter = 0;
            return true;
        } else {
            this.counter++;
            return false;
        }
    }

    // TODO
    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) throws PasswordException {

        if (newPassword.length() < 8 || newPassword.equals(oldPassword)) {
            throw new PasswordException();
        }

        if (oldPassword.equals(this.PASS)) {
            this.PASS = newPassword;
            return true;
        } else {
            return false;
        }
    }

    // -----------------------------------
    // Products Methods

    @Override
    public Product addProduct(Product product) throws IncorrectInputsException {

        if (String.valueOf(product.getCodeId()).length() < 5) {
            throw new IncorrectInputsException();
        }

        Product p = this.productsModel.getByNr(product.getCodeId());

        // Falls Product bereits existiert, dann StockNr aktualisieren:
        if (p != null) {
            if (this.productsModel.updateStockNr(p.getCodeId(),(p.getStock()+product.getStock()))) {
                return this.productsModel.getByNr(p.getCodeId());
            } else {
                throw new IncorrectInputsException();
            }
            // Wenn nicht existiert dann neu hinzufügen:
        } else {
            if (this.productsModel.add(product)) {
                return this.productsModel.getByNr(product.getCodeId());
            } else {
                throw new IncorrectInputsException();
            }
        }
    }

    @Override
    public boolean deleteProduct(int productNr) {

        if (this.productsModel.remove(productNr)) {
            this.getAllProducts();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean clearProductsList() throws NoItemsException {

        if (!this.productsModel.clear()) {
            throw new NoItemsException();
        }

        if (this.productsModel.clear()) {
            this.getAllProducts();
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Product> getAllProducts() {

        // speichern um später Berechnungen damit zu machen :
        this.productsList = this.productsModel.getAll();
        return this.productsList;
    }

    @Override
    public Product updateProductId(int productID, int newProductID) throws ProductExistsException {

        if (this.productsModel.getByNr(newProductID) != null) {
            throw new ProductExistsException();
        }

        if (this.productsModel.updateProductId(productID, newProductID)) {
            // Liste aktualisieren:
            this.getAllOrders();
            return this.productsModel.getByNr(newProductID);
        }
        return null;
    }

    @Override
    public Product updateProductTitle(int productID, String title) {

        if (this.productsModel.updateTitle(productID, title)) {
            // Liste aktualisieren:
            this.getAllOrders();
            return this.productsModel.getByNr(productID);
        }
        return null;
    }

    @Override
    public Product updateProductDescription(int productID, String description) {

        if (this.productsModel.updateDescription(productID, description)) {
            // Liste aktualisieren:
            this.getAllOrders();
            return this.productsModel.getByNr(productID);
        }
        return null;
    }

    @Override
    public Product updateProductPries(int productID, double pries) {

        if (this.productsModel.updatePries(productID, pries)) {
            // Liste aktualisieren:
            this.getAllOrders();
            return this.productsModel.getByNr(productID);
        }
        return null;
    }

    @Override
    public Product updateProductStockNr(int productID, int stock) {

        if (this.productsModel.updateStockNr(productID, stock)) {
            // Liste aktualisieren:
            this.getAllOrders();
            return this.productsModel.getByNr(productID);
        }
        return null;
    }

    @Override
    public Product getProduct(int productNr) {
        return this.productsModel.getByNr(productNr);
    }

    // -----------------------------------
    // Orders Methods

    @Override
    public Order getOrder(int orderNr) throws IncorrectInputsException {

        for (Order o : this.ordersList) {
            if (o.getOrderID()==orderNr) {
                return o;
            }
        }

        throw new IncorrectInputsException();
    }

    @Override
    public ArrayList<Order> getAllOrders() {

        // speichern um später Berechnungen damit zu machen
        this.ordersList = this.ordersModel.getAll();

        if(this.ordersList.size()!=0) {
            return this.ordersList;
        }
        return null;
    }

    @Override
    public OrderState changeOrderState(Order order, OrderState state) {

        if (this.ordersModel.updateState(order, state)) {
            return state;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteOrder(Order order) {

        return this.ordersModel.remove(order.getOrderID());
    }

    @Override
    public boolean clearOrdersList() throws NoItemsException {

        if (!this.ordersModel.clear()) {
            throw new NoItemsException();
        }

        return this.ordersModel.clear();
    }
}