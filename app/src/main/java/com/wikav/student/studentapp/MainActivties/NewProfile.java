package com.wikav.student.studentapp.MainActivties;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.wikav.student.studentapp.BottomNavigationViewHelper;
import com.wikav.student.studentapp.BottomSheet;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class NewProfile extends AppCompatActivity {
    ImageView imView;

    TextView name,email,bio,mobile,sclName,className;
    SessionManger sessionManger;
    BottomNavigationView bottomNavigationView;
    Button logut,editProfile;
    Bitmap bitmap;
    String getId;
    RequestBuilder requestBuilder;
    HashMap<String, String> user;
   // private final String uplod="https://192.168.43.188/android/upload.php";
    private final String uplod="https://schoolian.website/android/upload.php";

RequestOptions option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
       // imView = findViewById(R.id.profile);
        Config config=new Config(this);

        config.CheckConnection();
        setUpBottomNavigationView();
        sessionManger=new SessionManger(this);
        sessionManger.checkLogin();
       logut=(Button)findViewById(R.id.logoutPro);
        editProfile=(Button)findViewById(R.id.editPro);
        option = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.man);
        name=findViewById(R.id.name);
       email=findViewById(R.id.emailof);
       mobile=findViewById(R.id.mobileof);
      className=findViewById(R.id.location);
      sclName=findViewById(R.id.designation);
      user=sessionManger.getUserDetail();
       bio=findViewById(R.id.bio);

        imView = findViewById( R.id.profile );

        //imageView.setImageURI(Uri.parse(Photo));
        profileData();


        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewProfile.this);
                alertDialogBuilder.setMessage("Are you sure, You want to logout");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        sessionManger.logOut();
                                        Intent intent = new Intent( NewProfile.this,Login.class );
                                        startActivity(intent );
                                        finish();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

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
//                Intent intent = new Intent( NewProfile.this,EditProfile.class );
//                startActivity(intent);
                BottomSheet bottomSheet=new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(),"example");


            }
        });


    }

    public void chooseFile(View view) {


            //Intent intent = new Intent();
            //intent.setType("image/*");
            //
//            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
               /* Uri filePath = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imView.setImageBitmap(null);
                    imView.setImageBitmap(bitmap);
                    UploadPicture(getId, getStringImage(bitmap));
                    //imView.setImageBitmap(null);
                   // imView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Uri selectedImage = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                bitmap = (BitmapFactory.decodeFile(picturePath));
                imView.setImageBitmap(bitmap);
                UploadPicture(getId, getStringImage(bitmap));


            }
        }

    private void UploadPicture(final String id, final String photo) {

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
                                Toast.makeText(NewProfile.this, "Success!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(NewProfile.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(NewProfile.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
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


    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }
    private void setUpBottomNavigationView(){
        bottomNavigationView = findViewById( R.id.bottomnavigationview );
        BottomNavigationViewHelper.enableNavigation( NewProfile.this,bottomNavigationView );
       // bottomNavigationView.setSelectedItemId(R.id.profile);
       // bottomNavigationView.setItemIconTintList(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NewProfile.this,HomeMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }



    public void profileData() {

    if (user != null) {
        String Ename = user.get(sessionManger.NAME);
        String Elastname = user.get(sessionManger.LASTNAME);
        String Eemail = user.get(sessionManger.EMAIL);
        String Esid = user.get(sessionManger.SID);
        String Ebio = user.get(sessionManger.BIO);
        String mobile = user.get(sessionManger.MOBILE);
        String sclName = user.get(sessionManger.SCLNAME);
        String clasName = user.get(sessionManger.CLAS);
        String photo = user.get(sessionManger.PHOTO);


        //  imView.setImageURI(Uri.parse(to));
//

        Glide.with(NewProfile.this).load(photo).apply(option).into(imView);

//
//        else
//        {
//            imView.setImageResource(R.drawable.man);
//        }
        name.setText(Ename+" "+Elastname);
        this.mobile.setText(mobile);
        className.setText(clasName);
        this.sclName.setText("SID: " + user.get(sessionManger.SID));
        this.bio.setText(Ebio);
        email.setText(Eemail);
//        sid.setText(Esid);
//        bio.setText(Ebio);
        getId = Esid;
    }
}

}

