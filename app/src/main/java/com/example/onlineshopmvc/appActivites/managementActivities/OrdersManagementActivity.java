package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OrdersManagementActivity extends AppCompatActivity {

    private RecyclerView ordersDisplay;
    private OrdersManagementActivity_CustomAdapter customAdapterOrdersDisplay;
    private ManagementApp managementApp;

    private ArrayList<String> idNumbers;
    private ArrayList<String> dates;
    private ArrayList<String> states;
    private ArrayList<String> customerNames;
    private ArrayList<String> pries;
    private ArrayList<String> firstLineAddresses;
    private ArrayList<String> cities;
    private ArrayList<String> postCodes;
    private ArrayList<String> emails;
    private ArrayList<String> products;
    private TextView noOrdersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_management);

        // Attribute initialisieren:
        this.managementApp = ManagementAppImpl.getAppManagementInstance(this);
        this.ordersDisplay = findViewById(R.id.ordersDisplay);
        this.idNumbers = new ArrayList<>();
        this.dates = new ArrayList<>();
        this.states = new ArrayList<>();
        this.customerNames = new ArrayList<>();
        this.pries = new ArrayList<>();
        this.firstLineAddresses = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.postCodes = new ArrayList<>();
        this.emails = new ArrayList<>();
        this.products = new ArrayList<>();
        this.noOrdersText = findViewById(R.id.noOrdersText);

        // Daten abrufen und in Arrays abspeichern:
        storeDataInArrays();

        // Custom-Adapter linken:
        this.customAdapterOrdersDisplay = new OrdersManagementActivity_CustomAdapter(this,this.idNumbers,this.dates,this.states);
        ordersDisplay.setAdapter(customAdapterOrdersDisplay);
        ordersDisplay.setLayoutManager(new LinearLayoutManager(this));

        // Line separator zwischen Oders:
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.productsdisplay_divider));
        ordersDisplay.addItemDecoration(dividerItemDecoration);
    }

    private void storeDataInArrays() {
        ArrayList<Order> ordersList = this.managementApp.getAllOrders();

        if (ordersList != null && ordersList.size()>0) {
            for(Order o : ordersList) {
                this.idNumbers.add(String.valueOf(o.getOrderID()));
                this.customerNames.add(o.getCustomerName());
                this.states.add(o.getState().toString());
                this.pries.add(String.valueOf(o.getTotalPries()));
                this.firstLineAddresses.add(o.getFirstLineAddress());
                this.cities.add(o.getCity());
                this.postCodes.add(String.valueOf(o.getPostcode()));
                this.emails.add(String.valueOf(o.getEmail()));
                this.dates.add(o.getDate().toString());
            }
        } else {
            noOrdersText.setVisibility(View.VISIBLE);
        }
    }
}