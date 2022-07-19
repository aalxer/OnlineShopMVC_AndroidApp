package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.IncorrectInputsException;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appModel.Product;

public class AddProductActivity extends AppCompatActivity {

    private ManagementApp managementApp = ManagementAppImpl.getAppManagementInstance(this);

    private EditText codeID;
    private EditText title;
    private EditText description;
    private EditText pries;
    private EditText stockNr;
    private TextView addSuccessText;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        this.codeID = (EditText) findViewById(R.id.codeInput);
        this.title = (EditText) findViewById(R.id.titleInput);
        this.description = (EditText) findViewById(R.id.descriptionInput);
        this.pries = (EditText) findViewById(R.id.priesInput);
        this.stockNr = (EditText) findViewById(R.id.stockNrInput);
        this.addBtn = (Button) findViewById(R.id.productAddBtn);
        this.addSuccessText = (TextView) findViewById(R.id.addSuccessText);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct(view);
            }
        });
    }

    private void addProduct(View view) {

        int codeID = Integer.parseInt(this.codeID.getText().toString());
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        double pries = Double.parseDouble(this.pries.getText().toString());
        int stocknr = Integer.parseInt(this.stockNr.getText().toString());

        Product newProduct = new Product(codeID,title,description,pries,stocknr);

        try {
            this.managementApp.addProduct(newProduct);
            addSuccessText.setText("Product has been added successfully");
            //Toast.makeText(getApplicationContext(), "Product has been added successfully", Toast.LENGTH_SHORT).show();
        } catch (IncorrectInputsException e) {
            Toast.makeText(getApplicationContext(), "check your information !", Toast.LENGTH_SHORT).show();
        }
    }

}