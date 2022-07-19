package com.example.onlineshopmvc.appActivites.managementActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopmvc.R;

import java.util.ArrayList;

public class OrdersManagementActivity_CustomAdapter extends RecyclerView.Adapter<OrdersManagementActivity_CustomAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<String> idNumbers;
    private ArrayList<String> dates;
    private ArrayList<String> states;

    public OrdersManagementActivity_CustomAdapter(Context context, ArrayList<String> idNumbers, ArrayList<String> dates, ArrayList<String> states) {
        this.context = context;
        this.idNumbers = idNumbers;
        this.dates = dates;
        this.states = states;
    }

    @NonNull
    @Override
    public OrdersManagementActivity_CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.activity_orders_management_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersManagementActivity_CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.orderID.setText(String.valueOf(idNumbers.get(position)));
        holder.orderDate.setText(String.valueOf(dates.get(position)));
        holder.orderState.setText(String.valueOf(states.get(position)));

        // Order Seite:
        holder.ordersDisplayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DisplayOrderActivity.class);
                // OrderId der angeklickten Bestellung übergeben:
                intent.putExtra("orderID",String.valueOf(idNumbers.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idNumbers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView orderID;
        private TextView orderDate;
        private TextView orderState;
        private LinearLayout ordersDisplayLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.orderID = itemView.findViewById(R.id.orderIDNr);
            this.orderDate = itemView.findViewById(R.id.orderDateDate);
            this.orderState = itemView.findViewById(R.id.orderStateState);
            this.ordersDisplayLayout = itemView.findViewById(R.id.ordersDisplayLayout);

        }
    }
}
