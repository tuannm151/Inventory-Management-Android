package com.example.admin.PTITScan.controller.ManageStorage;

import android.util.Log;

import com.example.admin.PTITScan.model.Storage;
import com.example.admin.PTITScan.view.activities.ManageStorage.ListStorageActivity;
import com.example.admin.PTITScan.view.activities.ManageStorage.adapter.StorageAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListStorageController {
    private ListStorageActivity view;
    private StorageAdapter adapter;
    private FirebaseAuth firebaseAuth;
    DatabaseReference storageReference;


    public ListStorageController(ListStorageActivity activity) {
        this.view = activity;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.storageReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getEmail().replace(".", "")).child("Storages");
    }

    public void setupAdapter() {
        FirebaseRecyclerOptions<Storage> options = new FirebaseRecyclerOptions.Builder<Storage>().setQuery(storageReference, Storage.class).build();
        adapter = new StorageAdapter(options, this);
    }

    public void searchStorage(String filterText) {
        Query query;
        if (filterText != null && !filterText.isEmpty()) {
            query = storageReference.orderByChild("name").startAt(filterText).endAt(filterText + "\uf8ff");
        } else {
            query = storageReference;
        }

        FirebaseRecyclerOptions<Storage> options =
                new FirebaseRecyclerOptions.Builder<Storage>()
                        .setQuery(query, Storage.class)
                        .build();
        adapter = new StorageAdapter(options, this);
        view.setAdapter(adapter);
        adapter.startListening();
    }

    public void addStorage(String name, String location) {
        if(name.isEmpty() || location.isEmpty()) {
            view.showToast("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        Storage storage = new Storage(name, location);
        storageReference.push().setValue(storage).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                view.showToast("Thêm kho hàng thành công");
            } else {
                view.showToast("Đã xảy ra lỗi");
            }
        });
    }

    public void editStorage(String id, String name, String location) {
        if(name.isEmpty() || location.isEmpty()) {
            view.showToast("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        Storage storage = new Storage(name, location);
        storageReference.child(id).setValue(storage).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                view.showToast("Sửa kho hàng thành công");
            } else {
                view.showToast("Đã xảy ra lỗi");
            }
        });
    }

    public void deleteStorage(String id) {
        storageReference.child(id).removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                view.showToast("Xóa kho hàng thành công");
            } else {
                view.showToast("Đã xảy ra lỗi");
            }
        });
    }

    public void startAdapter() {
        adapter.startListening();
    }

    public void stopAdapter() {
        adapter.stopListening();
    }

    public StorageAdapter getAdapter() {
        return adapter;
    }

    public void handleViewClickEdit(String id, Storage model) {
        Storage storage = new Storage(id, model.getName(), model.getLocation());
        view.showEditStorageDialog(storage);
    }

    public void handleViewClickDelete(String id, Storage model) {
        Storage storage = new Storage(id, model.getName(), model.getLocation());
        view.showDeleteStorageDialog(storage);
    }
}
