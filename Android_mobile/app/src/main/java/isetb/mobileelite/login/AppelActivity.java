package isetb.mobileelite.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import isetb.mobileelite.login.Model.Appel;
import isetb.mobileelite.login.Utils.Apis;
import isetb.mobileelite.login.Utils.AppelService;
import isetb.mobileelite.login.Utils.Client;
import isetb.mobileelite.login.Adapter.AppelAdapter;
import isetb.mobileelite.login.Utils.TokenHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppelActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppelAdapter appelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appel);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnAddCall = findViewById(R.id.btnAddCall);
        btnAddCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Add Call activity
                Intent intent = new Intent(AppelActivity.this, AddCallActivity.class);
                startActivity(intent);
            }
        });
        // Fetch calls using Retrofit
        fetchCalls();
    }

    private void fetchCalls() {
        // Retrieve the access token securely from SharedPreferences (or another secure storage)
        String accessToken = "Bearer " + TokenHelper.getAccessToken(this); // Securely fetch the token

        // If the access token is not available, prompt the user to log in again
        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "Access token not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return; // Prevent fetching calls without a valid token
        }

        // Create the Retrofit instance and call the AppelService
        AppelService appelService = Apis.getAppelService(this); // Use Apis to get the service
        Call<List<Appel>> call = appelService.getAllCalls(accessToken);

        call.enqueue(new Callback<List<Appel>>() {
            @Override
            public void onResponse(Call<List<Appel>> call, Response<List<Appel>> response) {
                if (response.isSuccessful()) {
                    List<Appel> appelList = response.body();
                    if (appelList != null && !appelList.isEmpty()) {
                        appelAdapter = new AppelAdapter(appelList, appelService);
                        recyclerView.setAdapter(appelAdapter);
                    } else {
                        Toast.makeText(AppelActivity.this, "No calls found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<List<Appel>> call, Throwable t) {
                Toast.makeText(AppelActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Handle error responses (e.g., 401 Unauthorized for expired tokens)
    private void handleErrorResponse(Response<List<Appel>> response) {
        int statusCode = response.code();
        if (statusCode == 401) {
            // Token might be expired, handle refresh logic or prompt user to log in again
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            // You can redirect to a login activity or refresh the token here
            // Intent loginIntent = new Intent(AppelActivity.this, LoginActivity.class);
            // startActivity(loginIntent);
        } else {
            Toast.makeText(this, "Failed to load calls. Error code: " + statusCode, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                // Check if a new call or an updated call is passed back
                if (data.hasExtra("NEW_CALL") || data.hasExtra("UPDATED_CALL")) {
                    // Fetch calls again to get the updated list
                    fetchCalls();
                }
            }
        }
    }

}