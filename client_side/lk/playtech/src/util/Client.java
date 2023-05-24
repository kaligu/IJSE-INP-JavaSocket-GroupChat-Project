/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/23/2023
  @ Time         : 7:00 PM
*/
package util;

import com.sun.security.sasl.ClientFactoryImpl;
import controller.ChatFormController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
        Thread thread = new Thread(() -> {
            try {
                bufferedWriter.write(username + " : " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything();
            }
        });
        thread.start();
    }

    public void listenForMessage(VBox vBox){
        Thread thread = new Thread(() -> {
            while (socket.isConnected()) {
                try {

                    String message = bufferedReader.readLine();
//                    ChatFormController.receiveMessage(vBox,message);
                    System.out.println("clientclass"+message);

                    System.out.println("controlclass"+message);
                    HBox hBox = new HBox();
                    hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
                    Label messageLbl = new Label(message);
                    messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
                    hBox.getChildren().add(messageLbl);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            vBox.getChildren().add(hBox);
                        }
                    });

                } catch (IOException e) {
                    closeEverything();
                    break;
                }
            }
        });
        thread.start();
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
