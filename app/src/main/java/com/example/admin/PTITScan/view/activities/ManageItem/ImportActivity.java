package com.example.admin.PTITScan.view.activities.ManageItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.PTITScan.R;

public class ImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
    }

    public void importManual (View view)
    {
        startActivity(new Intent(this, AddItemActivity.class));
    }
}
