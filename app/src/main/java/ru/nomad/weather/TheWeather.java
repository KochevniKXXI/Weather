package ru.nomad.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TheWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_weather);

        final TextView textView = findViewById(R.id.current_city);
        final TextView wind = findViewById(R.id.wind);
        final TextView humidity = findViewById(R.id.humidity);
        final TextView pressure = findViewById(R.id.pressure);
        final TextView water = findViewById(R.id.water);
        
        textView.setText(getIntent().getExtras().getString(FirstSettings.CITY));
        
        if (!getIntent().getExtras().getBoolean(FirstSettings.CHECK_WIND)) {
            wind.setVisibility(View.GONE);
        }
        if (!getIntent().getExtras().getBoolean(FirstSettings.CHECK_HUMIDITY)) {
            humidity.setVisibility(View.GONE);
        }
        if (!getIntent().getExtras().getBoolean(FirstSettings.CHECK_PRESSURE)) {
            pressure.setVisibility(View.GONE);
        }
        if (!getIntent().getExtras().getBoolean(FirstSettings.CHECK_WATER)) {
            water.setVisibility(View.GONE);
        }
    }
}