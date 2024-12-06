package isetb.mobileelite.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class adduser extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, birthDateInput, passwordInput;
    private Spinner roleSpinner;
    private Button AddButton;

    // Format de date standard
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // updated date format
    private static final String TAG = "Adduser";

    // Gson instance for serializing dates
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Format de date pour LocalDate
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");
        setContentView(R.layout.activity_adduser); // Ensure the layout file matches

        // Initialisation des éléments de l'interface utilisateur
        initUI();

        // Gestionnaire de clic pour le champ de date
        birthDateInput.setOnClickListener(v -> showDatePicker());

        // Gestionnaire de clic pour le bouton d'enregistrement
        AddButton.setOnClickListener(v -> validateForm());
    }

    private void initUI() {
        try {
            firstNameInput = findViewById(R.id.edfirstname);
            lastNameInput = findViewById(R.id.edlastname);
            emailInput = findViewById(R.id.idEmail);
            birthDateInput = findViewById(R.id.birthdate);
            passwordInput = findViewById(R.id.idpass);
            AddButton = findViewById(R.id.bouton);
            roleSpinner = findViewById(R.id.roleSpinner1);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"EMPLOYEE", "CONTROLLER", "MANAGER"});
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
                    // Format the date as yyyy-MM-dd
                    String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
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
                // Parse the birth date using the format yyyy-MM-dd
                birthDate = dateFormat.parse(birthDateString);
            } catch (ParseException e) {
                errors.append("Invalid birth date format. Please use yyyy-MM-dd.\n");
            }
        }

        if (errors.length() == 0) {
            // Création de l'utilisateur
            User user = new User(firstName, lastName, email, role, password, false);
            user.setBirthDate(birthDate);
            Log.d(TAG, "User object created: " + user);
            AddUser(user);
        } else {
            Log.e(TAG, "Validation errors: " + errors);
            Toast.makeText(this, errors.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void AddUser(User user) {
        UserService userApi = Apis.getService(this);
        Log.d(TAG, "API service initialized");

        // Serialize the user object using Gson with the custom date format
        String jsonUser = gson.toJson(user);
        Log.d(TAG, "Serialized user: " + jsonUser);

        Call<Void> call = userApi.addUserWithApprouval(user);
        Log.d(TAG, "API call prepared for user: " + user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User registered successfully");
                    Toast.makeText(adduser.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    navigateToWelcome(user);
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    Toast.makeText(adduser.this, "Registration failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                Toast.makeText(adduser.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToWelcome(User user) {
        Log.d(TAG, "Navigating to welcome screen for user: " + user.getFirst_name());
        Intent intent = new Intent(adduser.this, MainActivity.class);
        intent.putExtra("FIRST_NAME", user.getFirst_name());
        startActivity(intent);
    }
}
