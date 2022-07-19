package com.example.onlineshopmvc;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appController.managementAppLogic.NoItemsException;
import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appController.managementAppLogic.PasswordException;
import com.example.onlineshopmvc.appController.managementAppLogic.ProductExistsException;
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
public class TestManagementAppLogik {

    private ManagementApp managementApp;
    private OrdersModelImpl ordersModel;

    @Before
    public void createDatebase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.managementApp = ManagementAppImpl.getAppManagementInstance(context);
        this.ordersModel = new OrdersModelImpl(context);
    }

    @Test
    public void testLogin() throws PasswordException {
        Assert.assertTrue(managementApp.login("123"));
    }

    @Test
    public void testLogin_IncorrectPassword() throws PasswordException {
        Assert.assertFalse(managementApp.login("falschesPassword"));
    }

    @Test (expected = PasswordException.class)
    public void testLogin_PasswordException() throws PasswordException {
        managementApp.login("falschesPassword");
        managementApp.login("falschesPassword");
        managementApp.login("falschesPassword");
        managementApp.login("falschesPassword");
    }

    @Test
    public void testPasswordChange() throws PasswordException {
        Assert.assertTrue(managementApp.changePassword("123","adminnew"));
    }

    @Test
    public void testAddProduct() throws IncorrectInputsException, NoItemsException {

        Product p = new Product(1234567890,"First Product","Nun",99.9,1);
        Product addedProduct = this.managementApp.addProduct(p);
        System.out.println(addedProduct);

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testAddExistProduct() throws IncorrectInputsException, NoItemsException, NoItemsException {

        Product p = new Product(124345,"First Product","Nun",99.9,1);
        this.managementApp.addProduct(p);
        this.managementApp.addProduct(p);
        Product addedProduct = this.managementApp.addProduct(p);

        Assert.assertEquals(3,addedProduct.getStock());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testUpdateProductID() throws NoItemsException, IncorrectInputsException, ProductExistsException, ProductExistsException {

        Product p = new Product(12345,"First Product","Nun",99.9,1);
        this.managementApp.addProduct(p);

        Product updatedP = this.managementApp.updateProductId(12345,112233);

        System.out.println(updatedP);
        Assert.assertEquals(112233,updatedP.getCodeId());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test (expected = ProductExistsException.class)
    public void testUpdateProductIDToExistID() throws NoItemsException, IncorrectInputsException, ProductExistsException, ProductExistsException {

        Product p = new Product(60001,"First Product","Nun",99.9,100);
        Product p2 = new Product(60002,"First Product","Nun",99.9,100);
        this.managementApp.addProduct(p);
        this.managementApp.addProduct(p2);

        Product updatedP = this.managementApp.updateProductId(60001,60002);

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testUpdateProductTitle() throws NoItemsException, IncorrectInputsException, ProductExistsException {

        Product p = new Product(12345,"First Product","Nun",99.9,1);
        this.managementApp.addProduct(p);

        Product updatedP = this.managementApp.updateProductTitle(12345,"Updated Title");

        System.out.println(updatedP);
        Assert.assertEquals("Updated Title",updatedP.getTitle());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testUpdateProductDescription() throws NoItemsException, IncorrectInputsException, ProductExistsException {

        Product p = new Product(12345,"First Product","Nun",99.9,1);
        this.managementApp.addProduct(p);

        Product updatedP = this.managementApp.updateProductDescription(12345,"Updated Description");

        System.out.println(updatedP);
        Assert.assertEquals("Updated Description",updatedP.getDescription());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testUpdateProductPries() throws NoItemsException, IncorrectInputsException, ProductExistsException {

        Product p = new Product(12345,"First Product","Nun",99.9,1);
        this.managementApp.addProduct(p);

        Product updatedP = this.managementApp.updateProductPries(12345,20.9);

        System.out.println(updatedP);
        //Assert.assertEquals(20.9,updatedP.getPries());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testUpdateProductStockNumber() throws NoItemsException, IncorrectInputsException, ProductExistsException {

        Product p = new Product(12345,"First Product","Nun",99.9,1);
        this.managementApp.addProduct(p);

        Product updatedP = this.managementApp.updateProductStockNr(12345,200);

        System.out.println(updatedP);
        Assert.assertEquals(200,updatedP.getStock());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testRemoveProduct() throws NoItemsException, IncorrectInputsException {

        Product p = new Product(12345,"Title","Nun",3.4,2);
        Product p2 = new Product(112233,"Title","Nun",3.4,2);

        this.managementApp.addProduct(p);
        this.managementApp.addProduct(p2);

        boolean deleted = this.managementApp.deleteProduct(112233);

        System.out.println(this.managementApp.getAllProducts());
        Assert.assertTrue(deleted);

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearProductsList();
    }

    @Test
    public void testGetAllOrders() throws NoItemsException {
        // falls es bereits Bestellungen aus anderen Tests gibt
        this.ordersModel.clear();

        ArrayList<Integer> productsList = new ArrayList<>();
        Product p = new Product(4,"Title1","Nun",3.4,2);
        Product p2 = new Product(6,"Title2","Nun",3.4,2);
        productsList.add(p.getCodeId());
        productsList.add(p2.getCodeId());
        Order o = new Order(7888, LocalDate.now(),"Test Customer", OrderState.AWAITING_FULFILLMENT,45.3,productsList,"Anywhere","Berlin",4983,"test@test.test");
        this.ordersModel.add(o);

        ArrayList<Integer> productsList2 = new ArrayList<>();
        Product p3 = new Product(10,"Title1","Nun",3.4,2);
        Product p4 = new Product(15,"Title2","Nun",3.4,2);
        productsList.add(p3.getCodeId());
        productsList.add(p4.getCodeId());
        Order o2 = new Order(4308, LocalDate.now(),"Test Customer", OrderState.AWAITING_FULFILLMENT,45.3,productsList,"Anywhere","Berlin",4983,"test@test.test");
        this.ordersModel.add(o2);

        ArrayList<Order> orders = this.managementApp.getAllOrders();
        System.out.println(orders.toString());

        // nur um die App Aufzuräumen nach dem Test
        this.managementApp.clearOrdersList();
    }
}