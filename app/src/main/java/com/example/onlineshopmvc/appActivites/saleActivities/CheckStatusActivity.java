package com.example.onlineshopmvc.appActivites.saleActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.OrderState;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;

public class CheckStatusActivity extends AppCompatActivity {

    private EditText orderIdText;
    private TextView orderStatusText;
    private Button checkStatusBtn;

    private SaleApp saleApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);

        this.saleApp = SaleAppImpl.getSaleAppInstance(this);

        this.orderIdText = (EditText) findViewById(R.id.orderIdFeld_checkStatus);
        this.orderStatusText = (TextView) findViewById(R.id.orderStatusText);
        this.checkStatusBtn = (Button) findViewById(R.id.checkStatusBtn);

        checkStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orderID = Integer.parseInt(orderIdText.getText().toString());
                try {
                    OrderState currentState = saleApp.getOrderState(orderID);
                    String currentStateString = currentState.toString();
                    orderStatusText.setText("The current state of the Order is "+currentStateString+", we will inform you by email when the status is updated.");

                } catch (IncorrectInputsException e) {
                    orderStatusText.setText("No order found referenced to this Order ID !");
                    orderStatusText.setTextColor(Color.RED);
                }
            }
        });
    }
}