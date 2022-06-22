package ru.nomad.weather;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    interface OnCardCityClickListener {
        void onCardClick (String city, int position);
    }

    private final OnCardCityClickListener onClickListener;
    private final String[] cities;
    private final TypedArray panoramas;

    public ListCityAdapter(String[] cities, TypedArray panoramas, OnCardCityClickListener onClickListener) {
        this.cities = cities;
        this.panoramas = panoramas;
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
        ((CardCity) holder).city.setText(cities[position]);
        ((CardCity) holder).panorama.setImageResource(panoramas.getResourceId(position, -1));
//        ((CardCity) holder).panorama.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        ((CardCity) holder).panorama.setBackgroundResource(R.drawable.ic_msc);

        holder.itemView.setOnClickListener(v -> onClickListener.onCardClick(cities[holder.getAdapterPosition()], holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return cities.length;
    }

    static class CardCity extends RecyclerView.ViewHolder {

        TextView city;
        ImageView panorama;

        public CardCity(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
            panorama = itemView.findViewById(R.id.panorama);
        }


    }
}
