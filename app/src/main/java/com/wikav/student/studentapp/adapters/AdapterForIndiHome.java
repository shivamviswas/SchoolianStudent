package com.wikav.student.studentapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wikav.student.studentapp.MainActivties.Login;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.individual.CommentActivity;
import com.wikav.student.studentapp.individual.Ind_HomeActivity;
import com.wikav.student.studentapp.model.IndiHomeModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wikav-pc on 11/27/2018.
 */

public class AdapterForIndiHome extends RecyclerView.Adapter<AdapterForIndiHome.MyViewHolder> {

    private Context context;
    private List<IndiHomeModel> myData;
    RequestOptions option;
    String ComId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public AdapterForIndiHome(Context context, List<IndiHomeModel> myData) {
        this.context = context;
        this.myData = myData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.feed_for_indi, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String postId = myData.get(position).BlogPostId;
        String postText = myData.get(position).getPost();
        String image = myData.get(position).getImage();
        String thumb = myData.get(position).getThumb();
        holder.setBlogImage(image,thumb);
        holder.setDescText(postText);
        final String currentUser=firebaseAuth.getCurrentUser().getUid();
        String user_id = myData.get(position).getUserId();
        //User Data will be retrieved here...

        if(firebaseAuth.getCurrentUser()!=null) {
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        String userName = task.getResult().getString("Name");
                        String userImage = task.getResult().getString("Profile_Pic");

                        holder.setUserData(userName, userImage);


                    } else {

                        //Firebase Exception

                    }

                }
            });

                long millisecond = myData.get(position).getTimeStamp().getTime();
                String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
                holder.setTimes(dateString);


            //Get Likes Count
            firebaseFirestore.collection("Posts/" + postId + "/Likes").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        int count = documentSnapshots.size();

                        holder.updateLikesCount(count);

                    } else {

                        holder.updateLikesCount(0);

                    }

                }
            });


            //Get Likes
            firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUser).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
                @SuppressLint("NewApi")
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()) {


                        holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.ic_star_black_24dp));


                    } else {


                        holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.ic_star));


                    }

                }
            });

            firebaseFirestore.collection("Posts/" + postId + "/comments").document(currentUser).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
                @SuppressLint("NewApi")
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()) {


                        holder.blogCommentBtn.setImageDrawable(context.getDrawable(R.drawable.ic_commentred));


                    }

                }
            });

            firebaseFirestore.collection("Posts/" + postId + "/comments").addSnapshotListener((Activity)context, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots,  FirebaseFirestoreException e) {

                    assert documentSnapshots != null;
                    if (!documentSnapshots.isEmpty()) {

                        int count = documentSnapshots.size();

                        holder.updateCoomentCount(count);

                    } else {

                        holder.updateCoomentCount(0);

                    }

                }
            });


            holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUser).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {

                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUser).set(likesMap);

                            } else {

                                firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUser).delete();

                            }

                        }
                    });
                }
            });

            holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context, CommentActivity.class);
                    intent.putExtra("postId",postId);
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            Intent intent =new Intent(context,Login.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserImage;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;
        private TextView blog_comment_count;

        private ImageView blogCommentBtn;


        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);

        }

        public void setDescText(String descText) {

            descView = mView.findViewById(R.id.blog_desc);
            if(descText.equals(""))
            {
                descView.setVisibility(View.GONE);
            }
            else {
                descView.setText(descText);
            }



        }

        public void setBlogImage(String downloadUri, String thumbUri) {
            blogImageView = mView.findViewById(R.id.blog_image);

            if(downloadUri.equals("N/A"))
            {
                blogImageView.setVisibility(View.GONE);
            }
            else {


                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.loading_shape);

                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                        Glide.with(context).load(thumbUri)
                ).into(blogImageView);
            }
        }

        public void setTimes(String date) {

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }

        public void setUserData(String name, String image) {

            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogUserName = mView.findViewById(R.id.blog_user_name);

            blogUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions().placeholder(R.drawable.man);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(blogUserImage);
        }

        public void updateLikesCount(int count){

            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count + " Likes");

        }
        public void updateCoomentCount(int count){

            blog_comment_count = mView.findViewById(R.id.blog_comment_count);
            blog_comment_count.setText(count + " Comments");

        }

    }
}
