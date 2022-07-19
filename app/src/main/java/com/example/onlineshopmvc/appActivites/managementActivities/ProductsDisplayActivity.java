package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appModel.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductsDisplayActivity extends AppCompatActivity {

    private RecyclerView productsView;
    private FloatingActionButton addBtn;

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> codes;
    private ArrayList<String> prises;
    private ArrayList<String> stocknrs;

    private CustomAdapterProductsDisplay customAdapterProductsDisplay;

    private ManagementApp managementApp = ManagementAppImpl.getAppManagementInstance(this);

    public ProductsDisplayActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productsdisplay_layout);

        productsView = (RecyclerView) findViewById(R.id.productsDisplay);
        addBtn = (FloatingActionButton) findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProduct = new Intent(ProductsDisplayActivity.this,AddProductActivity.class);
                startActivity(addProduct);
            }
        });

        this.titles = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.codes = new ArrayList<>();
        this.prises = new ArrayList<>();
        this.stocknrs = new ArrayList<>();

        storeDataInArrays();

        customAdapterProductsDisplay = new CustomAdapterProductsDisplay(this,titles,descriptions,codes,prises,stocknrs);
        productsView.setAdapter(customAdapterProductsDisplay);
        productsView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.productsdisplay_divider));
        productsView.addItemDecoration(dividerItemDecoration);
    }

    private void storeDataInArrays() {
        ArrayList<Product> productsList = this.managementApp.getAllProducts();

        if (productsList.size()>0) {
            for(Product p : productsList) {
                this.titles.add(p.getTitle());
                this.descriptions.add(p.getDescription());
                this.codes.add(String.valueOf(p.getCodeId()));
                this.prises.add(p.getPries().toString());
                this.stocknrs.add(String.valueOf(p.getStock()));
            }
        } else {
            // TODO
        }
    }
}