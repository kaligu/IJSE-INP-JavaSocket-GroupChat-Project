/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 5:08 PM
*/
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import util.Server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 5:08 PM
 */
public class MainFormController {

    //*******  port  *******
    private static int port = 9000;

    @FXML
    public Button btnChangeStatus;
    @FXML
    public ImageView imgviewstatus;

    public Text txtstatus;
    Server server;

    public void initialize() {}

    public void clickOnActionbtnChangeStatus(ActionEvent actionEvent) {
        if((btnChangeStatus.getText().equals("Start"))){  //if true = btn text is "start"

            //notify gui server accept successed
            imgviewstatus.setImage(new Image("images/readytouseicon.png"));
            txtstatus.setText("Ready to use");
            btnChangeStatus.setText("Stop");

            //create serversocket object
            System.out.println("Server is start");
            Thread thread = new Thread(() -> {
                ServerSocket serverSocket  = null;
                try {
                    serverSocket = new ServerSocket(5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Server is Running");
                server = new Server(serverSocket);
                server.runServer();
            });
            thread.start();


        }else if((btnChangeStatus.getText().equals("Stop"))){  //if true = btn text is "stop"
            System.out.println("Server is Running");
            server.runServer();

            //notify gui server socket closed
            imgviewstatus.setImage(new Image("images/stopedicon.png"));
            txtstatus.setText("Stopped");
            btnChangeStatus.setText("Start");
        }

    }
}
