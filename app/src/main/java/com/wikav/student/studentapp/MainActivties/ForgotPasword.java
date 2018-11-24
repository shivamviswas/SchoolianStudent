package com.wikav.student.studentapp.MainActivties;

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
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SendOtp;
import com.wikav.student.studentapp.submitOtp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgotPasword extends AppCompatActivity {
EditText mobile;
String Mobile=null;
int i;
    Random rnd;
    ProgressBar progressBar;
    TextInputLayout hint;
    Button send_otp;
    private final String uplod="https://schoolian.website/android/sendOtp.php";

   // SendOtp Otpsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pasword);
        Config config=new Config(this);
        config.CheckConnection();
        mobile=findViewById(R.id.mobileForget);

        progressBar=findViewById(R.id.progressForgot);
        hint=findViewById(R.id.forgetInput);
        send_otp=findViewById(R.id.send_otp);
        rnd = new Random();
         i = rnd.nextInt(999999) + 90000;
        //Otpsend=new SendOtp();
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void sendsOtp(View view) {
        Mobile=mobile.getText().toString().trim();
        if(!Mobile.isEmpty()&&!Mobile.equals("")&&Mobile.length()>9)
        {
            String msg="Your OTP is "+i+". Thank You.";
            UploadPicture(Mobile,msg);}

        else
        {
            hint.setError("Please Enter Registered OR Valid Mobile Number");


        }


    }

    private void UploadPicture(final String Mobiles, final String msg) {


        //   progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        send_otp.setVisibility(View.GONE);

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
                                Toast.makeText(ForgotPasword.this, "OTP sending...", Toast.LENGTH_LONG).show();

                                SendOtp otp = new SendOtp(Mobiles,msg);
                                if(otp.sendOtp()) {
                                    Intent intent = new Intent(ForgotPasword.this, submitOtp.class);
                                    intent.putExtra("mobile", Mobile);
                                    intent.putExtra("otp", ""+i);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                            else {
                                hint.setError("You are not a Schoolian user.");
                                Toast.makeText(ForgotPasword.this, "You are not a Schoolian user. Please enter valid Mobile number", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                send_otp.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            send_otp.setVisibility(View.VISIBLE);
                            Toast.makeText(ForgotPasword.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        send_otp.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgotPasword.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", Mobiles);
                params.put("checkotp", "1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
