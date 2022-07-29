package com.naztec.storesy.Custom;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.naztec.storesy.Models.CategoryModel;

import java.util.ArrayList;
import java.util.Objects;

public class DBQueries {

    /**
     * To Store Category Names
     *
     * @implSpec categoryName(String), iconUrl(String)
     * @implNote Values Inserted from fetchCategories()
     */
    public static ArrayList<CategoryModel> categories = new ArrayList<>();


    /**
     * Interface to see if the given task is completed successfully or not
     * using interface because FirebaseQuery can't return any value
     *
     * @implSpec used in fetchCategories()
     */
    public interface TaskResult {
        void isTaskCompleted(boolean taskResult);
    }

    /**
     * To Get the Categories from Database
     *
     * @param context    to make the use of Context whenever needed
     * @param taskResult Interface to return True whenever a category is fetched from DB
     * @implSpec Invoked from HomeFragment onCreateView
     */
    public static void fetchCategories(Context context, TaskResult taskResult) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("CATEGORIES").orderBy("index").get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CategoryModel categoryData = new CategoryModel(document.getId(),
                                    document.getString("url"));
                            categories.add(categoryData);
                            taskResult.isTaskCompleted(true);
                        }
                    } else {
                        Toast.makeText(context, "Categories Error : " +
                                        Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
