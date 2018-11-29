package com.wikav.student.studentapp.MainActivties;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wikav.student.studentapp.BottomNavigationViewHelper;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.ContactUs_NevActivity;
import com.wikav.student.studentapp.MySingleton;
import com.wikav.student.studentapp.NoInternetActivity;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;
import com.wikav.student.studentapp.adapters.RecyclerViewAdapter;
import com.wikav.student.studentapp.model.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.support.v7.widget.RecyclerView.*;
import static com.wikav.student.studentapp.Spash_Screen.PERMISSIONS_MULTIPLE_REQUEST;

@SuppressLint("NewApi")
public class HomeMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    private Config config;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private RequestQueue requestQueue;
    private int requestCount = 1, s;
    private List<Anime> lstAnime;
    LinearLayout img;
    private String school_id, scl_pic;
    private RecyclerView recyclerView;
    int currentItems, totalItem, scrolledOutItem;
    boolean isScrolling = false,isSwiping;
    ImageView feedpost;
    private SwipeRefreshLayout swipeRefresh;
    Parcelable state;
    public static int index = -1;
    public static int tops= -1;
    private RecyclerViewAdapter adaptorRecycler;
    private LinearLayoutManager layoutManager;
    BottomNavigationView bottomNavigationView;
    SessionManger sessionManger;
    // private final String URL_PRODUCTS = "https://schooli.000webhostapp.com/android/getPostData.php";
    private final String URL_PRODUCTS = "https://schoolian.website/android/getPostData.php";
    final String DATA_URL = "https://schoolian.website/android/getUserFeed2.php?page=";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        setUpBottomNavigationView();


        config = new Config(this);

        config.CheckConnection();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManger = new SessionManger(this);
        sessionManger.checkLogin();
        feedpost = findViewById(R.id.feedpost);
        HashMap<String, String> user = sessionManger.getUserDetail();
        school_id = user.get(sessionManger.SCL_ID);
        scl_pic = user.get(sessionManger.Scl_Photo);


        checkAndroidVersion();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        img = view.findViewById(R.id.scl_info);
        TextView txt = view.findViewById(R.id.scl_head);
        // Toast.makeText(this, scl_pic, Toast.LENGTH_SHORT).show();
        // img.setBackgroundResource(R.drawable.drawer);

        Glide.with(HomeMenuActivity.this).load(scl_pic).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                img.setBackground(resource);
            }
        });

        txt.setText("");

        recyclerView = (RecyclerView) findViewById(R.id.recycleerview);
        // adaptorRecycler=new

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
      //  recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        //Adding an scroll change listener to recyclerview
        requestQueue = Volley.newRequestQueue(this);
        //getData(true);




// Inside `onCreate()` lifecycle method, put the below code :

        if(state != null) {
            layoutManager.onRestoreInstanceState(state);

        }

        swipeRefresh=findViewById(R.id.userSwipe);
swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
       // swipeRefresh.setRefreshing(true);
        isSwiping=true;
        getHomePageRecordData();
    }
});
        // recyclerView.setOnScrollChangeListener(this);


//        final String response2 = SessionManger.getString(HomeMenuActivity.this, SessionManger.HOME_FEED_KEY);
//
//        parseData(response);
        // loadProducts();
        // jsonrequest();
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    // AbsListView.OnScrollListener.
                    //Toast.makeText(HomeMenuActivity.this, "scroling"+newState, Toast.LENGTH_SHORT).show();

                }
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
//                        feedpost.setVisibility(View.VISIBLE);
                        feedpost.setAlpha(1f);
                        feedpost.setVisibility(View.VISIBLE);
                    }
                    else {
                        feedpost.setAlpha(0f);
                        feedpost.setVisibility(View.GONE);
                    }
                }
*/

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Toast.makeText(HomeMenuActivity.this, ""+dy, Toast.LENGTH_SHORT).show();
                currentItems = layoutManager.getChildCount();
                totalItem = layoutManager.getItemCount();
                scrolledOutItem = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrolledOutItem == totalItem)) {


//                        feedpost.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    fecthData();


                }
            }
        });
    }

    private void getHomePageRecordData() {

        List<Anime> localData = sessionManger.getHomePageData();

        if(localData != null && localData.size() > 0) {
            lstAnime = localData;
            adaptorRecycler = new RecyclerViewAdapter(this, lstAnime);
            recyclerView.setAdapter(adaptorRecycler);
            if(isSwiping)
            {
                swipeRefresh.setRefreshing(false);
            }
            //requestCount = Integer.parseInt(lstAnime.get(lstAnime.size()-1).getPageId());
        } else {
            lstAnime = new ArrayList<>();
            requestCount = 1;
            requestQueue.add(getDataFromServer(requestCount, school_id));
        }
    }

    private void fecthData() {

        if(!config.haveNetworkConnection()) {

            Intent in=new Intent(this, NoInternetActivity.class);
            startActivity(in);
            finish();
            return;
        }

        //  getData(true);
        requestQueue.add(getDataFromServer(requestCount, school_id));
        requestCount++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }


///////////////////Navigation Drawer

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_camera2:
                Intent intent = new Intent(HomeMenuActivity.this, MyPost_NevActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_camera:
                Intent intent1 = new Intent(HomeMenuActivity.this, Schoolina_NevActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_gallery:
                Intent intent2 = new Intent(HomeMenuActivity.this, Message_NevActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_manage:
                Intent intent3 = new Intent(HomeMenuActivity.this, ContactUs_NevActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_slideshow:
                Intent intent4 = new Intent(HomeMenuActivity.this, Feedback_NevActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_send:
                Intent intent5 = new Intent(HomeMenuActivity.this, Help_NevActivity.class);
                startActivity(intent5);
                break;
                case R.id.profilePage:
                Intent intent6 = new Intent(HomeMenuActivity.this, NewProfile.class);
                startActivity(intent6);
                    break;
                case R.id.logoutId:
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeMenuActivity.this);
                    alertDialogBuilder.setMessage("Are you sure, you want to Logout");
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    sessionManger.logOut2();
                                    Intent intent = new Intent( HomeMenuActivity.this,Login.class );
                                    startActivity(intent );
                                    finish();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            alertDialogBuilder.setCancelable(true);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();;

                break;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
///////////////////Navigation Drawer finish //////////


    // method for bottom navigation view
    private void setUpBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        BottomNavigationViewHelper.enableNavigation(HomeMenuActivity.this, bottomNavigationView);

    }


    ///////////////////Upolad post
    public void feedPost(View view) {

        Intent intent = new Intent(HomeMenuActivity.this, feedUpload.class);
        startActivity(intent);
    }
    ///////////////////Upolad post finish//////////////

    ///////////////////back button
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    ///////////////////back button finish////////////


    ///////////////////check android version


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            //  Toast.makeText(this, "Starting...", Toast.LENGTH_SHORT).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(HomeMenuActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(HomeMenuActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (HomeMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (HomeMenuActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(HomeMenuActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.MEDIA_CONTENT_CONTROL,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            // Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile) {
                        // write your logic here
                    } else {
                        Snackbar.make(HomeMenuActivity.this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                    new String[]{Manifest.permission
                                                            .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                                    PERMISSIONS_MULTIPLE_REQUEST);
                                        }
                                    }
                                }).show();
                    }
                }
                break;
        }
    }


    /////////////////// finish check android version////////


//    @Override
//    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        if (isLastItemDisplaying(recyclerView)) {
//            //Calling the method getdata again
//            getData(true);
//
//        }
//       // Toast.makeText(this, "valeu" +scrollY, Toast.LENGTH_SHORT).show();
//    }


//
//    void swipeRefresh()
//    {
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                getData(false);
//                repeatRefesh();
//            }
//        }, 30000);
//
//
//    }
//
//    private void repeatRefesh() {
//        swipeRefresh();
//    }
//    private void demofeed(final String id) {
//
//
//    }

    private JsonArrayRequest getDataFromServer(int requestCount, String school_id) {

        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress1);

        //Displaying Progressbar
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL + String.valueOf(requestCount) + "&scl_id=" + school_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

//                        SessionManger.putString(HomeMenuActivity.this, SessionManger.HOME_FEED_KEY, response.toString());
//
//
//                        final String response2 = SessionManger.getString(HomeMenuActivity.this, SessionManger.HOME_FEED_KEY);
//                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        //  Toast.makeText(HomeMenuActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }


    private void parseData(JSONArray array) {

        Set<Anime> set = new HashSet<>();

        for (int i = 0; i < array.length(); i++) {

            //Creating the superhero object
            Anime anime = new Anime();
            JSONObject jsonObject = null;
            try {
                //Getting json
                jsonObject = array.getJSONObject(i);

                //Adding data to the superhero object
                anime.setName(jsonObject.getString("st_name"));
//                        anime.setDescription(jsonObject.getString("description"));
                anime.setStars(jsonObject.getString("stars"));
                anime.setPosts(jsonObject.getString("posts"));
                anime.setPostId(jsonObject.getString("post_id"));
                anime.setProfilePic(jsonObject.getString("profile_pic"));
                anime.setSId(jsonObject.getString("sid"));
                anime.setTime(jsonObject.getString("time"));
                anime.setComment("Answer ("+jsonObject.getString("comment")+")");
                anime.setPageId(jsonObject.getString("page"));
                anime.setImage_url(jsonObject.getString("post_pic"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            set.add(anime);
            //swipeRefresh.setRefreshing(false);
            if(isSwiping)
            {
                swipeRefresh.setRefreshing(false);
            }
        }

        if(lstAnime != null) {
            set.addAll(lstAnime);
        }

        lstAnime = new ArrayList<>(set);
        Collections.sort(lstAnime, new Comparator<Anime>() {
            @Override
            public int compare(Anime o1, Anime o2) {
                return o2.getIdd().compareTo(o1.getIdd());
            }
        });

        if(adaptorRecycler != null) {
            adaptorRecycler.updateData(lstAnime);
            adaptorRecycler.notifyDataSetChanged();
        } else {
            adaptorRecycler = new RecyclerViewAdapter(this, lstAnime);
            recyclerView.setAdapter(adaptorRecycler);


        }

        sessionManger.saveHomePageData(lstAnime);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    protected void onPause() {
        super.onPause();
        //requestQueue.add(getDataFromServer(requestCount,school_id));
        config.CheckConnection();
//        mBundleRecyclerViewState = new Bundle();
//        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        state = layoutManager.onSaveInstanceState();


}

    @Override
    protected void onRestart() {
        super.onRestart();
        config.CheckConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHomePageRecordData();
       // getDataFromServer(1,school_id);
        config.CheckConnection();

//layoutManager.onSaveInstanceState();
//        mBundleRecyclerViewState = new Bundle();
//        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

        if (state != null) {
            layoutManager.onRestoreInstanceState(state);
        }
    }


}
