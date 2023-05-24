/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 6:04 PM
*/
package util;

import javafx.scene.control.Alert;
import util.impl.ClientConnecrion;
import util.impl.ClientsHandler;

import java.io.IOException;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 6:04 PM
 */
public class Server {
    private int port;
    private java.net.ServerSocket serverSocket;
    boolean bool;
    ClientsObservable clientsObservable;
    //clients holding arraylist

    public Server() {
    }

    public boolean initializeServerSocket(int port){
        this.port = port;
        try {
            clientsObservable = new ClientsHandler();
            //create Server Socket
            bool=true;
            System.out.println("Server is start");
            this.serverSocket = new java.net.ServerSocket(port);
            return true;
        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
//            alert.show();
            return false;
        }
    }

    public void openServerSocket(){
        while (!(serverSocket.isClosed())) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("new Client Connected");

                Thread thread = new Thread(() -> {
                    clientsObservable.addClient(new ClientConnecrion(socket));
                })

                ;
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
                closeServerSocket();
            }
        }
//        //accept server socket
//        if( serverSocket!=null) {  //check server socket not null and already serversocket is closed
//            System.out.println("Server is Running");
//            Socket socket = null;
//            try {
//                socket = serverSocket.accept();
//                System.out.println("new Client Connected");
//                Thread thread1 = new Thread(new ClientHandler(socket));
//                thread1.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //socket send to clienthandler
//
////                Thread thread = new Thread(() -> {
////                    ClientHandler clientHandler = new ClientHandler(this.socket);
////                    clientHandlers.add(clientHandler);
////                });
////                thread.start();
////                broadcastMessage("Joined Chat");
//
//        }
    }

    public void closeServerSocket(){
        try {
            if( serverSocket!=null){
                bool=true;
                this.serverSocket.close(); //close server socket
                System.out.println("Server is closed");
//                this.dataInputStream.close();
//                this.dataOutputStream.close();
//                this.socket.close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e+"");
            alert.show();
        }
    }



}
