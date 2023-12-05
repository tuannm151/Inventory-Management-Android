package com.example.admin.PTITScan.controller.Authentication;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.admin.PTITScan.model.User;
import com.example.admin.PTITScan.view.activities.Authentication.RegisterActivity;
import com.example.admin.PTITScan.view.activities.dashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterController {
    private FirebaseAuth auth;
    private RegisterActivity registerActivity;

    public RegisterController(RegisterActivity registerActivity) {
        auth = FirebaseAuth.getInstance();
        this.registerActivity = registerActivity;
    }

    public void registerUser(final String name, final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(
                                    name,
                                    email
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            registerActivity.hideProgressDialog();
                                            if (task.isSuccessful()) {
                                                registerActivity.showToast("Đăng ký thành công");
                                                registerActivity.startActivity(new Intent(registerActivity, dashboardActivity.class));
                                            } else {
                                                registerActivity.showToast("Đăng ký thất bại");
                                            }
                                        }
                                    });

                        } else {
                            registerActivity.hideProgressDialog();
                            registerActivity.showToast("Đăng ký thất bại");
                        }
                    }
                });
    }
}
