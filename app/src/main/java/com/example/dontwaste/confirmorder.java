package com.example.dontwaste;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class confirmorder extends AppCompatActivity {
    public EditText _txtMessage;
    public Button _btnsend;
    private FirebaseUser user;
    private DatabaseReference reference,reference1;
    private String userId;
    private Users users;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);

        _txtMessage= (EditText)findViewById(R.id.txtMessage);
        _btnsend= (Button)findViewById(R.id.buttonsend);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference1 = FirebaseDatabase.getInstance().getReference("Donors");
        userId = user.getUid();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        String key=bundle.getString("keyid");

        // get information from database for displaying
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);

                if (users != null) {
                    String oName = users.orgName;
                    String rName = users.regName;
                    String email = users.email;
                    String phNo = users.phone;

                    _txtMessage.setEnabled(false);

                    _txtMessage.setText("Dear donor,\n\n" +
                            "I am "+rName+" from "+oName+". We are really in need of food. The donation i.e., "+bundle.getString("FoodName")+" of "+bundle.getString("Quantity")+" Kg you made at DontWaste app will help us. So, the purpose of this mail is to inform you that, I am interested to collect the food you donated on or before "+bundle.getString("Date")+", "+bundle.getString("Time")+" by meeting you at the address provided i.e., "+bundle.getString("Address")+". Your help will  be appreciated forever. Hoping to meet you soon.\n\n" +
                            "Thank You\n\n" +
                            "Regards,\n"+rName+"\n"+oName+"\n"+phNo+"\n" +
                            "\n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(confirmorder.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        _btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = "dontwaste0408@gmail.com";
                final String password = "\"Dontwaste0408@\"";

                Properties props = new Properties();
                props.put("mail.smtp.auth","true");
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.port","587");

                Session session = Session.getInstance(props , new javax.mail.Authenticator(){
                    @Override
                    protected  PasswordAuthentication getPasswordAuthentication(){
                        return  new PasswordAuthentication(username, password);
                    }
                });

                try{
                    javax.mail.Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bundle.getString("Email")));
                    message.setSubject("Request For Your Food (DontWaste)");
                    message.setText(_txtMessage.getText().toString());
                    Transport.send(message);
                    Toast.makeText(getApplicationContext(),"Email sent to the donor Successfully!!", Toast.LENGTH_LONG).show();
                }
                catch (MessagingException e){
                    throw new RuntimeException(e);
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}