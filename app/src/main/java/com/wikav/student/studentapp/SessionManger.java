package com.wikav.student.studentapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wikav.student.studentapp.MainActivties.HomeMenuActivity;
import com.wikav.student.studentapp.MainActivties.Login;
import com.wikav.student.studentapp.MainActivties.NewProfile;
import com.wikav.student.studentapp.model.Anime;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wikav-pc on 6/11/2018.
 */

public class SessionManger {

    public static SharedPreferences sharedPref;
    SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editorForResponse;
    public SharedPreferences.Editor editor, editor2;
    public Context context;
    int Private = 0;

    private static final String PRIF = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String SID = "SID";
    public static final String BIO = "BIO";
    public static final String PHOTO = "PHOTO";
    public static final String SCL_ID = "SCL_ID";
    public static final String CLAS = "CLAS";
    public static final String LASTNAME = "LASTNAME";
    public static final String MOBILE = "MOBILE";
    public static final String SCLNAME = "SCLNAME";
    public static final String Scl_Photo = "sclpic";
    public static final String PASS = "PASS";
    public static final String HOMEPAGE_RECORD = "homepagerecords";

    private static final String PREFERENCE_NAME = "APP_PREFERENCE";
    public static final String STAR = "STAR";
    public static final String STARKey = "STARKEY";

    public static final String HOME_FEED_KEY = "home_feed";
    public static final String COMMENT_KEY = "comments";
    public static final String POST = "post";
    public static final String POSTKEY = "postkey";

    public SessionManger(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PRIF, Private);
        editor = sharedPreferences.edit();
        editor2 = sharedPreferences.edit();

    }

    public static void putString(Context context, String key, String value) {
        sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editorForResponse = sharedPref.edit();
        editorForResponse.putString(key, value);
        editorForResponse.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public void createSession(String name, String email, String bio, String sid, String photo, String scl_pic, String scl_id, String classs, String lstname, String mobile, String sclname, String pass) {
        editor2.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(SID, sid);
        editor.putString(PASS, pass);
        editor.putString(BIO, bio);
        editor.putString(PHOTO, photo);
        editor.putString(Scl_Photo, scl_pic);
        editor.putString(SCL_ID, scl_id);
        editor.putString(CLAS, classs);
        editor.putString(LASTNAME, lstname);
        editor.putString(MOBILE, mobile);
        editor.putString(SCLNAME, sclname);
        editor2.apply();
        editor.apply();
    }

    public void UpdateSession(String name, String email, String bio, String sid, String photo, String scl_id, String classs, String lstname, String mobile, String sclname, String pass) {
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(SID, sid);
        editor.putString(PASS, pass);
        editor.putString(BIO, bio);
        editor.putString(PHOTO, photo);
        editor.putString(SCL_ID, scl_id);
        editor.putString(CLAS, classs);
        editor.putString(LASTNAME, lstname);
        editor.putString(MOBILE, mobile);
        editor.putString(SCLNAME, sclname);
        // editor.putString(STAR,"1");

        editor.apply();
    }

    public boolean isLoging() {
        return sharedPreferences.getBoolean(LOGIN, false);

    }

    public void checkLogin() {
        if (!isLoging()) {

            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
            ((HomeMenuActivity) context).finish();
        }

    }

    public void saveHomePageData(List<Anime> data) {

        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(HOMEPAGE_RECORD, json);
        editor.commit();
    }

    public List<Anime> getHomePageData() {

        List<Anime> arrayItems = null;

        String serializedObject = sharedPreferences.getString(HOMEPAGE_RECORD, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Anime>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(BIO, sharedPreferences.getString(BIO, null));
        user.put(SID, sharedPreferences.getString(SID, null));
        user.put(PHOTO, sharedPreferences.getString(PHOTO, null));
        user.put(SCL_ID, sharedPreferences.getString(SCL_ID, null));
        user.put(CLAS, sharedPreferences.getString(CLAS, null));
        user.put(LASTNAME, sharedPreferences.getString(LASTNAME, null));
        user.put(MOBILE, sharedPreferences.getString(MOBILE, null));
        user.put(SCLNAME, sharedPreferences.getString(SCLNAME, null));
        user.put(Scl_Photo, sharedPreferences.getString(Scl_Photo, null));
        user.put(PASS, sharedPreferences.getString(PASS, null));
        return user;
    }

    public void logOut() {
        editor.clear();
        editor.commit();
        editor2.clear();
        editor2.commit();
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        ((NewProfile) context).finish();
    }
    public void logOut2() {
        editor.clear();
        editor.commit();
        editor2.clear();
        editor2.commit();
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        ((HomeMenuActivity) context).finish();
    }

    public void setClas(String clas) {
        editor.clear();
        editor.putString(CLAS, clas);
        editor.apply();
    }

    public void setProName(String name) {
        editor.clear();
        editor.putString(NAME, name);
        editor.apply();
    }

    public void porfilepic(String name) {

        editor.putString(PHOTO, name);
        editor.apply();
    }


    public void setProEmail(String email) {
        editor.clear();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public void setLastname(String lastname) {
        editor.clear();
        editor.putString(LASTNAME, lastname);
        editor.apply();
    }

    public void setProPhone(String phone) {
        editor.clear();
        editor.putString(MOBILE, phone);
        editor.apply();
    }

    public void setProBio(String bio) {
        editor.putString(BIO, bio);
        editor.apply();
    }

    public String getSchoolid() {
        return SCL_ID;
    }

    public static void putStringSar(Context context, String key2, String value,String key1) {
        sharedPref = context.getSharedPreferences(POST, Context.MODE_PRIVATE);
        editorForResponse = sharedPref.edit();
        editorForResponse.putString(key2, value);
        editorForResponse.putBoolean(key1, false);

        editorForResponse.apply();
        editorForResponse.commit();
    }


    public static String getPosted(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(POST, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static boolean stared(Context context, String key)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(POST, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, true);
    }




}
