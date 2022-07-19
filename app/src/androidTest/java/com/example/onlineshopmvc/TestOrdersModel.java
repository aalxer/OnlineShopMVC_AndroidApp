package com.example.onlineshopmvc;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.OrdersModelImpl;
import com.example.onlineshopmvc.appModel.Product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
public class TestOrdersModel {

    private OrdersModelImpl ordersModel;

    @Before
    public void createDatebase() {
        Context context = ApplicationProvider.getApplicationContext();
        ordersModel = new OrdersModelImpl(context);
    }

    @Test
    public void testAddOrder() {
        ArrayList<Integer> productsList = new ArrayList<>();
        Product p = new Product(100032,"Title1","Nun",3.4,2);
        Product p2 = new Product(100046,"Title2","Nun",3.4,2);
        productsList.add(p.getCodeId());
        productsList.add(p2.getCodeId());
        Order o = new Order(7888, LocalDate.now(),"Test Customer", OrderState.AWAITING_FULFILLMENT,45.3,productsList,"Anywhere","Berlin",4983,"test@test.test");

        boolean added = ordersModel.add(o);
        Assert.assertTrue(added);
    }

    @Test
    public void testGetAllOrders() {

        ArrayList<Order> o = ordersModel.getAll();
        Assert.assertNotEquals(o.size()==0,o.size());

        System.out.println(o.toString());
    }

    // erst wenn die AddTests durchgeführt sind damit wir Daten in DB haben
    @Test
    public void testClearOrdersList() {
        Assert.assertTrue(ordersModel.clear());
    }

    @Test
    public void testUpdateState() {
        ArrayList<Integer> productsList = new ArrayList<>();
        Product p = new Product(4,"Title1","Nun",3.4,2);
        Product p2 = new Product(6,"Title2","Nun",3.4,2);
        productsList.add(p.getCodeId());
        productsList.add(p2.getCodeId());
        Order o = new Order(7888, LocalDate.now(),"Test Customer", OrderState.AWAITING_FULFILLMENT,45.3,productsList,"Anywhere","Berlin",4983,"test@test.test");

        ordersModel.add(o);
        boolean updated = ordersModel.updateState(o,OrderState.DECLINED);
        Assert.assertTrue(updated);

        ordersModel.clear();
    }

    @Test
    public void testRemoveOrder() {
        ArrayList<Integer> productsList = new ArrayList<>();
        Product p = new Product(4,"Title1","Nun",3.4,2);
        Product p2 = new Product(6,"Title2","Nun",3.4,2);
        productsList.add(p.getCodeId());
        productsList.add(p2.getCodeId());
        Order o = new Order(55, LocalDate.now(),"Test Customer", OrderState.AWAITING_FULFILLMENT,45.3,productsList,"Anywhere","Berlin",4983,"test@test.test");
        ordersModel.add(o);

        boolean removed = ordersModel.remove(55);
        Assert.assertTrue(removed);
    }
}