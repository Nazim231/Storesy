package com.naztec.storesy.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.Adapters.CategoryAdapter;
import com.naztec.storesy.Adapters.MultiLayoutAdapter;
import com.naztec.storesy.Custom.DBQueries;
import com.naztec.storesy.Models.MultiLayoutModel;
import com.naztec.storesy.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView rvCategories, rvMultiLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvCategories = view.findViewById(R.id.rv_categories);
        rvMultiLayout = view.findViewById(R.id.rv_multi_layout);

        // Categories
        rvCategories.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter adapter = new CategoryAdapter(DBQueries.categories);
        rvCategories.setAdapter(adapter);

        if (DBQueries.categories.size() == 0)
            DBQueries.fetchCategories(view.getContext(), taskResult ->
                    adapter.notifyDataSetChanged()
            );
        else
            adapter.notifyDataSetChanged();

        // Home Data (Multi Layout)

        // Testing Data
        ArrayList<MultiLayoutModel> layoutsList = new ArrayList<>();
        layoutsList.add(new MultiLayoutModel(0, "Banner 1"));
        layoutsList.add(new MultiLayoutModel(1, "Daily Deals"));
        layoutsList.add(new MultiLayoutModel(1, "Trending"));
        layoutsList.add(new MultiLayoutModel(0, "Banner 2"));
        layoutsList.add(new MultiLayoutModel(1, "Men's Wear"));


        rvMultiLayout.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));
        MultiLayoutAdapter multiLayoutAdapter = new MultiLayoutAdapter(layoutsList);
        rvMultiLayout.setAdapter(multiLayoutAdapter);

        return view;
    }
}