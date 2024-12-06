package isetb.mobileelite.login.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import isetb.mobileelite.login.Model.User;
import isetb.mobileelite.login.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private final TextView ed2, ed3, ed4, ed5, ed6;
    private final Button edit, delete;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        ed2 = itemView.findViewById(R.id.e2); // First Name
        ed3 = itemView.findViewById(R.id.e3); // Last Name
        ed4 = itemView.findViewById(R.id.e4); // Email
        ed5 = itemView.findViewById(R.id.e5); // Birth Date
        ed6 = itemView.findViewById(R.id.e6); // Role
        edit = itemView.findViewById(R.id.edit); // Edit Button
        delete = itemView.findViewById(R.id.delete); // Delete Button
    }

    public void bind(User user) {
        // Check if user is not null
        if (user == null) {
            return; // Prevent null pointer if user is not initialized properly
        }

        // First Name
        if (user.getFirst_name() != null) {
            ed2.setText(user.getFirst_name());
        } else {
            ed2.setText(""); // Set an empty string if the first name is null
        }

        // Last Name
        if (user.getLast_name() != null) {
            ed3.setText(user.getLast_name());
        } else {
            ed3.setText(""); // Set an empty string if the last name is null
        }

        // Email
        if (user.getEmail() != null) {
            ed4.setText(user.getEmail());
        } else {
            ed4.setText(""); // Set an empty string if email is null
        }

        // Birth Date: Format Date using SimpleDateFormat
        if (user.getBirthDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sdf.format(user.getBirthDate()); // Format Date object
            ed5.setText(formattedDate);
        } else {
            ed5.setText(""); // Set an empty string if birth date is null
        }

        // Role: Assuming Role is an Enum, use toString() or name() method
        if (user.getRole() != null) {
            ed6.setText(user.getRole().toString()); // or user.getRole().name()
        } else {
            ed6.setText(""); // Set an empty string if role is null
        }
    }



    public Button getEditButton() {
        return edit;
    }

    public Button getDeleteButton() {
        return delete;
    }
}
