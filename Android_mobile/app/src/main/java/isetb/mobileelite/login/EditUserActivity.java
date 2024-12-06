package isetb.mobileelite.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import isetb.mobileelite.login.Model.User;
import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.Utils.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    private EditText firstNameEdit, lastNameEdit, emailEdit;
    private Button saveButton;
    private UserService userService;
    private User user; // Class-level user variable for reuse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Initialize views
        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        saveButton = findViewById(R.id.saveButton);

        // Initialize the service (assuming Retrofit has been set up in your app)
        userService = Apis.getService(this);

        // Get the user data passed from the previous activity (via intent)
        user = (User) getIntent().getSerializableExtra("USER");
        if (user == null) {
            Log.e("EditUserActivity", "No User data received!");
            Toast.makeText(this, "No user data provided!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d("EditUserActivity", "Received User ID: " + user.getId());
        }


        populateFields(user);

        // Set the save button's onClickListener
        saveButton.setOnClickListener(v -> {
            // Validate inputs before proceeding
            if (!validateInputs()) {
                return; // If validation fails, do not proceed
            }

            // Update the user object with new values
            user.setFirst_name(firstNameEdit.getText().toString().trim());
            user.setLast_name(lastNameEdit.getText().toString().trim());
            user.setEmail(emailEdit.getText().toString().trim());

            // Call the service to update the user
            updateUser(user);
        });
    }

    /**
     * Populate the EditText fields with user data.
     *
     * @param user The user whose data is being edited.
     */
    private void populateFields(User user) {
        firstNameEdit.setText(user.getFirst_name());
        lastNameEdit.setText(user.getLast_name());
        emailEdit.setText(user.getEmail());
    }

    /**
     * Validate the inputs to ensure they are not empty.
     *
     * @return True if inputs are valid, false otherwise.
     */
    private boolean validateInputs() {
        if (TextUtils.isEmpty(firstNameEdit.getText().toString().trim())) {
            firstNameEdit.setError("First name is required!");
            return false;
        }
        if (TextUtils.isEmpty(lastNameEdit.getText().toString().trim())) {
            lastNameEdit.setError("Last name is required!");
            return false;
        }
        if (TextUtils.isEmpty(emailEdit.getText().toString().trim())) {
            emailEdit.setError("Email is required!");
            return false;
        }
        return true;
    }

    /**
     * Update the user using the Retrofit service.
     *
     * @param user The user to update.
     */
    private void updateUser(User user) {
        userService.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Show a success message
                    Toast.makeText(EditUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();

                    // Return the updated user data to the previous activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("UPDATED_USER", user);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    // Handle server-side errors
                    Toast.makeText(EditUserActivity.this, "Failed to update user. Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network or other failures
                Toast.makeText(EditUserActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
