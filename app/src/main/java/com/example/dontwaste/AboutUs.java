package com.example.dontwaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AboutUs extends AppCompatActivity implements View.OnClickListener{
    ImageView spkIn,varIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        spkIn=(ImageView)findViewById(R.id.spkInsta);
        spkIn.setOnClickListener(this);

        varIn=(ImageView)findViewById(R.id.varInsta);
        varIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spkInsta:
                goToUrl("https://www.instagram.com/suraj_pai_k/");
                break;
            case R.id.varInsta:
                goToUrl("https://www.instagram.com/varun.prabhu.777/");
                break;
        }
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent WebView = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(WebView);
    }
}