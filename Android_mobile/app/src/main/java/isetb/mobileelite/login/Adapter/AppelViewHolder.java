package isetb.mobileelite.login.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import isetb.mobileelite.login.Model.Appel;
import isetb.mobileelite.login.R;

public class AppelViewHolder extends RecyclerView.ViewHolder {

    TextView call_id, date, duration, description;
    ImageView edit_icon, delete_icon;

    public AppelViewHolder(@NonNull View itemView) {
        super(itemView);
        call_id = itemView.findViewById(R.id.call_id);
        date = itemView.findViewById(R.id.date);
        duration = itemView.findViewById(R.id.duration);
        description = itemView.findViewById(R.id.description);
        edit_icon = itemView.findViewById(R.id.edit_icon);
        delete_icon = itemView.findViewById(R.id.delete_icon);
    }

    public void bind(Appel appel) {
        call_id.setText(String.valueOf(appel.getId()));
        date.setText(appel.getDate());
        duration.setText(String.valueOf(appel.getDuration()));
        description.setText(appel.getDescription());
    }
}

