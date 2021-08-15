package com.example.dontwaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupPage extends AppCompatActivity implements View.OnClickListener{
    private TextView loginS;
    private EditText org,name,email,phone,pass,cpass;
    private Button signup;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    // REGEX for validation of password
    private static final String password_pattern="^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    Pattern psPattern= Pattern.compile(password_pattern);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        // Initialising firebase
        mAuth = FirebaseAuth.getInstance();

        // If user is Signed up already clicking on Login to continue
        loginS=(TextView) findViewById(R.id.slogin);
        loginS.setOnClickListener(this);

        signup=(Button) findViewById(R.id.signupBtn);
        signup.setOnClickListener(this);

        org=(EditText) findViewById(R.id.orgName);
        name=(EditText) findViewById(R.id.regName);
        email=(EditText) findViewById(R.id.sEmail);
        phone=(EditText) findViewById(R.id.phNumber);
        pass=(EditText) findViewById(R.id.password);
        cpass=(EditText) findViewById(R.id.cpassword);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
    }

    // Upon click perform some operations
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.slogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.signupBtn:
                validateForm();
                break;
        }
    }

    // To check whether the form has valid data
    private void validateForm(){
        String oName=org.getText().toString().trim();
        String rName=name.getText().toString().trim();
        String eMail=email.getText().toString().trim();
        String pHone=phone.getText().toString().trim();
        String passWord=pass.getText().toString().trim();
        String cpassWord=cpass.getText().toString().trim();

        if(oName.isEmpty()){
            org.setError("Organization Name is required!");
            org.requestFocus();
            return;
        }

        if(rName.isEmpty()){
            name.setError("Full Name is required!");
            name.requestFocus();
            return;
        }

        if(eMail.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        // To check whether entered email address is valid or not
        if(!Patterns.EMAIL_ADDRESS.matcher(eMail).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        if(pHone.isEmpty()){
            phone.setError("Phone Number is required!");
            phone.requestFocus();
            return;
        }
        else if(pHone.length()<10 || pHone.length()>10){
            phone.setError("Please provide valid phone number!");
            phone.requestFocus();
            return;
        }

        // Check for validity of Password
        if(passWord.isEmpty()){
            pass.setError("Password is required!");
            pass.requestFocus();
            return;
        }
        else if(!psPattern.matcher(passWord).matches()){
            pass.setError("* Password should contain uppercase and lowercase letters.\n" +
                    "* Password should contain letters, numbers and special characters.\n" +
                    "* Minimum length of the password is 8).\n");
            pass.requestFocus();
            return;
        }

        if(cpassWord.isEmpty()){
            cpass.setError("Enter password for confirmation!");
            cpass.requestFocus();
            return;
        }
        else if(passWord.equals(cpassWord)){
            progressBar.setVisibility(View.VISIBLE);

            // firebase authentication
            mAuth.createUserWithEmailAndPassword(eMail,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Users us=new Users(oName,rName,eMail,pHone,passWord);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupPage.this,"You are registered Successfully!",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    org.setText(null);
                                    name.setText(null);
                                    email.setText(null);
                                    phone.setText(null);
                                    pass.setText(null);
                                    cpass.setText(null);
                                    startActivity(new Intent(SignupPage.this, MainActivity.class));
                                }
                                else{
                                    Toast.makeText(SignupPage.this,"Failed to register! Try Again!",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignupPage.this,"Failed to register! Try Again!",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        else{
            cpass.setError("Both password don't match!");
            cpass.requestFocus();
            return;
        }
    }
}