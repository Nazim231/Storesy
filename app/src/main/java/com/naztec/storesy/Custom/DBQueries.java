package com.naztec.storesy.Custom;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.naztec.storesy.Models.CategoryModel;
import com.naztec.storesy.Models.MultiLayoutModel;
import com.naztec.storesy.Models.ViewSectionItemsModel;

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
     * To Store those Category Names whose Data/Layouts/Sections has been fetched
     *
     * @implNote Values Inserted from HomeFragment, CategorizedActivity
     */
    public static ArrayList<String> loadedCategories = new ArrayList<>();
    public static ArrayList<ArrayList<MultiLayoutModel>> sectionsData = new ArrayList<>();

    /**
     * Interface to see if the given task is completed successfully or not
     * using interface because FirebaseQuery can't return any value
     *
     * @implSpec used in fetchCategories(), fetchSectionData()
     */
    public interface TaskResult {
        void isTaskCompleted(boolean taskResult);
    }

    /**
     * To Get the Categories from Database
     *
     * @param context    to make the use of Context whenever needed
     * @param taskResult Interface to return True whenever a category is fetched from DB
     * @implNote Invoked from HomeFragment onCreateView
     */
    public static void fetchCategories(Context context, TaskResult taskResult) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("CATEGORIES").orderBy("index").get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CategoryModel categoryData = new CategoryModel(document.getId(),
                                    document.getString("url"));
                            /* HomeFragment automatically show the Home Category Data so we don't
                             * need to add Home to the Category Layout */
                            if (!document.getId().equals("Home"))
                                categories.add(categoryData);
                            taskResult.isTaskCompleted(true);
                        }
                    } else {
                        Toast.makeText(context, "Categories Error : " +
                                        Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.v("CategoriesError: ", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });

    }

    /**
     * To Get the Specific Category Related Data
     *
     * @param context       to make the Use of Context whenever needed
     * @param categoryIndex to store the fetched data at this index value in sectionData ArrayList
     * @param categoryName  to get the data inside this specific category
     * @param taskResult    Interface to return True whenever a product is fetched from the specified category
     * @implNote Invoked from HomeFragment, CategorizedActivity
     */
    public static void fetchSectionData(Context context, int categoryIndex, String categoryName, TaskResult taskResult) {
        CollectionReference db = FirebaseFirestore.getInstance().collection("CATEGORIES")
                .document(categoryName).collection("SECTIONS");

        db.orderBy("index", Query.Direction.ASCENDING).get(Source.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    // Section Name
                    String secName = documentSnapshot.getId();
                    int layoutID = Objects.requireNonNull(documentSnapshot.getLong("layoutID"))
                            .intValue();
                    // Fetching Products Under this Section
                    db.document(secName).collection("DATA").get(Source.SERVER)
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    ArrayList<String> products = new ArrayList<>();
                                    for (QueryDocumentSnapshot product : task2.getResult()) {
                                        String prodID = product.getString("prodName");
                                        products.add(prodID);
                                        taskResult.isTaskCompleted(true);
                                    }

                                    sectionsData.get(categoryIndex).add(
                                            new MultiLayoutModel(layoutID, secName, products));
                                }
                            });
                }
            } else {
                Log.v("SECTION_DATA_FAILURE", "categoryIndex: " + categoryIndex +
                        "categoryName: " + categoryName +
                        "Error: " + Objects.requireNonNull(task.getException()).getMessage());
                Toast.makeText(context, "Error : SECTION_DATA_FAILURE", Toast.LENGTH_SHORT)
                        .show();

            }
        });
    }

    public interface SectionItems {
        void setSectionItems(ViewSectionItemsModel productDetails);
    }

    public static void fetchSectionItems(Context context, String categoryName, String sectionName, SectionItems sectionItems) {
        FirebaseFirestore.getInstance().collection("CATEGORIES").document(categoryName)
                .collection("SECTIONS").document(sectionName)
                .collection("DATA").get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot productIDs : task.getResult()) {
                            String prodID = productIDs.getId();
                            // fetching the product required details
                            FirebaseFirestore.getInstance().collection("PRODUCTS")
                                    .document(prodID).get(Source.SERVER)
                                    .addOnSuccessListener(querySnapshot -> {
                                        ViewSectionItemsModel prodDetails = querySnapshot
                                                .toObject(ViewSectionItemsModel.class);
                                        sectionItems.setSectionItems(prodDetails);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FETCH_SEC_PROD", e.getMessage());
                                        Toast.makeText(context, "Error: FETCH_SEC_PROD", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.e("FETCH_SECTION_ITEMS",
                                Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(context, "Error: FETCH_SECTION_ITEMS", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
