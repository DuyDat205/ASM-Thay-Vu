package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar; // Import RatingBar
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnHome = findViewById(R.id.btnHome);

        btnBack.setOnClickListener(v -> finish());
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if(rating >= 4) {
                Toast.makeText(FeedbackActivity.this, "Thanks for the high rating!", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSend = findViewById(R.id.btnSendFeedback);
        btnSend.setOnClickListener(v -> {
            Toast.makeText(FeedbackActivity.this, "Feedback Sent to Developer!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}