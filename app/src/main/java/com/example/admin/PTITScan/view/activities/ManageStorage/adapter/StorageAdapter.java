package com.example.admin.PTITScan.view.activities.ManageStorage.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.controller.ManageStorage.ListStorageController;
import com.example.admin.PTITScan.model.Storage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class StorageAdapter extends FirebaseRecyclerAdapter<Storage, StorageAdapter.StorageViewHolder> {
    private final ListStorageController controller;

    public StorageAdapter(@NonNull FirebaseRecyclerOptions<Storage> options, ListStorageController controller) {
        super(options);
        this.controller = controller;
    }

    @Override
    protected void onBindViewHolder(@NonNull StorageViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Storage model) {
        holder.name.setText(model.getName());
        holder.location.setText(model.getLocation());
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
    public StorageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storage_item, viewGroup, false);
        return new StorageViewHolder(view);
    }

    static class StorageViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView location;

        Button editButton;
        Button deleteButton;

        public StorageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            location = itemView.findViewById(R.id.locationTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }


    }
}