package com.example.syantig.Client.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.syantig.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    //view
    private EditText edfname, edsname, edemail, edpasscode, edphone;
    private ProgressBar ProgressBar;
    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //findview
        edfname = findViewById(R.id.ed_fname);
        edsname = findViewById(R.id.ed_sname);
        edemail = findViewById(R.id.ed_email_regist);
        edpasscode = findViewById(R.id.ed_pass_regist);
        edphone = findViewById(R.id.ed_phone);
        ProgressBar= findViewById(R.id.progressbar_register);

        findViewById(R.id.btn_register).setOnClickListener(v -> validationData());
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        //onclick to finish this Activity
        findViewById(R.id.igv_closeregist).setOnClickListener(view -> {
            finish();
            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void validationData()
    {
        String fname = edfname.getText().toString().trim();
        String sname = edsname.getText().toString().trim();
        String email = edemail.getText().toString().trim();
        String passcode = edpasscode.getText().toString().trim();
        String phone = edphone.getText().toString();


        if (fname.isEmpty()) {
            edfname.requestFocus();
            //Toast.makeText(this, "fname is required", Toast.LENGTH_SHORT).show();
            showAlert("fname is required");
            return;

        }
        if (sname.isEmpty()) {
            edsname.requestFocus();
            //Toast.makeText(this, "sname is required", Toast.LENGTH_SHORT).show();
            showAlert("sname is required");

            return;

        }

        if (email.isEmpty())
        {
            edemail.requestFocus();
            // Toast.makeText (this,"Email is required",Toast.LENGTH_SHORT).show();

            //الحاجة اللي بتبقي هتظهر علي الشاشة اسمها ارجمينت
            showAlert("Email is required");
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edemail.requestFocus();
            //Toast.makeText(this, "Invalid Email Address  \n Email must be name@company.com ", Toast.LENGTH_SHORT).show();
            showAlert("Invalid Email Address  \n Email must be name@company.com ");

            return;

        }
        if (passcode.isEmpty()) {
            edpasscode.requestFocus();
            //Toast.makeText(this, "passcode is required", Toast.LENGTH_SHORT).show();
            showAlert("passcode is required");

            return;

        }
        if (passcode.length() < 6) {
            edpasscode.requestFocus();
            //Toast.makeText(this, "passcode must be at least 6 digits ", Toast.LENGTH_SHORT).show();
            showAlert("passcode must be at least 6 digits ");
            return;
        }

        if (phone.isEmpty()) {
            edphone.requestFocus();
            //Toast.makeText(this, "phone is required", Toast.LENGTH_SHORT).show();
            showAlert("phone is required");
            return;

        }
        if (phone.length() < 11)
        {
            edphone.requestFocus();
           // Toast.makeText(this, "phone must be 11 digits ", Toast.LENGTH_SHORT).show();
            showAlert("phone must be 11 digits ");
            return;
        }


       // Toast.makeText(this, "All data in valid", Toast.LENGTH_SHORT).show();
        signUpWithFirebase(email,passcode);


    }

    private void signUpWithFirebase(String email, String passcode)
    {
        ProgressBar.setVisibility(View.VISIBLE);
firebaseAuth.createUserWithEmailAndPassword(email, passcode)
        .addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                saveUserData();

            } else {
                ProgressBar.setVisibility(View.GONE);
                showAlert(task.getException().getMessage());
            }
        });
    }

    private void saveUserData() {
        if (firebaseAuth.getCurrentUser() != null)
        {

            String userID = firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> user = new HashMap<>();
            user.put("fname", edfname.getText().toString().trim());
            user.put("sname", edsname.getText().toString().trim());
            user.put("email", edemail.getText().toString().trim());
            user.put("passcode", edpasscode.getText().toString().trim());
            user.put("phone", edphone.getText().toString().trim());
            user.put("Id",userID);
            firestore.collection("users")
                    .document(userID)
                    .set(user)
                    .addOnCompleteListener(task ->
                    {
                      if (task.isSuccessful())
                      {
                          ProgressBar.setVisibility(View.GONE);
                          new AlertDialog.Builder(this)
                                  .setTitle("Congratulation")
                                  .setMessage("account created successful")
                                  .setCancelable(false)
                                  .setIcon(R.drawable.ic_done)
                                  .setPositiveButton("ok", (dialogInterface, i) -> {
                                      startActivity(new  Intent(RegisterActivity.this, LoginActivity.class) );

                                  })
                                  .create()
                                  .show();
                      } else
                          {
                          ProgressBar.setVisibility(View.GONE);

                          showAlert( "Error \n" + task .getException().getMessage());
                      }
                    });


        }
    }
    //الحاجة اللي بستقبلها في الميثود براميتار
   private void showAlert(String msg)
    {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("ok", null)
                .create()
                .show();
    }

}