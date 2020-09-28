package com.daffodil.assignment.ui.map.adapter;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodil.assignment.R;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private List<Address> addresses;
    private Context context;

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PlacesViewHolder(inflater.inflate(R.layout.item_place, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {

        if(addresses != null && addresses.size() > position){

            holder.address_tv.setText(addresses.get(position).getAddressLine(addresses.get(position).getMaxAddressLineIndex()));

        }


    }

    @Override
    public int getItemCount() {
        return addresses != null ? addresses.size() : 0;
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder {

        TextView address_tv, lat_lng_tv;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);

            address_tv = itemView.findViewById(R.id.address_tv);
            lat_lng_tv = itemView.findViewById(R.id.lat_lng_tv);
        }
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
