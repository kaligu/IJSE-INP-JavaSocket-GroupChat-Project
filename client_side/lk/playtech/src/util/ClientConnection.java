/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/28/2023
  @ Time         : 8:18 AM
*/
package util;

import javafx.scene.layout.VBox;

import java.io.File;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/28/2023
 * Time    : 8:18 AM
 */
public interface ClientConnection {
    void clientSendMsgtoServer(String message);  //send messages to server

    void clientSendImgtoServer(File imageFile); //send image file to server

    void listentoServerMsgsImgs(VBox vBox); //listen Serever msgs and update at UI,get UI's Vbox to update it
}
