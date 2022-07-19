package com.example.onlineshopmvc;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appController.saleAppLogic.OperationFailedException;
import com.example.onlineshopmvc.appController.saleAppLogic.OrderStateException;
import com.example.onlineshopmvc.appController.saleAppLogic.ProductSoldOutException;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.OrdersModelImpl;
import com.example.onlineshopmvc.appModel.Product;
import com.example.onlineshopmvc.appModel.ProductsModelImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import javax.mail.MessagingException;

@RunWith(AndroidJUnit4.class)
public class TestSaleAppLogik {

    private SaleApp saleApp;
    private OrdersModelImpl ordersModel;
    private ProductsModelImpl productsModel;

    @Before
    public void createDatebase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.saleApp = new SaleAppImpl(context);
        this.ordersModel = new OrdersModelImpl(context);
        this.productsModel = new ProductsModelImpl(context);
    }

    @Test
    public void testAddToShoppingCart() throws OperationFailedException {
        Product newProduct = new Product(67890,"First Product","Nun",99.9,1);
        Product newProduct2 = new Product(12343,"Second Product","Nun",99.9,1);

        Product addedProduct = this.saleApp.addToShoppingCart(newProduct);
        Product addedProduct2 = this.saleApp.addToShoppingCart(newProduct2);

        Assert.assertEquals(newProduct,addedProduct);
        Assert.assertEquals(newProduct2,addedProduct2);
    }

    @Test
    public void testToOrder() throws OperationFailedException, ProductSoldOutException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());

        Assert.assertNotNull(result);
        System.out.println(result.toString());

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test
    public void testStockNrAfterOrder() throws OperationFailedException, ProductSoldOutException {

        //Product p = new Product(68940,"Title1","Nun",3.4,3);
        Product newProduct = new Product(876,"First Product","Nun",99.9,10);
        this.productsModel.add(newProduct);
        this.saleApp.addToShoppingCart(newProduct);

        Order result = this.saleApp.toOrder("name","surename","email@email.com","Street 1","Berlin",3424);

        Assert.assertEquals(9,saleApp.getProductByNr(876).getStock());

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test
    public void testRandStockNrAfterOrder() throws OperationFailedException, ProductSoldOutException {

        //Product p = new Product(68940,"Title1","Nun",3.4,1);

        Product newProduct = new Product(89400,"First Product","Nun",99.9,1);
        this.productsModel.add(newProduct);
        this.saleApp.addToShoppingCart(newProduct);

        Order result = this.saleApp.toOrder("name","surename","email@email.com","Street 1","Berlin",3424);

        Assert.assertEquals(0,saleApp.getProductByNr(89400).getStock());

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test (expected = ProductSoldOutException.class)
    public void testProductSoldOutException() throws OperationFailedException, ProductSoldOutException {

        Product p = new Product(47845,"Title1","Nun",3.4,1);
        this.productsModel.add(p);

        // einmal das letzte bestellen:
        this.saleApp.addToShoppingCart(p);
        Order result = this.saleApp.toOrder("name","surename","email@email.com","Street 1","Berlin",3424);

        // bestellen bei 0 Lagerbestand:
        this.saleApp.addToShoppingCart(p);
        Order result2 = this.saleApp.toOrder("name","surename","email@email.com","Street 1","Berlin",3424);

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test
    public void testGetOrderState() throws OperationFailedException, ProductSoldOutException, MessagingException, IncorrectInputsException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());
        OrderState currentState = this.saleApp.getOrderState(result.getOrderID());

        System.out.println("Aktuller Status der Bestellung : " + order.getOrderID() + " is : " +currentState);

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test
    public void testPay() throws OperationFailedException, ProductSoldOutException, MessagingException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());
        boolean payResult = this.saleApp.pay(result);

        Assert.assertTrue(payResult);

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test
    public void testStateChangedAfterPay() throws OperationFailedException, ProductSoldOutException, MessagingException, IncorrectInputsException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());
        boolean payResult = this.saleApp.pay(result);

        OrderState currentState = this.saleApp.getOrderState(result.getOrderID());

        Assert.assertEquals(OrderState.AWAITNG_PACKAGING, currentState);

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test
    public void testCancelAnOrder() throws OperationFailedException, ProductSoldOutException, MessagingException, IncorrectInputsException, OrderStateException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());
        this.saleApp.pay(result);

        boolean cancelResult = this.saleApp.cancelOrder(result.getOrderID(),result.getEmail(),result.getCustomerName());

        Assert.assertTrue(cancelResult);

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test (expected = IncorrectInputsException.class)
    public void testCancelAnOrder_IncorrectInputsException() throws OperationFailedException, ProductSoldOutException, MessagingException, IncorrectInputsException, OrderStateException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());
        this.saleApp.pay(result);

        boolean cancelResult = this.saleApp.cancelOrder(result.getOrderID(),result.getEmail(),"Fake Name");

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    @Test (expected = OrderStateException.class)
    public void testCancelAnOrder_OrderStateException() throws OperationFailedException, ProductSoldOutException, MessagingException, IncorrectInputsException, OrderStateException {

        Order order = getOrder(3,3);
        Order result = this.saleApp.toOrder(order.getCustomerName(),order.getCustomerName(),order.getEmail(),order.getFirstLineAddress(),order.getCity(),order.getPostcode());
        this.saleApp.pay(result);
        this.ordersModel.updateState(result,OrderState.SHIPPED);

        this.saleApp.cancelOrder(result.getOrderID(),result.getEmail(),result.getCustomerName());

        this.ordersModel.clear();
        this.productsModel.clear();
    }

    private Order getOrder(int stockNr1, int stockNr2) {

        //this.productsModel.remove(4);
        //this.productsModel.remove(6);
        this.productsModel.clear();
        this.ordersModel.clear();

        Product p = new Product(47845,"Title1","Nun",3.4,stockNr1);
        Product p2 = new Product(67840,"Title2","Nun",3.4,stockNr2);
        this.productsModel.add(p);
        this.productsModel.add(p2);

        ArrayList<Integer> productsList = new ArrayList<>();
        productsList.add(p.getCodeId());
        productsList.add(p2.getCodeId());

        Order order = new Order(7888, LocalDate.now(),"Test Customer", OrderState.AWAITING_PAYMENT,45.3,productsList,"Anywhere","Berlin",4983,"alwailiahmed95@gmail.com");

        return order;
    }
}