package com.wikav.student.studentapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.wikav.student.studentapp.MainActivties.HomeMenuActivity;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;
import com.wikav.student.studentapp.adapters.AdapterForComment;
import com.wikav.student.studentapp.model.CommentAnime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AnimeActivity extends AppCompatActivity {

    private String name,posts,postPic,proPic,posId,star,sid,userId,Photo,nameCom;


    private RecyclerView.LayoutManager layoutManager;
    private final String JSON_URL = "https://schoolian.website/android/getComments.php" ;
    private final String JSON_URL2 ="https://schoolian.website/android/comments.php" ;
    private final String Delete_URL ="https://schoolian.website/android/deletePost.php" ;
    private JsonArrayRequest request ;

    private List<CommentAnime> lstAnime ;
    private RecyclerView recyclerView ;
        EditText sendCom;


    //Volley Request Queue


    //The request counter to send ?page=1, ?page=2  requests

    SessionManger sessionManger;
    //SessionManger sessionManger2;
  //  String sclId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // hide the default actionbar
      //  getSupportActionBar().hide();

        // Recieve data
       sendCom=findViewById(R.id.sentI);
         name  = getIntent().getExtras().getString("name");
         posts = getIntent().getExtras().getString("posts");
         postPic = getIntent().getExtras().getString("postPic") ;
         proPic = getIntent().getExtras().getString("proPic");
         posId = getIntent().getExtras().getString("posId") ;
         star=getIntent().getExtras().getString("star") ;
         sid=getIntent().getExtras().getString("sid");

////
       TextView tv_name = findViewById(R.id.anime_name_on);
        TextView postss = findViewById(R.id.post_on);
////        TextView tv_categorie = findViewById(R.id.aa_categorie) ;
////        TextView tv_description = findViewById(R.id.aa_description);
        TextView tv_rating  = findViewById(R.id.starGeti) ;
      PhotoView img = findViewById(R.id.thumbnail_on);
        CircleImageView prof=findViewById(R.id.profile_pic_on);
//
//        // setting values to each view
//
        tv_name.setText(name);
        postss.setText(posts);
//        tv_description.setText(description);
       tv_rating.setText(star);
//        tv_studio.setText(studio);

       RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        // set image using Glide
        if(postPic.equals("")||postPic.equals("NA"))
        {
            img.setMaxHeight(0);
            img.setVisibility(View.GONE);

        }
        else
        {
            img.setVisibility(View.VISIBLE);

            Glide.with(this).load(postPic).into(img);
        }
        Glide.with(this).load(proPic).into(prof);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewComment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        lstAnime = new ArrayList<>();

        sessionManger=new SessionManger(this);
        HashMap<String, String> user=sessionManger.getUserDetail();
        String Esid = user.get(sessionManger.SID);
        String Ename = user.get(sessionManger.NAME);
        String phot = user.get(sessionManger.PHOTO);

        userId=Esid;




        shocoments(posId);





    }



    private void shocoments(final String posId) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray= jsonObject1.getJSONArray("usercomments");

                    if(success.equals("1"))
                    {
                        for(int i=0 ; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            CommentAnime anime = new CommentAnime() ;
                            anime.setNameCom(jsonObject.getString("userName"));
//                        anime.setDescription(jsonObject.getString("description"));

                            anime.setComment(jsonObject.getString("comment"));
                            anime.setIdCom(jsonObject.getString("com_id"));

                            anime.setImage_url(jsonObject.getString("profile_pic"));
                            lstAnime.add(anime);
                        }
                        setuprecyclerview(lstAnime);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(HomeMenuActivity.this,"Error 1: "+e.toString(),Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(HomeMenuActivity.this,"Error 2: "+error.toString(),Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("post_id",posId);

                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setuprecyclerview(List<CommentAnime> userPostsList) {
 AdapterForComment adaptorRecycler = new AdapterForComment(this,lstAnime) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptorRecycler);

    }







  /*  @Override
    public void onBackPressed() {

        Intent intent =new Intent(AnimeActivity.this, HomeMenuActivity.class);
        startActivity(intent);
    }*/

    public void deletEdit(View view) {

        final CharSequence[] options = { "Delete","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AnimeActivity.this);

        builder.setTitle("Your Post!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Delete"))

                {

                    if (userId.equals(sid))
                    {

                        deletePost(posId);
                    }
                    else {
                        Toast.makeText(AnimeActivity.this, "Sorry!!! This is not your post "+sessionManger.SCL_ID+" "+sid, Toast.LENGTH_LONG).show();
                    }

                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    public void sendComment(View view) {

        String anwser=sendCom.getText().toString().trim();
        if(anwser.isEmpty()||anwser.equals(""))
        {
            //// nothing
        }
        else
        {
            setSendCom(anwser,posId,userId);
            respose();
//            lstAnime.clear();
//            shocoments(posId);
        }





    }
public void respose()
{

        CommentAnime anime = new CommentAnime() ;
        anime.setNameCom(nameCom);
//                        anime.setDescription(jsonObject.getString("description"));

        anime.setComment(sendCom.getText().toString());

    anime.setIdCom(" ");
        anime.setImage_url(Photo);
   // Toast.makeText(this, Photo, Toast.LENGTH_LONG).show();

        lstAnime.add(anime);
    setuprecyclerview(lstAnime);
}


    private void setSendCom(final String anwser, final String posId,final String userId) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, JSON_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    if(success.equals("1")) {
                        sendCom.setText("");
                        Toast.makeText(AnimeActivity.this, "Sending...", Toast.LENGTH_LONG).show();
                        lstAnime.clear();
                        shocoments(posId);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   Toast.makeText(AnimeActivity.this,"Error 1: "+e.toString(),Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AnimeActivity.this,"Error 2: "+error.toString(),Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("post_id",posId);
                param.put("student_id",userId);
                param.put("comment",anwser);

                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void deletePost( final String posId)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Delete_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1"))
                    {
                        Intent in=new Intent(AnimeActivity.this,HomeMenuActivity.class);
                        startActivity(in);
                        Toast.makeText(AnimeActivity.this, "Your post has Deleted !!!", Toast.LENGTH_SHORT).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(HomeMenuActivity.this,"Error 1: "+e.toString(),Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(HomeMenuActivity.this,"Error 2: "+error.toString(),Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("post_id",posId);

                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
