package com.wikav.student.studentapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.request.RequestOptions;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;
import com.wikav.student.studentapp.activities.AnimeActivity;
import com.wikav.student.studentapp.model.Anime;
import com.wikav.student.studentapp.model.Animes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerViewAdapterUser extends RecyclerView.Adapter<RecyclerViewAdapterUser.MyViewHolder> {

    private Context mContext ;
    private List<Anime> mData ;
    RequestOptions option;
    SessionManger sessionManger;
    private final String URL_PRODUCTS = "https://schoolian.website/android/getStar.php";
    //String URL_PRODUCTS="https://schooli.000webhostapp.com/android/getStar.php";
    int status=1,star=1,getStatus;
    String postId,valueOfstar;
    private List<Animes> lstAnime ;


    public RecyclerViewAdapterUser(Context mContext, List<Anime> mData) {
        this.mContext = mContext;
        this.mData = mData;


        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    public void changeDataSet(List<Anime> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view ;



        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.anime_row_item,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;

        if(getStatus==1)
        {
            viewHolder.star.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else
        {
            viewHolder.star.setImageResource(android.R.drawable.btn_star_big_off);
        }

        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(mContext, AnimeActivity.class);
                i.putExtra("name",mData.get(viewHolder.getAdapterPosition()).getName());
               // i.putExtra("anime_description",mData.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("proPic",mData.get(viewHolder.getAdapterPosition()).getProfilePic());
                i.putExtra("posts",mData.get(viewHolder.getAdapterPosition()).getPosts());
               i.putExtra("posId",mData.get(viewHolder.getAdapterPosition()).getIdd());
                i.putExtra("star",mData.get(viewHolder.getAdapterPosition()).getStar());
                i.putExtra("postPic",mData.get(viewHolder.getAdapterPosition()).getImage_url());

                mContext.startActivity(i);

            }
        });


        //////////////////////////////////////dilog//////////////////////////////////




//        viewHolder.view_container.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                final CharSequence[] options = { "Delete", "Edit","Cancel" };
//
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//                builder.setTitle("Your Post!");
//
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//
//                    @Override
//
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (options[item].equals("Delete"))
//
//                        {
//
//                        }
//
//                        else if (options[item].equals("Edit"))
//
//                        {
//
//                        }
//
//                        else if (options[item].equals("Cancel")) {
//
//                            dialog.dismiss();
//
//                        }
//
//                    }
//
//                });
//
//                builder.show();
//
//
//
//
//                return false;
//            }
//        });



        /////////////////////////////////////////////////////////// star get///////////////////////////////////////////////////




//        viewHolder.getStar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                setStars(postId,star,status);
//
//
//                viewHolder.star.setImageResource(android.R.drawable.btn_star_big_on);
//
//
//
//            }
//        });


        return viewHolder;



    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
       holder.tv_rating.setText("");
       holder.time.setText(mData.get(position).getTime());
      //  holder.tv_studio.setText(mData.get(position).getProfilePic());
      holder.tv_category.setText(mData.get(position).getPosts());

        // Load Image from the internet and set it into Imageview using Glide
        postId=String.valueOf(mData.get(position).getIdd());
        valueOfstar=String.valueOf(mData.get(position).getStar());



    Glide.with(mContext).load(mData.get(position).getImage_url()).into(holder.img_thumbnail);
        Glide.with(mContext).load(mData.get(position).getProfilePic()).into(holder.tv_pro1);




    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name ;
        TextView tv_rating ;
        TextView time ;
        TextView tv_category;
        ImageView img_thumbnail,star;
        CircleImageView tv_pro1;

        LinearLayout view_container;





        public MyViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            //getStar=itemView.findViewById(R.id.setStar);
            tv_name = itemView.findViewById(R.id.anime_name);
           tv_category = itemView.findViewById(R.id.postss);
            tv_rating = itemView.findViewById(R.id.starGets);
            tv_pro1 = itemView.findViewById(R.id.profile_pic);
            star=itemView.findViewById(R.id.starIMG);
            time=itemView.findViewById(R.id.timestamp);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

    private void setStars(final String id,final int star,final int status) {


        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String success = jsonObject1.getString("success");
                    JSONArray jsonArray= jsonObject1.getJSONArray("allStars");

                    if(success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Anime anime = new Anime();
                            anime.setStars(jsonObject.getString("stars"));
                            getStatus = Integer.parseInt(jsonObject.getString("status"));
                            Toast.makeText(mContext,": "+getStatus,Toast.LENGTH_LONG).show();

                        }
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
                   Toast.makeText(mContext,"Error 1: "+e.toString(),Toast.LENGTH_LONG).show();
//                    button.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      Toast.makeText(mContext,"Error 2: "+error.toString(),Toast.LENGTH_LONG).show();
//                        button.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
             param.put("post_id",id);
                param.put("stars", String.valueOf(star));
                param.put("status", String.valueOf(status));

                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


}
