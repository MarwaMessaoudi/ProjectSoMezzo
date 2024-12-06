package isetb.mobileelite.login.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import isetb.mobileelite.login.Model.Appel;
import isetb.mobileelite.login.R;
import isetb.mobileelite.login.UpdateCallActivity;
import isetb.mobileelite.login.Utils.AppelService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppelAdapter extends RecyclerView.Adapter<AppelAdapter.AppelViewHolder> {

    private List<Appel> appelList;
    private AppelService callService;

    // Constructor
    public AppelAdapter(List<Appel> appelList, AppelService callService) {
        this.appelList = appelList != null ? appelList : new ArrayList<>();
        this.callService = callService;
    }

    @Override
    public AppelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appel, parent, false);
        return new AppelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppelViewHolder holder, int position) {
        Appel appel = appelList.get(position);
        holder.bind(appel);

        // Edit icon click listener
        holder.edit_icon.setOnClickListener(v -> updateAppel(appel, v));

        // Delete icon click listener
        holder.delete_icon.setOnClickListener(v -> showDeleteConfirmationDialog(appel, position, v.getContext()));
    }

    private void showDeleteConfirmationDialog(Appel appel, int position, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Call")
                .setMessage("Are you sure you want to delete this call?")
                .setPositiveButton("Yes", (dialog, which) -> deleteCall(appel, position, context))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteCall(Appel appel, int position, Context context) {
        callService.deleteCall(appel.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // On success, remove the item from the list and notify the adapter
                    appelList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Call deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete call. Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateAppel(Appel appel, View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UpdateCallActivity.class);
        intent.putExtra("CALL_ID", appel.getId());  // Pass the ID for the update
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return appelList.size();
    }

    // Method to update the list after adding, removing, or modifying items
    public void updateList(List<Appel> newAppelList) {
        this.appelList = newAppelList != null ? newAppelList : new ArrayList<>();
        notifyDataSetChanged();  // Notify that the data set has changed
    }

    public static class AppelViewHolder extends RecyclerView.ViewHolder {

        TextView call_id, date, duration, description;
        ImageView edit_icon, delete_icon;

        public AppelViewHolder(View itemView) {
            super(itemView);
            // Initialize all views
            call_id = itemView.findViewById(R.id.call_id);
            date = itemView.findViewById(R.id.date);
            duration = itemView.findViewById(R.id.duration);
            description = itemView.findViewById(R.id.description);
            edit_icon = itemView.findViewById(R.id.edit_icon);
            delete_icon = itemView.findViewById(R.id.delete_icon);
        }

        // Bind data to views in the ViewHolder
        public void bind(Appel appel) {
            call_id.setText(String.valueOf(appel.getId()));  // Set call ID
            date.setText(appel.getDate());  // Set call date
            duration.setText(String.valueOf(appel.getDuration()));  // Set call duration
            description.setText(appel.getDescription());  // Set call description
        }
    }
}
