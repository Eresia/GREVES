package me.lepesant.greves.station;

import android.content.Intent;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import me.lepesant.greves.R;
import me.lepesant.greves.main.Main;
import me.lepesant.greves.network.Client;
import me.lepesant.greves.network.exception.ClientNotConnectedException;
import ucp.greves.network.DataInformations;

/**
 * Created by Bastien on 28/03/2016.
 */
public class RefreshStation implements Runnable{

    private String stationName;
    private StationInformation activity;

    private TextView[][] timeTextView;
    private TextView[][] directionTextView;
    private boolean isStopped;

    public RefreshStation(String stationName, StationInformation activity) throws NoSuchFieldException, IllegalAccessException{
        this.stationName = stationName;
        this.activity = activity;

        timeTextView = new TextView[2][3];
        directionTextView = new TextView[2][3];
        Class res = R.id.class;
        for(int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Field idTime = res.getField("Axe" + ((int) (i + 1)) + "Train" + ((int) (j + 1)) + "Time");
                Field idDirection = res.getField("Axe" + ((int) (i + 1)) + "Train" + ((int) (j + 1)) + "Direction");
                timeTextView[i][j] = (TextView) activity.findViewById(idTime.getInt(null));
                directionTextView[i][j] = (TextView) activity.findViewById(idDirection.getInt(null));
            }
        }
        isStopped = false;
    }

    @Override
    public void run(){
        Client client = Client.getInstance();
        try {
            while(!isStopped) {
                ArrayList<DataInformations> informations = client.getStationData(stationName);
                for (int i = 0; i < 2; i++) {
                    DataInformations data = informations.get(i);
                    RefreshView refresh = new RefreshView(i, data, timeTextView, directionTextView);
                    activity.runOnUiThread(refresh);

                }
                Thread.sleep(1000);
            }
            client.close();
            Intent intentMain = new Intent(activity, Main.class);
            intentMain.putExtra("Message", "Connexion ended");
            activity.startActivity(intentMain);

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            Intent intentMain = new Intent(activity, Main.class);
            intentMain.putExtra("Message", "Problem with server connexion");
            activity.startActivity(intentMain);
        } catch (ClientNotConnectedException e){
            Intent intentMain = new Intent(activity, Main.class);
            intentMain.putExtra("Message", "Problem with server connexion");
            activity.startActivity(intentMain);
        }
    }

    public void stopNetwork(){
        isStopped = true;
    }
}
