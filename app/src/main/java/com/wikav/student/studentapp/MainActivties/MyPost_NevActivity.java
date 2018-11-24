package com.wikav.student.studentapp.MainActivties;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.wikav.student.studentapp.adapters.RecyclerViewAdapterUser;
import com.wikav.student.studentapp.model.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPost_NevActivity extends AppCompatActivity {
   // GridView androidGridView;
   private List<Anime> lstAnime;

    private String school_id,sid;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefresh;

    private RecyclerViewAdapterUser adaptorRecycler;

    BottomNavigationView bottomNavigationView;
    SessionManger sessionManger;
    private final String URL_PRODUCTS = "https://schoolian.website/android/getUserPost.php";
    //private final String URL_PRODUCTS = "httpss://192.168.43.188/android/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_post);
        Config config=new Config(this);

        config.CheckConnection();
        swipeRefresh = findViewById(R.id.userPostSwipe);
sessionManger=new SessionManger(this);

        HashMap<String, String> user = sessionManger.getUserDetail();
        String Ename = user.get(sessionManger.SCL_ID);
        String id = user.get(sessionManger.SID);
        school_id = Ename;
        sid=id;

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //if internet is available
                lstAnime.clear();
                getUserPost(school_id,sid);
                //else
                //swipe_refresh.setRefreshing(false);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycleerviewUser);
        lstAnime = new ArrayList<>();











        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar( toolbar );


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "My Posts" );
        }

        getUserPost(school_id,sid);
    }

    private void parseHomeFeed(String response) throws JSONException {

        // stopProgressBar();
       // Toast.makeText(this, "call hogya", Toast.LENGTH_SHORT).show();
        JSONObject jsonObject1 = new JSONObject(response);
        String success = jsonObject1.getString("success");
        JSONArray jsonArray = jsonObject1.getJSONArray("userPost");

        if (success.equals("1")) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Anime anime = new Anime();
                anime.setName(jsonObject.getString("st_name"));
//                        anime.setDescription(jsonObject.getString("description"));
                anime.setStars(jsonObject.getString("stars"));
                anime.setPosts(jsonObject.getString("posts"));
                anime.setPostId(jsonObject.getString("post_id"));
                anime.setProfilePic(jsonObject.getString("profile_pic"));
                anime.setImage_url(jsonObject.getString("post_pic"));
                anime.setTime(jsonObject.getString("time")); lstAnime.add(anime);
            }
            setuprecyclerview(lstAnime);
    }

    }


    private void getUserPost(final String scid,final String id) {



        //showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //stopProgressBar();

                    // ok karete he
                    // usse jo Recycleviwe create hota he jo thoda sa ajib lg rha he user ko pehle he viwe create mile bs usme value cange hoti rhe

                   //SessionManger.putString(MyPost_NevActivity.this, SessionManger.CONTACT, response);
                    parseHomeFeed(response);

                    //save home response to shared preference





                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(HomeMenuActivity.this, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(HomeMenuActivity.this, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("school_id", scid);
                param.put("sid", id);

                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setuprecyclerview(List<Anime> userPostsList) {

        adaptorRecycler = new RecyclerViewAdapterUser(this, userPostsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptorRecycler);

//        if(adaptorRecycler == null) {
//            adaptorRecycler = new RecyclerViewAdapterUser(this, userPostsList);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setAdapter(adaptorRecycler);
//        } else {
//            adaptorRecycler.changeDataSet(userPostsList);
//            adaptorRecycler.notifyDataSetChanged();
//        }

        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( this,HomeMenuActivity.class );
        startActivity( intent );
    }
}


