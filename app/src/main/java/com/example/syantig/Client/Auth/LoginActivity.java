package com.example.syantig.Client.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.syantig.Client.MainActivity;
import com.example.syantig.Client.splashActivity;
import com.example.syantig.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //view
    private EditText email_log, pass_log;
    private CheckBox Rememberme;
    //firebase
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//findView
        email_log = findViewById(R.id.email_login);
        pass_log = findViewById(R.id.pass_login);
        Rememberme = findViewById(R.id.remember);

        // firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //intent to RegisterActivity
        findViewById(R.id.tx_create).setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            //onClick to login
            findViewById(R.id.btn_login).setOnClickListener(view1 -> validationData());


        });
    }

    private void validationData() {
        String email = email_log.getText().toString().trim();
        String passcode = pass_log.getText().toString().trim();
        if (email.isEmpty()) {
            email_log.requestFocus();
            // Toast.makeText (this,"Email is required",Toast.LENGTH_SHORT).show();

            //الحاجة اللي بتبقي هتظهر علي الشاشة اسمها ارجمينت
            showAlert("Email is required");
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_log.requestFocus();
            //Toast.makeText(this, "Invalid Email Address  \n Email must be name@company.com ", Toast.LENGTH_SHORT).show();
            showAlert("Invalid Email Address  \n Email must be name@company.com ");

            return;

        }
        if (passcode.isEmpty()) {
            pass_log.requestFocus();
            //Toast.makeText(this, "passcode is required", Toast.LENGTH_SHORT).show();
            showAlert("passcode is required");

            return;

        }
        if (passcode.length() < 6) {
            pass_log.requestFocus();
            //Toast.makeText(this, "passcode must be at least 6 digits ", Toast.LENGTH_SHORT).show();
            showAlert("passcode must be at least 6 digits ");
            return;
        }
        signInWithFirebase(email, passcode);

    }

    private void signInWithFirebase(String email, String passcode)
    {
        firebaseAuth.signInWithEmailAndPassword(email, passcode)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        if (Rememberme.isChecked()) {
                            getSharedPreferences("login", MODE_PRIVATE)
                                    .edit().putBoolean("islogin", true)
                                    .apply();
                        }
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        showAlert("Error \n" + task.getException().getMessage());
                    }
                });
    }

    private void showAlert(String msg)
    {
        new AlertDialog.Builder(this)
                .setTitle("Error").setMessage(msg).setIcon(R.drawable.ic_error)
                .setPositiveButton("ok", null).create().show();
    }
     @Override
    protected void onStart(){
        super.onStart();
        boolean islogin = getSharedPreferences("login",MODE_PRIVATE).getBoolean("islogin",false);
        if (islogin )
        {
           goToMain();
        }

    }
    void  goToMain()
    {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}
