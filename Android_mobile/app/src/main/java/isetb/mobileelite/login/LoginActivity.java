package isetb.mobileelite.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.listener.ApiResponseListener;
import isetb.mobileelite.login.Model.LoginRequest;
import isetb.mobileelite.login.Model.LoginResponse;
import isetb.mobileelite.login.Utils.TokenHelper;
import isetb.mobileelite.login.Utils.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private UserService userService;
    private EditText emailField, passwordField;
    private CheckBox rememberMeCheckbox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UserService using Apis
        userService = Apis.getService(this);

        // Initialize UI elements
        emailField = findViewById(R.id.emailEditText);
        passwordField = findViewById(R.id.passwordEditText);
        rememberMeCheckbox = findViewById(R.id.rememberMe);
        loginButton = findViewById(R.id.btn);


        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            boolean rememberMe = rememberMeCheckbox.isChecked();

            if (!email.isEmpty() && !password.isEmpty()) {
                // Call the login method
                login(email, password, rememberMe, new ApiResponseListener<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse response) {
                        handleLoginSuccess(response, rememberMe);
                    }
                    @Override
                    public void onIsnotActive(String activestatus){
                        Toast.makeText(LoginActivity.this, activestatus, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String email, String password, boolean rememberMe, ApiResponseListener<LoginResponse> listener) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call the login API
        userService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String accessToken = loginResponse.getAccessToken();

                    // Extract isActive from the token
                    boolean isActive = TokenHelper.extractIsActive(accessToken);
                    // Check if the user is active
                    if (isActive) {
                        runOnUiThread(() -> listener.onIsnotActive("Your account is not activated yet. Please contact the administration."));
                    } else {
                        listener.onSuccess(loginResponse);
                    }
                } else {
                    runOnUiThread(() ->  listener.onError("Login failed: Invalid credentials or server error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    private void handleLoginSuccess(LoginResponse response, boolean rememberMe) {
        String accessToken = response.getAccessToken();
        String refreshToken = response.getRefreshToken();

        // Save tokens in SharedPreferences
        TokenHelper.saveTokens(this, accessToken, refreshToken);

        // Optionally save the rememberMe flag
        TokenHelper.saveRememberMe(this, rememberMe);

        // Get user information from the response
        String firstName = response.getUser().getFirst_name();
        String lastName = response.getUser().getLast_name();
        String email = response.getUser().getEmail();
        String role = response.getUser().getRole().name();  // Convert role to String

        Log.d("LoginActivity", "Role: " + role);  // Log role for debugging
        if (response.getUser().getRole() == null) {
            Log.e("LoginActivity", "User role is null");
        } else {
            Log.d("LoginActivity", "User role: " + response.getUser().getRole());
        }

        // Pass user information to DashboardActivity
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        intent.putExtra("role", role);  // Pass role as String
        Toast.makeText(LoginActivity.this, "Role being sent: " + role, Toast.LENGTH_SHORT).show();

        startActivity(intent);
        finish(); // Close LoginActivity
    }

}
