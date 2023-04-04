package com.example.justclean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
    Button restpass;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email= (EditText) findViewById(R.id.resetpassemail);
        restpass=(Button) findViewById(R.id.resetpassbutton);
        mAuth=FirebaseAuth.getInstance();

        restpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });
    }

    private void resetPassword() {
        String emails=email.getText().toString().trim();
        if (emails.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(emails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPassword.this, "Try Again! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
        }

}