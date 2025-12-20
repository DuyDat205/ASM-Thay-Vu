package com.example.campusexpensemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import java.util.Calendar;


public class AddExpenseActivity extends AppCompatActivity {

    private EditText etAmount, etDescription, etDate;
    private Spinner spinnerCategory;
    private Button btnSave;
    private TextView tvHeaderTitle;
    private DatabaseHelper myDB;

    private boolean isEdit = false;
    private String expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        myDB = new DatabaseHelper(this);
        
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        ImageButton btnBack = findViewById(R.id.btnBack);

        String[] categories = {"Food & Dining", "Transport", "Rent", "Entertainment", "Education"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) ->
                    etDate.setText(day + "/" + (month + 1) + "/" + year),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        if(etDate.getText().toString().isEmpty()) {
            Calendar c = Calendar.getInstance();
            etDate.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
        }

        if (getIntent().getBooleanExtra("isEditMode", false)) {
            isEdit = true;
            expenseId = getIntent().getStringExtra("id");
            etAmount.setText(getIntent().getStringExtra("amount"));
            etDescription.setText(getIntent().getStringExtra("desc"));
            etDate.setText(getIntent().getStringExtra("date"));

            String cat = getIntent().getStringExtra("category");
            if(cat != null) {
                int spinnerPosition = adapter.getPosition(cat);
                spinnerCategory.setSelection(spinnerPosition);
            }

            tvHeaderTitle.setText("Edit Expense");
            btnSave.setText("Update Changes");
        }

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString();
            String desc = etDescription.getText().toString();
            String date = etDate.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // Save or update the expense first
            if (isEdit) {
                myDB.updateExpense(expenseId, amount, desc, category, date);
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
            } else {
                myDB.addExpense(amount, desc, category, date);
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
            }

            // -----------------------------
            // NEW: Budget check & warning
            // -----------------------------
            double limit = myDB.getBudgetLimit(category);
            String warningMessage = null;

            if (limit > 0) { // only check if a budget is set for this category
                double spent = myDB.getCategorySpent(category);
                if (spent > limit) {
                    double diff = spent - limit;
                    warningMessage = "You have exceeded the budget for " + category + ".\n\n"
                            + "Budget: " + String.format("%.2f", limit) + "\n"
                            + "Current spending: " + String.format("%.2f", spent) + "\n"
                            + "Over by: " + String.format("%.2f", diff);
                }
            }

            if (warningMessage != null) {
                final String msg = warningMessage;
                new AlertDialog.Builder(this)
                        .setTitle("Budget exceeded")
                        .setMessage(msg)
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            finish();   // close screen after acknowledging
                        })
                        .show();
            } else {
                // No warning => just close as before
                finish();
            }
        });

    }
}