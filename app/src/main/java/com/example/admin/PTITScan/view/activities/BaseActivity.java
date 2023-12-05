package com.example.admin.PTITScan.view.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog processDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        processDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog(String message) {
        processDialog.setMessage(message);
        processDialog.show();
    }
    public void hideProgressDialog() {
        processDialog.dismiss();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
