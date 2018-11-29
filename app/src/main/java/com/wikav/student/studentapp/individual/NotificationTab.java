package com.wikav.student.studentapp.individual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wikav.student.studentapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationTab extends Fragment {


    public NotificationTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_marks__fragment2, container, false );
    }

}
