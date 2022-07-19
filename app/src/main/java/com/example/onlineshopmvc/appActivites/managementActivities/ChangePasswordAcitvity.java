package com.example.onlineshopmvc.appActivites.managementActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementApp;
import com.example.onlineshopmvc.appController.managementAppLogic.ManagementAppImpl;
import com.example.onlineshopmvc.appController.managementAppLogic.PasswordException;

public class ChangePasswordAcitvity extends AppCompatActivity {

    private ManagementApp managementApp = ManagementAppImpl.getAppManagementInstance(this);

    private Button changePassBtn;
    private EditText oldPassword;
    private EditText newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_layout);

        changePassBtn = (Button) findViewById(R.id.passChangeBtn);
        oldPassword = (EditText) findViewById(R.id.oldPassFeld);
        newPassword = (EditText) findViewById(R.id.newPassFeld);

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword(view);
            }
        });
    }

    public void changePassword(View view) {
        String oldPass = this.oldPassword.getText().toString();
        String newPass = this.newPassword.getText().toString();

        try {
            this.managementApp.changePassword(oldPass,newPass);
            Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
        } catch (PasswordException e) {
            e.printStackTrace();
        }
    }
}
