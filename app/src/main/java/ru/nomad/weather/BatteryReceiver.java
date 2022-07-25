package ru.nomad.weather;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.core.app.NotificationCompat;

public class BatteryReceiver extends BroadcastReceiver {
    private int messageId = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_sun_weather_round)
                .setContentTitle("Weather");
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            builder.setContentText("Низкий уровень заряда батареи!");
        } else if (intent.getAction().equals(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
            builder.setContentText("Нет сети!");
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}