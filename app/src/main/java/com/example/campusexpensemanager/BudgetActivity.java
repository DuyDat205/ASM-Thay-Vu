package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BudgetActivity extends AppCompatActivity {

    private EditText etFood, etTransport, etRent, etEntertainment, etEducation;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        myDB = new DatabaseHelper(this);

        etFood = findViewById(R.id.etBudgetFood);
        etTransport = findViewById(R.id.etBudgetTransport);
        etRent = findViewById(R.id.etBudgetRent);
        etEntertainment = findViewById(R.id.etBudgetEntertainment);
        etEducation = findViewById(R.id.etBudgetEducation);

        // Load saved budget limits (if any)
        setEditTextFromLimit(etFood, myDB.getBudgetLimit("Food & Dining"));
        setEditTextFromLimit(etTransport, myDB.getBudgetLimit("Transport"));
        setEditTextFromLimit(etRent, myDB.getBudgetLimit("Rent"));
        setEditTextFromLimit(etEntertainment, myDB.getBudgetLimit("Entertainment"));
        setEditTextFromLimit(etEducation, myDB.getBudgetLimit("Education"));

        Button btnSave = findViewById(R.id.btnSaveBudget);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                double foodLimit = parseDoubleOrZero(etFood);
                double transportLimit = parseDoubleOrZero(etTransport);
                double rentLimit = parseDoubleOrZero(etRent);
                double entertainmentLimit = parseDoubleOrZero(etEntertainment);
                double educationLimit = parseDoubleOrZero(etEducation);

                myDB.setBudget("Food & Dining", foodLimit);
                myDB.setBudget("Transport", transportLimit);
                myDB.setBudget("Rent", rentLimit);
                myDB.setBudget("Entertainment", entertainmentLimit);
                myDB.setBudget("Education", educationLimit);

                Toast.makeText(BudgetActivity.this, "Budgets saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageButton btnHome = findViewById(R.id.btnHome);
        if (btnHome != null) {
            btnHome.setOnClickListener(v ->
                    startActivity(new Intent(BudgetActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            );
        }
    }

    private void setEditTextFromLimit(EditText editText, double limit) {
        if (editText != null && limit > 0) {
            if (limit == (long) limit) {
                editText.setText(String.valueOf((long) limit));
            } else {
                editText.setText(String.valueOf(limit));
            }
        }
    }

    private double parseDoubleOrZero(EditText editText) {
        if (editText == null) return 0;
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) return 0;
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
