package me.lepesant.greves.station;

import android.widget.TextView;

import ucp.greves.network.DataInformations;

/**
 * Created by Bastien on 29/03/2016.
 */
public class RefreshView implements Runnable{

    private final int i;
    private final DataInformations data;
    private final TextView[][] timeTextView, directionTextView;

    public RefreshView(int i, DataInformations data, TextView[][] timeTextView, TextView[][] directionTextView){
        this.i = i;
        this.data = data;
        this.timeTextView = timeTextView;
        this.directionTextView = directionTextView;
    }

    public void run(){
        for (int j = 0; j < 3; j++) {
            if (j >= data.getTrains().size()) {
                timeTextView[i][j].setText("---------");
                directionTextView[i][j].setText("---------");
            } else {
                timeTextView[i][j].setText(data.getTimes().get(j));
                directionTextView[i][j].setText(data.getLastStations().get(j));
            }
        }
    }
}
