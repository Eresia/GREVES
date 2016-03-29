package me.lepesant.greves.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import me.lepesant.greves.network.exception.ClientNotConnectedException;
import ucp.greves.network.DataInformations;

/**
 * Created by Bastien on 28/03/2016.
 */
public class Client {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    boolean isConnected;

    private static Client instance = new Client();

    public Client(){
        socket = null;
        input = null;
        output = null;
        isConnected = false;
    }

    public void beginConnexion(String ip, int port) throws IOException{
        InetSocketAddress server = new InetSocketAddress(InetAddress.getByName(ip), port);
        socket = new Socket();
        socket.connect(server, 100);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        isConnected = true;
    }

    public ArrayList<String> getStationList() throws IOException, ClassNotFoundException, ClientNotConnectedException {
        if(!isConnected) {
            throw new ClientNotConnectedException();
        }
        output.writeObject(new String("list"));
        return (ArrayList<String>) input.readObject();
    }

    public ArrayList<DataInformations> getStationData(String station) throws IOException, ClassNotFoundException, ClientNotConnectedException{
        if(!isConnected) {
            throw new ClientNotConnectedException();
        }
        output.writeObject(new String("station:" + station));
        ArrayList<DataInformations> informations = (ArrayList<DataInformations>) input.readObject();
        return informations;
    }

    public void close(){
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isConnected = false;
    }

    public static Client getInstance(){
        return instance;
    }
}
