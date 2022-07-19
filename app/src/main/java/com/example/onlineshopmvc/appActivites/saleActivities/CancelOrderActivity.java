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
import com.example.onlineshopmvc.appController.saleAppLogic.OrderStateException;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;

import javax.mail.MessagingException;

public class CancelOrderActivity extends AppCompatActivity {

    private EditText orderIdText;
    private EditText customerNameText;
    private EditText emailText;
    private TextView successText;
    private Button cancelBtn;

    private SaleApp saleApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        this.saleApp = SaleAppImpl.getSaleAppInstance(this);

        this.orderIdText = (EditText) findViewById(R.id.orderIdFeld);
        this.customerNameText = (EditText) findViewById(R.id.customerNameFeld);
        this.emailText = (EditText) findViewById(R.id.emailFeld);
        this.successText = (TextView) findViewById(R.id.cancelSuccessText);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int orderID = Integer.parseInt(orderIdText.getText().toString());
                String name = customerNameText.getText().toString();
                String email = emailText.getText().toString();

                try {
                    saleApp.cancelOrder(orderID,name,email);
                    successText.setText("Order has been canceled successful");
                } catch (OrderStateException e) {
                    successText.setText("The order is in a state in which it can no longer be cancelled !");
                    successText.setTextColor(Color.RED);
                } catch (IncorrectInputsException e) {
                    successText.setText("Check your input !");
                    successText.setTextColor(Color.RED);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}