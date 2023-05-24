/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 11:19 PM
*/
package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import util.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 11:19 PM
 */
public class ChatFormController implements Observer {

    public VBox vboxpane;
    public TextField txtfldgetname;
    public TextArea txtareamsg;
    public TextField txtfldmsg;
    public Text txtsendername;
    Client client;


    public void initialize(){



    }

    public void txtfldmsgOnAction(ActionEvent actionEvent) {
        String message = txtfldmsg.getText();

        if (!message.isEmpty()) {
            sendMessage(message);
            txtareamsg.appendText("me"+message);
            txtfldmsg.clear();
            client.clientSendMessage(message);
//            textField.clear();
        }
    }

    private void sendMessage(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:  #27ae60;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vboxpane.getChildren().add(hBox);

    }

    public void receiveMessage(String message, VBox vBox){
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
    }

    public void btnjoinchat(ActionEvent actionEvent) {
        txtsendername.setText(txtfldgetname.getText());
        try {
            client = new Client(new Socket("localhost",9000),txtfldgetname.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.listenForMessage(vboxpane);
        client.clientSendMessage("");
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
