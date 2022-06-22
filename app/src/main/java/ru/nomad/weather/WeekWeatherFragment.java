package ru.nomad.weather;

import static ru.nomad.weather.WeatherFragment.SETTINGS;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekWeatherFragment extends Fragment {

    public WeekWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param settings Parameter 1.
     * @return A new instance of fragment WeekWeatherFragment.
     */
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