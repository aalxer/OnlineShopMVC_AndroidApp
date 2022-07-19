package com.example.onlineshopmvc.appModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ProductsModelImpl extends SQLiteOpenHelper implements ProductsModel_ManagementApp, ProductsModel_SaleApp {

    // Database create info:
    private Context context;
    private static final String DATABASE_NAME = "Products1.db";
    private static final int DATABASE_VERSION = 1;

    // Define Database Schema :
    private static final String TABLE_NAME = "Products";
    private static final String COLUMN_CODEID = "CodeID";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_PRIES = "Pries";
    private static final String COLUMN_STOCK = "Stock";

    private ArrayList<Product> products = new ArrayList<>();;

    public ProductsModelImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // --------------------------------------
    // Create Database
    // (SQLiteOpenHelper functions) :
    // --------------------------------------

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES =  "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CODEID + " INTEGER PRIMARY KEY," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_PRIES + " DOUBLE," +
                COLUMN_STOCK + " INTEGER)";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // --------------------------------------
    // Database Management Area
    // (ProductModelInterface functions) :
    // --------------------------------------

    @Override
    public boolean add(Product product) {

        int pID = product.getCodeId();
        String pTitle = product.getTitle();
        String pDescription = product.getDescription();
        Double pPries = product.getPries();
        int pStock = product.getStock();

        // Gets the data repository in write mode :
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values :
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODEID, pID);
        values.put(COLUMN_TITLE, pTitle);
        values.put(COLUMN_DESCRIPTION, pDescription);
        values.put(COLUMN_PRIES, pPries);
        values.put(COLUMN_STOCK, pStock);

        // Insert the new row, returning the primary key value of the new row :
        long result = db.insert(TABLE_NAME, null, values);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean remove(int productNr) {
        SQLiteDatabase db = this.getWritableDatabase();

        int deleted = db.delete(TABLE_NAME, "CodeID=?", new String[]{(Integer.toString(productNr))});
        if (deleted==0) {
            return false;
        }
        // update list
        this.getAll();

        return true;
    }

    @Override
    public boolean clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_NAME, null, null);

        if (deleted==0) {
            return false;
        }
        return true;
    }

    // !
    // ausgehend davon dass die Methode getAll() immer bereits aufgerufen ist !
    @Override
    public Product getByNr(int productNr) {

        this.getAll();

        for(Product p : this.products) {
            if (p.getCodeId() == productNr) {
                return p;
            }
        }
        return null;
    }

    // !
    // ausgehend davon dass die Methode getAll() immer bereits aufgerufen ist
    @Override
    public ArrayList<Product> getByName(String title) {
        ArrayList<Product> searchResult = new ArrayList<>();

        if (this.products.size()==0) {
            this.getAll();
        }

        for(Product p : this.products) {
            if (p.getTitle().equalsIgnoreCase(title)) {
                searchResult.add(p);
            }
        }
        return searchResult;
    }

    @Override
    public ArrayList<Product> getAll() {
        // Alte Products löschen und alles neu hinzufügen
        this.products.clear();

        // Gets the data repository in read mode :
        SQLiteDatabase db = this.getReadableDatabase();

        // Get data query :
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        // Cursor move to the next line in the table :
        while(cursor.moveToNext()) {
            // Get values of the line :
            int productCode = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CODEID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            double pries = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRIES));
            int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK));

            // add as Product Object to the list :
            this.products.add(new Product(productCode, title, description, pries, stock));
        }
        cursor.close();

        return this.products;
    }

    @Override
    public boolean updateProductId(int productID, int newProductId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // create new value :
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODEID, newProductId);

        // select which product is to be updated :
        String selection = COLUMN_CODEID + " = ? ";
        String[] selectionArgs = {Integer.toString(productID)};

        // update the product :
        int updated = db.update(TABLE_NAME, values, selection, selectionArgs);

        if (updated == 0) {
            return false;
        }
        // Liste aktulisieren:
        this.getAll();

        return true;
    }

    @Override
    public boolean updateTitle(int productID, String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        // create new value :
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);

        // select which product is to be updated :
        String selection = COLUMN_CODEID + " = ? ";
        String[] selectionArgs = {Integer.toString(productID)};

        // update the product :
        int updated = db.update(TABLE_NAME, values, selection, selectionArgs);

        if (updated == 0) {
            return false;
        }
        // Liste aktulisieren:
        this.getAll();

        return true;
    }

    @Override
    public boolean updateDescription(int productID, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        // create new value :
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);

        // select which product is to be updated :
        String selection = COLUMN_CODEID + " = ? ";
        String[] selectionArgs = {Integer.toString(productID)};

        // update the product :
        int updated = db.update(TABLE_NAME, values, selection, selectionArgs);

        if (updated == 0) {
            return false;
        }
        // Liste aktulisieren:
        this.getAll();

        return true;
    }

    @Override
    public boolean updatePries(int productID, double pries) {
        SQLiteDatabase db = this.getWritableDatabase();

        // create new value :
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRIES, pries);

        // select which product is to be updated :
        String selection = COLUMN_CODEID + " = ? ";
        String[] selectionArgs = {Integer.toString(productID)};

        // update the product :
        int updated = db.update(TABLE_NAME, values, selection, selectionArgs);

        if (updated == 0) {
            return false;
        }
        // Liste aktulisieren:
        this.getAll();

        return true;
    }

    @Override
    public boolean updateStockNr(int productID, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();

        // create new value :
        ContentValues values = new ContentValues();
        values.put(COLUMN_STOCK, stock);

        // select which product is to be updated :
        String selection = COLUMN_CODEID + " = ? ";
        String[] selectionArgs = {Integer.toString(productID)};

        // update the product :
        int updated = db.update(TABLE_NAME, values, selection, selectionArgs);

        if (updated == 0) {
            return false;
        }
        // Liste aktulisieren:
        this.getAll();

        return true;
    }
}