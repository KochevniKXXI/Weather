package ru.nomad.weather;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;

import ru.nomad.weather.model.Connection;
import ru.nomad.weather.model.WeatherRequest;

public class ListCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    interface OnCardCityClickListener {
        void onCardClick(String city, int position);
    }

    private final OnCardCityClickListener onClickListener;
    private final HashSet<String> cities;
    private final Iterator<String> iterator;
    private Resources resources;
    private Context context;

    public ListCityAdapter(HashSet<String> cities, OnCardCityClickListener onClickListener) {
        this.cities = cities;
        iterator = cities.iterator();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvi_card_city, parent, false);
        resources = parent.getResources();
        context = parent.getContext();
        return new CardCity(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CardCity) holder).city.setText(iterator.next());

        try {
            Connection connection = new Connection(((CardCity) holder).city.getText().toString());

            new Thread(() -> {
                try {
                    final WeatherRequest weatherRequest = connection.getWeatherRequest();
                    connection.getHandler().post(() -> {
                        ((CardCity) holder).cardHistory.setBackgroundResource(resources.getIdentifier(String.format("bc_%sd", weatherRequest.getWeather()[0].getIcon().substring(0, 2)), "drawable", context.getPackageName()));
                        ((CardCity) holder).temperature.setText(resources.getString(R.string.temperature, Math.round(weatherRequest.getMain().getTemp())));
                        ((CardCity) holder).imageWeather.setImageResource(resources.getIdentifier(String.format("ic_%s", weatherRequest.getWeather()[0].getIcon()), "drawable", context.getPackageName()));
                    });
                } catch (IOException e) {
//                    connection.getHandler().post(() -> {
//                        working.setVisibility(View.GONE);
//                        error.setVisibility(View.VISIBLE);
//                    });
//                    Log.e(TAG, "Fail connection!", e);
                    e.printStackTrace();
                } finally {
                    connection.closeConnection();
                }
            }).start();
        } catch (MalformedURLException e) {
//            working.setVisibility(View.GONE);
//            error.setVisibility(View.VISIBLE);
//            Log.e(TAG, "Fail URI!", e);
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> onClickListener.onCardClick(((CardCity) holder).city.getText().toString(), holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class CardCity extends RecyclerView.ViewHolder {

        LinearLayout cardHistory;
        TextView city;
        TextView temperature;
        ImageView imageWeather;

        public CardCity(@NonNull View itemView) {
            super(itemView);
            cardHistory = itemView.findViewById(R.id.card_history);
            city = itemView.findViewById(R.id.city);
            temperature = itemView.findViewById(R.id.temperature);
            imageWeather = itemView.findViewById(R.id.image_weather);
        }


    }
}
