package ru.nomad.weather;

import static ru.nomad.weather.WeatherFragment.SETTINGS;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CitySelectionFragment extends Fragment {

    private Settings currentSettings;
    private boolean isLandscape;

    public static CitySelectionFragment newInstance() {
        CitySelectionFragment fragment = new CitySelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentSettings = (Settings) savedInstanceState.getSerializable(SETTINGS);
        } else {
            currentSettings = new Settings("", true, true, true, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fCitySelection = inflater.inflate(R.layout.fragment_city_selection, container, false);
        EditText inputCity = fCitySelection.findViewById(R.id.input_city);
        Button button = fCitySelection.findViewById(R.id.check_weather);
        button.setOnClickListener(v -> {
            currentSettings = new Settings(inputCity.getText().toString(), true, true, true,true);
            History.getInstance().addCity(currentSettings.getCity());
            showWeather(currentSettings);
        });
        return fCitySelection;
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