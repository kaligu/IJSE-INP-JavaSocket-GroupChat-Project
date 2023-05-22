/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 6:04 PM
*/
package util;

import javafx.scene.control.Alert;

import javax.print.DocFlavor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 6:04 PM
 */
public class ServerSocket {
    private java.net.ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerSocket() {
    }

    public boolean initializeServerSocket(int port){
        try {
            //create Server Socket
            this.serverSocket = new java.net.ServerSocket(port);
            return true;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
            alert.show();
            return false;
        }
    }

    public void openServerSocket(){
        try {
            //accept server socket
            if( serverSocket!=null && serverSocket.isClosed() ) {  //check server socket not null and already serversocket is closed
                this.socket = serverSocket.accept();
                //initialize input , output streams
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
            alert.show();
        }
    }

    public void closeServerSocket(){
        try {
            if( serverSocket!=null){
                this.serverSocket.close(); //close server socket
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
            alert.show();
        }
    }

//    public void sendDataToOutputStream(){
//        try {
//            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
//            alert.show();
//        }
//    }
//
//    public void getDataFromInputStream(){
//        try {
//            dataInputStream = new DataInputStream(socket.getInputStream());
//        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
//            alert.show();
//        }
//    }

}
