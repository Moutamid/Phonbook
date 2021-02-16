package dev.moutamid.sampoorankranti;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState, 0);
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Dashboard");
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        //urduSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.nav_urdu_id).getActionView();

        findViewById(R.id.notificationbtndashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, NotoficationActivity.class));
            }
        });

        findViewById(R.id.menubtndashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void setHeaderDetails(String name) {
        View header = navigationView.getHeaderView(0);

        //private SwitchCompat urduSwitch;
//        TextView nav_name = (TextView) header.findViewById(R.id.nav_header_email_id);
//        TextView nav_phone_number = (TextView) header.findViewById(R.id.nav_header_email_status_id);

//        String number = utils.getStoredString(MainActivity.this, USER_EMAIL);
//        String name = utils.getStoredString(MainActivity.this, USER_NUMBER);

//        nav_phone_number.setText(number.substring(0, 11));
//        nav_name.setText(name);

    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new HomeFragment())
                        .commit();
                toolbar.setTitle("Dashboard");
                closeDrawer();
                break;
            case R.id.nav_logout_id:
                closeDrawer();
//                showLogoutDialog();
                break;
        }
        return true;
    }

    /**
     * Checks if the navigation drawer is open - if so, close it
     */
    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}