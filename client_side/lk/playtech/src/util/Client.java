/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/23/2023
  @ Time         : 7:00 PM
*/
package util;

import com.sun.security.sasl.ClientFactoryImpl;
import controller.ChatFormController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/23/2023
 * Time    : 7:00 PM
 */
public class Client {
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket,String username){
        try{
            this.socket = socket;
            this.username = username;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    public void clientSendMessage(String message) {
        try {
            bufferedWriter.write(username + " : " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void listenForMessage(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        System.out.println(bufferedReader.readLine());
                        String message = bufferedReader.readLine();
                        ChatFormController.receiveMessage(message, vBox);
                    } catch (IOException e) {
                        closeEverything();
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(){
        try {
            if (bufferedReader != null ){
                bufferedReader.close();
            }

            if (bufferedWriter != null ){
                bufferedWriter.close();
            }

            if (socket != null ){
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
