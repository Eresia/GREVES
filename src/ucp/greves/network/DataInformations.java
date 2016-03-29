package ucp.greves.network;

import java.io.Serializable;
import java.util.ArrayList;

public class DataInformations implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 844684583325858905L;
    private final ArrayList<Integer> trains;
    private final ArrayList<String> times, lastStations;

    public DataInformations(ArrayList<Integer> trains, ArrayList<String> times, ArrayList<String> lastStations){
        this.trains = trains;
        this.times = times;
        this.lastStations = lastStations;
    }

    public ArrayList<Integer> getTrains(){
        return trains;
    }

    public ArrayList<String> getTimes(){
        return times;
    }

    public ArrayList<String> getLastStations(){
        return lastStations;
    }
    
    @Override
    public String toString(){
        return "Trains : " + trains + "\nTimes : " + times + "\nLastStations : " + lastStations;
    }
    
    
}
