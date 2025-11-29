package com.example.campusexpensemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etAmount, etDescription, etDate; // Thêm etDate
    private Spinner spinnerCategory;
    private Button btnSave;
    private TextView tvHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Ánh xạ
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate); // Ánh xạ ô ngày
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Setup Spinner
        String[] categories = {"Food", "Transport", "Rent", "Entertainment", "Education"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // --- 1. FRONTEND: HIỆU ỨNG CHỌN NGÀY ---
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // --- 2. FRONTEND: GIẢ LẬP CHẾ ĐỘ SỬA (EDIT MODE) ---
        // Nếu Activity này được mở kèm tín hiệu "isEditMode", nó sẽ biến thành form Sửa
        if (getIntent().getBooleanExtra("isEditMode", false)) {
            tvHeaderTitle.setText("Edit Expense"); // Đổi tiêu đề
            btnSave.setText("Update Changes");     // Đổi tên nút

            // Điền sẵn dữ liệu giả để Demo
            etAmount.setText("50.00");
            etDescription.setText("Lunch with friends");
            etDate.setText("27/11/2025");
        }

        // Nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nút Save (Chỉ hiện thông báo giả)
        btnSave.setOnClickListener(v -> {
            Toast.makeText(AddExpenseActivity.this, "Saved Successfully (Frontend Demo)!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}