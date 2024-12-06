package isetb.mobileelite.login.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import isetb.mobileelite.login.Model.User;
import isetb.mobileelite.login.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private final OnEditClickListener editClickListener;
    private final OnDeleteClickListener deleteClickListener;

    // Interface for edit button click
    public interface OnEditClickListener {
        void onEdit(User user);
    }

    // Interface for delete button click
    public interface OnDeleteClickListener {
        void onDelete(User user, int position);
    }

    // Constructor for the adapter
    public UserAdapter(List<User> userList, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        this.userList = userList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Get the current user
        User user = userList.get(position);

        holder.id.setText(user.getId().toString());
        holder.userfName.setText(user.getFirst_name());
        holder.userlName.setText(user.getLast_name());
        holder.emailTextView.setText(user.getEmail()); // Make sure email is shown
        // Get the birth date and format it
        Date date = user.getBirthDate(); // Assuming the birthDate is a Date object
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (date != null) {
            holder.birthDateTextView.setText(sdf.format(date));
        } else {
            holder.birthDateTextView.setText("No date available");
        }
        holder.roleTextView.setText(user.getRole().toString()); // Display role as string

        // Set the click listeners for the buttons
        holder.editButton.setOnClickListener(v -> editClickListener.onEdit(user));
        holder.deleteButton.setOnClickListener(v -> deleteClickListener.onDelete(user, position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userfName, userlName, emailTextView, roleTextView, id, birthDateTextView;
        Button editButton, deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            id = itemView.findViewById(R.id.e2);
            userfName = itemView.findViewById(R.id.e3); // First name
            userlName = itemView.findViewById(R.id.e4); // Last name
            emailTextView = itemView.findViewById(R.id.e6);
            roleTextView = itemView.findViewById(R.id.e7);
            birthDateTextView = itemView.findViewById(R.id.e5);
            editButton = itemView.findViewById(R.id.edit); // Edit button
            deleteButton = itemView.findViewById(R.id.delete); // Delete button
        }
    }
}
