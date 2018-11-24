package com.wikav.student.studentapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class submitOtp extends AppCompatActivity {
    String mobile,otp;
    EditText enterOtp;
    int i;
    Random rnd;
    ProgressBar progressBar;
    TextInputLayout hint;
    Button submit_otp;
    private final String uplod="https://schoolian.website/android/sendOtp.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_otp);
       // Intent in=getIntent();
        mobile=getIntent().getExtras().getString("mobile");
        otp=getIntent().getExtras().getString("otp");
        //
        // Toast.makeText(this, "work : "+otp+mobile, Toast.LENGTH_SHORT).show();
        enterOtp=findViewById(R.id.enterOtp);
        hint=findViewById(R.id.otp_hint);
        progressBar=findViewById(R.id.progressOtp);
        rnd = new Random();
        i = rnd.nextInt(999999) + 100000;
        //Otpsend=new SendOtp();
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void subOtp(View view) {
        String enter=enterOtp.getText().toString().trim();
        if(!enter.equals("")&&!enter.isEmpty()) {
            if (enter.equals(otp)) {
                Intent intent = new Intent(submitOtp.this, Confirm_password_Activity.class);
                intent.putExtra("mobile", mobile);
                //intent.putExtra("otp", ""+i);
                startActivity(intent);
                finish();
            }
            else
            {
                hint.setError("Invalid OTP");
                //Toast.makeText(this, "not Work", Toast.LENGTH_SHORT).show();
            }
        }

        else
        {
            hint.setError("Please Enter OTP");
            //Toast.makeText(this, "not Work", Toast.LENGTH_SHORT).show();
        }
    }

    public void resendOtp(View view) {
//        Intent in=new Intent(submitOtp.this,ForgotPasword.class);
//        startActivity(in);
        UploadPicture();

    }
    private void UploadPicture() {


        //   progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        submit_otp.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        // Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(submitOtp.this, "OTP sending...", Toast.LENGTH_SHORT).show();
                                otp= String.valueOf(i);
                                SendOtp otps = new SendOtp(mobile,"Your OTP is "+i+".Thank You.");
                                otps.sendOtp();
                            }
                            else {
                                hint.setError("You are not a Schoolian user.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            submit_otp.setVisibility(View.VISIBLE);
                            Toast.makeText(submitOtp.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        submit_otp.setVisibility(View.VISIBLE);
                        Toast.makeText(submitOtp.this, "Try Again!"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("checkOtp", "1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
