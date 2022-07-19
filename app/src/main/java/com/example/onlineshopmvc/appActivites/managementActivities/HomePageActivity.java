package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;

public class HomePageActivity extends AppCompatActivity {

    private ManagementApp managementApp;

    private TextView changePassword;
    private Button productsBtn;
    private Button ordersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);

        this.managementApp = ManagementAppImpl.getAppManagementInstance(this);
        this.ordersBtn = findViewById(R.id.ordersBtn);
        this.changePassword = (TextView) findViewById(R.id.changePass);
        this.productsBtn = (Button) findViewById(R.id.productsBtn);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  changePasswordIntent = new Intent(HomePageActivity.this, ChangePasswordAcitvity.class);
                startActivity(changePasswordIntent);
            }
        });

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  productsDisplayIntent = new Intent(HomePageActivity.this, ProductsDisplayActivity.class);
                startActivity(productsDisplayIntent);
            }
        });

        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this,OrdersManagementActivity.class);
                startActivity(intent);
            }
        });
    }
}