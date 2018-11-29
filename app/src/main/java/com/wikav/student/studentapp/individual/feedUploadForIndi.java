package com.wikav.student.studentapp.individual;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

//import com.android.volley.request.SimpleMultiPartRequest;

public class feedUploadForIndi extends AppCompatActivity {
    private static final int MAX_LENGTH = 100;
    ImageButton imageButton;
    Uri resultUri;
    String BLANK_IMAGE = "N/A", BLANK_POST = "";
    private ImageView viewImage;
    private Button imageSelect, uploadBtn;
    private EditText postText;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;
    private Bitmap compres;
    String rand;
    Uri dwnld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_upload_for_indi);
        Config config = new Config(this);

        config.CheckConnection();
        viewImage = (ImageView) findViewById(R.id.postImage2);
        uploadBtn = findViewById(R.id.postUp);
        imageSelect = findViewById(R.id.choosIm);
        imageButton = findViewById(R.id.imageButton);
        progressBar=findViewById(R.id.progressFeedUplod);
        postText = findViewById(R.id.postText);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImage.setImageBitmap(null);
                viewImage.setVisibility(View.GONE);
                imageSelect.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.GONE);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

        /*uploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                final String et=postText.getText().toString().trim();

                if(et.isEmpty()&&i==0)
                {
                    postText.setError("Please Enter Query Or Select Image");
                }
                else {

                    if (i == 0) {
                        //
                        UploadOnlyWrite(getId, et);

                    } else if (et.isEmpty() && i == 1) {
                        // Toast.makeText(feedUpload.this, "et worrk", Toast.LENGTH_SHORT).show();
                        UploadOnlyPicture(getId, getStringImage(thumbnail));
                    } else {
                        //Toast.makeText(feedUpload.this, "waorklslsls", Toast.LENGTH_SHORT).show();
                        UploadWithPicture(getId, et, getStringImage(thumbnail));
                    }

                }
            }

        });*/

    }
/////////////////////////////////////////////////////////////// UPLOAD WITH PICTURE//////////////////////////////////////////////////////////////////////////////////


    /*private void UploadWithPicture(final String id, final String et, final String stringImage) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        feedUploadForIndi.this.setFinishOnTouchOutside(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(feedUploadForIndi.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(feedUploadForIndi.this,HomeMenuActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(feedUploadForIndi.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(feedUploadForIndi.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id",id);
                params.put("post_pic",stringImage);
                params.put("posts",et);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

/////////////////////////////////////////////////////////////// UPLOAD ONLY TEXT//////////////////////////////////////////////////////////////////////////////////

    private void UploadOnlyWrite(final String id, final String et) {
        final String BLANK="NA";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        feedUploadForIndi.this.setFinishOnTouchOutside(false);
         StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(feedUploadForIndi.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(feedUploadForIndi.this,HomeMenuActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(feedUploadForIndi.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(feedUploadForIndi.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id",id);
                params.put("posts",et);
                params.put("post_pic",BLANK);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

    public void chooseImage(View view) {


        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(feedUploadForIndi.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//                    startActivityForResult(intent, 1);

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 1);
                    }

                }

                else if (options[item].equals("Choose from Gallery"))

                {

//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                    startActivityForResult(intent, 2);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image*//*");
//                    intent.putExtra("crop", "true");
//                    intent.putExtra("scale", true);
//                    intent.putExtra("outputX", 256);
//                    intent.putExtra("outputY", 256);
//                    intent.putExtra("aspectX", 1);
//                    intent.putExtra("aspectY", 1);
//                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, 2);


                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }



    @SuppressLint("LongLogTag")
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Bundle extras = data.getExtras();
               thumbnail = (Bitmap) extras.get("data");
                  viewImage.setVisibility(View.VISIBLE);
                    imageSelect.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                         viewImage.setImageBitmap(thumbnail);
                        i=1;

            }

            else if (requestCode == 2) {

                Uri selectedImage = data.getData();

//                String[] filePath = { MediaStore.Images.Media.DATA };
//
//                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//
//                c.moveToFirst();
//
//                int columnIndex = c.getColumnIndex(filePath[0]);
//
//                String picturePath = c.getString(columnIndex);
//
//                c.close();
//                final Bundle extras = data.getExtras();
//
//                    //Get image
//                    thumbnail = (Bitmap) extras.get("data");

                try {
                    thumbnail= MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    viewImage.setVisibility(View.VISIBLE);
                    imageSelect.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    viewImage.setImageBitmap(thumbnail);
                    viewImage.setRotation(0);
                    i=1;
                } catch (IOException e) {
                    e.printStackTrace();
                }


               // thumbnail = (BitmapFactory.decodeFile(picturePath));


            }

        }

    }

    /////////////////////////////////////////////////////////////// UPLOAD PICTURE ONLY//////////////////////////////////////////////////////////////////////////////////
    private void UploadOnlyPicture(final String id, final String photo) {
        final String BLANK="";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        feedUploadForIndi.this.setFinishOnTouchOutside(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(feedUploadForIndi.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(feedUploadForIndi.this,HomeMenuActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(feedUploadForIndi.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(feedUploadForIndi.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", id);
                params.put("post_pic", photo);
                params.put("posts", BLANK);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }*/


    public void chooseImageIndi(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setOutputCompressQuality(90)
                .start(feedUploadForIndi.this);
    }


    private void uploadPost() {
        progressBar.setVisibility(View.VISIBLE);
        final String posttext = postText.getText().toString();
        if (resultUri == null && posttext.equals("")) {

            postText.setError("Please Select Image Or type something");
        } else {
            if (resultUri == null) {
                finalUpload(BLANK_IMAGE, posttext, BLANK_IMAGE);
            } else {
                rand = UUID.randomUUID().toString();
                StorageReference imagePath = storageReference.child("PostImage").child(rand + "jpg");
                imagePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                           dwnld = task.getResult().getDownloadUrl();

                            File file=new File(resultUri.getPath());
                            try {
                                compres=new Compressor(feedUploadForIndi.this).setMaxHeight(200).setMaxWidth(200).setQuality(20).compressToBitmap(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream boas=new ByteArrayOutputStream();
                            compres.compress(Bitmap.CompressFormat.JPEG,50,boas);
                            byte[] thumbdata=boas.toByteArray();

                            UploadTask image =storageReference.child("PostImage/thumb").child(rand + "jpg").putBytes(thumbdata);

                            image.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   String linkThumb=taskSnapshot.getDownloadUrl().toString();
                                    if (posttext.equals(BLANK_POST)) {
                                        finalUpload(String.valueOf(dwnld), BLANK_POST,linkThumb);
                                    } else {
                                        finalUpload(String.valueOf(dwnld), posttext,linkThumb);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });




                        } else {
                            String ex = task.getException().getMessage();
                            Toast.makeText(feedUploadForIndi.this, "Storage Error" + ex, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        }

    }

    void finalUpload(String image, String post, String linkThumb) {
       String user= mAuth.getCurrentUser().getUid();
        Map<String,Object> userMap=new HashMap<>();
        userMap.put("userId",user);
        userMap.put("image",image);
        userMap.put("post",post);
        userMap.put("thumb",linkThumb);
        userMap.put("timeStamp",FieldValue.serverTimestamp());
        firebaseFirestore.collection("Posts").add(userMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(feedUploadForIndi.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(feedUploadForIndi.this,Ind_HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    String ex = task.getException().getMessage();
                    Toast.makeText(feedUploadForIndi.this, "Storage Error" + ex, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(feedUploadForIndi.this, Ind_HomeActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                //Toast.makeText(this, ""+resultUri, Toast.LENGTH_SHORT).show();
                viewImage.setImageURI(resultUri);
                viewImage.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                imageSelect.setVisibility(View.GONE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
