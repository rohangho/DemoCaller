package com.example.democaller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democaller.R;
import com.example.democaller.model.DisplayModel;

import java.util.List;


public class PhoneDisplayerAdapter extends RecyclerView.Adapter<PhoneDisplayerAdapter.MyViewHolder> {

    List<DisplayModel> allContact;

    public PhoneDisplayerAdapter(List<DisplayModel> allContact) {
        this.allContact = allContact;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_container, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.contactNumber.setText(allContact.get(position).getNumber());
        holder.contactName.setText(allContact.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return allContact.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.name);
            contactNumber = itemView.findViewById(R.id.number);
        }
    }
}
