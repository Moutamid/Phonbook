package dev.moutamid.sampoorankranti;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        utils = new Utils();
        firebaseAuth = FirebaseAuth.getInstance();

        if (checkCurrentActivity()) {
            return;
        }

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState, 0);
    }

    private boolean checkCurrentActivity() {

        //current_activity
        //main
        //second
        //dashboard

        if (utils.getStoredString(DashboardActivity.this, "current_activity").equals("Error")) {

            finish();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        }

        utils.storeString(DashboardActivity.this, "current_activity", "dashboard");

        if (utils.getStoredString(DashboardActivity.this, "current_activity").equals("main")) {

            finish();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        }

        if (utils.getStoredString(DashboardActivity.this, "current_activity").equals("second")) {

            finish();
            Intent intent = new Intent(DashboardActivity.this, SecondRegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return false;
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid())
                .child("isEmailVerified")
                .setValue(firebaseAuth.getCurrentUser().isEmailVerified());

        setHeaderDetails();
    }

    private void setHeaderDetails() {

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        View header = navigationView.getHeaderView(0);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //private SwitchCompat urduSwitch;
        TextView nav_name = (TextView) header.findViewById(R.id.nav_header_email_id);
        TextView nav_emailstatus = (TextView) header.findViewById(R.id.nav_header_email_status_id);

//        nav_name.setText(firebaseAuth.getCurrentUser().getEmail());
        nav_name.setText(utils.getStoredString(DashboardActivity.this, "userEmail"));
        if (firebaseAuth.getCurrentUser().isEmailVerified())
            nav_emailstatus.setText("Verified!");
        else nav_emailstatus.setText("Not verified!");

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
                showLogoutDialog();
                break;
        }
        return true;
    }

    private void showLogoutDialog() {

        utils.showDialog(DashboardActivity.this,
                "Are you sure!",
                "You really want to logout?",
                "Yes",
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // LOGOUT
                        firebaseAuth.signOut();
                        utils.removeSharedPref(DashboardActivity.this);

                        finish();
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        Toast.makeText(DashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                },
                true
        );
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