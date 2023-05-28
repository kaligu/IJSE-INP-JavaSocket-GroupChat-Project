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

    public Socket socket;  //client socket
    DataInputStream dataInputStream; //client's input stream
    DataOutputStream dataOutputStream; //client's output stream
    private String username; //client username


    public ClientConnectionImpl(Socket socket, String username){ //socket data initialize
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
                dataOutputStream.writeUTF(username + " : " + message); //send msg
                dataOutputStream.flush();
            } catch (IOException e) {
                System.out.println("msg sent error");
            }
        });
        thread.start();
    }

    public void clientSendImgtoServer(File imageFile){
        Thread thread = new Thread(() -> {
            //send msg to server "image" before read image ready to read image file
            try {
                dataOutputStream.writeUTF("image");
                dataOutputStream.flush();

            // Read the image file
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Get the file size
            long fileSize = imageFile.length();

            // Send the file packet bag size to the server
            dataOutputStream.writeLong(fileSize);
            System.out.println("Received file size: " + fileSize);

            // Send the file data to the server
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);  //send one by one image's packets
            }

            // Close the streams and socket
            bufferedInputStream.close();
            dataOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void listentoServerMsgsImgs(VBox vBox){
        Thread thread = new Thread(() -> {
            while (socket.isConnected()) {
                String message = null;

                try {
                    message = dataInputStream.readUTF();  //read server msgs
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(message.contains("Image")){
                    ChatFormController.receiveImg(vBox, message); //update at UI
                }else{
                    ChatFormController.receivemsg(vBox,message); //update at UI
                }
            }
        });
        thread.start();
    }

    private void closeSocket(){
        try {
            if (socket != null ){
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
