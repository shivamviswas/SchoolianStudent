package com.wikav.student.studentapp;

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
import com.wikav.student.studentapp.MainActivties.NewProfile;

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

public class BottomSheet extends BottomSheetDialogFragment {
    Button button;
    SessionManger sessionManger;
    EditText name,lastname,email,phone,bio,passowrd;
    ProgressBar progressBar;
    NewProfile profile;

    final String Url="https://schoolian.website/android/updateProfile.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet,container,false);
        sessionManger=new SessionManger(getActivity());
        HashMap<String, String> user=sessionManger.getUserDetail();
        final String Ename = user.get(sessionManger.NAME);
        String last = user.get(sessionManger.LASTNAME);
        String Eemail = user.get(sessionManger.EMAIL);
        final String Esid = user.get(sessionManger.SID);
        String Ebio = user.get(sessionManger.BIO);
        String mobile = user.get(sessionManger.MOBILE);
        String sclName = user.get(sessionManger.SCLNAME);
        String clasName = user.get(sessionManger.CLAS);
        String pass=user.get(sessionManger.PASS);
        final String sclId=user.get(sessionManger.SCL_ID);

        name=v.findViewById(R.id.upName);
        lastname=v.findViewById(R.id.upLastName);
        email=v.findViewById(R.id.upemail);
        phone=v.findViewById(R.id.upmobile);
        bio=v.findViewById(R.id.upbio);
        passowrd=v.findViewById(R.id.upPass);
profile=new NewProfile();
        name.setText(Ename);
        lastname.setText(last);
        email.setText(Eemail);
        phone.setText(mobile);
        bio.setText(Ebio);
        passowrd.setText(pass);
        button=v.findViewById(R.id.submitUpdate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String names=name.getText().toString();
                final String lastnames=lastname.getText().toString();
                final String emails=email.getText().toString();
                final String bios=bio.getText().toString();
                final String pass= passowrd.getText().toString();
                final String phones=phone.getText().toString();

                if(!names.equals("")&&!lastnames.equals("")&&!emails.equals("")&&!bios.equals("")&&!pass.equals("")&&!phones.equals(""))
                {
                    if(emailValidator(emails))
                    {

                        if(phones.charAt(0)=='6'||phones.charAt(0)=='7'||phones.charAt(0)=='8'||phones.charAt(0)=='9')
                        {
                            if(phone.length()>9)
                            {
                                updateClick(names,lastnames,emails,bios,pass,phones,sclId,Esid);

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
                    lastname.setError("please fill all fields");
                    email.setError("please fill all fields");
                    bio.setError("please fill all fields");
                    passowrd.setError("please fill all fields");
                    phone.setError("please fill all fields");
                }

            }
        });


progressBar=v.findViewById(R.id.upprog);

        return v;
    }

    private void updateClick(final String name, final String last, final String email, final String bio, final String pass, final String phone, final String sclId, final String esid) {
       //progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("update");


                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String bio = object.getString("bio").trim();
                                    String sid = object.getString("sid").trim();
                                    String photo = object.getString("photo").trim();
                                    String scl_id = object.getString("school_id").trim();
                                    String classs = object.getString("class").trim();
                                    String lastname = object.getString("lastname").trim();
                                    String sclname = object.getString("scl_name").trim();
                                    String phone = object.getString("phone").trim();
                                    String pass = object.getString("pass").trim();
//                                    sessionManger.editor.clear();
//                                    sessionManger.editor.commit();
                                    sessionManger.UpdateSession(name, email, bio, sid, photo, scl_id, classs, lastname, phone,sclname,pass);
                                    profile.profileData();
                                }


                                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                              BottomSheet.super.dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                           // progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Try Again!"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("scl_id", sclId);
                params.put("sid", esid);
                params.put("phone", phone);
                params.put("email", email);
                params.put("pass", pass);
                params.put("bio", bio);
                params.put("name", name);
                params.put("last", last);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

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
