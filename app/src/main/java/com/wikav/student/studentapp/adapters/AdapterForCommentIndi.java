package com.wikav.student.studentapp.adapters;

/**
 * Created by wikav-pc on 7/4/2018.
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.model.CommentAnime;
import com.wikav.student.studentapp.model.CommentModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aws on 11/03/2018.
 */

public class AdapterForCommentIndi extends RecyclerView.Adapter<AdapterForCommentIndi.MyViewHolder> {

    private Context mContext ;
    private List<CommentModel> myData ;
    RequestOptions option;
    String ComId;
    FirebaseFirestore firebaseFirestore;


    public AdapterForCommentIndi(Context mContext, List<CommentModel> mData) {
        this.mContext = mContext;
        this.myData = mData;


        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.man);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view ;
firebaseFirestore=FirebaseFirestore.getInstance();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.comment_feed,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;


        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.comments.setText(myData.get(position).getComment());
        holder.setIsRecyclable(false);
            try {
                long millisecond = myData.get(position).getTimeStamp().getTime();
                String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
                holder.time.setText(dateString);
            }catch (Exception e) {

               /// Toast.makeText(mContext, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
               String c= new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
                holder.time.setText(c);
            }


            String Userid=myData.get(position).getUser_id();

        firebaseFirestore.collection("Users").document(Userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    String userName = task.getResult().getString("Name");
                    String userImage = task.getResult().getString("Profile_Pic");

                   holder.Username.setText(userName);
                    Glide.with(mContext).load(userImage).apply(option).thumbnail(.3f).into(holder.profileImage);


                } else {

                    //Firebase Exception

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

     TextView Username,time,comments;
     CircleImageView profileImage;

        public MyViewHolder(View itemView) {
            super(itemView);

          Username=itemView.findViewById(R.id.UsernameComnet);
          time=itemView.findViewById(R.id.timestampForCommnet);
          comments=itemView.findViewById(R.id.Maincommnets);
          profileImage=itemView.findViewById(R.id.porfilpic_commnent);


        }


    }


}

