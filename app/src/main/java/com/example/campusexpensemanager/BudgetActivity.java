package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BudgetActivity extends AppCompatActivity {
    private EditText etFood, etTransport;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        myDB = new DatabaseHelper(this);
        etFood = findViewById(R.id.etBudgetFood);
        etTransport = findViewById(R.id.etBudgetTransport);

        double foodLimit = myDB.getBudgetLimit("Food & Dining");
        double transLimit = myDB.getBudgetLimit("Transport");
        if(foodLimit > 0) etFood.setText(String.valueOf((int)foodLimit));
        if(transLimit > 0) etTransport.setText(String.valueOf((int)transLimit));

        findViewById(R.id.btnSaveBudget).setOnClickListener(v -> {
            double fVal = etFood.getText().toString().isEmpty() ? 0 : Double.parseDouble(etFood.getText().toString());
            double tVal = etTransport.getText().toString().isEmpty() ? 0 : Double.parseDouble(etTransport.getText().toString());

            myDB.setBudget("Food & Dining", fVal);
            myDB.setBudget("Transport", tVal);

            Toast.makeText(this, "Budgets Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }
}