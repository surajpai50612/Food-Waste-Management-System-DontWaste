package com.example.dontwaste;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dontwaste.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class mydesignadapter extends RecyclerView.ViewHolder {

    TextView Food,orgName;
    Button view;
    ImageView image;

    public mydesignadapter(@NonNull View itemView) {
        super(itemView);

        Food=(TextView)itemView.findViewById(R.id.post_fName);
        orgName = (TextView)itemView.findViewById(R.id.post_orgName);
        view  = (Button)itemView.findViewById(R.id.viewbtn);
        image  = (ImageView)itemView.findViewById(R.id.img);
    }
}
