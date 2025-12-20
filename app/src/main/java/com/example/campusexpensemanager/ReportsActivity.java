package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReportsActivity extends AppCompatActivity {

    private DatabaseHelper myDB;

    private ProgressBar pbFood, pbTransport, pbRent, pbEntertainment, pbEducation;
    private TextView tvFoodPercent, tvTransPercent, tvRentPercent, tvEntertainmentPercent, tvEducationPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        myDB = new DatabaseHelper(this);

        pbFood = findViewById(R.id.pbFood);
        pbTransport = findViewById(R.id.pbTransport);
        pbRent = findViewById(R.id.pbRent);
        pbEntertainment = findViewById(R.id.pbEntertainment);
        pbEducation = findViewById(R.id.pbEducation);

        tvFoodPercent = findViewById(R.id.tvFoodPercent);
        tvTransPercent = findViewById(R.id.tvTransPercent);
        tvRentPercent = findViewById(R.id.tvRentPercent);
        tvEntertainmentPercent = findViewById(R.id.tvEntertainmentPercent);
        tvEducationPercent = findViewById(R.id.tvEducationPercent);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnHome = findViewById(R.id.btnHome);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnHome != null) {
            btnHome.setOnClickListener(v ->
                    startActivity(new Intent(ReportsActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            );
        }

        updateProgress();
    }

    private void updateProgress() {
        updateSingleCategory("Food & Dining", pbFood, tvFoodPercent);
        updateSingleCategory("Transport", pbTransport, tvTransPercent);
        updateSingleCategory("Rent", pbRent, tvRentPercent);
        updateSingleCategory("Entertainment", pbEntertainment, tvEntertainmentPercent);
        updateSingleCategory("Education", pbEducation, tvEducationPercent);
    }

    private void updateSingleCategory(String category, ProgressBar bar, TextView label) {
        if (bar == null || label == null) return;

        double limit = myDB.getBudgetLimit(category);
        double spent = myDB.getCategorySpent(category);

        if (limit <= 0) {
            bar.setProgress(0);
            label.setText("--");
            return;
        }

        int percent = (int) Math.min(100, Math.round((spent / limit) * 100));
        bar.setProgress(percent);
        label.setText(percent + "%");
    }
}
