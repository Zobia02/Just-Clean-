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
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName, editTextEmail, editTextPassword, editTextPhoneno;
    private Button regButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appblue)));
        mAuth = FirebaseAuth.getInstance();
        editTextName=(EditText) findViewById(R.id.regname);
        editTextEmail=(EditText) findViewById(R.id.regEmail);
        editTextPassword=(EditText) findViewById(R.id.regPassword);
        editTextPhoneno=(EditText) findViewById(R.id.regPhoneno);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        regButton=(Button) findViewById(R.id.registerbutton);
        regButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registerbutton:
                registerbutton();
                break;
        }
        
    }

    private void registerbutton() {
        String username=editTextName.getText().toString().trim();
        String email= editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String phoneno=editTextPhoneno.getText().toString().trim();
        if (username.isEmpty()){
            editTextName.setError("Username required");
            editTextName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
    }
        if (password.isEmpty()){
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassword.setError("Password length must be atleast 6 characters");
            editTextPassword.requestFocus();
            return;
        }
        if (phoneno.isEmpty()){
            editTextPhoneno.setError("Mobile Number required");
            editTextPhoneno.requestFocus();
            return;
        }
        if (phoneno.length()<11) {
            editTextPhoneno.setError("Please provide a valid mobile number");
            editTextPhoneno.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user=new User(username,email,phoneno);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.navdrawer");
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(Register.this, "Failed to register! Please Try Again!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }


                                }
                            });
                        }else{
                            Toast.makeText(Register.this,"Failed to register! Please Try Again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}