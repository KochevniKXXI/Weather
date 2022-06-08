package ru.nomad.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class FirstSettings extends AppCompatActivity {

    public static final String CITY = "city";
    public static final String CHECK_WIND = "check_wind";
    public static final String CHECK_HUMIDITY = "check_humidity";
    public static final String CHECK_PRESSURE = "check_pressure";
    public static final String CHECK_WATER = "check_water";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_settings);

        final Button button = findViewById(R.id.check_the_weather);
        final EditText editText = findViewById(R.id.edit_city);
        final CheckBox[] checkBoxes = {findViewById(R.id.check_wind),
                findViewById(R.id.check_humidity),
                findViewById(R.id.check_pressure),
                findViewById(R.id.check_water)
        };

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
            Intent intent = new Intent(this, TheWeather.class);
            intent.putExtra(CITY, editText.getText().toString().trim());
            intent.putExtra(CHECK_WIND, checkBoxes[0].isChecked());
            intent.putExtra(CHECK_HUMIDITY, checkBoxes[1].isChecked());
            intent.putExtra(CHECK_PRESSURE, checkBoxes[2].isChecked());
            intent.putExtra(CHECK_WATER, checkBoxes[3].isChecked());
            startActivity(intent);
        });
    }
}