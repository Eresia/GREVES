package ucp.greves.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;

import ucp.greves.controller.GodModeController;
import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.time.UndefinedTimeException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.NextTrainInformations;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;
import ucp.greves.network.exception.BadNetworkInformationException;

public class ClientManagement extends Thread{

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	
	public ClientManagement(Socket socket){
		this.socket = socket;
		in = null;
		out = null;
	}
	
	@Override
	public void run(){
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			
			try{
				while(socket.isConnected() && !GodModeController.simulationStopped()){
					
					String message = (String) in.readObject();
					if(message == null){
						throw new BadNetworkInformationException("No message");
					}
					String data[] = message.split(":");
					//System.out.println("Message envoy� : " + message);
					
					switch(data[0]){
						case "list":
							ArrayList<String> stationNames = StationController.StringlistOfGlobalStationsName();
							out.writeObject(stationNames);
							break;
						case "station":
							if(data.length != 2){
								throw new BadNetworkInformationException("Bad number of information exception : " + data.length);
							}
							ArrayList<DataInformations> result = new ArrayList<DataInformations>();
							GlobalStation station = StationController.getGlobalStationByName(data[1]);
							if(station == null){
								throw new BadNetworkInformationException("Station " + data[1] + " don't exist");
							}
							for(Integer i : station.getStations()){
								Station s = StationController.getStationByCantonId(i);
								ArrayList<Integer> trains = new ArrayList<Integer>();
								ArrayList<String> times = new ArrayList<String>();
								ArrayList<String> lastStations = new ArrayList<String>();
								ArrayList<NextTrainInformations> nextTrains = s.getNextTrains(3);
								for(NextTrainInformations info : nextTrains){
									Train train = TrainController.getRunningTrainById(info.getId());
									trains.add(info.getId());
									try {
										times.add(info.getTime().getString());
									} catch (UndefinedTimeException e) {
										times.add("Unkwow time");
									}
									Integer last = train.getRoadMap().getLastStation();
									lastStations.add(StationController.getStationByCantonId(last).getName());
								}
								result.add(new DataInformations(trains, times, lastStations));
							}
							out.writeObject(result);
							break;
					}
				}
			} catch(BadNetworkInformationException e){
				e.printStackTrace();
			} catch(AsynchronousCloseException e){
				
			} catch (TrainNotExistException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (IOException | ClassNotFoundException e) {
			
		}
		
		try {
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void close() throws IOException{
		if(socket.isConnected()){
			if(in != null){
				in.close();
			}
			
			if(out != null){
				out.close();
			}
			
			if(socket != null){
				socket.close();
			}
		}
	}
}
