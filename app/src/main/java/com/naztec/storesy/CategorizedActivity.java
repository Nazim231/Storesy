package com.naztec.storesy;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.Adapters.MultiLayoutAdapter;
import com.naztec.storesy.Custom.DBQueries;

import java.util.ArrayList;


public class CategorizedActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvMultiLayout;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized);

        toolbar = findViewById(R.id.categorized_toolbar);
        setSupportActionBar(toolbar);
        ActionBar appBar = getSupportActionBar();
        assert appBar != null;
        appBar.setDisplayHomeAsUpEnabled(true);
        String categoryName = getIntent().getStringExtra("categoryName");
        appBar.setTitle(categoryName);

        rvMultiLayout = findViewById(R.id.rv_categorized_multi_layout);
        rvMultiLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // Category Data (Multi Layout)
        if (DBQueries.loadedCategories.contains(categoryName)) {
            int index = DBQueries.loadedCategories.indexOf(categoryName);
            MultiLayoutAdapter adapter = new MultiLayoutAdapter(DBQueries.sectionsData.get(index));
            rvMultiLayout.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            DBQueries.loadedCategories.add(categoryName);
            int index = DBQueries.loadedCategories.indexOf(categoryName);
            DBQueries.sectionsData.add(index, new ArrayList<>());
            MultiLayoutAdapter adapter = new MultiLayoutAdapter(DBQueries.sectionsData.get(index));
            rvMultiLayout.setAdapter(adapter);
            DBQueries.fetchSectionData(this, index, categoryName, taskResult ->
                    adapter.notifyDataSetChanged()
            );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}