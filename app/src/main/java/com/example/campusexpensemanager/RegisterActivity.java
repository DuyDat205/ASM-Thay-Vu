package com.example.campusexpensemanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUser, etPass, etConfirm;
    private Button btnRegister;
    private TextView tvLoginLink;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDB = new DatabaseHelper(this);

        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        etConfirm = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        btnRegister.setOnClickListener(v -> {
            String user = etUser.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                if (myDB.registerUser(user, pass)) {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLoginLink.setOnClickListener(v -> finish());
    }
}