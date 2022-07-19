package ru.nomad.weather;

import static ru.nomad.weather.InputCityBottomSheetDialogFragment.INPUT_CITY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class HistoryFragment extends Fragment {

    private boolean isLandscape;
    private HashSet<String> queryCity;
    private LinkedHashSet<String> visitedCities;
    private RecyclerView listCity;
    ListCityAdapter adapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fHistory = inflater.inflate(R.layout.fragment_history, container, false);
        listCity = fHistory.findViewById(R.id.list_city);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listCity.setLayoutManager(layoutManager);
        ListCityAdapter.OnCardCityClickListener cardCityClickListener = (city, position) -> {
            History.getInstance().removeCity(city);
            showWeather(city);
        };
//        if (queryCity == null) {
            adapter = new ListCityAdapter(visitedCities, cardCityClickListener);
//        } else {
//            adapter = new ListCityAdapter(queryCity, cardCityClickListener);
//        }
//        listCity.setAdapter(adapter);
        setHasOptionsMenu(true);
        return fHistory;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.setCities(visitedCities);
        listCity.setAdapter(adapter);

        // Определение, можно ли будет расположить рядом прогноз в другом фрагменте
        View weatherFrame = getActivity().findViewById(R.id.weather_forecast);
        // getActivity - получить контекст activity, где расположен фрагмент
        isLandscape = weatherFrame != null && weatherFrame.getVisibility() == View.VISIBLE;
        // Если можно отобразить рядом прогноз, то сделаем это
        if (isLandscape) {
            showWeather(visitedCities.toArray()[visitedCities.size() - 1].toString());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
        MenuItem search = menu.findItem(R.id.app_bar_search);
        SearchView searchCity = (SearchView) search.getActionView();
        searchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    Iterator<String> iterator = visitedCities.iterator();
                    queryCity = new HashSet<>();
                    while (iterator.hasNext()) {
                        String string = iterator.next();
                        if (string.startsWith(query)) {
                            queryCity.add(string);
                        }
                    }
                } else {
                    queryCity = null;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void showWeather(String selectedCity) {
        if (isLandscape) {
// Проверим, что фрагмент с прогнозом существует в activity
            WeatherFragment weather = (WeatherFragment) getParentFragmentManager().findFragmentById(R.id.weather_forecast);
// если есть необходимость, то выведем прогноз
            if (weather == null || !weather.getCity().equals(selectedCity)) {
// Создаем новый фрагмент с текущей позицией для вывода прогноза
                weather = WeatherFragment.newInstance(selectedCity);
// Выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.weather_forecast, weather); // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), WeatherActivity.class);
            intent.putExtra(INPUT_CITY, selectedCity);
            startActivity(intent);
        }
    }
}