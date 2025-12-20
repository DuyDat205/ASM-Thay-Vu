package com.example.campusexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    private DatabaseHelper myDB;
    private MyAdapter adapter;
    private ArrayList<ExpenseModel> expenseList;
    private TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);
        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        rvExpenses = findViewById(R.id.rvExpenses);

        tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddExpenseActivity.class)));

        findViewById(R.id.navReports).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReportsActivity.class)));
        findViewById(R.id.navBudget).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BudgetActivity.class)));
        findViewById(R.id.navFeedback).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FeedbackActivity.class)));

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        double totalSpent = myDB.getTotalSpent();
        double initialBudget = 2000.0;
        double remaining = initialBudget - totalSpent;
        tvTotalBalance.setText("$" + String.format("%.2f", remaining));

        expenseList = new ArrayList<>();
        Cursor cursor = myDB.getAllExpenses();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                expenseList.add(new ExpenseModel(
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(1)
                ));
            }
        }

        adapter = new MyAdapter(this, expenseList);
        rvExpenses.setAdapter(adapter);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
    }

    class ExpenseModel {
        String id, desc, category, date;
        double amount;
        public ExpenseModel(String id, String d, String c, String date, double a) {
            this.id = id; this.desc = d; this.category = c; this.date = date; this.amount = a;
        }
    }

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
            holder.tvCategory.setText(item.category + " • " + item.date);
            holder.tvAmount.setText("-$" + item.amount);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddExpenseActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("amount", String.valueOf(item.amount));
                intent.putExtra("desc", item.desc);
                intent.putExtra("date", item.date);
                intent.putExtra("category", item.category);
                intent.putExtra("isEditMode", true);
                context.startActivity(intent);
            });

            holder.btnDelete.setOnClickListener(v -> {
                myDB.deleteExpense(item.id);
                loadData();
                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvDesc, tvCategory, tvAmount;
            ImageButton btnDelete;
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