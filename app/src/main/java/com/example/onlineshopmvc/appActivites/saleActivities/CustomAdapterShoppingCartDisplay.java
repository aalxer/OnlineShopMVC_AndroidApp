package com.example.onlineshopmvc.appActivites.saleActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopmvc.R;

import java.util.ArrayList;

public class CustomAdapterShoppingCartDisplay extends RecyclerView.Adapter<CustomAdapterShoppingCartDisplay.MyViewHolder>{

    private Context context;
    private ArrayList<String> titles;
    private ArrayList<String> codes;
    private ArrayList<String> prises;

    public CustomAdapterShoppingCartDisplay(Context context, ArrayList<String> titles, ArrayList<String> codes, ArrayList<String> prises) {
        this.context = context;
        this.titles = titles;
        this.codes = codes;
        this.prises = prises;
    }

    @NonNull
    @Override
    public CustomAdapterShoppingCartDisplay.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.shoppingcart_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterShoppingCartDisplay.MyViewHolder holder, int position) {
        holder.productTitle.setText(String.valueOf(titles.get(position)));
        holder.productID.setText(String.valueOf(codes.get(position)));
        holder.productPries.setText(String.valueOf(prises.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.codes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView productTitle, productID, productPries;
        private LinearLayout ProductDisplayShoppingCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.productTitle = (TextView) itemView.findViewById(R.id.titleView_shoppingCart);
            this.productID = (TextView) itemView.findViewById(R.id.productCodeView_shoppingCart);
            this.productPries = (TextView) itemView.findViewById(R.id.priesView_shoppingCart);
            this.ProductDisplayShoppingCart = (LinearLayout) itemView.findViewById(R.id.shoppingcart_rowMainLayout);
        }
    }
}
