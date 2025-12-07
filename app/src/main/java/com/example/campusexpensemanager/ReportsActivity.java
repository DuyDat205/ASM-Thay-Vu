package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReportsActivity extends AppCompatActivity {
    private DatabaseHelper myDB;
    private ProgressBar pbFood, pbTransport;
    private TextView tvFoodPercent, tvTransPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        myDB = new DatabaseHelper(this);
        pbFood = findViewById(R.id.pbFood);
        pbTransport = findViewById(R.id.pbTransport);
        tvFoodPercent = findViewById(R.id.tvFoodPercent);
        tvTransPercent = findViewById(R.id.tvTransPercent);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });

        loadReport();
    }

    private void loadReport() {
        double foodLimit = myDB.getBudgetLimit("Food & Dining");
        double foodSpent = myDB.getCategorySpent("Food & Dining");
        if (foodLimit > 0 && pbFood != null) {
            int p = (int) ((foodSpent / foodLimit) * 100);
            pbFood.setProgress(p);
            if(tvFoodPercent != null) tvFoodPercent.setText(p + "%");
        }

        double transLimit = myDB.getBudgetLimit("Transport");
        double transSpent = myDB.getCategorySpent("Transport");
        if (transLimit > 0 && pbTransport != null) {
            int p = (int) ((transSpent / transLimit) * 100);
            pbTransport.setProgress(p);
            if(tvTransPercent != null) tvTransPercent.setText(p + "%");
        }
    }
}