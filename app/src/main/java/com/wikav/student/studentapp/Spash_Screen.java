package com.wikav.student.studentapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.student.studentapp.MainActivties.HomeMenuActivity;
import com.wikav.student.studentapp.MainActivties.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Spash_Screen extends AppCompatActivity {
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;
        SessionManger sessionManger;
       ProgressBar progressBar;
       ImageView retry,logopic,sclfont;
       TextView tapto, pst;
    AlertDialog.Builder builder;
Config    config;
    private final String uplod="https://schoolian.website/android/getAppUpdate.php";
    private final String URL_PRODUCTS = "https://schoolian.website/android/getPostData.php";
    private final String URL = "https://schoolian.website/android/login.php";
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash__screen);
       // retry = findViewById(R.id.retry);
       // tapto = findViewById(R.id.tapto);
        logopic = findViewById(R.id.logo);
        sclfont = findViewById(R.id.sclFont);
        pst = findViewById(R.id.slogo);
     //   tapto = findViewById(R.id.tapto);
     //  ImageView imageView = findViewById(R.id.logo);
        sessionManger = new SessionManger(this);
        progressBar = findViewById(R.id.progressBar);




      //  Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
     //   imageView.startAnimation(animation);
//        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
//        pst.startAnimation(animation1);
     //  logopic.animate().alpha(1f);

        config=new Config(this);

        if(config.haveNetworkConnection())
        {
            logopic.animate().translationYBy(-200f).setDuration(1200);

            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                    sclfont.animate().alpha(1f).setDuration(1500);
                    pst.animate().alpha(1f).setDuration(1500);


                }

            }, 1*1000);
            updateApp();
        }
        else
        {
            Intent intent =new Intent(this,NoInternetActivity.class);
            startActivity(intent);
            finish();
        }


//        int Permission_All = 1;
//
//        String[] Permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.MEDIA_CONTENT_CONTROL,
//                Manifest.permission.INTERNET,
//                Manifest.permission.ACCESS_NETWORK_STATE};
//        if(!hasPermissions(this, Permissions)){
//            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
//        }

        /*

        if (haveNetworkConnection() == true) {

            progressBar.setVisibility(View.VISIBLE);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);


                        if (!sessionManger.isLoging()) {
                            Intent intent = new Intent(Spash_Screen.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Intent intent = new Intent(Spash_Screen.this, HomeMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            thread.start();

        } else {

            retry.setVisibility(View.VISIBLE);
            tapto.setVisibility(View.VISIBLE);
            logopic.setVisibility(View.GONE);
            pst.setVisibility(View.GONE);
        }


///////////////////////////////////////////////////////////////////////////////////////////
    <uses-permission android:name="android.permission.READ_PROFILE" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission
        android:name="android.permission.MEDIA_CONTENT_CONTROL"
        tools:ignore="ProtectedPermissions" />



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }








//    public static boolean hasPermissions(Context context, String... permissions){
//
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
//            for(String permission: permissions){
//                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED){
//                    return  false;
//                }
//            }
//        }
//        return true;
//    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//       finish();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        finish();
//    }




    public void updateApp(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                              JSONArray jsonArray = jsonObject.getJSONArray("update");
                              progressBar.setVisibility(View.GONE);

                            //  Toast.makeText(Spash_Screen.this, "Success!"+response, Toast.LENGTH_SHORT).show();
                              int code=0;
                              String link="",msg="";
                              if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    code = object.getInt("version_code");
                                    link = object.getString("updatelink").trim();
                                    msg = object.getString("msg").trim();
                                }

                                Checkupdate(code,link,msg);



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(Spash_Screen.this, "Try Again!"+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Spash_Screen.this, "Try Again!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("getupdate","1");
                params.put("app","student");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void Checkupdate(int cod, final String link,final String msg) {
        if(versionCode<cod)
    { progressBar.setVisibility(View.GONE);
        builder = new AlertDialog.Builder(Spash_Screen.this);

        builder.setTitle("Update!");


        builder.setMessage(msg);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Spash_Screen.this, "Updating...", Toast.LENGTH_SHORT).show();
                String url = "https://"+link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        builder.setCancelable(true);
       final AlertDialog alertDialog= builder.create();
        builder.show();
        Spash_Screen.this.setFinishOnTouchOutside(true);


    }
    else {

        progressBar.setVisibility(View.VISIBLE);


            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                   // Toast.makeText(Spash_Screen.this, "work", Toast.LENGTH_SHORT).show();
                    if (!sessionManger.isLoging()) {

                       // Toast.makeText(Spash_Screen.this, "work2", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Spash_Screen.this, Login.class);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(Spash_Screen.this, HomeMenuActivity.class);
                        startActivity(intent);
                    }

                    // close this activity

                    finish();

                }

            }, 2*1000);

        }

    }




}

