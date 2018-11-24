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
import android.widget.ProgressBar;
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
import com.wikav.student.studentapp.adapters.AdapterForSyllabus;
import com.wikav.student.studentapp.model.Syllabus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tab3 extends Fragment {
    View view;

    Context context;
    SessionManger sessionManger;
    String sclid,clas;
    private List<Syllabus> lstAnime ;
    private RecyclerView recyclerView ;
    ProgressBar progressBar;
    private final String JSON_URL = "https://schoolian.website/android/sallybus.php" ;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=activity;

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.tab3,container,false);
        Config config=new Config(getActivity());

        config.CheckConnection();
        sessionManger=new SessionManger(context);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String Esclid = user.get(sessionManger.SCL_ID);
        String Ecls = user.get(sessionManger.CLAS);
        String Sid= user.get(sessionManger.SID);
        sclid=Esclid;
        recyclerView=view.findViewById(R.id.recyclerviewSyllbus);
        progressBar=view.findViewById(R.id.progressSyllabus);
        lstAnime=new ArrayList<>();
        //sidi=Sid;
        clas=Ecls;
        showMarks(sclid,clas);
        return view;

}
    private void showMarks(final String sclid, final String clas) {

       // Toast.makeText(getActivity(),"method call",Toast.LENGTH_LONG).show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                    JSONObject jsonObject1=new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray= jsonObject1.getJSONArray("syllabus");

                    if(success.equals("1"))
                    {
                       // mobileArray= new String[jsonArray.length()];
                        for(int i=0 ; i<jsonArray.length();i++)
                        {

                            Syllabus syllabus=new Syllabus();
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                           // mobileArray[i]=jsonObject.get("subject").toString();
                            syllabus.setSubject(jsonObject.getString("subject"));
                            syllabus.setSyllabusComplete(String.valueOf(jsonObject.getInt("per")));
                            syllabus.setChapter(jsonObject.getString("syllabus")+" Chapters done");
                            lstAnime.add(syllabus);
                        }
                        setuprecyclerview(lstAnime);

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
                param.put("clas",clas);
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
    private void setuprecyclerview(List<Syllabus> userPostsList) {


        AdapterForSyllabus adaptorRecycler = new AdapterForSyllabus(getActivity(),userPostsList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptorRecycler);

    }
}
