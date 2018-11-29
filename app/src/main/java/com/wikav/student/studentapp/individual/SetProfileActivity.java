package com.wikav.student.studentapp.individual;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wikav.student.studentapp.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetProfileActivity extends AppCompatActivity {
EditText emailUp,NameUp,MobileUp;
CircleImageView imageView;
String email,mobile,name,pass;
    Uri resultUri=null;
    String defaultPic="https://firebasestorage.googleapis.com/v0/b/schoolian-ef50a.appspot.com/o/man.jpg?alt=media&token=180db899-eeef-42e7-88e4-2becdcfe13de";
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        emailUp=findViewById(R.id.Emailup);
        progressBar=findViewById(R.id.progressBar3);
        MobileUp=findViewById(R.id.upmobile);
        NameUp=findViewById(R.id.upName);
        imageView=findViewById(R.id.profileImage);
        email= getIntent().getStringExtra("email");
        mobile= getIntent().getStringExtra("mobile");
        name= getIntent().getStringExtra("name");
        pass= getIntent().getStringExtra("pass");
        mAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        emailUp.setText(email);
        NameUp.setText(name);
        MobileUp.setText(mobile);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
                {
                    if(ContextCompat.checkSelfPermission(SetProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(SetProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else
                    {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetProfileActivity.this);
                    }
                }
                else
                {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(SetProfileActivity.this);
                }

            }
        });

    }

    public void submitUp(View view) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(mobile)) {

            if(resultUri==null)
            {   progressBar.setVisibility(View.VISIBLE);

                final String uid = mAuth.getCurrentUser().getUid();
                Map<String,String > userMap=new HashMap<>();
                userMap.put("Name", name);
                userMap.put("Email", email);
                userMap.put("Mobile", mobile);
                userMap.put("Password", pass);
                userMap.put("Bio", "N/A");
                userMap.put("Profile_Pic", defaultPic);
                firebaseFirestore.collection("Users").document(uid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent =new Intent(SetProfileActivity.this,Ind_HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            String ex=task.getException().getMessage();
                            Toast.makeText(SetProfileActivity.this, "Firestore Error"+ex, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }


                    }
                });
            }
            else {
                    final String uid = mAuth.getCurrentUser().getUid();
                progressBar.setVisibility(View.VISIBLE);
                StorageReference imagePath = storageReference.child("Profile_Images").child(uid + ".jpg");
                imagePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                       /* Intent intent =new Intent(IndiLogin.this,SetProfileActivity.class);
                        startActivity(intent);
                        finish();*/
                            Uri download_image = task.getResult().getDownloadUrl();
                            Map<String, String> userMap = new HashMap<>();
                            userMap.put("Name", name);
                            userMap.put("Email", email);
                            userMap.put("Mobile", mobile);
                            userMap.put("Password", pass);
                            userMap.put("Bio", "N/A");
                            userMap.put("Profile_Pic", String.valueOf(download_image));
                            firebaseFirestore.collection("Users").document(uid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SetProfileActivity.this, Ind_HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        String ex = task.getException().getMessage();
                                        Toast.makeText(SetProfileActivity.this, "Firestore Error" + ex, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }


                                }
                            });

                        } else {
                            String ex = task.getException().getMessage();
                            Toast.makeText(SetProfileActivity.this, "Image Error" + ex, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }

 /*   public void chooseFile() {

        Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                //Toast.makeText(this, ""+resultUri, Toast.LENGTH_SHORT).show();
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}
