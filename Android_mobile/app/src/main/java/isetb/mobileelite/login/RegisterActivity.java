package isetb.mobileelite.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import isetb.mobileelite.login.Model.Role;
import isetb.mobileelite.login.Model.User;
import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.Utils.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, birthDateInput, passwordInput, confirmPasswordInput;
    private Spinner roleSpinner;
    private Button registerButton;

    // Format de date standard
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");
        setContentView(R.layout.activity_register); // Ensure the layout file matches

        // Initialisation des éléments de l'interface utilisateur
        initUI();

        // Gestionnaire de clic pour le champ de date
        birthDateInput.setOnClickListener(v -> showDatePicker());

        // Gestionnaire de clic pour le bouton d'enregistrement
        registerButton.setOnClickListener(v -> validateForm());
    }

    private void initUI() {
        try {
            firstNameInput = findViewById(R.id.firstNameInput);
            lastNameInput = findViewById(R.id.lastNameInput);
            emailInput = findViewById(R.id.emailInput);
            birthDateInput = findViewById(R.id.birthDateInput);
            passwordInput = findViewById(R.id.passwordInput);
            confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
            registerButton = findViewById(R.id.registerButton);
            roleSpinner = findViewById(R.id.roleSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"EMPLOYEE", "CONTROLLER", "MANAGER"}
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roleSpinner.setAdapter(adapter);

            Log.d(TAG, "UI initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing UI elements", e);
            throw new RuntimeException("Error initializing UI", e); // Crash explicitly if a view is missing
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    birthDateInput.setText(date);
                    Log.d(TAG, "Selected date: " + date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateForm() {
        Log.d(TAG, "validateForm started");

        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String birthDateString = birthDateInput.getText().toString().trim();
        String roleString = (roleSpinner.getSelectedItem() != null) ? roleSpinner.getSelectedItem().toString() : "";
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        StringBuilder errors = new StringBuilder();

        // Validation des champs
        if (firstName.isEmpty()) errors.append("First name is required.\n");
        if (lastName.isEmpty()) errors.append("Last name is required.\n");
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.append("Valid email is required.\n");
        }
        if (birthDateString.isEmpty()) errors.append("Birth date is required.\n");
        if (password.isEmpty() || password.length() < 6) {
            errors.append("Password must be at least 6 characters long.\n");
        }
        if (!password.equals(confirmPassword)) {
            errors.append("Passwords do not match.\n");
        }

        Role role;
        try {
            role = Role.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            errors.append("Invalid role selected.\n");
            role = null;
        }

        Date birthDate = null;
        if (!birthDateString.isEmpty()) {
            try {
                birthDate = dateFormat.parse(birthDateString);
            } catch (ParseException e) {
                errors.append("Invalid birth date format.\n");
            }
        }

        if (errors.length() == 0) {
            // Création de l'utilisateur
            User user = new User(firstName, lastName, email, role, password, false);
            user.setBirthDate(birthDate);
            Log.d(TAG, "User object created: " + user);
            registerUser(user);
        } else {
            Log.e(TAG, "Validation errors: " + errors);
            Toast.makeText(this, errors.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(User user) {
        UserService userApi = Apis.getService(this);
        Log.d(TAG, "API service initialized");

        Call<Void> call = userApi.addUser(user);
        Log.d(TAG, "API call prepared for user: " + user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User registered successfully");
                    Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    navigateToWelcome(user);
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToWelcome(User user) {
        Log.d(TAG, "Navigating to welcome screen for user: " + user.getFirst_name());
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("FIRST_NAME", user.getFirst_name());
        startActivity(intent);
    }
    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
