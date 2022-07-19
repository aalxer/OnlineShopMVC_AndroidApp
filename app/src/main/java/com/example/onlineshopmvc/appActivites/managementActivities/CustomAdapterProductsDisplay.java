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

public class CustomAdapterProductsDisplay extends RecyclerView.Adapter<CustomAdapterProductsDisplay.MyViewHoler> {

    private Context context;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> codes;
    private ArrayList<String> prises;
    private ArrayList<String> stocknrs;

    public CustomAdapterProductsDisplay(Context context, ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<String> codes, ArrayList<String> prises, ArrayList<String> stocknrs) {
        this.context = context;
        this.titles = titles;
        this.descriptions = descriptions;
        this.codes = codes;
        this.prises = prises;
        this.stocknrs = stocknrs;
    }

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.productview_row, parent, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, @SuppressLint("RecyclerView") int position) {

        holder.productTitle.setText(String.valueOf(titles.get(position)));
        holder.productID.setText(String.valueOf(codes.get(position)));
        holder.productDescription.setText(String.valueOf(descriptions.get(position)));
        holder.productPries.setText(String.valueOf(prises.get(position)));
        holder.productStockNr.setText(String.valueOf(stocknrs.get(position)));

        // Update Site
        holder.ProductDisplayMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                // die aktuellen Infos übergeben:
                intent.putExtra("title",String.valueOf(titles.get(position)));
                intent.putExtra("codeID",String.valueOf(codes.get(position)));
                intent.putExtra("description",String.valueOf(descriptions.get(position)));
                intent.putExtra("pries",String.valueOf(prises.get(position)));
                intent.putExtra("stocknr",String.valueOf(stocknrs.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {

        private TextView productTitle, productID, productDescription, productPries, productStockNr;
        private LinearLayout ProductDisplayMain;

        public MyViewHoler(@NonNull View itemView) {
            super(itemView);

            this.productTitle = (TextView) itemView.findViewById(R.id.titleView);
            this.productID = (TextView) itemView.findViewById(R.id.productCodeView);
            this.productDescription = (TextView) itemView.findViewById(R.id.descriptionView);
            this.productPries = (TextView) itemView.findViewById(R.id.priesView);
            this.productStockNr = (TextView) itemView.findViewById(R.id.stockNrView);
            this.ProductDisplayMain = (LinearLayout) itemView.findViewById(R.id.ProductDisplayMain);
         }
    }
}
