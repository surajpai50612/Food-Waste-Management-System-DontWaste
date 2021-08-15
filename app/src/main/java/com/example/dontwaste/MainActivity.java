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
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView signupL,forgotPassword;
    private EditText editTextEmail,editTextPassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    // REGEX for validation of password
    private static final String password_pattern="^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    Pattern psPattern= Pattern.compile(password_pattern);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising firebase
        mAuth = FirebaseAuth.getInstance();

        // If user is not Signed up clicking on Signup to create new account
        signupL=(TextView) findViewById(R.id.lsignup);
        signupL.setOnClickListener(this);

        forgotPassword=(TextView) findViewById(R.id.fpassword);
        forgotPassword.setOnClickListener(this);

        login=(Button) findViewById(R.id.loginBtn);
        login.setOnClickListener(this);

        editTextEmail=(EditText) findViewById(R.id.lemail);
        editTextPassword=(EditText) findViewById(R.id.lpassword);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.lsignup:
                startActivity(new Intent(this,SignupPage.class));
                break;
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.fpassword:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }
    }

    private void  userLogin(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        // To check whether entered email address is valid or not
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        // Check for validity of Password
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        else if(!psPattern.matcher(password).matches()){
            editTextPassword.setError("* Password should contain uppercase and lowercase letters.\n" +
                    "* Password should contain letters, numbers and special characters.\n" +
                    "* Minimum length of the password is 8).\n");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // firebase authentication
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // To check whether email is verified
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        // redirect to user profile
                        startActivity(new Intent(MainActivity.this,Home.class));
                        Toast.makeText(MainActivity.this, "You are Successfully Loggedin!", Toast.LENGTH_LONG).show();
                        editTextEmail.setText(null);
                        editTextPassword.setText(null);
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        // to verify mail
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your mail to verify your account", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        editTextEmail.setText(null);
                        editTextPassword.setText(null);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Failed to login! Invalid credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    editTextEmail.setText(null);
                    editTextPassword.setText(null);
                }
            }
        });
    }
}