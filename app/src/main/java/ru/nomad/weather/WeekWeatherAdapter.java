package ru.nomad.weather;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class WeekWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String[] daysOfWeek = new String[7];

    public WeekWeatherAdapter() {
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i] = LocalDate.now().getDayOfWeek().plus(i + 1).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }
    }

    @NonNull
    @Override
    public DayWeather onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_day_weather, parent, false);
        return new DayWeather(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DayWeather) holder).day.setText(daysOfWeek[position]);
        ((DayWeather) holder).temperature.setText("+21°C/+13°C");
    }

    @Override
    public int getItemCount() {
        return daysOfWeek.length;
    }

    static class DayWeather extends RecyclerView.ViewHolder {

        TextView day;
        TextView temperature;

        public DayWeather(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            temperature = itemView.findViewById(R.id.day_temperature);
        }
    }
}
