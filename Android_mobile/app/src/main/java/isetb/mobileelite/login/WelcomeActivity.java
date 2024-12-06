package isetb.mobileelite.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {
    TextView welcomeMessage;
    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Handle system bars insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        welcomeMessage = findViewById(R.id.welcomeMessage);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Retrieve first name from Intent
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        if (firstName != null) {
            welcomeMessage.setText("Bonjour " + firstName + "!");
        }

        // Set click listeners for buttons
        btnSignIn.setOnClickListener(v -> {
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        btnSignUp.setOnClickListener(v -> {
            Intent registrationIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(registrationIntent);
        });
    }
}
