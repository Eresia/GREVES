package me.lepesant.greves.station;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import me.lepesant.greves.R;
import me.lepesant.greves.main.Main;
import me.lepesant.greves.network.Client;
import me.lepesant.greves.network.exception.ClientNotConnectedException;

public class StationList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        Intent intent = getIntent();
        Client client = Client.getInstance();
        try {
            ArrayList<String> informations = client.getStationList();
            ListAdapter adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, informations);
            ListView view = (ListView) findViewById(R.id.stationListLayout);
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent informationIntent = new Intent(StationList.this, StationInformation.class);
                    informationIntent.putExtra("StationName", (String) parent.getAdapter().getItem(position));
                    startActivity(informationIntent);
                }
            });
            view.setAdapter(adapt);
        } catch (IOException | ClassNotFoundException e) {
            Intent intentMain = new Intent(StationList.this, Main.class);
            intentMain.putExtra("Message", "Problem in station list");
            startActivity(intentMain);
        } catch (ClientNotConnectedException e){
            Intent intentMain = new Intent(StationList.this, Main.class);
            intentMain.putExtra("Message", "Client not connected (station list)");
            startActivity(intentMain);
        }
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Client.getInstance().close();
    }
}
