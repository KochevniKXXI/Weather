package ru.nomad.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nomad.weather.model.WeatherRequest;

public class ListCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    interface OnCardCityClickListener {
        void onCardClick(String city, int position);
    }

    private final OnCardCityClickListener onClickListener;
    private String[] cities;
    private Resources resources;
    private Context context;

    public ListCityAdapter(LinkedHashSet<String> cities, OnCardCityClickListener onClickListener) {
        this.cities = cities.toArray(new String[0]);
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
        ((CardCity) holder).city.setText(cities[getItemCount() - 1 - position]);
        Connection.getInstance().getOpenWeather().loadWeather(((CardCity) holder).city.getText().toString() + ", RU", BuildConfig.WEATHER_API_KEY,"metric", "ru")
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            ((CardCity) holder).cardHistory.setBackgroundResource(resources.getIdentifier(String.format("bc_%sd", response.body().getWeather()[0].getIcon().substring(0, 2)), "drawable", context.getPackageName()));
                            ((CardCity) holder).temperature.setText(resources.getString(R.string.temperature, Math.round(response.body().getMain().getTemp())));
                            ((CardCity) holder).imageWeather.setImageResource(resources.getIdentifier(String.format("ic_%s", response.body().getWeather()[0].getIcon()), "drawable", context.getPackageName()));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {

                    }
                });

        holder.itemView.setOnClickListener(v -> onClickListener.onCardClick(((CardCity) holder).city.getText().toString(), holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return cities.length;
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

    public void setCities(LinkedHashSet<String> cities) {
        this.cities = cities.toArray(new String[0]);
    }
}
