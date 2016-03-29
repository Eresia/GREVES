package me.lepesant.greves.network.exception;

/**
 * Created by Bastien on 28/03/2016.
 */
public class ClientNotConnectedException extends Exception{

    public ClientNotConnectedException(){
        super();
    }

    public ClientNotConnectedException(String s){
        super(s);
    }
}
