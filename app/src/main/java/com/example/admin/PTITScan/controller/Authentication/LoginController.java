package com.example.admin.PTITScan.controller.Authentication;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.admin.PTITScan.view.activities.Authentication.LoginActivity;
import com.example.admin.PTITScan.view.activities.dashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginController {
    private LoginActivity loginActivity;

    private FirebaseAuth auth;

    public LoginController(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        auth = FirebaseAuth.getInstance();
    }

    public void resetpasword(String resetemail){
        loginActivity.showProgressDialog("Đang gửi email đặt lại mật khẩu...");
        auth.sendPasswordResetEmail(resetemail)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loginActivity.showToast("Đã gửi email đặt lại mật khẩu.");
                        } else {
                            loginActivity.showToast("Lỗi! Vui lòng thử lại.");
                        }
                        loginActivity.hideProgressDialog();
                    }
                });
    }




    public void validate(String userEmail, String userPassword){
        loginActivity.showProgressDialog("Đang xử lý yêu cầu...");
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginActivity.hideProgressDialog();
                if(task.isSuccessful()){
                    loginActivity.showToast("Đăng nhập thành công");
                    loginActivity.startActivity(new Intent(loginActivity, dashboardActivity.class));
                }else{
                    loginActivity.showToast("Đăng nhập thất bại");
                }
            }
        });
    }
}
