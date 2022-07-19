package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appActivites.saleActivities.CustomAdapterShoppingCartDisplay;
import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.appModel.Product;

import java.util.ArrayList;

public class DisplayOrderActivity extends AppCompatActivity {

    private ManagementApp managementApp;
    private Order orderToDisplay;
    private ArrayList<String> orderStateList;
    private ArrayList<String> titles;
    private ArrayList<String> codes;
    private ArrayList<String> prises;

    private TextView orderNumberOrderDisplay;
    private TextView orderDateOrderDisplay;
    private TextView orderAmountOrderDisplay;
    private TextView customerNameOrderDisplay;
    private TextView emailOrderDisplay;
    private TextView cityOrderDisplay;
    private TextView postcodeOrderDisplay;
    private TextView streetOrderDisplay;
    private Button changeOrderStateBtn;
    private Button removeOrderBtn;
    private Spinner orderStateMenu;
    private RecyclerView recyclerViewOrderDisplay;

    private CustomAdapterShoppingCartDisplay customAdapterShoppingCartDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);

        this.managementApp = ManagementAppImpl.getAppManagementInstance(this);
        this.orderStateList = new ArrayList<>();
        this.codes = new ArrayList<>();
        this.titles = new ArrayList<>();
        this.prises = new ArrayList<>();
        this.orderNumberOrderDisplay = findViewById(R.id.orderNumberOrderDisplay);
        this.orderDateOrderDisplay = findViewById(R.id.orderDateOrderDisplay);
        this.orderAmountOrderDisplay = findViewById(R.id.orderAmountOrderDisplay);
        this.customerNameOrderDisplay = findViewById(R.id.customerNameOrderDisplay);
        this.emailOrderDisplay = findViewById(R.id.emailOrderDisplay);
        this.cityOrderDisplay = findViewById(R.id.cityOrderDisplay);
        this.postcodeOrderDisplay = findViewById(R.id.postcodeOrderDisplay);
        this.streetOrderDisplay = findViewById(R.id.streetOrderDisplay);
        this.changeOrderStateBtn = findViewById(R.id.changeOrderStateBtn);
        this.removeOrderBtn = findViewById(R.id.removeOrderBtn);
        this.orderStateMenu = findViewById(R.id.orderStateMenu);
        this.recyclerViewOrderDisplay = findViewById(R.id.recyclerViewOrderDisplay);

        // Buttons events:
        changeOrderStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeOrderState();
            }
        });

        removeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeOrder();
            }
        });

        // Order State Drop Down Menu:
        storeOrderStatesInArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,this.orderStateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStateMenu.setAdapter(adapter);

        // Die angeklickte Bestellung holen durch die übergebene Bestellungsnummer:
        String orderIdInString = getIntent().getStringExtra("orderID");
        int orderID = Integer.parseInt(orderIdInString);
        try {
            this.orderToDisplay = this.managementApp.getOrder(orderID);
        } catch (IncorrectInputsException e) {
            e.printStackTrace();
        }

        // Bestellungsdetalis eintragen:
        orderNumberOrderDisplay.setText(String.valueOf(this.orderToDisplay.getOrderID()));
        orderDateOrderDisplay.setText(this.orderToDisplay.getDate().toString());
        orderAmountOrderDisplay.setText(String.valueOf(this.orderToDisplay.getTotalPries()));
        customerNameOrderDisplay.setText(this.orderToDisplay.getCustomerName());
        emailOrderDisplay.setText(this.orderToDisplay.getEmail());
        cityOrderDisplay.setText(this.orderToDisplay.getCity());
        postcodeOrderDisplay.setText(String.valueOf(this.orderToDisplay.getPostcode()));
        streetOrderDisplay.setText(this.orderToDisplay.getFirstLineAddress());

        // Aktueller Status auswählen:
        int statePosition = this.getStatePosition(this.orderToDisplay.getState().toString());
        orderStateMenu.setSelection(statePosition);

        // Daten holen und speichern:
        if(storeDataInArrays()) {
            // Adapter:
            customAdapterShoppingCartDisplay = new CustomAdapterShoppingCartDisplay(this,titles,codes,prises);
            recyclerViewOrderDisplay.setAdapter(customAdapterShoppingCartDisplay);
            recyclerViewOrderDisplay.setLayoutManager(new LinearLayoutManager(this));

            // Line divider:
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.productsdisplay_divider));
            recyclerViewOrderDisplay.addItemDecoration(dividerItemDecoration);
        }
    }

    private void storeOrderStatesInArray() {
        for (OrderState o : OrderState.values()) {
            this.orderStateList.add(o.toString());
        }
    }

    private int getStatePosition(String orderState) {
        for(int i = 0; i <= this.orderStateList.size() ; i++) {
            if (this.orderStateList.get(i).equals(orderState)) {
                return i;
            }
        }
        return 0;
    }

    private void changeOrderState() {
        String selectedOrder = orderStateMenu.getSelectedItem().toString();
        OrderState newState = OrderState.valueOf(selectedOrder);
        OrderState result = this.managementApp.changeOrderState(this.orderToDisplay,newState);
        if (result != null) {
            Toast.makeText(this, "Order State has been changed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error daring execution !", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeOrder() {
        boolean result = this.managementApp.deleteOrder(this.orderToDisplay);
        if (result) {
            Toast.makeText(this, "Order has been removed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error daring execution !", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean storeDataInArrays() {
        ArrayList<Integer> productsNumbers = this.orderToDisplay.getOrderedProducts();
        if (productsNumbers == null) {
            return false;
        }
        for(int i : productsNumbers) {
            Product tempProduct = this.managementApp.getProduct(i);
            this.titles.add(tempProduct.getTitle());
            this.prises.add(String.valueOf(tempProduct.getPries()));
            this.codes.add(String.valueOf(tempProduct.getCodeId()));
        }
        return true;
    }
}