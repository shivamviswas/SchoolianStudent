package com.wikav.student.studentapp.MainActivties;

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

import com.wikav.student.studentapp.BottomNavigationViewHelper;
import com.wikav.student.studentapp.Config;
import com.wikav.student.studentapp.FragmentActivityForRegistration;
import com.wikav.student.studentapp.Marks_Fragment2;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.Subject_Fragment1;
import com.wikav.student.studentapp.SyllabusFragment_3;

public class SubjectActivity_2 extends AppCompatActivity {
    private FragmentActivityForRegistration.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_subject_2 );

        Config config=new Config(this);

        config.CheckConnection();

        setUpBottomNavigationView();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new FragmentActivityForRegistration.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    //for bottom navigation
    private void setUpBottomNavigationView(){
         bottomNavigationView = findViewById( R.id.bottomnavigationview );
        BottomNavigationViewHelper.enableNavigation( SubjectActivity_2.this,bottomNavigationView );

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
            switch(position){
                case 0:
                    Subject_Fragment1 tab1 = new Subject_Fragment1();
                    return tab1;
                case 1:
                    Marks_Fragment2 tab2 = new Marks_Fragment2();
                    return tab2;
                case 2:
                    SyllabusFragment_3 tab3 = new SyllabusFragment_3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }public void onBackPressed() {
        Intent intent = new Intent( SubjectActivity_2.this,HomeMenuActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity( intent );
    }
}
