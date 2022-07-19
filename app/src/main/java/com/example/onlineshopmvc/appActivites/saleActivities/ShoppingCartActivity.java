package com.example.onlineshopmvc.appActivites.saleActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;
import com.example.onlineshopmvc.appModel.Product;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {

    private SaleApp saleApp;
    private ArrayList<String> titles;
    private ArrayList<String> codes;
    private ArrayList<String> prises;
    private CustomAdapterShoppingCartDisplay customAdapterShoppingCartDisplay;
    private RecyclerView productsView;
    private TextView totalPrise;
    private Button checkoutBtn;
    private TextView noItemsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        this.saleApp = SaleAppImpl.getSaleAppInstance(this);
        this.titles = new ArrayList<>();
        this.codes = new ArrayList<>();
        this.prises = new ArrayList<>();
        this.checkoutBtn = (Button) findViewById(R.id.checkoutBtnShoppingCart);
        this.totalPrise = (TextView) findViewById(R.id.TotalPriseAmount);
        this.productsView = (RecyclerView) findViewById(R.id.shoppingCartRecyclerView);
        this.noItemsText = findViewById(R.id.noItemsText);

        // to order:
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingCartActivity.this,ToOrderActivity.class);
                startActivity(intent);
            }
        });

        //checkoutBtn.setVisibility(View.VISIBLE);
        // Daten holen und speichern:
        if(storeDataInArrays()) {
            // Adapter:
            customAdapterShoppingCartDisplay = new CustomAdapterShoppingCartDisplay(this,titles,codes,prises);
            productsView.setAdapter(customAdapterShoppingCartDisplay);
            productsView.setLayoutManager(new LinearLayoutManager(this));

            // Line divider:
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.productsdisplay_divider));
            productsView.addItemDecoration(dividerItemDecoration);

            // set total pries:
            totalPrise.setText(this.getTotalPrise());
        }
    }

    private boolean storeDataInArrays() {

        ArrayList<Product> productsList = this.saleApp.showShoppingCart();

        if (productsList != null && productsList.size()>0) {
            for(Product p : productsList) {
                this.titles.add(p.getTitle());
                this.codes.add(String.valueOf(p.getCodeId()));
                this.prises.add(p.getPries().toString());
            }
            return true;
        } else {
            checkoutBtn.setVisibility(View.GONE);
            noItemsText.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private String getTotalPrise() {
        double total = 0;
        for (String p : this.prises) {
            total += Double.parseDouble(p);
        }
        return String.valueOf(total);
    }
}