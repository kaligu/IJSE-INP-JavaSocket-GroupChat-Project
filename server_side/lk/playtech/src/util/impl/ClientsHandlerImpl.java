/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 7:11 PM
*/
package util.impl;

import util.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 7:11 PM
 */
public class ClientsHandlerImpl implements ClientHandler {

    public static ArrayList<ClientsHandlerImpl> clientsHandlerImplArrayList = new ArrayList<>(); //clients holding array using client connections as observers

    private String userName;  //socket's username
    private DataOutputStream dataOutputStream; //socket output stream
    private DataInputStream dataInputStream; //socket input stream

    public ClientsHandlerImpl() {}

    public void listentoClientsMsgsImgs() {

        Thread thread = new Thread(() -> {
            String message;
            while (true) { //if connected
            try {
                message = this.dataInputStream.readUTF();

                if(message.contains("image")){
                    long fileSize = this.dataInputStream.readLong();

                    String fileName = "client_side/lk/playtech/resources/Shared-Img-Location/Shared-Image.png";
                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);

                    // Receive and save the file data
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;

                    while (totalBytesRead < fileSize && (bytesRead = this.dataInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                    }

                    fileOutputStream.close();
                    broadcastImagesToClients(this.userName);

                }else{
                    broadcastMsgsToClients(message,this.userName);
                }

            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        });
        thread.start();
    }

    @Override
    public void addClient(Socket socket) {
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.userName = dataInputStream.readUTF();
            clientsHandlerImplArrayList.add(this);
            broadcastMsgsToClients(this.userName + "Joined to Chat..." , this.userName); //send to all clients
            listentoClientsMsgsImgs(); //start to listen all clients
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(ClientsHandlerImpl clientsHandlerImpl) {
        clientsHandlerImplArrayList.remove(clientsHandlerImpl);  //remove from array
        broadcastMsgsToClients(clientsHandlerImpl.userName+ " has left from chat...!", clientsHandlerImpl.userName);
    }

    public void broadcastMsgsToClients(String msg, String senderUsername) {
        try {
            for (ClientsHandlerImpl clientsHandlerImpl : clientsHandlerImplArrayList) {   //get all clients to send message
                if(!clientsHandlerImpl.userName.equals(senderUsername)){
                    clientsHandlerImpl.dataOutputStream.writeUTF(msg);
                    clientsHandlerImpl.dataOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcastImagesToClients(String senderUsername) {
        try {
            for (ClientsHandlerImpl clientsHandlerImpl : clientsHandlerImplArrayList) {   //get all clients to send message
                if(!clientsHandlerImpl.userName.equals(senderUsername)){
                    clientsHandlerImpl.dataOutputStream.writeUTF(senderUsername+" : Image recieved"); //send client to inform send msg="Image" to inform UI
                    clientsHandlerImpl.dataOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
