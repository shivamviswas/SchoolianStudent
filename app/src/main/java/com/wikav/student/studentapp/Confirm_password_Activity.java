package com.wikav.student.studentapp;

import android.content.Intent;
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
import com.wikav.student.studentapp.MainActivties.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Confirm_password_Activity extends AppCompatActivity {
   EditText pass,cpas;
   String mobile;
   TextInputLayout hint;
   ProgressBar progressBar;
   Button submit_pass;
    private final String uplod="http://schoolian.website/android/sendOtp.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_confirm_password_ );

        pass = findViewById( R.id. passFirst);
        cpas = findViewById( R.id.passSecond );
        hint = findViewById( R.id.cpassHint );
        progressBar = findViewById( R.id.proCpass );
        submit_pass = findViewById( R.id.pass_submit );
        mobile=getIntent().getExtras().getString("mobile");

    }


    public void confirmPassword(View view) {
        if(cpas.getText().toString().equals(pass.getText().toString())){
           final String passs=pass.getText().toString();
            sendPassword(mobile,passs);
        }
        else {
          hint.setError("Password and confirm password not match");
        }
    }
    private void sendPassword(final String mobile, final String pass) {


        //   progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        submit_pass.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uplod,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        // Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Confirm_password_Activity.this, "Successfully changed", Toast.LENGTH_SHORT).show();

                                String msg = "Dear User, You have successfully changed your password. Now your Username : " + mobile + " and Password is " + pass + ". Thank You.";
                                SendOtp otp = new SendOtp(mobile, msg);
                                if (otp.sendOtp()) {
                                    Intent intent = new Intent(Confirm_password_Activity.this, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else {
                                hint.setError("Password not change");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            submit_pass.setVisibility(View.VISIBLE);
                            Toast.makeText(Confirm_password_Activity.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        submit_pass.setVisibility(View.VISIBLE);
                        Toast.makeText(Confirm_password_Activity.this, "Try Again!"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("password", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
