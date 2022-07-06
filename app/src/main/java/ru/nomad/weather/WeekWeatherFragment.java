package ru.nomad.weather;

import static ru.nomad.weather.WeatherFragment.SETTINGS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WeekWeatherFragment extends Fragment {

    public static WeekWeatherFragment newInstance(Settings settings) {
        WeekWeatherFragment fragment = new WeekWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(SETTINGS, settings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fWeekWeather = inflater.inflate(R.layout.fragment_week_weather, container, false);
        RecyclerView weekWeather = fWeekWeather.findViewById(R.id.week_weather);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        weekWeather.setLayoutManager(layoutManager);
        WeekWeatherAdapter adapter = new WeekWeatherAdapter();
        weekWeather.setAdapter(adapter);

        return fWeekWeather;
    }
}