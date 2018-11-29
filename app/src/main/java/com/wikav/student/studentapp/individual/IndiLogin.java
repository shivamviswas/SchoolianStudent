package com.wikav.student.studentapp.individual;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wikav.student.studentapp.R;
import com.wikav.student.studentapp.Spash_Screen;

import java.util.Objects;

public class IndiLogin extends AppCompatActivity {
private LinearLayout loginPage,signUpPage;
private EditText email,pass,emailReg,passReg,mobileReg,nameReg;
public TextInputLayout hintEmail,hintPass;
private Button login,register;
private ProgressBar loginProgress;
private ProgressBar regProgressBar;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indi_login);
        loginPage=findViewById(R.id.loginPage);
        signUpPage=findViewById(R.id.signUpPage);
        email=findViewById(R.id.input_email);
        pass=findViewById(R.id.input_password);
        loginProgress=findViewById(R.id.Loginprogress);
        regProgressBar=findViewById(R.id.progress_register);
        mAuth=FirebaseAuth.getInstance();
        hintEmail=findViewById(R.id.email_hint);
        hintPass=findViewById(R.id.pass_hint);
        emailReg=findViewById(R.id.input_email_register);
        mobileReg=findViewById(R.id.mobile_input_register);
        passReg=findViewById(R.id.input_password_register);
        nameReg=findViewById(R.id.input_name_register);

    }

    public void onLogin(View view) {
    String emailText=email.getText().toString();
    String passText=pass.getText().toString();
    if(!TextUtils.isEmpty(emailText)&&!TextUtils.isEmpty(passText))
    {
        loginProgress.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailText,passText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful())
              {
                  Intent intent =new Intent(IndiLogin.this,Ind_HomeActivity.class);
                  startActivity(intent);
                  finish();
              }
              else
              {
                  String ex=task.getException().getMessage();

                  final String BAD_EMAIL="The email address is badly formatted";
                  final String WRONG_PASS="The password is invalid or the user does not have a password";
                  final String INVALID_USER="The password is invalid or the user does not have a password";
                  if(ex.equals(BAD_EMAIL))
                  {
                      hintEmail.setError("Please type right email format");
                  }
                  else if(ex.equals(INVALID_USER))
                  {
                      hintEmail.setError("Invalid user or wrong password");
                  }
                  else
                  {
                      Toast.makeText(IndiLogin.this, "Error: "+ex, Toast.LENGTH_SHORT).show();
                  }



                  Log.i("Eeeeee","Error: "+ex);

              }
                loginProgress.setVisibility(View.GONE);
            }
        });


    }

    }
    public void onRegister(View view) {
        final String email = emailReg.getText().toString();
        final String pass = passReg.getText().toString();
        final String name = nameReg.getText().toString();
        final String mobile = mobileReg.getText().toString();


        if (!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(mobile)) {
            regProgressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    regProgressBar.setVisibility(View.GONE);
                    if(task.isSuccessful())
                    {
                        Intent intent =new Intent(IndiLogin.this,SetProfileActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("name",name);
                        intent.putExtra("pass",pass);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        String ex=task.getException().getMessage();
                        Toast.makeText(IndiLogin.this, "Error"+ex, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void forgetpassword(View view) {
    }

    public void createNew(View view) {
        loginPage.setVisibility(View.GONE);
        signUpPage.setVisibility(View.VISIBLE);
    }



    public void backtologin(View view) {
        loginPage.setVisibility(View.VISIBLE);
        signUpPage.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent =new Intent(IndiLogin.this,Ind_HomeActivity.class);
            startActivity(intent);
            finish();

        }
    }

}
