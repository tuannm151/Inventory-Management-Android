package com.example.admin.PTITScan.view.activities.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.PTITScan.R;
import com.example.admin.PTITScan.model.User;
import com.example.admin.PTITScan.view.activities.dashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,editTextcPassword;
    public Button UserRegisterBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.departmentName);
        editTextEmail = findViewById(R.id.emailRegister);
        editTextPassword = findViewById(R.id.passwordRegister);
        editTextcPassword= findViewById(R.id.confirmPassword);
        UserRegisterBtn= findViewById(R.id.button_register);
//        editTextPhone = findViewById(R.id.edit_text_phone);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        //  findViewById(R.id.button_register).setOnClickListener(this);

        UserRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }


    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString().trim();
        String cpassword = editTextcPassword.getText().toString().trim();
        // final String phone = editTextPhone.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("It's empty");
            editTextEmail.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            editTextName.setError("It's Empty");
            editTextName.requestFocus();
            return;
        }



        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Not a valid emailaddress");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Its empty");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Less length");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.equals(cpassword)){
            editTextcPassword.setError("Password Donot Match");
            editTextcPassword.requestFocus();
            return;
        }

//        if (phone.isEmpty()) {
//            editTextPhone.setError(getString(R.string.input_error_phone));
//            editTextPhone.requestFocus();
//            return;
//        }
//
//        if (phone.length() != 10) {
//            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
//            editTextPhone.requestFocus();
//            return;
//        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

//                            addStudent();

                            final User user = new User(
                                    name,
                                    email

                            );
                            //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            //important to retrive data and send data based on user email
                            FirebaseUser usernameinfirebase = mAuth.getCurrentUser();
                            String UserID=usernameinfirebase.getEmail();
                           // String result = UserID.substring(0, UserID.indexOf("@"));
                            String resultemail = UserID.replace(".","");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(resultemail).child("UserDetails")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this, dashboardActivity.class));
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("Error: "+task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }





}
