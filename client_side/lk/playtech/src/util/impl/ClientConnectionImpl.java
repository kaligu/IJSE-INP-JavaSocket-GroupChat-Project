/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/23/2023
  @ Time         : 7:00 PM
*/
package util.impl;

import controller.ChatFormController;
import javafx.scene.layout.VBox;
import util.ClientConnection;

import java.io.*;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/23/2023
 * Time    : 7:00 PM
 */
public class ClientConnectionImpl implements ClientConnection {
    public Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    private String username;


    public ClientConnectionImpl(Socket socket, String username){
        try{
            this.socket = socket;
            this.username = username;
            this.dataInputStream = new DataInputStream(this.socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void clientSendMsgtoServer(String message) {
        Thread thread = new Thread(() -> {
            try {
                dataOutputStream.writeUTF(username + " : " + message);
                dataOutputStream.flush();
                System.out.println("***sent"+message);
            } catch (IOException e) {
                closeEverything();
            }

        });
        thread.start();
    }

    public void clientSendImgtoServer(File imageFile){
        Thread thread = new Thread(() -> {
            try {
                dataOutputStream.writeUTF("image");
                dataOutputStream.flush();

            // Read the image file
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Get the file size
            long fileSize = imageFile.length();

            // Send the file size to the server
            dataOutputStream.writeLong(fileSize);
            System.out.println("Received file size: " + fileSize);

            // Send the file data to the server
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }


            // Close the streams and socket
            bufferedInputStream.close();
            dataOutputStream.flush();

                System.out.println("***sent"+"image");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();



    }

    public void listentoServerMsgsImgs(VBox vBox){


        Thread thread = new Thread(() -> {
            while (socket.isConnected()) {
                boolean isimage = false;
                long size;
                String message = null;
                try {
                    message = dataInputStream.readUTF();
                    System.out.println("110  "+message);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(message.contains("image")){
                    message=username+" : Image Received.";
                    System.out.println("116path  "+message);
                    isimage=true;
                    System.out.println("118path"+message);

                    System.out.println("client rimage recived********************************************");
                    ChatFormController.receiveImg(vBox, message);
                }else{
                    isimage=false;
                    System.out.println("124path "+message);
                    ChatFormController.receivemsg(vBox,message);
                }

                }
        });
        thread.start();
    }

    public void closeEverything(){


    }

}
