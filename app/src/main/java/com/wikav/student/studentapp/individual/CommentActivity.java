package com.wikav.student.studentapp.individual;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.adapters.AdapterForCommentIndi;
import com.wikav.student.studentapp.model.CommentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

        RecyclerView recyclerView;
        String getPostIdIntent;
        Toolbar toolbar;
        EditText sendCommentField;
        FirebaseAuth mAuth;
        FirebaseFirestore firebaseFirestore;
        String currentUser;
        List<CommentModel> list;
        AdapterForCommentIndi adapterForCommentIndi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        toolbar=findViewById(R.id.main_toolbar_Comemnts);
        setUpToolbar();
        sendCommentField =findViewById(R.id.sentConmmentIndi);
        getPostIdIntent=getIntent().getStringExtra("postId");
        recyclerView=findViewById(R.id.recycleerviewForCommennt);
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        list=new ArrayList<>();
        adapterForCommentIndi=new AdapterForCommentIndi(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterForCommentIndi);
        Query query=firebaseFirestore.collection("Posts/"+getPostIdIntent+"/comments").orderBy("timeStamp",Query.Direction.ASCENDING);
        query.addSnapshotListener(CommentActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!documentSnapshots.getDocumentChanges().isEmpty()){
                for (DocumentChange doc:documentSnapshots.getDocumentChanges())
                {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Log.i("docCh", doc.getDocument().toString());
                        CommentModel commentModel=doc.getDocument().toObject(CommentModel.class);
                        list.add(commentModel);
                        sendCommentField.setText("");
                        adapterForCommentIndi.notifyDataSetChanged();
                    }
                }
            }
            }
        });






    }

    public void sendComment(View view) {

        String sendComment = sendCommentField.getText().toString();
        if(!sendComment.isEmpty())
        {
            Map <String,Object> commentMap=new HashMap<>();
            commentMap.put("comment",sendComment);
            commentMap.put("user_id",currentUser);
            commentMap.put("timeStamp", FieldValue.serverTimestamp());
            firebaseFirestore.collection("Posts/"+getPostIdIntent+"/comments").add(commentMap).addOnCompleteListener(CommentActivity.this,new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                   if(task.isSuccessful())
                   {
                       Log.i("ssst","wrok");
                       Toast.makeText(CommentActivity.this, "Sending...", Toast.LENGTH_LONG).show();
                       sendCommentField.setText("");
                       sendCommentField.setFocusable(false);

                   }
                   else {
                       String ex=task.getException().getMessage();
                       Toast.makeText(CommentActivity.this, "FireStoreError "+ex, Toast.LENGTH_SHORT).show();
                       sendCommentField.setText("");
                   }
                }
            });

        }

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setLogo(R.mipmap.icon);

    }
}
