package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton; // Import ImageButton
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalBalance;
    private RecyclerView rvExpenses;
    private MyAdapter adapter;
    private ArrayList<ExpenseModel> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        rvExpenses = findViewById(R.id.rvExpenses);

        // Nút FAB Thêm mới
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddExpenseActivity.class)));

        // Các nút điều hướng khác
        findViewById(R.id.navReports).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReportsActivity.class)));
        findViewById(R.id.navBudget).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BudgetActivity.class)));
        findViewById(R.id.navFeedback).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FeedbackActivity.class)));

        // TẠO DỮ LIỆU GIẢ ĐỂ HIỂN THỊ FRONTEND
        expenseList = new ArrayList<>();
        expenseList.add(new ExpenseModel("Lunch with friends", "Food", 50.0));
        expenseList.add(new ExpenseModel("Bus Ticket", "Transport", 2.5));
        expenseList.add(new ExpenseModel("Monthly Rent", "Rent", 500.0));

        adapter = new MyAdapter(this, expenseList);
        rvExpenses.setAdapter(adapter);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
    }

    // --- MODEL ---
    class ExpenseModel {
        String desc, category;
        double amount;
        public ExpenseModel(String d, String c, double a) {
            this.desc = d; this.category = c; this.amount = a;
        }
    }

    // --- ADAPTER (Xử lý hiệu ứng Sửa/Xóa) ---
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private android.content.Context context;
        private ArrayList<ExpenseModel> list;

        MyAdapter(android.content.Context context, ArrayList<ExpenseModel> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ExpenseModel item = list.get(position);
            holder.tvDesc.setText(item.desc);
            holder.tvCategory.setText(item.category + " • 27/11/2025");
            holder.tvAmount.setText("-$" + item.amount);

            // 1. FRONTEND: Bấm vào dòng -> Mở giao diện Sửa
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddExpenseActivity.class);
                intent.putExtra("isEditMode", true); // Báo hiệu là mở chế độ Sửa
                context.startActivity(intent);
            });

            // 2. FRONTEND: Bấm vào thùng rác -> Hiện thông báo Xóa
            holder.btnDelete.setOnClickListener(v -> {
                Toast.makeText(context, "Deleted Item (Frontend Demo)", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvDesc, tvCategory, tvAmount;
            ImageButton btnDelete; // Nút xóa

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDesc = itemView.findViewById(R.id.tvItemDesc);
                tvCategory = itemView.findViewById(R.id.tvItemCategory);
                tvAmount = itemView.findViewById(R.id.tvItemAmount);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }
}