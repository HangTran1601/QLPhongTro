package com.example.quanlyphongtro.Customer.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyphongtro.Customer.Sqlite.AccountSqlite;
import com.example.quanlyphongtro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView textView;
    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    TextView forgot;
    AccountSqlite accountSqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        signUp();
        signIn();
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });
    }
    public void signUp(){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void signIn(){

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseSetUp();
            }
        });
    }
    public void firebaseSetUp(){
        String email = editEmail.getText().toString();
        String pass = editPassword.getText().toString();
        // Sử dụng Firebase Authentication để đăng nhập với email và mật khẩu
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Kiểm tra xem quá trình đăng nhập có thành công hay không
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // Lưu thông tin tài khoản vào SQLite database
                            accountSqlite.insertAccount(email,0);
                            startActivity(intent);
                        } else {
                            // Nếu đăng nhập thất bại, hiển thị thông báo lỗi
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //thiết lập quên mật khẩu
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.forgot, null);
        builder.setView(dialogView);

        EditText emailEditText = dialogView.findViewById(R.id.editTextEmail);
        Button reset = dialogView.findViewById(R.id.buttonResetPassword);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                // Lấy email mà người dùng đã nhập
                String emailAddress = emailEditText.getText().toString();
                // Gửi email đặt lại mật khẩu
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Nếu gửi email thành công
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra email của bạn", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        // Thiết lập nút "Hủy Bỏ" cho hộp thoại
        builder.setNegativeButton("Hủy Bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void mapping(){
        editEmail =findViewById(R.id.editTextTaiKhoan);
        editPassword = findViewById(R.id.editTextMatKhau);
        textView = findViewById(R.id.textView_register);
        btnLogin = findViewById(R.id.buttonDangNhap);
        accountSqlite = new AccountSqlite(LoginActivity.this);

        forgot = findViewById(R.id.textView_forgotPassword);
    }

}