package com.example.dontwaste;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.dontwaste.R.layout.activity_request;

public class Request extends AppCompatActivity {

    RecyclerView recview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_request);

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<getinfo> options = new FirebaseRecyclerOptions.Builder<getinfo>().setQuery(FirebaseDatabase.getInstance().getReference().child("Donors"), getinfo.class).build();

        FirebaseRecyclerAdapter<getinfo, mydesignadapter> adapter =
                new FirebaseRecyclerAdapter<getinfo, mydesignadapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull mydesignadapter holder, int position, @NonNull getinfo model) {

                        holder.Food.setText(model.getFood());
                        holder.orgName.setText(model.getOrgname());

                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String keyid = model.getKeyid();

                                Bundle bundle = new Bundle();
                                bundle.putString("FoodName", model.getFood());
                                bundle.putString("Quantity", model.getQuantity());
                                bundle.putString("Date", model.getDate());
                                bundle.putString("Time", model.getTime());
                                bundle.putString("Address", model.getAddress());
                                bundle.putString("Email", model.getEmail());
                                bundle.putString("keyid", keyid);

                                Intent intent1 = new Intent(Request.this,confirmorder.class);
                                intent1.putExtras(bundle);

                                startActivity(intent1);

                                Intent intent = new Intent(Request.this, info.class);
                                intent.putExtra("keyid" , keyid);
                                startActivity(intent);
                            }
                        });
                        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
                    }

                    @NonNull
                    @Override
                    public mydesignadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                        mydesignadapter holder = new mydesignadapter(view);
                        return holder;
                    }
                };
        recview.setAdapter(adapter);
        adapter.startListening();
    }
}