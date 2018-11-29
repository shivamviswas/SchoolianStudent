package com.wikav.student.studentapp.individual;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wikav.student.studentapp.BottomNavigationViewHelper;
import com.wikav.student.studentapp.BottomSheet;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.MainActivties.HomeMenuActivity;
import com.wikav.student.studentapp.MainActivties.Login;
import com.wikav.student.studentapp.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class NewProfileIndi extends AppCompatActivity {
    ImageView imView;

    TextView name, email, bio, mobile, sclName, className;
    BottomNavigationView bottomNavigationView;
    Button logut, editProfile;

    Uri resultUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    String uid;
    private FirebaseFirestore firebaseFirestore;
    RequestOptions option;
    private String Username;
    private String Useremail;
    private String Usermobile;
    private String Userimge;
    private String UserBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_indi);
        // imView = findViewById(R.id.profile);
        Toolbar toolbar = findViewById(R.id.profile_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBarForprofile);
        Config config = new Config(this);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        config.CheckConnection();
        setUpBottomNavigationView();

        logut = (Button) findViewById(R.id.logoutPro);
        editProfile = (Button) findViewById(R.id.editPro);
        option = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.man);
        name = findViewById(R.id.name);
        email = findViewById(R.id.emailof);
        mobile = findViewById(R.id.mobileof);
        className = findViewById(R.id.location);
        sclName = findViewById(R.id.designation);
        bio = findViewById(R.id.bio);
        imView = findViewById(R.id.profile);

        //imageView.setImageURI(Uri.parse(Photo));
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    if (task.getResult().exists()) {
                        Username = task.getResult().getString("Name");
                        Useremail = task.getResult().getString("Email");
                        Usermobile = task.getResult().getString("Mobile");
                        Userimge = task.getResult().getString("Profile_Pic");
                        UserBio = task.getResult().getString("Bio");
                        name.setText(Username);
                        email.setText(Useremail);
                        mobile.setText(Usermobile);
                        bio.setText(UserBio);
                        Glide.with(NewProfileIndi.this).load(Userimge).apply(option).into(imView);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    String ex = task.getException().getMessage();
                    Toast.makeText(NewProfileIndi.this, "Firestore retrive Error" + ex, Toast.LENGTH_SHORT).show();
                }
            }
        });


        logut.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewProfileIndi.this);
                                         alertDialogBuilder.setMessage("Are you sure, You want to logout");
                                         alertDialogBuilder.setPositiveButton("Yes",
                                                 new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface arg0, int arg1) {
                                                         mAuth.signOut();
                                                         Intent intent = new Intent(NewProfileIndi.this, Login.class);
                                                         startActivity(intent);
                                                         finish();
                                                     }
                                                 });

                                         alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                             public void onClick(DialogInterface dialog, int which) {
                                                 alertDialogBuilder.setCancelable(true);
                                             }
                                         });

                                         AlertDialog alertDialog = alertDialogBuilder.create();
                                         alertDialog.show();
                                     }
                                 }
        );

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetForInd bottomSheet = new BottomSheetForInd();
                bottomSheet.show(getSupportFragmentManager(), "example");


            }
        });


    }

    public void chooseFile(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(NewProfileIndi.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                //Toast.makeText(this, ""+resultUri, Toast.LENGTH_SHORT).show();
                imView.setImageURI(resultUri);
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage() {


        progressBar.setVisibility(View.VISIBLE);
        StorageReference imagePath = storageReference.child("Profile_Images").child(uid + ".jpg");
        imagePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {
                    Uri download_image = task.getResult().getDownloadUrl();
                    updateImage(download_image);
                } else {
                    String ex = task.getException().getMessage();
                    Toast.makeText(NewProfileIndi.this, "Firestore Error" + ex, Toast.LENGTH_SHORT).show();
                    //  progressBar.setVisibility(View.GONE);
                }


            }
        });


    }

    void updateImage(Uri download_image) {

        Map<String, String> userMap = new HashMap<>();
        userMap.put("Profile_Pic", String.valueOf(download_image));
        firebaseFirestore.collection("Users").document(uid).set(userMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RefreshData();
                } else {
                    String ex = task.getException().getMessage();
                    Toast.makeText(NewProfileIndi.this, "Firestore Error" + ex, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    private void setUpBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        BottomNavigationViewHelperForIndi.enableNavigation(NewProfileIndi.this, bottomNavigationView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NewProfileIndi.this, Ind_HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(NewProfileIndi.this, Ind_HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /* private void UploadPicture(final String id, final String photo) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
     //   progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                String pic=jsonObject.getString("message");
                                sessionManger.porfilepic(pic);
                                Toast.makeText(NewProfileIndi.this, "Success!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(NewProfileIndi.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(NewProfileIndi.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sid", id);
                params.put("photo", photo);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }*/
//    public void profileData() {
//
//    if (user != null) {
//        String Ename = user.get(sessionManger.NAME);
//        String Elastname = user.get(sessionManger.LASTNAME);
//        String Eemail = user.get(sessionManger.EMAIL);
//        String Esid = user.get(sessionManger.SID);
//        String Ebio = user.get(sessionManger.BIO);
//        String mobile = user.get(sessionManger.MOBILE);
//        String sclName = user.get(sessionManger.SCLNAME);
//        String clasName = user.get(sessionManger.CLAS);
//        String photo = user.get(sessionManger.PHOTO);
//
//
//        //  imView.setImageURI(Uri.parse(to));
////
//
//        Glide.with(NewProfileIndi.this).load(photo).apply(option).into(imView);
//
////
////        else
////        {
////            imView.setImageResource(R.drawable.man);
////        }
//        name.setText(Ename+" "+Elastname);
//        this.mobile.setText(mobile);
//        className.setText(clasName);
//        this.sclName.setText("SID: " + user.get(sessionManger.SID));
//        this.bio.setText(Ebio);
//        email.setText(Eemail);
////        sid.setText(Esid);
////        bio.setText(Ebio);
//        getId = Esid;
//    }
//}
    void RefreshData() {
        //progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    if (task.getResult().exists()) {
                        Username = task.getResult().getString("Name");
                        Useremail = task.getResult().getString("Email");
                        Usermobile = task.getResult().getString("Mobile");
                        Userimge = task.getResult().getString("Profile_Pic");
                        UserBio = task.getResult().getString("Bio");
                        name.setText(Username);
                        email.setText(Useremail);
                        mobile.setText(Usermobile);
                        bio.setText(UserBio);
                        Glide.with(NewProfileIndi.this).load(Userimge).apply(option).into(imView);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    String ex = task.getException().getMessage();
                    Toast.makeText(NewProfileIndi.this, "Firestore retrive Error" + ex, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(NewProfileIndi.this, Login.class);
            startActivity(intent);
            finish();
        }

    }
}

