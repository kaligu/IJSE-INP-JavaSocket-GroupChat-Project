/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 7:11 PM
*/
package util.impl;

import controller.MainFormController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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

    public static ArrayList<ClientObserver> clientObserverArrayList = new ArrayList<>(); //clients holding array using client connections as observers

    public ClientsHandler() {}


    @Override
    public void addClient(ClientObserver clientObserver) {
        clientObserverArrayList.add(clientObserver);  //add to array
        broadcastMsgsToClients(clientObserver.getClientUsername() + "Joined to Chat..." , clientObserver.getClientUsername()); //send to all clients

        boolean isImageRecieving =false;
        String message;
        while (clientObserver.getSocket().isConnected()) { //if connected
            try {
                if(!isImageRecieving){
                    message = clientObserver.getDataInputStream().readUTF();
                    this.broadcastMsgsToClients(message,clientObserver.getClientUsername());
                    if(message.contains("image")){
                        isImageRecieving =true;
                    }else{
                        isImageRecieving = false;
                    }
                }else {
                    this.broadcastMsgsToClients("Image Recieved",clientObserver.getClientUsername());
                    System.out.println("image reciving");
                                            // Read the file size from the client
                long fileSize = clientObserver.getDataInputStream().readLong();
                System.out.println("Received file size: " + fileSize);

                // Discard the received data since the server is not saving it
                // Create a file output stream to save the received file
                String fileName = "pa";
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);

                // Receive and save the file data
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;
                while (totalBytesRead < fileSize && (bytesRead = clientObserver.getDataInputStream().read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }

                File file = new File(fileName);
                broadcastImagesToClients(file,clientObserver.getClientUsername());

                    isImageRecieving=false;
                }
            } catch (IOException e) {
//                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void removeClient(ClientObserver clientObserver) {
        clientObserverArrayList.remove(clientObserver);  //remove from array
        broadcastMsgsToClients(clientObserver.getClientUsername()+ " has left from chat...!", clientObserver.getClientUsername());
    }

    @Override
    public void broadcastMsgsToClients(String msg, String senderUsername) {
        Thread thread = new Thread(() -> {
            try {
                for (ClientObserver clientObserverf : clientObserverArrayList) {   //get all clients to send message
                    if(!clientObserverf.getClientUsername().equals(senderUsername)){
                        clientObserverf.getDataOutputStream().writeUTF(msg);
                        clientObserverf.getDataOutputStream().flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public void broadcastImagesToClients(File file, String senderUsername) {
        Thread thread = new Thread(() -> {
            try {
                String msg = file.getPath();
                // Send the file size and data to each client
                for (ClientObserver clientObserver : clientObserverArrayList) {
                    if (!clientObserver.getClientUsername().equals(senderUsername)) {
                        clientObserver.getDataOutputStream().writeUTF(msg);
                        clientObserver.getDataOutputStream().flush();
                    }
                }
                System.out.println("Image location sent successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
