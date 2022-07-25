package ru.nomad.weather;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Фрагменты
    private HistoryFragment historyFragment;
    // Окружающая (либо текущего местоположения, либо последнего запрошенного города)
    // температура и влажность
    private TextView ambientTemperature;
    private TextView relativeHumidity;
    // Датчики
    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    //
    private BatteryReceiver batteryReceiver;
    // "Слушатели" датчиков
    private final SensorEventListener temperatureListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            ambientTemperature.setText(String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener humidityListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            relativeHumidity.setText(String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNotificationChannel();

        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initFab();

        initFragments();

        initSensors();
        initViews();
    }

    // инициализация канала нотификаций
    private void initNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel("2", "name",
                importance);
        notificationManager.createNotificationChannel(channel);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFab() {
        FloatingActionButton inputCity = findViewById(R.id.input_city);
        inputCity.setOnClickListener(v -> {
            InputCityBottomSheetDialogFragment inputCityDialog = InputCityBottomSheetDialogFragment.newInstance();
            inputCityDialog.show(getSupportFragmentManager(), "input_city_dialog");
        });
    }

    private void initFragments() {
        historyFragment = HistoryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.city_selection, historyFragment).commit();
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    private void initViews() {
        ambientTemperature = findViewById(R.id.ambient_temperature);
        relativeHumidity = findViewById(R.id.relative_humidity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorTemperature != null) {
            sensorManager.registerListener(temperatureListener, sensorTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            ambientTemperature.setText("+21°C");
        }
        if (sensorHumidity != null) {
            sensorManager.registerListener(humidityListener, sensorHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            relativeHumidity.setText("13%");
        }
        batteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(temperatureListener, sensorTemperature);
        sensorManager.unregisterListener(humidityListener, sensorTemperature);
        unregisterReceiver(batteryReceiver);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}