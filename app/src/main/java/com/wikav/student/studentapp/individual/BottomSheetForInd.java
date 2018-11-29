package com.wikav.student.studentapp.individual;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wikav.student.studentapp.MainActivties.NewProfile;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.SessionManger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wikav-pc on 8/29/2018.
 */

public class BottomSheetForInd extends BottomSheetDialogFragment {
    Button button;
    SessionManger sessionManger;
    EditText name;
    EditText email;
    EditText phone;
    EditText bio;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    String uid;
    private FirebaseFirestore firebaseFirestore;
    RequestOptions option;
    private String Username;
    private String Useremail;
    private String Usermobile;
    private String Userimge;
    private String UserBio;
    final String Url="https://schoolian.website/android/updateProfile.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet_for_indi,container,false);;
        progressBar=v.findViewById(R.id.progressForBottomSheet);
        name=v.findViewById(R.id.upName);
        email=v.findViewById(R.id.upemail);
        phone=v.findViewById(R.id.upmobile);
        bio=v.findViewById(R.id.upbio);
        button=v.findViewById(R.id.submitUpdate);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    if (task.getResult().exists()) {
                        Username = task.getResult().getString("Name");
                        Useremail = task.getResult().getString("Email");
                        Usermobile = task.getResult().getString("Mobile");
                        Userimge = task.getResult().getString("Profile_Pic");
                        UserBio = task.getResult().getString("Bio");
                        name.setText(Username);
                        email.setText(Useremail);
                        phone.setText(Usermobile);
                        bio.setText(UserBio);
                    }

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String names=name.getText().toString();
                final String emails=email.getText().toString();
                final String bios=bio.getText().toString();

                final String phones=phone.getText().toString();

                if(!names.equals("")&&!emails.equals("")&&!bios.equals("")&&!phones.equals(""))
                {
                    if(emailValidator(emails))
                    {

                        if(phones.charAt(0)=='6'||phones.charAt(0)=='7'||phones.charAt(0)=='8'||phones.charAt(0)=='9')
                        {
                            if(phone.length()>9)
                            {
                                updateClick(names,emails,bios,phones);

                            }
                            else
                            {
                                phone.setError("Please Enter Valid Mobile No.");
                            }
                            //  submitMethod(name, school_id, lastname, phone, email, pass, selectedClassId);
                            // Toast.makeText(this, "Call Response" + kk, Toast.LENGTH_SHORT).show();
                        }
                      else
                      {
                          phone.setError("Please Enter Valid Mobile No.");

                      }
                    }

                }
                else
                {
                    name.setError("please fill all fields");
                    email.setError("please fill all fields");
                    bio.setError("please fill all fields");
                    phone.setError("please fill all fields");
                }

            }
        });




        return v;
    }

    private void updateClick(String names, String emails, String bios, String phones) {
      //  Toast.makeText(getActivity(), "Update Click", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> userMap = new HashMap<>();
        userMap.put("Name", names);
        userMap.put("Email", emails);
        userMap.put("Bio", bios);
        userMap.put("Mobile", phones);
        firebaseFirestore.collection("Users").document(uid).set(userMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successfully Update" , Toast.LENGTH_SHORT).show();
                    BottomSheetForInd.super.dismiss();
                } else {
                    String ex = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Firestore Error" + ex, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
