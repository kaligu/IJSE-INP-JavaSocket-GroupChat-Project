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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/23/2023
 * Time    : 7:00 PM
 */
public class Client {
    public Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    private String username;

    private boolean isImageRecieving = false;

    public Client(Socket socket,String username){
        try{
            this.socket = socket;
            this.username = username;
            this.dataInputStream = new DataInputStream(this.socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void clientSendMessage(String message) {
        Thread thread = new Thread(() -> {
            try {
                dataOutputStream.writeUTF(username + " : " + message);
                dataOutputStream.flush();
            } catch (IOException e) {
                closeEverything();
            }
        });
        thread.start();
    }

    public void clientSendImage(File imageFile){
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

            System.out.println("Image sent successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();


    }

    public void listenForMessage(VBox vBox,Client client){
        Thread thread = new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    if(!isImageRecieving){
                        String message = dataInputStream.readUTF();
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



                        if(message.contains("image")){
                            isImageRecieving =true;
                        }else{
                            isImageRecieving = false;
                        }
                    }else {
                        System.out.println("image reciving");
                        String imgurl = dataInputStream.readUTF();
                        System.out.println("controlclass"+imgurl);

                        // Read the file size from the client
//                        long fileSize = client.dataInputStream.readLong();
//                        System.out.println("133 Received file size: " + fileSize);

////                         Discard the received data since the server is not saving it
//                         Create a file output stream to save the received file

//                        System.out.println("138");
//                        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//
//                        System.out.println("141");
//                        // Receive and save the file data
//                        byte[] buffer = new byte[4*1024];
//                        int bytesRead;
//                        long totalBytesRead = 0;
//                        System.out.println("145");
//                        DataInputStream dataInputStream1 = new DataInputStream(socket.getInputStream());
//                        bytesRead = dataInputStream1.read(buffer);
//                        while (totalBytesRead < dataInputStream1.readLong() && (bytesRead != -1)) {
//                            System.out.println("bytesRead "+bytesRead);
//                            System.out.println("fileSize  "+dataInputStream1.readLong());
//
//                            fileOutputStream.write(buffer, 0, bytesRead);
//                            totalBytesRead += bytesRead;
//                            System.out.println("totalBytesRead  "+totalBytesRead);
//                        }
//                        System.out.println("ffdff"+dataInputStream1.read(buffer));
//                        System.out.println("150");



                        // Close the stream and socket
                        String fileName = "D:\\ijse socket project\\IJSE-INP-JavaSocket-GroupChat-Project\\server_side\\lk\\playtech\\resources\\server-imgs-memory\\Chat-img-location.png";
                        ImageView imageView = new ImageView();
                        imageView.setImage(new Image("server-imgs-memory/Chat-img-location.png"));

                        imageView.setFitWidth(300);
                        imageView.setFitHeight(300);

                        HBox hBox = new HBox();
                        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-padding: 10");
                        hBox.getChildren().add(imageView);

                        Platform.runLater(() -> {
                            vBox.getChildren().add(hBox);
                        });


                        System.out.println("Image received and discarded successfully.y y6+-++++++++++++++++++++++++++++++++++" );

                        isImageRecieving = false;
                    }



                } catch (IOException e) {
                    closeEverything();
                    break;
                }
            }
        });
        thread.start();
    }

    public void closeEverything(){


    }

}
