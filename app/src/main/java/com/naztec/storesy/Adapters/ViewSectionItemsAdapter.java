package com.naztec.storesy.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.Models.ViewSectionItemsModel;
import com.naztec.storesy.R;

import java.util.ArrayList;

public class ViewSectionItemsAdapter extends RecyclerView.Adapter<ViewSectionItemsAdapter.ViewHolder> {

    ArrayList<ViewSectionItemsModel> items;

    public ViewSectionItemsAdapter(ArrayList<ViewSectionItemsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_item,
                parent, false);

        return new ViewHolder(view);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProdName, txtPrice, txtMRP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProdName = itemView.findViewById(R.id.view_item_prod_name);
            txtPrice = itemView.findViewById(R.id.view_item_prod_selling_price);
            txtMRP = itemView.findViewById(R.id.view_item_prod_mrp);
        }

        public void setData(String prodName, String prodPrice, String prodMRP) {
            txtProdName.setText(prodName);
            txtPrice.setText(prodPrice);
            txtMRP.setText(prodMRP);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewSectionItemsModel data = items.get(position);
        holder.setData(data.getProdName(), data.getProdPrice(), data.getProdMRP());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
