package com.example.dontwaste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class info extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref;
    private String userID , keyid;

    private Button backbtn, requestbtn;
    TextView  disporgname,dispdonorname,dispphno, dispdate, disptime, dispquantity, dispaddress, dispfoodname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        requestbtn = (Button)findViewById(R.id.reqbtn);

        keyid = getIntent().getExtras().get("keyid").toString();

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        fetch();

        backbtn = (Button) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(info.this, Request.class));
            }
        });
        requestbtn = (Button) findViewById(R.id.reqbtn);

        disporgname = (TextView) findViewById(R.id.disporgname);
        dispdonorname = (TextView) findViewById(R.id.dispdname);
        dispphno = (TextView) findViewById(R.id.dispphno);
        dispaddress = (TextView) findViewById(R.id.dispaddress);
        dispfoodname = (TextView) findViewById(R.id.dispfoodname);
        disptime = (TextView) findViewById(R.id.disptime);
        dispdate = (TextView) findViewById(R.id.dispdate);
        dispquantity = (TextView) findViewById(R.id.dispquantity);


         requestbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(info.this,confirmorder.class));
              }
          });

    }

    public  void  fetch(){
        DatabaseReference documentReference = FirebaseDatabase.getInstance().getReference("Donors").child(keyid);

        documentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getinfo getinfo = snapshot.getValue(getinfo.class);

                dispdonorname.setText(getinfo.getRegName());
                disporgname.setText(getinfo.getOrgname());
                dispphno.setText(getinfo.getPhone());
                dispaddress.setText(getinfo.getAddress());
                dispdate.setText(getinfo.getDate());
                disptime.setText(getinfo.getTime());
                dispfoodname.setText(getinfo.getFood());
                dispquantity.setText(getinfo.getQuantity()+" Kg");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}