package com.wikav.student.studentapp.MainActivties;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;
import com.wikav.student.studentapp.adapters.AdapterForMarks;
import com.wikav.student.studentapp.model.Marks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tab2 extends Fragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView.LayoutManager layoutManager;
    private final String JSON_URL_EXAM = "https://schoolian.website/android/getExamName.php" ;
    private final String JSON_URL = "https://schoolian.website/android/Marks.php" ;


    private List<Marks> lstAnime ;
    private RecyclerView recyclerView ;
    SessionManger sessionManger;
    String sclid,clas,sidi;
    View view;
     Context context;
     Spinner spinner;
    String selectedSubject =null;
    ArrayAdapter<String> dataAdapter;
    List<String> examName;
    ProgressBar progressBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=activity;

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        sessionManger=new SessionManger(context);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String Esclid = user.get(sessionManger.SCL_ID);
        String Ecls = user.get(sessionManger.CLAS);
        String Sid= user.get(sessionManger.SID);
        sclid=Esclid;
        sidi=Sid;
        clas=Ecls;
        Config config=new Config(getActivity());

        config.CheckConnection();
       // showMarks(sclid,clas,sidi);
        getExamName(sclid,clas);
        view=inflater.inflate(R.layout.tab2,container,false);
        spinner=view.findViewById(R.id.spinnerClass);

        examName=new ArrayList<>();
        examName.add("Select Subject");
        examName.add("All Subject");
        spinner.setOnItemSelectedListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewMarks);
        lstAnime = new ArrayList<>();
        progressBar=view.findViewById(R.id.progressMarks);
       return view;

    }

private void getExamName(final String sclid, final String clas)
{

    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_EXAM, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                String success = jsonObject1.getString("success");
                JSONArray jsonArray = jsonObject1.getJSONArray("examName");

                if (success.equals("1")) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String classes = jsonObject.getString("examName");

                        examName.add(classes);


                    }
                    setAdapterMethod(examName);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
            }

        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Map<String, String> param = new HashMap<>();
            param.put("school_id", sclid);
            param.put("class", clas);

            return param;

        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    requestQueue.add(stringRequest);

}



    private void showMarks(final String sclid, final String sidi,final String sub) {

       // Toast.makeText(getActivity(),"method call",Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject1=new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray= jsonObject1.getJSONArray("marks");

                    if(success.equals("1"))
                    {
                        for(int i=0 ; i<jsonArray.length();i++)
                        {
                            //Toast.makeText(getActivity(),"work",Toast.LENGTH_LONG).show();
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            Marks anime = new Marks() ;
                            anime.setStName(jsonObject.getString("name"));
                            anime.setStSubject(jsonObject.getString("subject"));
                            anime.setObtmarks(jsonObject.getString("obmarks"));
                            anime.setExamName(jsonObject.getString("examname"));
                            anime.setTotalmarks(jsonObject.getString("totalmarks"));

                            // lstAnime = new ArrayList<teacher_list>();
                            lstAnime.add(anime);

                        }
                        setuprecyclerview(lstAnime);
                    }
                    else {
                        Toast.makeText(getActivity(), "No Exam Available of this Subject", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error 1: "+e.toString(),Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Error 2: "+error.toString(),Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id",sclid);
                param.put("sid",sidi);
                param.put("sub",sub);
                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setuprecyclerview(List<Marks> userPostsList) {


        AdapterForMarks adaptorRecycler = new  AdapterForMarks(getActivity(),userPostsList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptorRecycler);

    }

    private void setAdapterMethod(List<String> clsases) {

        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clsases);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSubject = parent.getItemAtPosition(position).toString();
        if (!selectedSubject.equals("Select Subject")) {
         //   showMarks(sclid, selectedSubject);
            showMarks(sclid,sidi,selectedSubject);
          //  Toast.makeText(getActivity(), selectedSubject, Toast.LENGTH_SHORT).show();

        }
       // Toast.makeText(getActivity(), selectedSubject, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

