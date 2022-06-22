package ru.nomad.weather;

import static ru.nomad.weather.WeatherFragment.SETTINGS;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstSettingsFragment extends Fragment {

    private Settings currentSettings;
    private boolean isEnough;

    Button button;
    EditText editText;
    CheckBox[] checkBoxes = new CheckBox[4];

    public FirstSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param settings Parameter 1.
     * @return A new instance of fragment FirstSettingsFragment.
     */
    public static FirstSettingsFragment newInstance(Settings settings) {
        FirstSettingsFragment fragment = new FirstSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(SETTINGS, settings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentSettings = (Settings) savedInstanceState.getSerializable(SETTINGS);
        } else {
            currentSettings = new Settings("", false, false, false, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_first_settings, container, false);

        button = layout.findViewById(R.id.check_the_weather);
        editText = layout.findViewById(R.id.edit_city);
        checkBoxes[0] = layout.findViewById(R.id.check_wind);
        checkBoxes[1] = layout.findViewById(R.id.check_humidity);
        checkBoxes[2] = layout.findViewById(R.id.check_pressure);
        checkBoxes[3] = layout.findViewById(R.id.check_water);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    button.setEnabled(!s.toString().isEmpty());
                } else {
                    button.setEnabled(false);
                }
            }
        });

        button.setOnClickListener(v -> {
            Settings newSettings = new Settings(editText.getText().toString(),
                    checkBoxes[0].isChecked(),
                    checkBoxes[1].isChecked(),
                    checkBoxes[2].isChecked(),
                    checkBoxes[3].isChecked());
            showWeather(newSettings);
        });

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Определение, можно ли будет расположить рядом прогноз в другом фрагменте
        View weatherFrame = getActivity().findViewById(R.id.weather_forecast);
        // getActivity - получить контекст activity, где расположен фрагмент
        isEnough = weatherFrame != null && weatherFrame.getVisibility() == View.VISIBLE;
        // Если можно нарисовать рядом герб, то сделаем это
        if (isEnough) {
            showWeather(currentSettings);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Settings newSettings = new Settings(editText.getText().toString(),
                checkBoxes[0].isChecked(),
                checkBoxes[1].isChecked(),
                checkBoxes[2].isChecked(),
                checkBoxes[3].isChecked());
        outState.putSerializable(SETTINGS, newSettings);
    }

    private void showWeather(Settings settings) {
        if (isEnough) {
// Выделим текущий элемент списка
// Проверим, что фрагмент с гербом существует в activity
            WeatherFragment weather = (WeatherFragment) getFragmentManager().findFragmentById(R.id.weather_forecast);
// если есть необходимость, то выведем герб
            if (weather == null || !weather.getParcel().equals(settings)) {
// Создаем новый фрагмент с текущей позицией для вывода герба
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