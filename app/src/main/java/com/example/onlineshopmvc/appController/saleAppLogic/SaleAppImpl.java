package com.example.onlineshopmvc.appController.saleAppLogic;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.onlineshopmvc.mailSender.MailSender;
import com.example.onlineshopmvc.mailSender.MailSenderImpl;
import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.OrdersModelImpl;
import com.example.onlineshopmvc.appModel.OrdersModel_SaleApp;
import com.example.onlineshopmvc.appModel.Product;
import com.example.onlineshopmvc.appModel.ProductsModelImpl;
import com.example.onlineshopmvc.appModel.ProductsModel_SaleApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.mail.MessagingException;

public class SaleAppImpl implements SaleApp{

    private static SaleApp instance;
    private MailSender mailSender;
    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    private int currentOrdersNumber = 0;

    // Access program models :
    private final OrdersModel_SaleApp ordersModel;
    private final ProductsModel_SaleApp productsModel;

    // Save Data :
    private ArrayList<Product> productsList;
    private ArrayList<Product> shoppingCart;

    // -----------------------------------
    // Singleton Pattern:

    public static SaleApp getSaleAppInstance(Context context) {

        if (SaleAppImpl.instance==null) {
            SaleAppImpl.instance = new SaleAppImpl(context);
        }
        return SaleAppImpl.instance;
    }

    // TODO
    // Nur beim testen, sonst muss private sein:
    public SaleAppImpl(Context context) {

        this.ordersModel = new OrdersModelImpl(context);
        this.productsModel = new ProductsModelImpl(context);
        this.productsList = new ArrayList<>();
        this.shoppingCart = new ArrayList<>();
        this.mailSender = new MailSenderImpl();
    }

    // -----------------------------------
    // Products Functions:

    @Override
    public ArrayList<Product> displayAllProducts() {

        this.productsList = this.productsModel.getAll();
        return this.productsList;
    }

    @Override
    public Product getProductByNr(int productNr) {

        return this.productsModel.getByNr(productNr);

    }

    @Override
    public ArrayList<Product> getProductByName(String title) {

        ArrayList<Product> result = this.productsModel.getByName(title);
        return result;
    }

    // -----------------------------------
    // Orders Functions:

    @Override
    public Product addToShoppingCart(Product product) throws OperationFailedException {

        boolean added = this.shoppingCart.add(product);
        if(added) {
            return product;
        } else {
            throw new OperationFailedException();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Order toOrder(String firstname, String surename, String email, String firstLineAddress, String city, int postcode) throws ProductSoldOutException, OperationFailedException {

        // Order erstellen:
        int orderID = this.getOrderID();
        LocalDate date = LocalDate.now();
        ArrayList<Integer> products = new ArrayList<>();
        for (Product p : this.shoppingCart) {
            products.add(p.getCodeId());
        }
        String customerName = firstname + " " + surename;
        double total = this.getTotal();
        Order order = new Order(orderID,date,customerName,OrderState.AWAITING_PAYMENT,total,products,firstLineAddress,city,postcode,email);

        // überprüfen ob die bestellten Products noch erhältlich sind:
        ArrayList<Integer> orderedProducts = order.getOrderedProducts();
        for (int pNr : orderedProducts) {
            Product product = this.getProductByNr(pNr);
            if (product.getStock() < 1) {
                throw new ProductSoldOutException();
            }
        }

        // Bestellung sperichern:
        boolean addStatement = this.ordersModel.add(order);
        if (!addStatement) {
            throw new OperationFailedException();
        }

        // Product Anzahl ändern:
        for (int pNr : orderedProducts) {
            Product product = this.getProductByNr(pNr);
            int newStockNr = product.getStock()-1;
            boolean updateStatement = this.productsModel.updateStockNr(pNr,newStockNr);

            if (!updateStatement) {
                throw new OperationFailedException();
            }
        }
        return order;
    }

    @Override
    public boolean pay(Order order) throws MessagingException {

        // optimal sollte hier die Zahlung (Transaktionsnummer, Datum ... ) in eine Datenbank gespeichert werden
        // ..

        // Order State ändern:
        boolean updateStatement = this.ordersModel.updateState(order,OrderState.AWAITNG_PACKAGING);
        if (!updateStatement) {
            return false;
        }

        // Shopping Cart aufräumen:
        this.shoppingCart.clear();

        // Bestllung per Email bestätigen:
        this.mailSender.sendMail(order.getEmail(),"Bestätigung deiner Bestellung bei ON-line Shop","Test Email ..");

        return true;
    }

    @Override
    public boolean cancelOrder(int orderNr, String email, String customerName) throws OrderStateException, IncorrectInputsException, MessagingException {

        Order orderToCancel = this.getOrder(orderNr);

        // Parameter überprüfen:
        if ( !orderToCancel.getEmail().equalsIgnoreCase(email) || !orderToCancel.getCustomerName().equalsIgnoreCase(customerName)) {
            throw new IncorrectInputsException();
        }

        // OrderState überprüfen:
        if (orderToCancel.getState().equals(OrderState.SHIPPED)) {
            throw new OrderStateException();
        }

        // Order stonieren und per Email bestätigen:
        boolean cancelStatement = this.ordersModel.updateState(orderToCancel, OrderState.CANCELLD);
        if (!cancelStatement) {
            return false;
        }
        this.mailSender.sendMail(orderToCancel.getEmail(),"Deine Bestellung wurde storniert","Test Email ..");

        return true;
    }

    @Override
    public OrderState getOrderState(int orderNr) throws IncorrectInputsException {

        OrderState orderState = this.getOrder(orderNr).getState();

        return orderState;
    }

    @Override
    public ArrayList<Product> showShoppingCart() {
        if (this.shoppingCart.size()==0) {
            return null;
        }
        return this.shoppingCart;
    }

    // ---------------------------------
    // Private Methods:

    private Order getOrder(int orderNr) throws IncorrectInputsException {
        Order order = this.ordersModel.getOrder(orderNr);
        if(order==null) {
            throw new IncorrectInputsException();
        }
        return order;
    }

    private double getTotal() {
        double total = 0;
        for (Product p : this.shoppingCart) {
            total = total + p.getPries();
        }
        return total;
    }

    private int getOrderID() {
        // Die Bestellungsnummer muss unique sein,
        // deswegen wir importieren die zuletzt gespeicherte Bestellungsnummer, zählen wir sie hoch
        // und dann speichern sie neu in der Datei.
        //this.importOrderCurrentNumber();
        //this.exportOrderCurrentNumber(this.currentOrdersNumber+1);
        int randomOrderID = (int)Math.floor(Math.random()*(999999-100000+1)+100000);
        return randomOrderID;
    }

    private void importOrderCurrentNumber() {
        try {
            this.inputStream = new FileInputStream("src\\orderNumber.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataInputStream dataInputStream = new DataInputStream(this.inputStream);
        try {
            if(dataInputStream.available()>0) {
                this.currentOrdersNumber = dataInputStream.readInt();
            } else {
                this.currentOrdersNumber = 224000;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void exportOrderCurrentNumber(int orderNumber) {
        try {
            this.outputStream = new FileOutputStream("orderNumber2.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream dataOutputStream = new DataOutputStream(this.outputStream);
        try {
            dataOutputStream.write(orderNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}