package com.example.onlineshopmvc.appActivites.saleActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.saleAppLogic.OperationFailedException;
import com.example.onlineshopmvc.appController.saleAppLogic.ProductSoldOutException;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;
import com.example.onlineshopmvc.appModel.Order;

public class ToOrderActivity extends AppCompatActivity {

    private SaleApp saleApp;

    private EditText firstname;
    private EditText surname;
    private EditText email;
    private EditText firstLineAddress;
    private EditText city;
    private EditText postCode;
    private TextView personalData;
    private TextView shippingAddress;
    private Button paymentBtn;
    private TextView reportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_order);

        this.saleApp = SaleAppImpl.getSaleAppInstance(this);

        this.firstname = findViewById(R.id.firstnameInput);
        this.surname = findViewById(R.id.surnameInput);
        this.email = findViewById(R.id.emailInput);
        this.firstLineAddress = findViewById(R.id.firstLineAddressInput);
        this.city = findViewById(R.id.cityInput);
        this.postCode = findViewById(R.id.postCodeInput);
        this.personalData = findViewById(R.id.personalDataText);
        this.shippingAddress = findViewById(R.id.shippingAddressText);
        this.reportText = findViewById(R.id.reportText);
        this.paymentBtn = findViewById(R.id.paymentBtn);

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toOrder();
            }
        });
    }

    private void toOrder() {

        // Falls die Inputs nicht vollständig sind:
        if (this.firstname == null || this.surname == null || this.email == null || this.firstLineAddress == null || this.city == null || this.postCode == null ) {
            Toast.makeText(this, "Check your input !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inpute auflösen:
        String firstname = this.firstname.getText().toString();
        String surename = this.surname.getText().toString();
        String email = this.email.getText().toString();
        String firstLineAddress = this.firstLineAddress.getText().toString();
        String city = this.city.getText().toString();
        int postCode = Integer.parseInt(this.postCode.getText().toString());

        try {
            Order order = this.saleApp.toOrder(firstname,surename,email,firstLineAddress,city,postCode);
            // wenn alles die Methode keine Exception auslöst dann kann zur Zahlungsseite gehen:
            Intent intent = new Intent(ToOrderActivity.this,CheckOutActivity.class);
            intent.putExtra("order",order);
            startActivity(intent);

        } catch (ProductSoldOutException e) {
            this.reportText.setText("We are sorry, Some of the items you want are sold out !");
        } catch (OperationFailedException e) {
            this.reportText.setText("Something went wrong during execution, please try again !");
        }
    }
}