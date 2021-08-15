package com.example.dontwaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Donate extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser user;
    private DatabaseReference reference;
    private EditText orgName, donName, phNo, address, foodName,foodQuantity,ordDate,ordTime;
    private Button donate, cancel;
    private ProgressBar progressBar;
    private String userid,imgUrl,emailAddress;
    private CircleImageView profileImg;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userid = user.getUid();

        orgName = (EditText) findViewById(R.id.oName);
        donName = (EditText) findViewById(R.id.dName);
        phNo = (EditText) findViewById(R.id.pNum);
        address = (EditText) findViewById(R.id.addr);
        foodName = (EditText) findViewById(R.id.fName);
        foodQuantity = (EditText) findViewById(R.id.fQuant);
        ordDate = (EditText) findViewById(R.id.oDate);
        ordTime = (EditText) findViewById(R.id.oTime);
        progressBar = (ProgressBar) findViewById(R.id.pBar);

        donate = (Button) findViewById(R.id.saveBtn);
        donate.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.pBar);

        orgName.setEnabled(false);
        donName.setEnabled(false);
        phNo.setEnabled(false);

        firestore = FirebaseFirestore.getInstance();
        profileImg = (CircleImageView) findViewById(R.id.Img);

        firestore.collection("Images").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        // retrieve image
                        imgUrl=task.getResult().getString("image");
                        Glide.with(Donate.this).load(imgUrl).into(profileImg);
                    }
                }
            }
        });

        // get information from database for displaying
        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userprofile = snapshot.getValue(Users.class);

                if(userprofile!= null){
                    String organizationName = userprofile.orgName;
                    String donorName = userprofile.regName;
                    String phoneNumber = userprofile.phone;
                    emailAddress = userprofile.email;

                    orgName.setText(organizationName);
                    donName.setText(donorName);
                    phNo.setText(phoneNumber);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Donate.this,"Error in displaying data!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                saveUser();
                break;
            case R.id.cancelBtn:
                address.setText("");
                foodName.setText("");
                foodQuantity.setText("");
                ordDate.setText("");
                ordTime.setText("");
                break;
        }
    }

    private void saveUser() {
        String Address = address.getText().toString().trim();
        String food = foodName.getText().toString().trim();
        String quantity = foodQuantity.getText().toString().trim();
        String date = ordDate.getText().toString().trim();
        String time = ordTime.getText().toString().trim();
        String org = orgName.getText().toString().trim();
        String donor = donName.getText().toString().trim();
        String pHone = phNo.getText().toString().trim();

        if (Address.isEmpty()) {
            address.setError("Address is required");
            address.requestFocus();
            return;
        }

        if (food.isEmpty()) {
            foodName.setError("Food Name is required");
            foodName.requestFocus();
            return;
        }

        if (quantity.isEmpty()) {
            foodQuantity.setError("Quantity is required");
            foodQuantity.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            ordDate.setError("Date is required");
            ordDate.requestFocus();
            return;
        }

        if (time.isEmpty()) {
            ordTime.setError("Time is required");
            ordTime.requestFocus();
            return;
        }

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM,DD,YYYY");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
        String saveCurrentTime = currentTime.format(calForDate.getTime());

        String random = saveCurrentDate +""+ saveCurrentTime;

        DatabaseReference documentReference= FirebaseDatabase.getInstance().getReference().child("Donors");
        Map<String,Object> user = new HashMap<>();
        user.put("Address", Address);
        user.put("Food", food);
        user.put("Quantity", quantity);
        user.put("Date", date);
        user.put("Time", time);
        user.put("orgName",org);
        user.put("regName", donor);
        user.put("phone", pHone);
        user.put("keyid", random);
        user.put("image", imgUrl);
        user.put("email", emailAddress);

        progressBar.setVisibility(View.VISIBLE);

        documentReference.child(random).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Donate.this, "Item donated successfully!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    address.setText("");
                    foodName.setText("");
                    foodQuantity.setText("");
                    ordDate.setText("");
                    ordTime.setText("");
                    startActivity(new Intent(Donate.this, Request.class));
                }
                else{
                    Toast.makeText(Donate.this, "Error! in donating!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}