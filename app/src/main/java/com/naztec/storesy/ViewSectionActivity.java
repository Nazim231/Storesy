package com.naztec.storesy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.Adapters.ViewSectionItemsAdapter;
import com.naztec.storesy.Custom.DBQueries;
import com.naztec.storesy.Models.ViewSectionItemsModel;

import java.util.ArrayList;

public class ViewSectionActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvViewSectionItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_section);

        // Getting Intent Data
        Intent intent = getIntent();
        String sectionName = intent.getStringExtra("sectionName");
        String categoryName = intent.getStringExtra("categoryName");

        toolbar = findViewById(R.id.view_section_toolbar);
        setSupportActionBar(toolbar);
        ActionBar appBar = getSupportActionBar();
        assert appBar != null;
        appBar.setDisplayHomeAsUpEnabled(true);
        // Setting Toolbar Title
        appBar.setTitle(sectionName);

        // RecyclerView
        rvViewSectionItems = findViewById(R.id.rv_view_section);
        rvViewSectionItems.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        // RecyclerView Data
        ArrayList<ViewSectionItemsModel> itemsData = new ArrayList<>();
        ViewSectionItemsAdapter adapter = new ViewSectionItemsAdapter(itemsData);
        rvViewSectionItems.setAdapter(adapter);

        // Fetching the Section Items
        DBQueries.fetchSectionItems(this, categoryName, sectionName, sectionItems -> {
           itemsData.add(sectionItems);
           adapter.notifyItemChanged(itemsData.size() - 1);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}