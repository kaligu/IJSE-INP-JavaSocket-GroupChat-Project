/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 6:04 PM
*/
package util;

import util.impl.ClientsHandlerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 6:04 PM
 */
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer(){
        System.out.println("runserver");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("new Client Connected");
                Thread thread = new Thread(() -> {
                    System.out.println("connected");
                    ClientsHandlerImpl clientsHandlerImpl = new ClientsHandlerImpl();
                    clientsHandlerImpl.addClient(socket);
                });
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
                closeServer();
            }
        }

    }

    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server Closed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
