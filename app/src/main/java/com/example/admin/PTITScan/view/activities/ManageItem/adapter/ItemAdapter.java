package com.example.admin.PTITScan.view.activities.ManageItem.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.controller.ManageItem.ListItemController;
import com.example.admin.PTITScan.model.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ItemAdapter extends FirebaseRecyclerAdapter<Item, ItemAdapter.ItemViewHolder> {
    private final ListItemController controller;

    public ItemAdapter(@NonNull FirebaseRecyclerOptions<Item> options, ListItemController controller) {
        super(options);
        this.controller = controller;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Item model) {
        holder.name.setText(model.getName());
        holder.category.setText(model.getCategory());
        holder.price.setText(model.getPrice());
        holder.barcode.setText(model.getBarcode());
        holder.editButton.setOnClickListener(v -> {
            String id = getRef(position).getKey();
            controller.handleViewClickEdit(id, model);
        });
        holder.deleteButton.setOnClickListener(v -> {
            String id = getRef(position).getKey();
            controller.handleViewClickDelete(id, model);
        });
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView category;
        TextView price;

        TextView barcode;

        Button editButton;
        Button deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            category = itemView.findViewById(R.id.categoryTextView);
            price = itemView.findViewById(R.id.priceTextView);
            barcode = itemView.findViewById(R.id.barcodeTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}