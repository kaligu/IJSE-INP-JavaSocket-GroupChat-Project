/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 7:11 PM
*/
package util.impl;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 7:11 PM
 */
public class ClientsHandler{

    public static ArrayList<ClientsHandler> clientsHandlerArrayList = new ArrayList<>(); //clients holding array using client connections as observers
    private String userName;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public ClientsHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.userName = dataInputStream.readUTF();
            clientsHandlerArrayList.add(this);
            broadcastMsgsToClients(this.userName + "Joined to Chat..." , this.userName); //send to all clients
            listeningClientMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listeningClientMsg() {
        Thread thread = new Thread(() -> {
//            String message;
//            while (true) {
//                try {
//                    message = this.dataInputStream.readUTF();
//                    System.out.println("server msg >"+message);
//                    broadcastMsgsToClients(message, userName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }

            boolean isImageRecieving =false;
            String message;
            while (true) { //if connected
            try {
                if(!isImageRecieving){
                    message = this.dataInputStream.readUTF();
                    broadcastMsgsToClients(message,this.userName);
                    if(message.contains("image")){
                        isImageRecieving =true;
                    }else{
                        isImageRecieving = false;
                    }
                }else {

                    broadcastMsgsToClients("a image Recieved!",this.userName);
                    System.out.println("image reciving");

                    long fileSize = this.dataInputStream.readLong();

                    String fileName = "images/Chat-img-location.png";
                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);

                    // Receive and save the file data
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;

                    while (totalBytesRead < fileSize && (bytesRead = this.dataInputStream.read(buffer)) != -1) {
                      fileOutputStream.write(buffer, 0, bytesRead);
                      totalBytesRead += bytesRead;
                    }

                    System.out.println("image recived");
                    File file = new File(fileName);
                    broadcastImagesToClients(file, this.userName);

                    //send


                    isImageRecieving=false;
                }
            } catch (IOException e) {
//                e.printStackTrace();
                break;
            }


        }
        });
        thread.start();
    }

//
//    public void addClient(ClientsHandler clientsHandler) {
//        clientConnectionArrayList.add(clientConnection);  //add to array
//

//        boolean isImageRecieving =false;
//        String message;
//        while (clientObserver.getSocket().isConnected()) { //if connected
//            try {
//                if(!isImageRecieving){
//                    message = clientObserver.getDataInputStream().readUTF();
//                    this.broadcastMsgsToClients(message,clientObserver.getClientUsername());
//                    if(message.contains("image")){
//                        isImageRecieving =true;
//                    }else{
//                        isImageRecieving = false;
//                    }
//                }else {
//                    this.broadcastMsgsToClients("Image Recieved",clientObserver.getClientUsername());
//                    System.out.println("image reciving");
//                                            // Read the file size from the client
//                long fileSize = clientObserver.getDataInputStream().readLong();
//                System.out.println("Received file size: " + fileSize);
//
//                // Discard the received data since the server is not saving it
//                // Create a file output stream to save the received file
//                String fileName = "path_to_save_image_file_12.jpg";
//                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//
//                // Receive and save the file data
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                long totalBytesRead = 0;
//                while (totalBytesRead < fileSize && (bytesRead = clientObserver.getDataInputStream().read(buffer)) != -1) {
//                    fileOutputStream.write(buffer, 0, bytesRead);
//                    totalBytesRead += bytesRead;
//                }
//
//                File file = new File(fileName);
//                broadcastImagesToClients(file,clientObserver.getClientUsername());
//
//                    isImageRecieving=false;
//                }
//            } catch (IOException e) {
////                e.printStackTrace();
//                break;
//            }
//        }
//    }

    public void removeClient(ClientsHandler clientsHandler) {
        clientsHandlerArrayList.remove(clientsHandler);  //remove from array
        broadcastMsgsToClients(clientsHandler.userName+ " has left from chat...!", clientsHandler.userName);
    }

    public void broadcastMsgsToClients(String msg, String senderUsername) {
        try {
            for (ClientsHandler clientsHandler : clientsHandlerArrayList) {   //get all clients to send message
                if(!clientsHandler.userName.equals(senderUsername)){
                    clientsHandler.dataOutputStream.writeUTF(msg);
                    clientsHandler.dataOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastImagesToClients(File file, String senderUsername) {
        String msg = file.getPath();
        System.out.println(msg);
        // Send the file size and data to each client
        for (ClientsHandler clientsHandler: clientsHandlerArrayList) {
            if (!clientsHandler.userName.equals(senderUsername)) {
                try {
                    System.out.println("************************************************************************************************");
                    clientsHandler.dataOutputStream.writeUTF("image");
                    clientsHandler.dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println("***sent"+"image");
            }
        }
        System.out.println("Image location sent successfully.");
    }

}
