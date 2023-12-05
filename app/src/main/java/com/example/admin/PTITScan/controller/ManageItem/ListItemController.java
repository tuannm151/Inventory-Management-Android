package com.example.admin.PTITScan.controller.ManageItem;

import com.example.admin.PTITScan.model.Item;
import com.example.admin.PTITScan.view.activities.ManageItem.ListItemActivity;
import com.example.admin.PTITScan.view.activities.ManageItem.adapter.ItemAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListItemController {
    private ListItemActivity view;
    private ItemAdapter adapter;
    private FirebaseAuth firebaseAuth;
    DatabaseReference itemReference;


    public ListItemController(ListItemActivity activity) {
        this.view = activity;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.itemReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getEmail().replace(".", "")).child("Items");
    }

    public void setupAdapter() {
        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(itemReference, Item.class).build();
        adapter = new ItemAdapter(options, this);
    }

    public void searchItem(String filterText) {
        Query query;
        if (filterText != null && !filterText.isEmpty()) {
            query = itemReference.orderByChild("barcode").startAt(filterText).endAt(filterText + "\uf8ff");
        } else {
            query = itemReference;
        }

        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, Item.class)
                        .build();
        adapter = new ItemAdapter(options, this);
        view.setAdapter(adapter);
        adapter.startListening();
    }

    public void addItem(String name, String category, String barcode, String price) {
        if(name.isEmpty() || category.isEmpty() || barcode.isEmpty() || price.isEmpty()) {
            view.showToast("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        Item item = new Item(name, category, price, barcode);
        itemReference.push().setValue(item).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                view.showToast("Thêm sản phẩm thành công");
            } else {
                view.showToast("Đã xảy ra lỗi");
            }
        });
    }

    public void editItem(String id, String name, String category, String barcode, String price) {
        if(name.isEmpty() || category.isEmpty() || barcode.isEmpty() || price.isEmpty()) {
            view.showToast("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        Item item = new Item(name, category, price, barcode);
        itemReference.child(id).setValue(item).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                view.showToast("Sửa sản phẩm thành công");
            } else {
                view.showToast("Đã xảy ra lỗi");
            }
        });
    }

    public void deleteItem(String id) {
        itemReference.child(id).removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                view.showToast("Xóa sản phẩm thành công");
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

    public ItemAdapter getAdapter() {
        return adapter;
    }

    public void handleViewClickEdit(String id, Item model) {
        Item item = new Item(id, model.getName(), model.getCategory(), model.getPrice(), model.getBarcode());
        view.showEditItemDialog(item);
    }

    public void handleViewClickDelete(String id, Item model) {
        Item item = new Item(id, model.getName(), model.getCategory(), model.getPrice(), model.getBarcode());
        view.showDeleteItemDialog(item);
    }
}
