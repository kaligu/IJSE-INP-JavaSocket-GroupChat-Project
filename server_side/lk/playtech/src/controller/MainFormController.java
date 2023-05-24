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
            Thread thread = new Thread(() ->{
                server = new Server();  //create Server object
                server.initializeServerSocket(port); //initialize port to server socket
                server.openServerSocket();  //accept server socket and assign to socket for requests
            });
            thread.start();

        }else if((btnChangeStatus.getText().equals("Stop"))){  //if true = btn text is "stop"

            server.closeServerSocket();  //close server socket

            //notify gui server socket closed
            imgviewstatus.setImage(new Image("images/stopedicon.png"));
            txtstatus.setText("Stopped");
            btnChangeStatus.setText("Start");
        }

    }
}
