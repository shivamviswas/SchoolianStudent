package com.wikav.student.studentapp.individual;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.ProgressBar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wikav.student.studentapp.MainActivties.Login;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.adapters.AdapterForIndiHome;
import com.wikav.student.studentapp.model.IndiHomeModel;

import java.util.ArrayList;
import java.util.List;

public class Ind_HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
private Toolbar toolbar;
FloatingActionButton floatingActionButton;
FirebaseFirestore firebaseFirestore;
private RecyclerView recyclerView;
List<IndiHomeModel> blog_list;
private BottomNavigationView bottomNavigationViewHelperForIndi;
private ProgressBar progressBar;
AdapterForIndiHome adapterForIndiHome;
    private DocumentSnapshot lastVisible;
    private  boolean isFirstPageFirstLoad=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_for_inidi);
        toolbar=findViewById(R.id.main_toolbar);
        setUpToolbar();
        floatingActionButton=findViewById(R.id.floatingActionButtonAddPost);
        mAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recycleerviewForIndi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        blog_list=new ArrayList<>();
        adapterForIndiHome=new AdapterForIndiHome(this,blog_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterForIndiHome);
        firebaseFirestore=FirebaseFirestore.getInstance();


        setBottomNavigationViewHelperForIndi();

        if(mAuth.getCurrentUser()!=null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (dy > 0) {
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        //floatingActionButton.setMaxHeight(0);
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }


                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {

                        loadMore();

                    }
                }
            });


            Query first = firebaseFirestore.collection("Posts").orderBy("timeStamp", Query.Direction.DESCENDING).limit(3);
            first.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (!documentSnapshots.isEmpty()) {

                            if (isFirstPageFirstLoad) {
                                lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            }

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                Log.i("docCh", doc.getDocument().toString());
                                String blogPostId = doc.getDocument().getId();
                                IndiHomeModel indiHomeModel = doc.getDocument().toObject(IndiHomeModel.class).withId(blogPostId);
                                if (isFirstPageFirstLoad) {
                                    blog_list.add(indiHomeModel);
                                } else {
                                    blog_list.add(0, indiHomeModel);
                                }
                                adapterForIndiHome.notifyDataSetChanged();
                            }
                        }
                    }
                    isFirstPageFirstLoad = false;
                }
            });


        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent =new Intent(Ind_HomeActivity.this,IndiLogin.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_for_indi_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout_indi)
        {
            logout();
        }
        return true;
    }


    public void setBottomNavigationViewHelperForIndi() {
        bottomNavigationViewHelperForIndi=findViewById(R.id.bottomForIndi);
        BottomNavigationViewHelperForIndi.enableNavigation(this,bottomNavigationViewHelperForIndi);

    }

    private void logout() {
        mAuth.signOut();
        Intent intent =new Intent(Ind_HomeActivity.this,Login.class);
        startActivity(intent);
        finish();
    }


    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schoolian");
        getSupportActionBar().setLogo(R.mipmap.icon);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_indi);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    public void goToFeed(View view) {
        Intent i=new Intent(Ind_HomeActivity.this,feedUploadForIndi.class);
        startActivity(i);
    }

    public void loadMore() {
        if (mAuth.getCurrentUser() != null) {
            Query nextQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(Ind_HomeActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (!documentSnapshots.isEmpty()) {
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                Log.i("docCh", doc.getDocument().toString());
                                String blogPostId = doc.getDocument().getId();
                                IndiHomeModel indiHomeModel = doc.getDocument().toObject(IndiHomeModel.class).withId(blogPostId);
                                blog_list.add(indiHomeModel);
                                adapterForIndiHome.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }
}
