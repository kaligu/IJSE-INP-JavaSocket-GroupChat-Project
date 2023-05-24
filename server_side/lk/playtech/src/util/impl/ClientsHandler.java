/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 7:11 PM
*/
package util.impl;

import util.ClientObserver;
import util.ClientsObservable;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 7:11 PM
 */
public class ClientsHandler implements ClientsObservable {



    public static ArrayList<ClientObserver> clientObserverArrayList = new ArrayList<>();

    public ClientsHandler() {

    }


    @Override
    public void addClient(ClientObserver clientObserver) {
        clientObserverArrayList.add(clientObserver);
        broadcastMsgsToClients(clientObserver.getClientUsername() + "Joined Chat...!" , clientObserver.getClientUsername());

        String message;
        while (clientObserver.getSocket().isConnected()) {
            try {
                message = clientObserver.getBufferedReader().readLine();
                this.broadcastMsgsToClients(message,clientObserver.getClientUsername());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void removeClient(ClientObserver clientObserver) {
        clientObserverArrayList.remove(clientObserver);
        broadcastMsgsToClients(clientObserver.getClientUsername()+ " has left from chat...!", clientObserver.getClientUsername());
    }

    @Override
    public void broadcastMsgsToClients(String msg, String senderUsername) {
        Thread thread = new Thread(() -> {
            System.out.println("Sending Message : "+msg);
            System.out.println(clientObserverArrayList.size());
            try {
                for (ClientObserver clientObserverf : clientObserverArrayList) {
                    if(!clientObserverf.getClientUsername().equals(senderUsername)){
                        System.out.println(clientObserverf.toString());
                        clientObserverf.getBufferedWriter().write(msg);
                        clientObserverf.getBufferedWriter().newLine();
                        clientObserverf.getBufferedWriter().flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }



}
