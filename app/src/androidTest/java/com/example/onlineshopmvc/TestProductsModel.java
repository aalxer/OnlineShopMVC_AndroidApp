package com.example.onlineshopmvc;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appModel.Product;
import com.example.onlineshopmvc.appModel.ProductsModelImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class TestProductsModel {

    private ProductsModelImpl productsModel;
    private String text = "Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test Einfach ein Test";

    @Before
    public void createDatebase() {
        Context context = ApplicationProvider.getApplicationContext();
        productsModel = new ProductsModelImpl(context);
    }

    @After
    public void closeDatabase() {
        productsModel.close();
    }

    @Test
    public void testAddProduct() throws IncorrectInputsException {

        productsModel.add(new Product(425435,"Product1",text,40.43,54));
        productsModel.add(new Product(123266,"Product2",text,643.43,24));
        Assert.assertTrue(productsModel.add(new Product(1967466,"Product3",text,24.7,43)));

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testGetAllProducts() {

        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));
        productsModel.add(new Product(5112,"Title3","Einfach ein Test",443.43,3));

        ArrayList<Product> products = productsModel.getAll();
        System.out.println(products);

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testGetByID() {
        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));
        // wurde so programmiert dass getAll() zuerst aufgerufen wird
        productsModel.getAll();

        Product p = productsModel.getByNr(4355);
        System.out.println(p);

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testGetByName() {
        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));
        // wurde so programmiert dass getAll() zuerst aufgerufen wird
        productsModel.getAll();
        ArrayList<Product> p = productsModel.getByName("Title2");
        System.out.println(p);

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testUpdateID() {

        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));
        Assert.assertTrue(productsModel.updateProductId(4355,200));

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testUpdateTitle() {
        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));

        Assert.assertTrue(productsModel.updateTitle(4355,"Updated Title"));

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();

    }

    @Test
    public void testUpdateDes() {
        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));

        Assert.assertTrue(productsModel.updateDescription(4355,"updated Description"));
        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testUpdatePries() {
        productsModel.add(new Product(4355,"Title2","Einfach ein Test",443.43,3));
        Assert.assertTrue(productsModel.updatePries(4355,99.9));

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testUpdateStock() {

        Product p = new Product(786451,"Title","Nun",3.4,2);
        productsModel.add(p);
        Assert.assertTrue(productsModel.updateStockNr(786451,100));
        System.out.println(productsModel.getByNr(786451));

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testRemoveProduct() {

        Product p = new Product(4,"Title","Nun",3.4,2);
        Product p2 = new Product(5,"Title","Nun",3.4,2);
        productsModel.add(p);
        productsModel.add(p2);

        boolean removeStatement = productsModel.remove(4);

        System.out.println(productsModel.getAll());
        Assert.assertTrue(removeStatement);

        // nur um die App Aufzuräumen nach dem Test
        productsModel.clear();
    }

    @Test
    public void testClearProductsDatabase() {
        productsModel.add(new Product(425435,"Product1",text,40.43,54));
        productsModel.add(new Product(123266,"Product2",text,643.43,24));
        Assert.assertTrue(productsModel.clear());
    }
}
