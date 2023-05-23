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
    //port
    private static int port = 9000;

    @FXML
    public Button btnChangeStatus;
    @FXML
    public ImageView imgviewstatus;

    public Text txtstatus;
    Thread thread1;
    Server server;
    boolean isSocketCanUse = false;

    public void initialize() {



    }

    public void clickOnActionbtnChangeStatus(ActionEvent actionEvent) {
        if((btnChangeStatus.getText().equals("Start"))){  //check button shows start

            if(true){
                //notify gui
                imgviewstatus.setImage(new Image("images/readytouseicon.png"));
                txtstatus.setText("Ready to use");
                btnChangeStatus.setText("Stop");
                //create util's serversocket object
                Thread thread = new Thread(() ->{
                    server = new Server();
                    isSocketCanUse = server.initializeServerSocket(port); //initialize port and return this port cam use or not
                    server.openServerSocket();
                });
                thread.start();


            }

        }else if((btnChangeStatus.getText().equals("Stop"))){  //check button shows stop

            //open server socket for requests
            server.closeServerSocket();

            //notify gui
            imgviewstatus.setImage(new Image("images/stopedicon.png"));
            txtstatus.setText("Stopped");
            btnChangeStatus.setText("Start");
        }

    }
}
