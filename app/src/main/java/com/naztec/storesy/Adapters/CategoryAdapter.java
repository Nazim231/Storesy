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
    // TODO : Remove below variables they are used just for testing
    ArrayList<String> products;
    boolean containCategories = false;

    public CategoryAdapter(boolean containCategories, ArrayList<CategoryModel> categories) {
        this.categories = categories;
        this.containCategories = containCategories;
    }

    public CategoryAdapter(ArrayList<String> products) {
        this.products = products;
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

        holder.categoryName.setText(containCategories ? categories.get(position).getCategoryName() : products.get(position));
    }

    @Override
    public int getItemCount() {
        return containCategories ? categories.size() : products.size();
    }

}
