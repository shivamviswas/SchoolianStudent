package com.wikav.student.studentapp.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.MainActivties.Login;
import com.wikav.student.studentapp.R;

public class CommunicationTab extends AppCompatActivity {
    //private FragmentActivityForRegistration.SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_tab);

        Config config = new Config(this);

        config.CheckConnection();

        setUpBottomNavigationView();
mAuth=FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.main_toolbar_forComm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schoolian");
        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    //for bottom navigation
    private void setUpBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomnavigationviewcomm);
        BottomNavigationViewHelperForIndi.enableNavigation(CommunicationTab.this, bottomNavigationView);

    }


    //fragment
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    MessagesTab tab1 = new MessagesTab();
                    return tab1;
                case 1:
                    NotificationTab tab2 = new NotificationTab();
                    return tab2;
                case 2:
                    FollowersTab tab3 = new FollowersTab();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(CommunicationTab.this, Ind_HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(CommunicationTab.this, Ind_HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
