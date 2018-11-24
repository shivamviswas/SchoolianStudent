package com.wikav.student.studentapp.MainActivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.wikav.student.studentapp.SessionManger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message_NevActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] bankNames={"Select","Principle","Teacher"};
    List<String> teacher;
    final String JSON_URL="https://schoolian.website/android/getTeacherMsg.php";
    final String JSON_TECHER="https://schoolian.website/android/sendToTeacher.php";
    final String JSON_ADMIN="https://schoolian.website/android/sendToTeacher.php";

    String selectedSubject =null;
    ArrayAdapter<String> dataAdapter;
    Button button;
    EditText msg,topic;
    SessionManger sessionManger;
    Spinner spinner;
    String itemcls = null,teacherName;
    String sclid,clas,sidi,stname,photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_message__nev );
        Config config=new Config(this);

        config.CheckConnection();
        sessionManger=new SessionManger(Message_NevActivity.this);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String Esclid = user.get(sessionManger.SCL_ID);
        String Ecls = user.get(sessionManger.CLAS);
        String pht = user.get(sessionManger.PHOTO);
        String Sid= user.get(sessionManger.SID);
        String Snam= user.get(sessionManger.NAME);
        String Lnam= user.get(sessionManger.LASTNAME);

        sclid=Esclid;
        sidi=Sid;
        clas=Ecls;
        stname=Snam+" "+Lnam;
        photo=pht;
        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        spinner = (Spinner) findViewById(R.id.SpinnTeacher);
        msg =  findViewById(R.id.messages);
        topic =  findViewById(R.id.topicSubject);
        spin.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(this);
        teacher=new ArrayList<>();
        getClasses(sclid);
        teacher.add("Select Teacher");

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar( toolbar );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "Messgae" );
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent( this, HomeMenuActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }
    private void getClasses(final String sclid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray = jsonObject1.getJSONArray("teacherName");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String classes = jsonObject.getString("teacherName");

                            teacher.add(classes);


                        }
                        setAdapterMethod(teacher);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Message_NevActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Message_NevActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Message_NevActivity.this);
        requestQueue.add(stringRequest);

    }

    private void setAdapterMethod(List<String> clsases) {

        dataAdapter = new ArrayAdapter<String>(Message_NevActivity.this, android.R.layout.simple_spinner_item, clsases);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent( this,HomeMenuActivity.class );
        startActivity( intent );
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.simpleSpinner) {
            itemcls = parent.getItemAtPosition(position).toString();
            if (itemcls.equals("Teacher")) {
                // shocoments(sclid,item);
                this.spinner.setVisibility(View.VISIBLE);
                //Toast.makeText(Message_NevActivity.this, itemcls, Toast.LENGTH_LONG).show();
            }
            else {
                this.spinner.setVisibility(View.GONE);
            }
        } else if (spinner.getId() == R.id.SpinnTeacher) {
            teacherName = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }


    public void sendMessage(View view) {
      String message=msg.getText().toString().trim();
      String topics=topic.getText().toString().trim();
      if(message.equals("")||message==null){
          msg.setError("Please Type message");
      }
      else {
          if(itemcls.equals("Teacher")){
              sendToTeacher(sclid,sidi,stname,clas,topics,message,teacherName);
          }else {
              sendToAdmin(sclid,sidi,stname,clas,topics,message,photo);
          }
      }
    }

    private void sendToAdmin(final String sclid, final String sidi, final String stname, final String clas, final String topics, final String message,final String photo) {
        Toast.makeText(Message_NevActivity.this, "Sending...", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");


                    if (success.equals("1")) {

                        Toast.makeText(Message_NevActivity.this, "Successfully Send...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Message_NevActivity.this,HomeMenuActivity.class);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Message_NevActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Message_NevActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", sclid);
                param.put("sid", sidi);
                param.put("class",clas);
                param.put("st_name", stname);
                param.put("msg", message);
                param.put("topic", topics);
                param.put("admin", "1");
                param.put("st", "st");
                param.put("photo", photo);
                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Message_NevActivity.this);
        requestQueue.add(stringRequest);}

    private void sendToTeacher(final String sclid, final String sidi, final String stname, final String clas, final String topics, final String message, final String techname) {
        Toast.makeText(Message_NevActivity.this, "Sending...", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");


                    if (success.equals("1")) {

                        Toast.makeText(Message_NevActivity.this, "Successfully Send...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Message_NevActivity.this,HomeMenuActivity.class);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Message_NevActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Message_NevActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", sclid);
                param.put("sid", sidi);
                param.put("class",clas);
                param.put("st_name", stname);
                param.put("msg", message);
                param.put("topic", topics);
                param.put("teacher", techname);
                param.put("teach", "1");
                param.put("st", "st");
                param.put("photo", photo);
                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Message_NevActivity.this);
        requestQueue.add(stringRequest);
    }
}
