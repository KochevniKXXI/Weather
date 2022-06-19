package ru.nomad.weather;

import static ru.nomad.weather.TheWeatherFragment.PARCEL;

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

    private Parcel parcel;

    public WeekWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parcel Parameter 1.
     * @return A new instance of fragment WeekWeatherFragment.
     */
    public static WeekWeatherFragment newInstance(Parcel parcel) {
        WeekWeatherFragment fragment = new WeekWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            parcel = (Parcel) savedInstanceState.getSerializable(PARCEL);
        } else {
            parcel = new Parcel("", false, false, false, false);
        }
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