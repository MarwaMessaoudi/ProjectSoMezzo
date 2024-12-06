package isetb.mobileelite.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import isetb.mobileelite.login.Adapter.UserAdapter;
import isetb.mobileelite.login.Model.User;
import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.Utils.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int EDIT_USER_REQUEST = 1; // Request code for editing user
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button add, logoutButton;
    private List<User> list = new ArrayList<>();
    TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logoutButton = findViewById(R.id.logoutButton);

        // Logout button click listener
        logoutButton.setOnClickListener(v -> logout());

        // Initialize views
        recyclerView = findViewById(R.id.recycler);
        add = findViewById(R.id.add1);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(list, this::onEditUser, this::onDeleteUser); // Add edit & delete callbacks
        recyclerView.setAdapter(userAdapter);

        // Add button functionality
        add.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, adduser.class);
            startActivity(i);
        });

        // Fetch users
        fetchUsers();
    }

    private void fetchUsers() {
        UserService userService = Apis.getService(this);
        Call<List<User>> call = userService.getAllUser();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.body() != null) {
                    list.clear();
                    list.addAll(response.body());
                    userAdapter.notifyDataSetChanged(); // Notify adapter of changes
                } else {
                    Toast.makeText(MainActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Error fetching users", t);
                Toast.makeText(MainActivity.this, "Error fetching users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Callback for editing a user
    private void onEditUser(User user) {
        Log.d("MainActivity", "Editing User ID: " + user.getId());
        Intent intent = new Intent(MainActivity.this, EditUserActivity.class);
        intent.putExtra("USER", user);  // Pass the user to the EditUserActivity
        startActivityForResult(intent, EDIT_USER_REQUEST);  // Start the activity for result
    }

    // Callback for deleting a user
    private void onDeleteUser(User user, int position) {
        // Initialize UserService
        UserService userService = Apis.getService(this);

        // Make the DELETE request
        Call<Void> call = userService.deleteUser(user.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // On success, remove the user from the list and notify the adapter
                    Toast.makeText(MainActivity.this, "User deleted successfully!", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    userAdapter.notifyItemRemoved(position);
                } else {
                    // Handle error
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error deleting user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure (network or other issues)
                Toast.makeText(MainActivity.this, "Failed to delete user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: ", t);
            }
        });
    }


    private void logout() {
        // Clear stored tokens or user session
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Clear all stored values
        editor.apply();

        // Navigate back to the LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);

        // Optionally show a toast
        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  // Call super method
        if (requestCode == EDIT_USER_REQUEST && resultCode == RESULT_OK && data != null) {
            User updatedUser = (User) data.getSerializableExtra("UPDATED_USER");
            if (updatedUser != null) {
                // Find and update the user in the list
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == updatedUser.getId()) {
                        list.set(i, updatedUser);  // Replace the old user with the updated user
                        userAdapter.notifyItemChanged(i);  // Notify the adapter that the item has changed
                        break;
                    }
                }
            }
        }
    }

}
