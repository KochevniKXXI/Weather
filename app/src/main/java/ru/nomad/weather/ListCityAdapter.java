package ru.nomad.weather;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ListCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    interface OnCardCityClickListener {
        void onCardClick (String city, int position);
    }

    private final OnCardCityClickListener onClickListener;
    private final HashSet<String> cities;
    private final Iterator<String> iterator;

    public ListCityAdapter(HashSet<String> cities, OnCardCityClickListener onClickListener) {
        this.cities = cities;
        iterator = cities.iterator();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvi_card_city, parent, false);
        return new CardCity(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CardCity) holder).city.setText(iterator.next());
//        ((CardCity) holder).panorama.setImageResource(panoramas.getResourceId(position, -1));
//        ((CardCity) holder).panorama.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        ((CardCity) holder).panorama.setBackgroundResource(R.drawable.ic_msc);

        holder.itemView.setOnClickListener(v -> onClickListener.onCardClick(((CardCity) holder).city.getText().toString(), holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class CardCity extends RecyclerView.ViewHolder {

        TextView city;

        public CardCity(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
        }


    }
}
