package me.lepesant.greves.station;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.lepesant.greves.R;
import me.lepesant.greves.main.Main;
import me.lepesant.greves.network.Client;
import ucp.greves.network.DataInformations;
import me.lepesant.greves.network.exception.ClientNotConnectedException;

public class StationInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_information);
        Intent intent = getIntent();
        String station = intent.getStringExtra("StationName");
        TextView stationNameText = (TextView) findViewById(R.id.stationName);
        stationNameText.setText("Gare de " + station);
        try {
            RefreshStation refresh = new RefreshStation(station, this);
            new Thread(refresh).start();
        } catch (NoSuchFieldException e) {
            Intent intentMain = new Intent(StationInformation.this, Main.class);
            intentMain.putExtra("Message", "Problem with server connexion");
            e.printStackTrace();
            startActivity(intentMain);
        } catch (IllegalAccessException e) {
            Intent intentMain = new Intent(StationInformation.this, Main.class);
            intentMain.putExtra("Message", "Problem with server connexion");
            e.printStackTrace();
            startActivity(intentMain);
        }
    }



}
