package com.example.dontwaste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginSuccessfull extends AppCompatActivity {
    private Button logout, update, reset;
    private EditText org, reg, eMail, phone;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Users users;
    private String oName, rName, email, phNo;
    private CircleImageView profile;
    private Uri imageUrl,downloadUri=null;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successfull);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);

        profile = (CircleImageView) findViewById(R.id.Img);

        firestore.collection("Images").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        // retrieve image
                        String imgUrl=task.getResult().getString("image");
                        Glide.with(LoginSuccessfull.this).load(imgUrl).into(profile);
                    }
                }
            }
        });

        logout = (Button) findViewById(R.id.logoutBtn);
        org = (EditText) findViewById(R.id.editOrgName);
        reg = (EditText) findViewById(R.id.editRegName);
        eMail = (EditText) findViewById(R.id.editEmail);
        phone = (EditText) findViewById(R.id.editPhone);
        update = (Button) findViewById(R.id.updateBtn);
        reset = (Button) findViewById(R.id.resetPasswordBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);

        eMail.setEnabled(false);

        // reset password
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        // to logout user
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LoginSuccessfull.this, MainActivity.class));
            }
        });

        // get information from database for displaying
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);

                if (users != null) {
                    oName = users.orgName;
                    rName = users.regName;
                    email = users.email;
                    phNo = users.phone;

                    org.setText(oName);
                    reg.setText(rName);
                    eMail.setText(email);
                    phone.setText(phNo);
//                    profile.setImageURI("images/"+userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginSuccessfull.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        // updating the profile
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        // updating the profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CHECK FOR PROFILE UPLOADING PERMISSION
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(LoginSuccessfull.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(LoginSuccessfull.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(LoginSuccessfull.this);
                    }
                }
            }
        });
    }

    private void resetPassword() {
        String emailId = eMail.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginSuccessfull.this, "Please check your mail to reset password!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(LoginSuccessfull.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginSuccessfull.this, "Something went wrong! Try Again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void update() {
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = snapshot.getValue(Users.class);

                if (users != null) {
                    oName = users.orgName;
                    rName = users.regName;
                    email = users.email;
                    phNo = users.phone;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginSuccessfull.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        if (isOrgNameChanged() && isNameChanged() && isPhoneChanged()) {
            Toast.makeText(this, "Data has been updated!", Toast.LENGTH_LONG).show();
        } else if (isOrgNameChanged()) {
            Toast.makeText(this, "Data has been updated!", Toast.LENGTH_LONG).show();
        } else if (isNameChanged()) {
            Toast.makeText(this, "Data has been updated!", Toast.LENGTH_LONG).show();
        } else if (isPhoneChanged()) {
            Toast.makeText(this, "Data has been updated!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data is same and cannot be updated!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOrgNameChanged() {
        if (!oName.equals(org.getText().toString())) {
            reference.child(userId).child("orgName").setValue(org.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if (!rName.equals(reg.getText().toString())) {
            reference.child(userId).child("regName").setValue(reg.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if (!phNo.equals(phone.getText().toString())) {
            reference.child(userId).child("phone").setValue(phone.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUrl = result.getUri();
                profile.setImageURI(imageUrl);
                uploadPicture();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(LoginSuccessfull.this, result.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadPicture() {
        progressDialog.setTitle("Uploading Image....");
        progressDialog.show();

        // Create a reference to "mountains.jpg"

        if (imageUrl != null) {
            final StorageReference imgRef = storageReference.child("Profiles").child(userId + ".jpg");

            imgRef.putFile(imageUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        saveToFireStore(task, imgRef);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginSuccessfull.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    int progressPercent= (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressDialog.setMessage("Percentage! "+progressPercent+" %");
                }
            });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(LoginSuccessfull.this, "Please select picture!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToFireStore(Task<UploadTask.TaskSnapshot> task, StorageReference imgRef) {
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUri=uri;

                HashMap<String,Object> map=new HashMap<>();
                map.put("image", downloadUri.toString());

                firestore.collection("Images").document(userId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(R.id.Img),"Image Uploaded!",Snackbar.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Failed to Upload!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}