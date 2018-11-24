package com.wikav.student.studentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;
import com.wikav.student.studentapp.activities.AnimeActivity;
import com.wikav.student.studentapp.model.Anime;
import com.wikav.student.studentapp.model.MyStar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Anime> mData;
    //  public ImageLoader imageLoader;
    RequestOptions option ,option2;
    private final String URL_PRODUCTS = "https://schoolian.website/android/getStar.php";
    //String URL_PRODUCTS="https://schooli.000webhostapp.com/android/getStar.php" https://schoolian.website/android/getPostData.php;

    String postId, Sid, valueOfstar;
    private List<Anime> lstAnime;
    MyViewHolder viewHolder;
    SessionManger sessionManger;
    SharedPreferences sharedPref;

    boolean isSetStar ;

    public RecyclerViewAdapter(Context mContext, List<Anime> mData) {
        this.mContext = mContext;
        this.mData = mData;


        // Request option for Glide
        option = new RequestOptions().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loading_shape);
        option2 = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.man).error(R.drawable.man);
    }

    public void updateData(List<Anime> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        sessionManger = new SessionManger(mContext);
        HashMap<String, String> user = sessionManger.getUserDetail();
        String Ename = user.get(sessionManger.SID);
        Sid = Ename;
        View view;


        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.test, parent, false);
        viewHolder = new MyViewHolder(view);

//        if(getStatus==1)
//        {
//            viewHolder.star.setImageResource(android.R.drawable.btn_star_big_on);
//        }
//        else
//        {
//            viewHolder.star.setImageResource(android.R.drawable.btn_star_big_off);
//        }

        return viewHolder;


    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Anime anime = mData.get(position);
        if (!anime.getImage_url().equals("NA")) {
//            imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
//            imageLoader.get(anime.getImage_url(),ImageLoader.getImageListener(holder.img_thumbnail,0,0));
//            holder.img_thumbnail.setImageUrl(anime.getImage_url(),imageLoader);
            Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);
        } else {
            holder.img_thumbnail.setVisibility(View.GONE);
        }

        holder.tv_name.setText(mData.get(position).getName());
        holder.ans.setText(mData.get(position).getComment());
        holder.tv_rating.setText(String.valueOf(mData.get(position).getStar()));
        //  holder.tv_studio.setText(mData.get(position).getProfilePic());
        holder.posts.setText(mData.get(position).getPosts());
        holder.tv_time.setText(mData.get(position).getTime());


        // Load Image from the internet and set it into Imageview using Glide

        valueOfstar = String.valueOf(mData.get(position).getStar());

        holder.star.setImageResource(android.R.drawable.btn_star_big_on);


        Glide.with(mContext).load(mData.get(position).getProfilePic()).apply(option2).into(holder.tv_pro1);


        holder.getStar.setOnClickListener(new View.OnClickListener() {
            boolean isclicked=true;
            int i;
            @Override
            public void onClick(View v) {
                if(isclicked)
                {
                    Toast.makeText(mContext, "liked", Toast.LENGTH_SHORT).show();
                    i= anime.getStar();
                    anime.setStars(String.valueOf(i+1));
                    holder.tv_rating.setText(String.valueOf(mData.get(position).getStar()));
                    setStars(mData.get(position).getIdd(),""+mData.get(position).getStar());
                    isclicked=false;

                }
                else
                {
                    i= anime.getStar();
                    if(i>0) {
                        Toast.makeText(mContext, "unliked", Toast.LENGTH_SHORT).show();
                         anime.setStars(String.valueOf(i - 1));
                         holder.tv_rating.setText(String.valueOf(mData.get(position).getStar()));
                        setStars(mData.get(position).getIdd(),""+mData.get(position).getStar());
                        isclicked = true;
                    }
                }

            }
        });



        holder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(mContext, AnimeActivity.class);
                i.putExtra("name", mData.get(position).getName());
                // i.putExtra("anime_description",mData.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("proPic", mData.get(position).getProfilePic());
                i.putExtra("posts", mData.get(position).getPosts());
                i.putExtra("posId", mData.get(position).getIdd());
                i.putExtra("star", mData.get(position).getStar());
                i.putExtra("sid", mData.get(position).getSId());
                i.putExtra("postPic", mData.get(position).getImage_url());

                mContext.startActivity(i);

            }
        });

        holder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(mContext, AnimeActivity.class);
                i.putExtra("name", mData.get(position).getName());
                // i.putExtra("anime_description",mData.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("proPic", mData.get(position).getProfilePic());
                i.putExtra("posts", mData.get(position).getPosts());
                i.putExtra("posId", mData.get(position).getIdd());
                i.putExtra("star", mData.get(position).getStar());
                i.putExtra("sid", mData.get(position).getSId());
                i.putExtra("postPic", mData.get(position).getImage_url());
                mContext.startActivity(i);

            }
        });

    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_rating;
        TextView tv_time;
        TextView posts, ans;
        ImageView img_thumbnail;
        ImageView star, staron;
        CircleImageView tv_pro1;
        LinearLayout view_container, answer;
        LinearLayout getStar;


        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.containeritem);
            answer = itemView.findViewById(R.id.answers);
            getStar = itemView.findViewById(R.id.setStar5);
            tv_name = itemView.findViewById(R.id.anime_name);
            ans = itemView.findViewById(R.id.answersText);
            tv_time = itemView.findViewById(R.id.timestamp);
            posts = itemView.findViewById(R.id.postss);
            tv_rating = itemView.findViewById(R.id.starGetss);
            tv_pro1 = itemView.findViewById(R.id.profile_pic);
            star = itemView.findViewById(R.id.starIMG2);
            // staron=itemView.findViewById(R.id.starIMG25);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

    private void setStars(final String id, final String star) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    //  JSONArray jsonArray= jsonObject1.getJSONArray("allStars");

                    if (success.equals("1")) {
////                        for (int i = 0; i < jsonArray.length(); i++) {
////                            JSONObject jsonObject = jsonArray.getJSONObject(i);
////                            Anime anime = new Anime();
////                            anime.setStars(jsonObject.getString("stars"));
////                            getStatus = Integer.parseInt(jsonObject.getString("status"));
                      //  Toast.makeText(mContext, ": " + star, Toast.LENGTH_LONG).show();
////
////                        }
                    }


//                    if(success.equals("1")) {
//
//                        Anime anime = new Anime() ;
//                        anime.setStars(jsonObject1.getString("starsss"));
//                        String s=jsonObject1.getString("status");
//                        Toast.makeText(mContext,": "+s,Toast.LENGTH_LONG).show();
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("post_id", id);
                param.put("stars", star);

                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


}
