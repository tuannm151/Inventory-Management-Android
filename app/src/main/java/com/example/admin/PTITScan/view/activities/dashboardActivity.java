package com.example.admin.PTITScan.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.view.activities.Authentication.LoginActivity;
import com.example.admin.PTITScan.view.activities.ManageItem.AddItemActivity;
import com.example.admin.PTITScan.view.activities.ManageItem.ImportActivity;
import com.example.admin.PTITScan.view.activities.ManageItem.ListItemActivity;
import com.example.admin.PTITScan.view.activities.ManageStorage.ListStorageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboardActivity extends AppCompatActivity implements View.OnClickListener  {
    private FirebaseAuth firebaseAuth;
    TextView firebasenameview;
    Button toast;


    private CardView addItems, deleteItems, scanItems, viewInventory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebasenameview = findViewById(R.id.firebasename);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");
        firebasenameview.setText("Xin chào, "+resultemail);


        addItems = (CardView)findViewById(R.id.addItems);
        deleteItems = (CardView) findViewById(R.id.deleteItems);
        scanItems = (CardView) findViewById(R.id.scanItems);
        viewInventory = (CardView) findViewById(R.id.viewInventory);

        addItems.setOnClickListener(this);
        deleteItems.setOnClickListener(this);
        scanItems.setOnClickListener(this);
        viewInventory.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.addItems : i = new Intent(this, ListItemActivity.class); startActivity(i); break;
            case R.id.deleteItems : i = new Intent(this, ImportActivity.class);startActivity(i); break;
            case R.id.viewInventory : i = new Intent(this, ListStorageActivity.class);startActivity(i); break;
            default: break;
        }
    }





    // logout below
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(dashboardActivity.this, LoginActivity.class));
        Toast.makeText(dashboardActivity.this,"Đăng xuất thành công", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.logoutMenu:{
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
