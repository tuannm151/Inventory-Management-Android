package com.example.admin.PTITScan.view.activities.ManageStorage;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.controller.ManageStorage.ListStorageController;
import com.example.admin.PTITScan.model.Storage;
import com.example.admin.PTITScan.view.activities.BaseActivity;
import com.example.admin.PTITScan.view.activities.ManageStorage.adapter.StorageAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class ListStorageActivity extends BaseActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView storageRecyclerView;
    private Button addButton;


    private ListStorageController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new ListStorageController(this);

        controller.setupAdapter();
        setContentView(R.layout.activity_list_storage);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        storageRecyclerView = findViewById(R.id.storageRecyclerView);
        addButton = findViewById(R.id.addButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        storageRecyclerView.setLayoutManager(layoutManager);
        storageRecyclerView.setAdapter(controller.getAdapter());

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            controller.searchStorage(searchQuery);
        });

        addButton.setOnClickListener((view) -> {
            showAddStorageDialog();
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.startAdapter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        controller.stopAdapter();
    }

    public void showAddStorageDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_storage);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        EditText nameEditText = dialog.findViewById(R.id.nameEditText);
        EditText locationEditText = dialog.findViewById(R.id.locationEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocompleteFragment);

        autocompleteFragment.setCountry("VN").setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // hide autocompleteFragment defaut search bar and se
        autocompleteFragment.getView().setVisibility(View.GONE);

        locationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // click on search bar
                autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).performClick();
            }
        });
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locationEditText.setText(place.getAddress());
            }


            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        dialogTitle.setText("Thêm kho hàng mới");

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String location = locationEditText.getText().toString();
            controller.addStorage(name, location);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showEditStorageDialog(Storage storage) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_storage);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        EditText nameEditText = dialog.findViewById(R.id.nameEditText);
        EditText locationEditText = dialog.findViewById(R.id.locationEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        dialogTitle.setText("Sửa thông tin kho hàng");
        nameEditText.setText(storage.getName());
        locationEditText.setText(storage.getLocation());

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocompleteFragment);

        autocompleteFragment.setCountry("VN").setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteFragment.getView().setVisibility(View.GONE);

        locationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                autocompleteFragment.setText(storage.getLocation());
                autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).performClick();
            }
        });
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locationEditText.setText(place.getAddress());
            }


            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String location = locationEditText.getText().toString();
            controller.editStorage(storage.getId(), name, location);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showDeleteStorageDialog(Storage storage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa kho hàng");
        builder.setMessage("Bạn có chắc chắn muốn xóa kho hàng " + storage.getName() + " không?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            controller.deleteStorage(storage.getId());
            dialog.dismiss();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void setAdapter(StorageAdapter adapter) {
        storageRecyclerView.setAdapter(adapter);
    }
}
