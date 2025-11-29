package com.example.campusexpensemanager;

import android.content.Intent; // Nhớ dòng này
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // Nhớ dòng này
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Ánh xạ ID
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // 2. Sự kiện bấm nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị thông báo
                Toast.makeText(RegisterActivity.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();

                // Thay vì đóng (finish), chúng ta chuyển thẳng vào màn hình chính luôn cho tiện
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình đăng ký để người dùng không back lại được
            }
        });

        // 3. Sự kiện bấm nút "Already have account?"
        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình đăng nhập
            }
        });
    }
}