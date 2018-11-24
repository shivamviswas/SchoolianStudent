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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback_NevActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    String[] bankNames={"Select Topic","App Features","App Quality","Schoolian Services","Others"};
    Button button;
    EditText feedbox;
    SessionManger sessionManger;
String postion;
    String sclId,stid,URL="https://schoolian.website/android/feedback.php";
    Spinner spin;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_feedback__nev );

       spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);
        feedbox=findViewById(R.id.edittextmessage);
        progressBar=findViewById(R.id.progressBar2);
        button=findViewById(R.id.feedbackSend);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar( toolbar );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "Feedback" );
        }
        sessionManger=new SessionManger(this);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String Esclid = user.get(sessionManger.SCL_ID);
        String Sid= user.get(sessionManger.SID);
        sclId=Esclid;
        stid=Sid;




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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( this,HomeMenuActivity.class );
        startActivity( intent );
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(bankNames[position].length()!=0){

            Toast.makeText(getApplicationContext(), bankNames[position], Toast.LENGTH_LONG).show();
            position(bankNames[position]);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

    public void feedbackSend(View view) {

        if(postion.equals("Select Topic"))
        {
            feedbox.setError("Please Select Topic");
        }
        else if(feedbox.getText().equals("")||feedbox.getText()==null)
        {
            feedbox.setError("Please type any feedback");
        }
        else
        {   String feedback=feedbox.getText().toString().trim();
            feedbackRespose(sclId,stid,feedback,postion);
        }


    }

    public void feedbackRespose(final String sclId, final String stid, final String feedback, final String topic)
    {
        button.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {


                            Toast.makeText(Feedback_NevActivity.this, "Thank you!!! Your Feedback is submitted...", Toast.LENGTH_LONG).show();
                            Toast.makeText(Feedback_NevActivity.this, "Thanks to Give a feedback to improve our quality and Service", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Feedback_NevActivity.this, HomeMenuActivity.class);
                            startActivity(intent);
                            finish();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Feedback_NevActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Feedback_NevActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("scl_id", sclId);
                param.put("st_id", stid);
                param.put("feedback", feedback);
                param.put("topic", topic);

                return param;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void position(String pos)
    {
        postion=pos;
    }

}
