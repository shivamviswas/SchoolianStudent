package com.wikav.student.studentapp.MainActivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<String> clsases;
    ArrayAdapter<String> dataAdapter;
    String selectedClassId=null,sclid=null;
    Spinner spinner;
    private final String JSON_URL = "https://schoolian.website/android/getClasses.php";
    private final String Url="https://schoolian.website/android/reg2.php";
    boolean isSetInitialText=true,isPhone=false,isSetInitialText2=true;
    private EditText school_id,name,lastname,email, pass,phone,clas;
    private ProgressBar progressBar;
    private Button submit;
    TextInputLayout scl_idError;
      // private String Url="https://192.168.43.188/android/reg2.php",getClasss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_student_registration );
        Config config=new Config(this);

        config.CheckConnection();
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle("Register");
        }

        school_id=findViewById(R.id.school_id);
        scl_idError=findViewById(R.id.scliderror);
        name = findViewById(R.id.sname);
        lastname=findViewById(R.id.lastname);
        email=findViewById(R.id.semail);
        phone=findViewById(R.id.snumber);
        pass=findViewById(R.id.spassword);
        progressBar=findViewById(R.id.prog);
        submit=findViewById(R.id.submit);
        spinner = findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener( this );
        clsases=new ArrayList<>();
        clsases.add("Select Class");

        school_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             // sclid=school_id.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isSetInitialText){
                    isSetInitialText = false;
                    scl_idError.setErrorEnabled(false);
                } else{
                    // perform your operation
                    sclid=school_id.getText().toString();
                    if(sclid.length()>=8)
                    {
                       // Toast.makeText(StudentRegistrationActivity.this, sclid, Toast.LENGTH_SHORT).show();
                        getClasses(sclid);
                    }

                }

            }
        });

//        phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (isSetInitialText2) {
//                    isSetInitialText2 = false;
//                } else {
//                    String ss = phone.getText().toString();
//             StringBuilder stringBuffer=new StringBuilder(ss);
//                    if (ss.length()>9)
//                    {
//                        if (stringBuffer.charAt(0) > 6) {
//                            isPhone = true;
//                        }
//                        else
//                        {
//                            isSetInitialText2 = true;
//                        }
//                    }
//
//                }
//            }
//        });

    }


    public void submit(View view) {
        final String name=this.name.getText().toString();
        final String school_id=this.school_id.getText().toString();
        final String lastname=this.lastname.getText().toString();
        final String phone=this.phone.getText().toString().trim();
        final String email=this.email.getText().toString();
        final String pass=this.pass.getText().toString();

        if(!name.equals("")&&!school_id.equals("")&&!lastname.equals("")&&!phone.equals("")&& !email.equals("")&&!pass.equals("")&&!selectedClassId.equals("Select Class")&&selectedClassId!=null){
         //   Toast.makeText(this, "True main", Toast.LENGTH_SHORT).show();

            if(emailValidator(email))
            {
                //Toast.makeText(this, "True email", Toast.LENGTH_SHORT).show();
               StringBuilder ch =new StringBuilder(phone);
               // int kk=ch.charAt(0);
                if(ch.charAt(0)=='6'||ch.charAt(0)=='7'||ch.charAt(0)=='8'||ch.charAt(0)=='9')
                {
                    if(phone.length()>9)
                    {
                        submitMethod(name, school_id, lastname, phone, email, pass, selectedClassId);
                    }
                    else
                    {
                        this.phone.setError("Please Enter Valid Mobile No.");
                    }
                  //  submitMethod(name, school_id, lastname, phone, email, pass, selectedClassId);
                   // Toast.makeText(this, "Call Response" + kk, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    this.phone.setError("Please Enter Valid Phone");
                }
            }
            else
            {
                this.email.setError("Please Enter Valid Email");
            }

        }
        else
        {
            this.name.setError("Please Fill All fields");
            this.school_id.setError("Please Fill All fields");
            this.lastname.setError("Please Fill All fields");
            this.phone.setError("Please Fill All fields");
            this.email.setError("Please Fill All fields");
            this.pass.setError("Please Fill All fields");
        }


    }




    private void submitMethod(final String name, final String school_id, final String lastname, final String phone, final String email, final String pass, final String clas) {

        progressBar.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1"))
                    {
                        Toast.makeText(StudentRegistrationActivity.this, "Mobile No. Already Exist",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    }
                    else if (success.equals("2"))
                    {
                        Toast.makeText(StudentRegistrationActivity.this, "Register Successfully",Toast.LENGTH_SHORT).show();
                       progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                        Intent intent =new Intent(StudentRegistrationActivity.this,HomeMenuActivity.class);
                        startActivity(intent);
                    }
                    else if (success.equals("4"))
                    {
                        Toast.makeText(StudentRegistrationActivity.this, "Invalid School Id! Please Enter Valid Id",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    }else if (success.equals("0"))
                    {
                        Toast.makeText(StudentRegistrationActivity.this, "Connection Error Please check the connection",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentRegistrationActivity.this, "Register Error"+e.toString(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentRegistrationActivity.this, "Register Error 2:"+error.toString(),Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> pram=new HashMap<>();
                pram.put("name",name);
                pram.put("email",email);
                pram.put("password",pass);
                pram.put("phone",phone);
                pram.put("school_id",school_id);
                pram.put("class",clas);
                pram.put("lastname",lastname);
                return pram;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }






    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent( this, Login.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( this,Login.class);
        startActivity( intent );
    }


    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {

        selectedClassId = parent.getItemAtPosition(position).toString();

    }




    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

    private void getClasses(final String sclid) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("className");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String classes = jsonObject.getString("className");

                            clsases.add(classes);


                        }
                        setAdapterMethod(clsases);
                    }
                    else
                    {
                       scl_idError.setError("Please Enter Valid School Id");
                       school_id.setText("");
                       isSetInitialText=true;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StudentRegistrationActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentRegistrationActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", sclid);

                return param;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(StudentRegistrationActivity.this);
        requestQueue.add(stringRequest);


    }

    private void setAdapterMethod(List<String> clsases) {

        dataAdapter = new ArrayAdapter<String>(StudentRegistrationActivity.this, android.R.layout.simple_spinner_item, clsases);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

