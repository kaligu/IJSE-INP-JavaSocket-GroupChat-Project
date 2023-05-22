/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 5:08 PM
*/
package controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import util.ServerSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 5:08 PM
 */
public class MainFormController {
    //port
    private static int port = 3000;

    @FXML
    public Button btnChangeStatus;
    @FXML
    public ImageView imgviewstatus;

    public Text txtstatus;
    Thread thread1;
    util.ServerSocket serverSocket;
    boolean isSocketCanUse = false;

    public void initialize() {

    }

    public void clickOnActionbtnChangeStatus(ActionEvent actionEvent) {
        if((btnChangeStatus.getText().equals("Start"))){  //check button shows start
            //create util's serversocket object
            serverSocket = new ServerSocket();
            isSocketCanUse = serverSocket.initializeServerSocket(port); //initialize port and return this port cam use or not
            if(isSocketCanUse){
                //notify gui
                imgviewstatus.setImage(new Image("images/readytouseicon.png"));
                txtstatus.setText("Ready to use");
                btnChangeStatus.setText("Stop");

                thread1 = new Thread(() -> {
                    //open server socket for requests
                    serverSocket.openServerSocket();

                });
                thread1.start();
            }

        }else if((btnChangeStatus.getText().equals("Stop"))){  //check button shows stop

            //open server socket for requests
            serverSocket.closeServerSocket();
            try {
                // Wait for the thread to complete gracefully
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //notify gui
            imgviewstatus.setImage(new Image("images/stopedicon.png"));
            txtstatus.setText("Stopped");
            btnChangeStatus.setText("Start");
        }

    }
}
