package com.example.dontwaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Home extends AppCompatActivity implements View.OnClickListener{
    ImageButton donate,request,about,settings;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        donate=(ImageButton)findViewById(R.id.donateBtn);
        donate.setOnClickListener(this);

        request=(ImageButton)findViewById(R.id.requestBtn);
        request.setOnClickListener(this);

        about=(ImageButton)findViewById(R.id.aboutusBtn);
        about.setOnClickListener(this);

        settings=(ImageButton)findViewById(R.id.settingsBtn);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.donateBtn:
                startActivity(new Intent(Home.this,Donate.class));
                break;
            case R.id.requestBtn:
                startActivity(new Intent(Home.this,Request.class));
                break;
            case R.id.aboutusBtn:
                startActivity(new Intent(Home.this,AboutUs.class));
                break;
            case R.id.settingsBtn:
                startActivity(new Intent(Home.this,LoginSuccessfull.class));
                break;
        }
    }
}