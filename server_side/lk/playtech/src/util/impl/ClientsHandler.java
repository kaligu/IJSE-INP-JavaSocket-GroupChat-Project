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
            boolean is = false;
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
                message = this.dataInputStream.readUTF();
                System.out.println("line 61");

                if(message.contains("image")){
                    isImageRecieving =true;
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
                    is =true;
                    fileOutputStream.close();
                    buffer=null;
                    bytesRead=0;
                    totalBytesRead=0;
                    fileName=null;
                    fileSize=0;

                    broadcastMsgsToClients("image",this.userName);

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
                Thread thread = new Thread(() -> {
                    try {
                        clientsHandler.dataOutputStream.writeUTF("image");
                        clientsHandler.dataOutputStream.flush();

                        // Read the image file
                        FileInputStream fileInputStream = new FileInputStream(file);
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                        // Get the file size
                        long fileSize = file.length();

                        // Send the file size to the server
                        clientsHandler.dataOutputStream.writeLong(fileSize);
                        System.out.println("Received file size: " + fileSize);

                        // Send the file data to the server
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                            clientsHandler.dataOutputStream.write(buffer, 0, bytesRead);
                        }


                        // Close the streams and socket
                        bufferedInputStream.close();
                        clientsHandler.dataOutputStream.flush();

                        System.out.println("***sent"+"image");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }
        System.out.println("Image location sent successfully.");
    }

}
