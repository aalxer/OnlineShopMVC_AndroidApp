package com.example.onlineshopmvc.appActivites.managementActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appController.managementAppLogic.PasswordException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginBtn;
    private EditText passEnterFeld;
    private TextView passEnterText;

    private final ManagementApp managementApp = ManagementAppImpl.getAppManagementInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_mainlayout);

        passEnterFeld = (EditText) findViewById(R.id.passEnterFeld);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        passEnterText = (TextView) findViewById(R.id.passEnterText);

        loginBtn.setOnClickListener(this);
    }

    public void login(View view) throws PasswordException {
        if (this.managementApp.login(passEnterFeld.getText().toString())) {

            //setContentView(R.layout.hpmepage_layout);
            Intent homepageActivity = new Intent(this, HomePageActivity.class);
            startActivity(homepageActivity);

        } else {
            Toast.makeText(getApplicationContext(), "Wrong password ! try again ..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            login(view);
        } catch (PasswordException e) {
            loginBtn.setVisibility(View.GONE);
            passEnterFeld.setVisibility(View.GONE);
            passEnterText.setText("Login has been blocked !");
            passEnterText.setTextColor(Color.RED);
        }
    }
}