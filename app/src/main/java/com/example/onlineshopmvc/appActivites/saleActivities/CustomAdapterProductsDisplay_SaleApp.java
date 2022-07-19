package com.example.onlineshopmvc.appActivites.saleActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.saleAppLogic.OperationFailedException;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;
import com.example.onlineshopmvc.appModel.Product;

import java.util.ArrayList;

public class CustomAdapterProductsDisplay_SaleApp extends RecyclerView.Adapter<CustomAdapterProductsDisplay_SaleApp.MyViewHolder> {

    private Context context;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> codes;
    private ArrayList<String> prises;
    private SaleApp saleApp;

    public CustomAdapterProductsDisplay_SaleApp(Context context, ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<String> codes, ArrayList<String> prises) {
        this.context = context;
        this.titles = titles;
        this.descriptions = descriptions;
        this.codes = codes;
        this.prises = prises;
        this.saleApp = SaleAppImpl.getSaleAppInstance(null);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.productview_row_sale, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.productTitle.setText(String.valueOf(titles.get(position)));
        holder.productID.setText(String.valueOf(codes.get(position)));
        holder.productDescription.setText(String.valueOf(descriptions.get(position)));
        holder.productPries.setText(String.valueOf(prises.get(position)));
        holder.addToBasketBtns.setVisibility(View.VISIBLE);

        int pID = Integer.parseInt(holder.productID.getText().toString());
        String pTitle = holder.productTitle.getText().toString();
        String pDescription = holder.productDescription.getText().toString();
        double pPries = Double.parseDouble(holder.productPries.getText().toString());
        int pStock = 1;
        Product productToAdd = new Product(pID,pTitle,pDescription,pPries,pStock);

        holder.addToBasketBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saleApp.addToShoppingCart(productToAdd);
                    String toastText = "Product " + pID + " added to your Shopping Basket";
                    Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
                } catch (OperationFailedException e) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView productTitle, productID, productDescription, productPries, productStockNr;
        private Button addToBasketBtns;
        private LinearLayout ProductDisplayMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.productTitle = (TextView) itemView.findViewById(R.id.titleView_sale);
            this.productID = (TextView) itemView.findViewById(R.id.productCodeView_sale);
            this.productDescription = (TextView) itemView.findViewById(R.id.descriptionView_sale);
            this.productPries = (TextView) itemView.findViewById(R.id.priesView_sale);
            this.addToBasketBtns = (Button) itemView.findViewById(R.id.addToBasketBtn);
            this.ProductDisplayMain = (LinearLayout) itemView.findViewById(R.id.productsSaleDisplay_rowPage);
        }
    }
}