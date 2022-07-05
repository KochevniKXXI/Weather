package ru.nomad.weather;

import static ru.nomad.weather.WeatherFragment.SETTINGS;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;

public class HistoryFragment extends Fragment {

    private Settings currentSettings;
    private boolean isLandscape;
    private HashSet<String> visitedCities;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        visitedCities = History.getInstance().getListCity();
        if (savedInstanceState != null) {
            currentSettings = (Settings) savedInstanceState.getSerializable(SETTINGS);
        } else {
            currentSettings = new Settings("", true, true, true, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fHistory = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView listCity = fHistory.findViewById(R.id.list_city);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listCity.setLayoutManager(layoutManager);
        ListCityAdapter.OnCardCityClickListener cardCityClickListener = (city, position) -> {
            currentSettings = new Settings(city, true, true, true, true);
            showWeather(currentSettings);
        };
        ListCityAdapter adapter = new ListCityAdapter(visitedCities, cardCityClickListener);
        listCity.setAdapter(adapter);
        return fHistory;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Определение, можно ли будет расположить рядом прогноз в другом фрагменте
        View weatherFrame = getActivity().findViewById(R.id.weather_forecast);
        // getActivity - получить контекст activity, где расположен фрагмент
        isLandscape = weatherFrame != null && weatherFrame.getVisibility() == View.VISIBLE;
        // Если можно отобразить рядом прогноз, то сделаем это
        if (isLandscape) {
            showWeather(currentSettings);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SETTINGS, currentSettings);
    }

    public void showWeather(Settings settings) {
        if (isLandscape) {
// Проверим, что фрагмент с прогнозом существует в activity
            WeatherFragment weather = (WeatherFragment) getFragmentManager().findFragmentById(R.id.weather_forecast);
// если есть необходимость, то выведем прогноз
            if (weather == null || !weather.getSettings().equals(settings)) {
// Создаем новый фрагмент с текущей позицией для вывода прогноза
                weather = WeatherFragment.newInstance(settings);
// Выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.weather_forecast, weather); // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), WeatherActivity.class);
            intent.putExtra(SETTINGS, settings);
            startActivity(intent);
        }
    }
}