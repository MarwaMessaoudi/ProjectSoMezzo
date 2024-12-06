package isetb.mobileelite.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView firstNameTextView, lastNameTextView, roleTextView,emailTextView;
    TextView headerNameTextView, headerEmailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set up ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                // Handle "Edit My Account"
            } else if (itemId == R.id.nav_logout) {
                // Handle Logout
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (itemId == R.id.nav_add_account) {
                Intent intent = new Intent(DashboardActivity.this, adduser.class);
                startActivity(intent);
                // Handle "Add Account"
            } else if (itemId == R.id.nav_my_calls) {
                Intent intent = new Intent(DashboardActivity.this, AppelActivity.class);
                startActivity(intent);
                // Handle "My Calls"
            }
            else if (itemId == R.id.nav_account_list) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                // Handle "Account List"
            }
            drawerLayout.closeDrawers();
            return true;
        });
        // Retrieve user details
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String role = getIntent().getStringExtra("role");

        if (role == null) {
            role = "EMPLOYEE"; // Default role in case of null
        }

        // Update navigation menu
        updateNavigationMenu(role);

        // Display user details
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        roleTextView = findViewById(R.id.roleTextView);

        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        emailTextView.setText(email);
        roleTextView.setText(role);
        View headerView = navigationView.getHeaderView(0);

        headerNameTextView = headerView.findViewById(R.id.headerNameTextView);
        headerEmailTextView = headerView.findViewById(R.id.headerEmailTextView);
        headerNameTextView.setText(firstName + " " + lastName);
        headerEmailTextView.setText(email);}

    private void updateNavigationMenu(String role) {
        Menu menu = navigationView.getMenu();

        // Common items are always visible
        menu.findItem(R.id.nav_profile).setVisible(true);
        menu.findItem(R.id.nav_logout).setVisible(true);

        if ("MANAGER".equals(role)) {
            // Show manager-specific items
            menu.findItem(R.id.nav_add_account).setVisible(true);
            menu.findItem(R.id.nav_account_list).setVisible(true);
            menu.findItem(R.id.nav_account_validation).setVisible(true);

            // Hide employee-specific items
            menu.findItem(R.id.nav_my_calls).setVisible(false);
        } else if ("EMPLOYEE".equals(role)) {
            // Show employee-specific items
            menu.findItem(R.id.nav_my_calls).setVisible(true);

            // Hide manager-specific items
            menu.findItem(R.id.nav_add_account).setVisible(false);
            menu.findItem(R.id.nav_account_list).setVisible(false);
            menu.findItem(R.id.nav_account_validation).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
