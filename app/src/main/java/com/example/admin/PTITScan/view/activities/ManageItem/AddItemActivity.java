package com.example.admin.PTITScan.view.activities.ManageItem;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.model.Item;
import com.example.admin.PTITScan.model.SpinnerItem;
import com.example.admin.PTITScan.model.Storage;
import com.example.admin.PTITScan.model.StorageItem;
import com.example.admin.PTITScan.view.activities.Authentication.LoginActivity;
import com.example.admin.PTITScan.view.activities.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class AddItemActivity extends BaseActivity {
    private TextView name, category, price, barcode;
    public static TextView resulttextview;

    public EditText quantity;
    Button scanbutton, additemtodatabase;
    DatabaseReference databaseReference;

    Spinner storageSpinner;

    private Item currentItem;

    private ActivityResultLauncher<Intent> scanBarcodeLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        resulttextview = findViewById(R.id.barcodeview);
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);
        name = findViewById(R.id.edititemname);
        category = findViewById(R.id.editcategory);
        price = findViewById(R.id.editprice);
        barcode= findViewById(R.id.barcodeview);
        quantity = findViewById(R.id.editquantity);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        storageSpinner = findViewById(R.id.storageSpinner);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ""));

        scanBarcodeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Log.d(TAG, "onActivityResult: " + intent.toString());
                        findItemByBarcode(intent.getStringExtra("result"));
                    }
                }
        );

        populateStorageSpinner();

        scanbutton.setOnClickListener(view -> scanBarcodeLauncher.launch(new Intent(getApplicationContext(), ScanCodeActivity.class)));

        additemtodatabase.setOnClickListener(view -> additemtodatabase());
    }

    public void findItemByBarcode(String barcode) {
        Log.d(TAG, "findItemByBarcode: " + barcode);
        DatabaseReference itemRef = databaseReference.child("Items");
        Query query = itemRef.orderByChild("barcode").startAt(barcode).endAt(barcode + "\uf8ff");
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        currentItem = snapshot.getValue(Item.class);
                        currentItem.setId(snapshot.getKey());
                        renderCurrentItem();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void renderCurrentItem() {
        if (currentItem != null) {
            name.setText(currentItem.getName());
            category.setText(currentItem.getCategory());
            price.setText(String.valueOf(currentItem.getPrice()));
            barcode.setText(currentItem.getBarcode());
        }
    }

    public void populateStorageSpinner() {
       Query query = databaseReference.child("Storages");
         query.get().addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                  ArrayList<SpinnerItem> spinnerItems = new ArrayList<>();
                  for (DataSnapshot snapshot : task.getResult().getChildren()) {
                      Storage storage = snapshot.getValue(Storage.class);
                      SpinnerItem spinnerItem = new SpinnerItem();
                      spinnerItem.key = snapshot.getKey();
                      spinnerItem.value = storage.getName();
                      spinnerItems.add(spinnerItem);
                  }
                  ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
                  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                  storageSpinner.setAdapter(adapter);
              } else {
                  showToast("Không thể lấy danh sách kho");
                  Log.d(TAG, "populateStorageSpinner: " + task.getException().getMessage());
              }
         });
    }

    public void additemtodatabase() {
        String itemname = name.getText().toString().trim();
        String itemcategory = category.getText().toString().trim();
        String itemprice = price.getText().toString().trim();
        String itembarcode = barcode.getText().toString().trim();
        String itemquantity = quantity.getText().toString().trim();
        String itemstorage = ((SpinnerItem) storageSpinner.getSelectedItem()).key;
        String storageName = ((SpinnerItem) storageSpinner.getSelectedItem()).value;

        if (TextUtils.isEmpty(itemname)) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(itemcategory)) {
            Toast.makeText(this, "Vui lòng nhập loại sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(itemprice)) {
            Toast.makeText(this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(itembarcode)) {
            Toast.makeText(this, "Vui lòng nhập mã vạch sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(itemquantity)) {
            Toast.makeText(this, "Vui lòng nhập số lượng sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(itemstorage)) {
            Toast.makeText(this, "Vui lòng chọn kho chứa sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        // log all the data
        Log.d(TAG, "additemtodatabase: " + itemname + " " + itemcategory + " " + itemprice + " " + itembarcode + " " + itemquantity + " " + itemstorage);

        StorageItem storageItem = new StorageItem();
        storageItem.setQuantity(Integer.parseInt(itemquantity));
        storageItem.setStorageReference(itemstorage);
        storageItem.setItemReference(currentItem.getId());

        databaseReference.child("StorageItems").push().setValue(storageItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Nhập " + itemquantity + " " + itemname + " vào kho " + storageName + " thành công");
                finish();
            } else {
                showToast("Có lỗi xảy ra");
                Log.d(TAG, "additemtodatabase: " + task.getException().getMessage());
            }
        });
        
    }
}
