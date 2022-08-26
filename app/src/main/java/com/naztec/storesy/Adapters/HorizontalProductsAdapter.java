package com.naztec.storesy.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.R;

import java.util.ArrayList;

public class HorizontalProductsAdapter extends RecyclerView.Adapter<HorizontalProductsAdapter.ViewHolder> {

    // TODO : Change the type of ArrayList to the CustomModel of ProductImageURL(String) and ProductName(String)
    ArrayList<String> products;

    public HorizontalProductsAdapter(ArrayList<String> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hor_prod_item, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView prodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodName = itemView.findViewById(R.id.hor_prod_item_name);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.prodName.setText(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
