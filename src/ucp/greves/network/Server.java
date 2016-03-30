package ucp.greves.network;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import ucp.greves.controller.GodModeController;

public class Server extends Thread{
	
	private int port;
	ArrayList<ClientManagement> clients;
	
	public Server(int port){
		this.port = port;
		clients = new ArrayList<ClientManagement>();
	}
	
	@Override
	public void run(){
		try {
		    ServerSocketChannel server = ServerSocketChannel.open();

		    server.socket().bind(new InetSocketAddress(port));
		    server.configureBlocking(false);
		    try {
				while(!GodModeController.simulationStopped()){
					SocketChannel client = server.accept();
					if(client != null){
						ClientManagement cm = new ClientManagement(client.socket());
						clients.add(cm);
						cm.start();
					}
					else{
						Thread.sleep(100);
					}
				}
		    }catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		    for(ClientManagement cm : clients){
		    	cm.close();
		    }
			server.close();
		} catch(BindException e){
			System.err.println("Another server is already launch");
			System.err.println("The serveur is stopping now");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
