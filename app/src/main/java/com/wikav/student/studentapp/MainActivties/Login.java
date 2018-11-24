package com.wikav.student.studentapp.MainActivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.MySingleton;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button button;
    TextView textView;
    EditText editTextemail, editTextpassword;
    private ProgressBar progressBar;
   private final String URL = "https://schoolian.website/android/login.php";
    SessionManger sessionManger;
    TextInputLayout pass_hint,email_hint;
    //private final String URL = "https://192.168.43.188/android/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Config config=new Config(this);

        config.CheckConnection();
        button = findViewById(R.id.btn_login);
        textView = findViewById(R.id.link);
        progressBar = findViewById(R.id.progress);
        pass_hint = findViewById(R.id.pass_hint);
        email_hint = findViewById(R.id.email_hint);
        sessionManger = new SessionManger(this);
        editTextemail = findViewById(R.id.input_email);
        editTextpassword = findViewById(R.id.input_password);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, StudentRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    //this is use for login
    public void onLogin(View view) {

        final String mail = editTextemail.getText().toString().trim();
        final String pass = editTextpassword.getText().toString().trim();


        if (mail.isEmpty()||mail.equals("")&&pass.isEmpty()||pass.equals("")) {
            email_hint.setError("Please Enter Mobile Number and Password");

        } else {

         login(mail, pass);
        }

    }


    private void login(final String mail, final String pass) {
        email_hint.setError(null);
        email_hint.setErrorEnabled(false);
        pass_hint.setError(null);
        pass_hint.setErrorEnabled(false);
        button.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name").trim();
                            String email = object.getString("email").trim();
                            String bio = object.getString("bio").trim();
                            String sid = object.getString("sid").trim();
                            String photo = object.getString("photo").trim();
                            String scl_pic = object.getString("scl_pic").trim();
                            String scl_id = object.getString("school_id").trim();
                            String classs = object.getString("class").trim();
                            String lastname = object.getString("lastname").trim();
                            String sclname = object.getString("scl_name").trim();
                            String phone = object.getString("phone").trim();
                            String pass = object.getString("pass").trim();

                            sessionManger.createSession(name, email, bio, sid, photo,scl_pic, scl_id, classs, lastname, phone,sclname,pass);
                            Intent intent = new Intent(Login.this, HomeMenuActivity.class);
                            //                    intent.putExtra("name",name);
//                            intent.putExtra("email",email);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else if (success.equals("0"))
                    {
                        pass_hint.setError("You have entered wrong password");
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    else if (success.equals("2"))
                    {
                        email_hint.setError("Invalid Mobile number or doesn't exist");
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                   // email_hint.setError("Invalid Mobile no. or Password");
                    //Toast.makeText(Login.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(Login.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("phone", mail);
                param.put("password", pass);
                return param;

            }
        };

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void forgetpassword(View view) {
        Intent intent= new Intent(this,ForgotPasword.class);
        startActivity(intent);
    }
}

