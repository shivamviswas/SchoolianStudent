package com.wikav.student.studentapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.wikav.student.studentapp.MainActivties.HomeMenuActivity;
import com.wikav.student.studentapp.MainActivties.NewProfile;
import com.wikav.student.studentapp.MainActivties.SubjectActivity_2;

public class BottomNavigationViewHelper {
    public static void enableNavigation(final Context context, BottomNavigationView view){
       view.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {


           //kya hua
           @TargetApi(Build.VERSION_CODES.O)
           @Override
           //bottomCheck
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {


               switch (item.getItemId()){
                   case R.id.home:



                           Intent intent1 = new Intent( context, HomeMenuActivity.class );
                           intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                           context.startActivity( intent1 );


                          // j=k=false;



                       break;
                   case R.id.subject:

                           //item.set;
                           Intent intent2 = new Intent( context, SubjectActivity_2.class );
                           intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                           context.startActivity( intent2 );



                        break;
                   case R.id.profile:

                           //Intent openIntent = new Intent(context, NewProfile.class);
//                           openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                           context.startActivity(openIntent);
                           Intent intent5 = new Intent( context, NewProfile.class );
                      intent5. setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                           context.startActivity( intent5 );



                        break;


               }

               return false;
           }
       } );

    }
}
