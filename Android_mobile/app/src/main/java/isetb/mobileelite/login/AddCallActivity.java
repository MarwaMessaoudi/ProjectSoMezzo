package isetb.mobileelite.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import isetb.mobileelite.login.Model.Appel;
import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.Utils.AppelService;
import isetb.mobileelite.login.Utils.Client;
import isetb.mobileelite.login.Utils.TokenHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCallActivity extends AppCompatActivity {

    private EditText etDate, etDuration, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_call);

        etDate = findViewById(R.id.etDate);
        etDuration = findViewById(R.id.etDuration);
        etDescription = findViewById(R.id.etDescription);

        // Set the click listener for the Date EditText to show the DatePickerDialog
        etDate.setOnClickListener(v -> showDatePickerDialog());

        findViewById(R.id.btnSaveCall).setOnClickListener(v -> addCall());
    }

    // Method to show the DatePicker dialog
    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddCallActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set the selected date to the EditText (etDate)
                    etDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void addCall() {
        // Get input values
        String date = etDate.getText().toString();
        String durationStr = etDuration.getText().toString();
        String description = etDescription.getText().toString();

        if (date.isEmpty() || durationStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert duration to int (you can add error handling here if necessary)
        int duration = Integer.parseInt(durationStr);

        // Create an Appel object with the required parameters
        long id = 0; // Or set a default or generate a unique ID if necessary
        Appel newCall = new Appel(id, date, duration, description);

        // Send the data to the server
        String accessToken = "Bearer " + TokenHelper.getAccessToken(this);
        AppelService appelService = Apis.getAppelService(this); // Use Apis to get the service
        Call<Appel> call = appelService.addCall(accessToken, newCall);

        call.enqueue(new Callback<Appel>() {
            @Override
            public void onResponse(Call<Appel> call, Response<Appel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddCallActivity.this, "Call added successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NEW_CALL", response.body()); // Pass the new call object
                    setResult(RESULT_OK, resultIntent);
                    finish();  // Close the activity  // Close the activity after adding the call
                } else {
                    Toast.makeText(AddCallActivity.this, "Failed to add call", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appel> call, Throwable t) {
                Toast.makeText(AddCallActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
