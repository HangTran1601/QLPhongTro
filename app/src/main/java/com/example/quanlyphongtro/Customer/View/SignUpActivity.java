package com.example.quanlyphongtro.Customer.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyphongtro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText email,password,password2;
    private Button signUp;
    private FirebaseAuth mAuth;
    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mapping();
        signUpMethod();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void mapping(){
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        password2 = findViewById(R.id.editTextPasswordAgain);
        signUp = findViewById(R.id.buttonSignUp);
        login = findViewById(R.id.textView_login);
    }
    public void signUpMethod(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseSignUp();
            }
        });
    }
    public void firebaseSignUp(){
        // Kiểm tra xem hai mật khẩu người dùng nhập có khớp nhau hay không
        if(!password.getText().toString().equals(password2.getText().toString())){
            // Nếu mật khẩu không khớp, hiển thị thông báo lỗi
            Toast.makeText(this, "Mat khau khong trung khop", Toast.LENGTH_SHORT).show();
        }
        // Nếu mật khẩu khớp, tiếp tục quá trình đăng ký
        else{
            String emailSignUp = email.getText().toString().trim();
            String passwordSignUp = password.getText().toString().trim();
            // Sử dụng Firebase Authentication để tạo tài khoản mới với email và mật khẩu
            mAuth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Kiểm tra xem quá trình tạo tài khoản có thành công hay không
                            if (task.isSuccessful()) {
                                // Nếu thành công, hiển thị thông báo thành công
                                Toast.makeText(SignUpActivity.this, "Dang ki thanh cong", Toast.LENGTH_SHORT).show();
                                // Chuyển sang LoginActivity (màn hình đăng nhập)
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // Nếu thất bại, hiển thị thông báo lỗi
                                Toast.makeText(SignUpActivity.this, "Email khong hop le hoac mat khau k du 6 ky tu.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}