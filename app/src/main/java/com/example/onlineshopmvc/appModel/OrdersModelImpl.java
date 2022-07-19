package com.example.onlineshopmvc.appModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class OrdersModelImpl extends SQLiteOpenHelper implements OrdersModel_ManagementApp, OrdersModel_SaleApp {

    // Database create info:
    private Context context;
    private static final String DATABASE_NAME = "Orders1.db";
    private static final int DATABASE_VERSION = 1;

    // Define Database Schema :
    private static final String TABLE_NAME = "Orders";
    private static final String COLUMN_ORDERID = "OrderID";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_STATE = "State";
    private static final String COLUMN_TOTAL = "Total";
    private static final String COLUMN_CUSTOMER = "Customer";
    private static final String COLUMN_ADDRESS = "Street_HausNr";
    private static final String COLUMN_CITY = "City";
    private static final String COLUMN_POSTCODE = "Postcode";
    private static final String COLUMN_PRODUCTS = "Products";
    private static final String COLUMN_EMAIL = "Email";

    private ArrayList<Order> orders = new ArrayList<>();

    public OrdersModelImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // --------------------------------------
    // Create Database
    // (SQLiteOpenHelper functions) :
    // --------------------------------------

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ORDERID + " INTEGER PRIMARY KEY," +
                COLUMN_DATE + " TEXT," +
                COLUMN_STATE + " TEXT," +
                COLUMN_TOTAL + " DOUBLE," +
                COLUMN_CUSTOMER + " TEXT," +
                COLUMN_ADDRESS + " TEXT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_POSTCODE + " INTEGER," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PRODUCTS + " BLOB)";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // --------------------------------------
    // Database Management Area
    // (OrdersModelInterface functions) :
    // --------------------------------------

    @Override
    public boolean add(Order order) {
        // Gets the data repository in write mode :
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values :
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDERID, order.getOrderID());
        values.put(COLUMN_DATE, order.getDate().toString());
        values.put(COLUMN_ADDRESS, order.getFirstLineAddress());
        values.put(COLUMN_POSTCODE, order.getPostcode());
        values.put(COLUMN_CITY, order.getCity());
        values.put(COLUMN_STATE, order.getState().toString());
        values.put(COLUMN_TOTAL, order.getTotalPries());
        values.put(COLUMN_CUSTOMER, order.getCustomerName());
        values.put(COLUMN_EMAIL, order.getEmail());

        // convert ProductsList to Blob:
        byte[] productsListAsByte = this.getProductsAsByte(order.getOrderedProducts());
        values.put(COLUMN_PRODUCTS, productsListAsByte);

        // Insert the new row, returning the primary key value of the new row :
        long result = db.insert(TABLE_NAME, null, values);

        if (result == -1) {
            return false;
        } else {
            //this.getAll();
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean remove(int orderID) {
        SQLiteDatabase db = this.getWritableDatabase();

        int deleted = db.delete(TABLE_NAME, "OrderID=?", new String[]{(Integer.toString(orderID))});

        if (deleted == 0) {
            return false;
        }
        this.getAll();

        return true;
    }

    // TODO test
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Order getOrder(int OrderID) {
        this.getAll();

        for (Order o : this.orders) {
            if(o.getOrderID()==OrderID) {
                return o;
            }
        }
        return null;
    }

    @Override
    public boolean clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_NAME, null, null);

        if (deleted == 0) {
            return false;
        }
        this.orders.clear();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public ArrayList<Order> getAll() {

        // Alte Products löschen und alles neu hinzufügen
        this.orders.clear();

        // Gets the data repository in read mode :
        SQLiteDatabase db = this.getReadableDatabase();

        // Get data query :
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        // Cursor move to the next line in the table :
        while (cursor.moveToNext()) {
            // Get values of the line :
            int orderID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERID));
            String dateInString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
            int postcode = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POSTCODE));
            String city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY));
            String orderStateInString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE));
            Double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            byte[] products = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PRODUCTS));

            // Convert Products-Blob to ArrayList
            ArrayList<Integer> productArrayList = this.getProductsAsList(products);

            LocalDate dateAsDate = LocalDate.parse(dateInString);

            // Convert orderState to Enum:
            OrderState orderStateEnum = OrderState.valueOf(orderStateInString);

            // add as Order Object to the list :
            this.orders.add(new Order(orderID,dateAsDate,name,orderStateEnum,total,productArrayList,address,city,postcode,email));
        }
        cursor.close();

        return this.orders;
    }

    @Override
    public boolean updateState(Order order, OrderState state) {
        SQLiteDatabase db = this.getWritableDatabase();

        // create new value :
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATE, state.toString());

        // select which product is to be updated :
        String selection = COLUMN_ORDERID + " = ? ";
        String[] selectionArgs = {Integer.toString(order.getOrderID())};

        // update the product :
        int updated = db.update(TABLE_NAME, values, selection, selectionArgs);

        if (updated == 0) {
            return false;
        }
        this.getAll();

        return true;
    }

    private byte[] getProductsAsByte(ArrayList<Integer> modeldata) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(modeldata);
            byte[] ordersListAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(ordersListAsBytes);
            return ordersListAsBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Integer> getProductsAsList(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            ArrayList<Integer> dataobj = (ArrayList<Integer>) ois.readObject();
            return dataobj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
