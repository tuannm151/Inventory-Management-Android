package com.example.admin.PTITScan.view.activities.ManageItem;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.controller.ManageItem.ListItemController;
import com.example.admin.PTITScan.model.Item;
import com.example.admin.PTITScan.view.activities.BaseActivity;
import com.example.admin.PTITScan.view.activities.ManageItem.adapter.ItemAdapter;
import com.google.android.libraries.places.api.Places;

public class ListItemActivity extends BaseActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView itemRecyclerView;
    private Button addButton;

    private Dialog currentDialog;

    private ActivityResultLauncher<Intent> scanBarcodeLauncher;

    private ListItemController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new ListItemController(this);

        controller.setupAdapter();
        setContentView(R.layout.activity_list_item);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        addButton = findViewById(R.id.addButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        itemRecyclerView.setLayoutManager(layoutManager);
        itemRecyclerView.setAdapter(controller.getAdapter());

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            controller.searchItem(searchQuery);
        });

        addButton.setOnClickListener((view) -> {
            showAddItemDialog();
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        }

        scanBarcodeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Log.d(TAG, "onActivityResult: " + intent.toString());
                        EditText barcodeEditText = currentDialog.findViewById(R.id.barcodeEditText);
                        barcodeEditText.setText(intent.getStringExtra("result"));
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.startAdapter();
    }

    public void showAddItemDialog() {
        Dialog dialog = new Dialog(this);
        currentDialog = dialog;
        dialog.setContentView(R.layout.dialog_item);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        EditText nameEditText = dialog.findViewById(R.id.nameEditText);
        EditText categoryEditText = dialog.findViewById(R.id.categoryEditText);
        EditText priceEditText = dialog.findViewById(R.id.priceEditText);
        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText barcodeEditText = dialog.findViewById(R.id.barcodeEditText);
        barcodeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        barcodeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Intent intent = new Intent(this, ScanCodeActivity.class);
                scanBarcodeLauncher.launch(intent);
            }
        });

        Button saveButton = dialog.findViewById(R.id.saveButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        dialogTitle.setText("Thêm sản phẩm mới");

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String category = categoryEditText.getText().toString();
            String price = priceEditText.getText().toString();
            String barcode = barcodeEditText.getText().toString();
            controller.addItem(name, category, barcode, price);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showEditItemDialog(Item item) {
        Dialog dialog = new Dialog(this);
        currentDialog = dialog;
        dialog.setContentView(R.layout.dialog_item);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        EditText nameEditText = dialog.findViewById(R.id.nameEditText);
        EditText categoryEditText = dialog.findViewById(R.id.categoryEditText);
        EditText priceEditText = dialog.findViewById(R.id.priceEditText);
        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText barcodeEditText = dialog.findViewById(R.id.barcodeEditText);
        barcodeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        barcodeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Intent intent = new Intent(this, ScanCodeActivity.class);
                scanBarcodeLauncher.launch(intent);
            }
        });

        Button saveButton = dialog.findViewById(R.id.saveButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        dialogTitle.setText("Sửa thông tin kho hàng");
        nameEditText.setText(item.getName());
        categoryEditText.setText(item.getCategory());
        priceEditText.setText(item.getPrice());
        barcodeEditText.setText(item.getBarcode());

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String category = categoryEditText.getText().toString();
            String price = priceEditText.getText().toString();
            String barcode = barcodeEditText.getText().toString();
            controller.editItem(item.getId(), name, category, barcode, price);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showDeleteItemDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm " + item.getName() + " không?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            controller.deleteItem(item.getId());
            dialog.dismiss();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void setAdapter(ItemAdapter adapter) {
        itemRecyclerView.setAdapter(adapter);
    }
}
