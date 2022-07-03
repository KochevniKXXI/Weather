package ru.nomad.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        HashSet<String> listCity = History.getInstance().getListCity();
        Iterator<String> iterator = listCity.iterator();
        LinearLayout layout = findViewById(R.id.llMain);
        while (iterator.hasNext()) {
            TextView textView = new TextView(this);
            textView.setText(iterator.next());
            layout.addView(textView);
        }
    }
}