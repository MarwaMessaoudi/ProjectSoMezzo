package isetb.mobileelite.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import isetb.mobileelite.login.Model.Appel;
import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.Utils.AppelService;
import isetb.mobileelite.login.Utils.Client;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCallActivity extends AppCompatActivity {

    private EditText dateEditText, durationEditText, descriptionEditText;
    private Button updateButton;
    private long callId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_call);

        // Initialize UI components
        dateEditText = findViewById(R.id.dateEditText);
        durationEditText = findViewById(R.id.durationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        updateButton = findViewById(R.id.updateButton);

        // Retrieve passed data
        callId = getIntent().getLongExtra("CALL_ID", -1);

        // Fetch the call details to prefill the form
        if (callId != -1) {
            fetchCallDetails(callId);
        } else {
            Toast.makeText(this, "Invalid call ID. Unable to fetch details.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up DatePicker for date input
        dateEditText.setOnClickListener(v -> showDatePicker());

        // Set the update button's click listener
        updateButton.setOnClickListener(v -> validateAndUpdateCall());
    }

    private void fetchCallDetails(long id) {
        // Create Retrofit service
        AppelService appelService = Apis.getAppelService(this); // Use Apis to get the service

        Call<Appel> call = appelService.getCallById(id);

        call.enqueue(new Callback<Appel>() {
            @Override
            public void onResponse(Call<Appel> call, Response<Appel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Appel appel = response.body();
                    populateFormFields(appel);
                } else {
                    Toast.makeText(UpdateCallActivity.this, "Failed to fetch call details.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Appel> call, Throwable t) {
                Toast.makeText(UpdateCallActivity.this, "Error fetching call details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateFormFields(Appel appel) {
        dateEditText.setText(appel.getDate());
        durationEditText.setText(String.valueOf(appel.getDuration()));
        descriptionEditText.setText(appel.getDescription());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateCallActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    dateEditText.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void validateAndUpdateCall() {
        String updatedDate = dateEditText.getText().toString().trim();
        String durationText = durationEditText.getText().toString().trim();
        String updatedDescription = descriptionEditText.getText().toString().trim();

        // Validate inputs
        if (updatedDate.isEmpty() || durationText.isEmpty() || updatedDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int updatedDuration;
        try {
            updatedDuration = Integer.parseInt(durationText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Duration must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an updated Appel object
        Appel updatedAppel = new Appel(callId, updatedDate, updatedDuration, updatedDescription);

        // Send the update request
        sendUpdateRequest(updatedAppel);
    }

    private void sendUpdateRequest(Appel updatedAppel) {
        AppelService appelService = Apis.getAppelService(this); // Use Apis to get the service
        Call<Appel> call = appelService.updateCall(callId, updatedAppel);

        call.enqueue(new Callback<Appel>() {
            @Override
            public void onResponse(Call<Appel> call, Response<Appel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateCallActivity.this, "Call updated successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("UPDATED_CALL", response.body()); // Pass the updated call object
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the activity// Close the activity and return to the previous screen
                } else {
                    Toast.makeText(UpdateCallActivity.this, "Failed to update call. Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appel> call, Throwable t) {
                Toast.makeText(UpdateCallActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}