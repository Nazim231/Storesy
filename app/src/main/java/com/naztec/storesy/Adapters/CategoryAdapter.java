package com.naztec.storesy.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.Models.CategoryModel;
import com.naztec.storesy.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<CategoryModel> categories;

    public CategoryAdapter(ArrayList<CategoryModel> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
        return new ViewHolder(view);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.txt_category_name);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categories.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

}
