package com.example.admin.PTITScan.view.activities.ManageItem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.model.Item;
import com.example.admin.PTITScan.view.activities.ManageItem.scanItemsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class viewInventoryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
   private TextView totalnoofitem, totalnoofsum;
   private int counttotalnoofitem =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        totalnoofitem= findViewById(R.id.totalnoitem);
        totalnoofsum = findViewById(R.id.totalsum);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));


        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    counttotalnoofitem = (int) dataSnapshot.getChildrenCount();
                    totalnoofitem.setText(Integer.toString(counttotalnoofitem));
                }else{
                    totalnoofitem.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    mdatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            int sum=0;
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Map<String,Object> map = (Map<String, Object>) ds.getValue();
                Object price = map.get("itemprice");
                int pValue = Integer.parseInt(String.valueOf(price));
                sum += pValue;
                totalnoofsum.setText(String.valueOf(sum));

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

    @Override
    protected  void  onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Item, scanItemsActivity.UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Item, scanItemsActivity.UsersViewHolder>
                (  Item.class,
                        R.layout.list_layout,
                        scanItemsActivity.UsersViewHolder.class,
                        mdatabaseReference )
        {
            @Override
            protected void populateViewHolder(scanItemsActivity.UsersViewHolder viewHolder, Item model, int position){

                viewHolder.setDetails(getApplicationContext(),model.getItembarcode(),model.getItemcategory(),model.getItemname(),model.getItemprice());
            }
        };

        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }

}
