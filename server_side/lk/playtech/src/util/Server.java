/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 6:04 PM
*/
package util;

import javafx.scene.control.Alert;
import util.impl.ClientConnecrion;
import util.impl.ClientsHandler;

import java.io.IOException;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 6:04 PM
 */
public class Server {
    private java.net.ServerSocket serverSocket;
    ClientsObservable clientsObservable; //create clients holding ClientsObservable interface instance


    public Server() {}

    public void initializeServerSocket(int port){
        try {

            clientsObservable = new ClientsHandler();  //assign implementation to initialize clientobservable

            this.serverSocket = new java.net.ServerSocket(port);

        } catch (IOException e) {
            System.out.println("this port already used");
        }
    }

    public void openServerSocket(){
        while (!(serverSocket.isClosed())) {   //if serversocket opened can accept
            try {
                Socket socket = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    clientsObservable.addClient(new ClientConnecrion(socket));  //request holding socket initialize as connection and it assign to clienthandler
                });
                thread.start();

            } catch (IOException e) {
                closeServerSocket();  //if exception received close serversocket
            }
        }
    }

    public void closeServerSocket(){
        try {
            if( serverSocket!=null){  //if server socket not null

                this.serverSocket.close(); //close server socket

            }
        } catch (IOException e) {
            System.out.println("server socket closing error!");
        }
    }



}
