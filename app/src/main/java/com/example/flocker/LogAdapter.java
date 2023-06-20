package com.example.flocker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private List<log.logItem> dataList;

    public LogAdapter(List<log.logItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        log.logItem data = dataList.get(position);
        holder.locNumTextView.setText(String.valueOf(data.locNum));

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedTime = outputFormat.format(data.openTime);
        holder.openTimeTextView.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locNumTextView;
        public TextView openTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            locNumTextView = itemView.findViewById(R.id.locNumTextView);
            openTimeTextView = itemView.findViewById(R.id.openTimeTextView);
        }
    }

}
