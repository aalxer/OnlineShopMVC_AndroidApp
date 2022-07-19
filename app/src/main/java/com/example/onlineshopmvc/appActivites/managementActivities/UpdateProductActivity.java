package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appController.managementAppLogic.ProductExistsException;

public class UpdateProductActivity extends AppCompatActivity {

    private ManagementApp managementApp = ManagementAppImpl.getAppManagementInstance(this);
    private String title;
    private String codeID;
    private String description;
    private String pries;
    private String stocknr;
    private EditText updateTitle;
    private EditText updateCode;
    private EditText updateDescription;
    private EditText updatePries;
    private EditText updateStocknr;
    private TextView updateSuccess;
    private Button updateBtn;
    private Button productDeleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        this.updateCode = (EditText) findViewById(R.id.updateCodeInput);
        this.updateTitle = (EditText) findViewById(R.id.updateTitleInput);
        this.updateDescription = (EditText) findViewById(R.id.updateDescriptionInput);
        this.updatePries = (EditText) findViewById(R.id.updatePriesInput);
        this.updateStocknr = (EditText) findViewById(R.id.updateStockNrInput);
        this.updateBtn = (Button) findViewById(R.id.productUpdateBtn);
        this.updateSuccess = (TextView) findViewById(R.id.updateSuccessText);
        this.productDeleteBtn = findViewById(R.id.productDeleteBtn);

        productDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(managementApp.deleteProduct(Integer.parseInt(codeID))) {
                    updateSuccess.setText("Product deleted successfully");
                } else {
                    Toast.makeText(UpdateProductActivity.this, "Product cannot be deleted !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!codeID.equals(updateCode.getText().toString())) {
                        managementApp.updateProductId(Integer.parseInt(codeID),Integer.parseInt(updateCode.getText().toString()));
                    }
                    if (!title.equals(updateTitle.getText().toString())) {
                        managementApp.updateProductTitle(Integer.parseInt(codeID),updateTitle.getText().toString());
                    }
                    if (!description.equals(updateDescription.getText().toString())) {
                        managementApp.updateProductDescription(Integer.parseInt(codeID),updateDescription.getText().toString());
                    }
                    if (!pries.equals(updatePries.getText().toString())) {
                        managementApp.updateProductPries(Integer.parseInt(codeID), Double.parseDouble(updatePries.getText().toString()));
                    }
                    if (!stocknr.equals(updateStocknr.getText().toString())) {
                        managementApp.updateProductStockNr(Integer.parseInt(codeID), Integer.parseInt(updateStocknr.getText().toString()));
                    }
                    updateSuccess.setText("Product updated successfully");
                } catch (ProductExistsException e) {
                    Toast.makeText(getApplicationContext(), "Product with these Code ID is already exists !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getAndSetIntentData();
    }

    private void getAndSetIntentData() {
        if(getIntent().hasExtra("title") && getIntent().hasExtra("codeID") && getIntent().hasExtra("description") && getIntent().hasExtra("pries") && getIntent().hasExtra("stocknr") ) {
            // Data aus dem Intent lesen:
            this.title = getIntent().getStringExtra("title");
            this.codeID = getIntent().getStringExtra("codeID");
            this.description= getIntent().getStringExtra("description");
            this.pries = getIntent().getStringExtra("pries");
            this.stocknr = getIntent().getStringExtra("stocknr");

            // Data in die neue Seite eintragen:
            this.updateTitle.setText(title);
            this.updateCode.setText(codeID);
            this.updateDescription.setText(description);
            this.updatePries.setText(pries);
            this.updateStocknr.setText(stocknr);
        }
    }
}