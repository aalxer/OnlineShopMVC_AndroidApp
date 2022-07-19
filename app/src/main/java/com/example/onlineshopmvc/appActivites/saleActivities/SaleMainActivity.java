package com.example.onlineshopmvc.appActivites.saleActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;
import com.example.onlineshopmvc.appModel.Product;

import java.util.ArrayList;

public class SaleMainActivity extends AppCompatActivity {

    private RecyclerView productsView;
    private Button shoppingCart;
    private CustomAdapterProductsDisplay_SaleApp customAdapterProductsDisplay;
    private TextView noItemsText;
    //private Toolbar toolbar;

    private SaleApp saleApp;

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> codes;
    private ArrayList<String> prises;
    private ArrayList<String> stocknrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_main);

        this.saleApp = SaleAppImpl.getSaleAppInstance(this);
        this.titles = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.codes = new ArrayList<>();
        this.prises = new ArrayList<>();
        this.stocknrs = new ArrayList<>();
        this.noItemsText = findViewById(R.id.noItemsText_homePage);
        productsView = (RecyclerView) findViewById(R.id.saleProductsRecyclerView);

        // Daten holen und speichern:
        storeDataInArrays();

        // Adapter:
        customAdapterProductsDisplay = new CustomAdapterProductsDisplay_SaleApp(this,titles,descriptions,codes,prises);
        productsView.setAdapter(customAdapterProductsDisplay);
        productsView.setLayoutManager(new LinearLayoutManager(this));

        // Line divider:
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.productsdisplay_divider));
        productsView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shoppingCartItem :
                this.goToShoppingCart();
                break;
            case R.id.orderCancelItem:
                this.cancelOrder();
                break;
            case R.id.checkStatusItem:
                this.checkStatus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToShoppingCart() {
        Intent intent = new Intent(SaleMainActivity.this,ShoppingCartActivity.class);
        startActivity(intent);
    }

    private void cancelOrder() {
        Intent intent = new Intent(SaleMainActivity.this,CancelOrderActivity.class);
        startActivity(intent);
    }

    private void checkStatus() {
        Intent intent = new Intent(SaleMainActivity.this,CheckStatusActivity.class);
        startActivity(intent);
    }

    private void storeDataInArrays() {
        ArrayList<Product> productsList = this.saleApp.displayAllProducts();

        if (productsList != null && productsList.size()>0) {
            for(Product p : productsList) {
                this.titles.add(p.getTitle());
                this.descriptions.add(p.getDescription());
                this.codes.add(String.valueOf(p.getCodeId()));
                this.prises.add(p.getPries().toString());
                this.stocknrs.add(String.valueOf(p.getStock()));
            }
        } else {
            noItemsText.setVisibility(View.VISIBLE);
        }
    }
}