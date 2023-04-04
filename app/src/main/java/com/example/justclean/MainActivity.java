package com.example.justclean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView registertxt;
    private TextView forgotpass;
    private EditText editTextemail,editTextpassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appblue)));
        registertxt=findViewById(R.id.registertxt);
        registertxt.setOnClickListener(this);
        login=(Button) findViewById(R.id.loginbutton);
        login.setOnClickListener(this);
        editTextemail=(EditText) findViewById(R.id.loginEmail);
        editTextpassword=(EditText) findViewById(R.id.loginPassword);
        progressBar=(ProgressBar) findViewById(R.id.loginprogressBar);
        forgotpass=(TextView) findViewById(R.id.loginforgotpassword);
        forgotpass.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registertxt:
                startActivity(new Intent(this,Register.class));
                break;
            case R.id.loginbutton:
                userlogin();
                break;
            case R.id.loginforgotpassword:
                Intent passi=new Intent(this,ForgotPassword.class);
                startActivity(passi);
                break;
    }
}

    private void userlogin() {
        String email=editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        if (email.isEmpty()){
            editTextemail.setError("Username required");
            editTextemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("Please provide a valid email");
            editTextemail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextpassword.setError("Required Field");
            editTextpassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextpassword.setError("Password length must be atleast 6 characters");
            editTextpassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Logged in successfully !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Intent i=getPackageManager().getLaunchIntentForPackage("com.example.navdrawer");
                    startActivity(i);

                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Incorrect email or password!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
}