package com.naztec.storesy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.naztec.storesy.Custom.DBQueries;
import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.Fragments.AccountFragment;
import com.naztec.storesy.Fragments.CartFragment;
import com.naztec.storesy.Fragments.HomeFragment;
import com.naztec.storesy.Fragments.OrdersFragment;
import com.naztec.storesy.Fragments.WishlistFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navView;
    FrameLayout frameLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.main_frame_layout);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);
        loadFragment(new HomeFragment());

        // Setting Data to the Navigation Header View
        View view = navView.getHeaderView(0);
        TextView personName = view.findViewById(R.id.header_user_name);
        personName.setText(UserAuthentications.userData.getFirstName());
        TextView personEmail = view.findViewById(R.id.header_user_email);
        personEmail.setText(UserAuthentications.userData.getEmail());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_cart:
                loadFragment(new CartFragment());
                break;
            case R.id.nav_wishlist:
                loadFragment(new WishlistFragment());
                break;
            case R.id.nav_orders:
                loadFragment(new OrdersFragment());
                break;
            case R.id.nav_account:
                loadFragment(new AccountFragment());
                break;
            default:
                loadFragment(new HomeFragment());
                break;

        }

        return true;

    }

    /**
     * Function to Set Fragment in the MainActivity FrameLayout
     *
     * @param fragment Fragment Object that needs to be shown to the user
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, fragment)
                .commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

}