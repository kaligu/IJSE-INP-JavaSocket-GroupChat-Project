/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : completed project inp
  @ Date         : 5/28/2023
  @ Time         : 9:15 AM
*/
package util;

import util.impl.ClientsHandlerImpl;

import java.io.File;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/28/2023
 * Time    : 9:15 AM
 */
public interface ClientHandler {

    void addClient(Socket socket); //add client socket

    void removeClient(ClientsHandlerImpl clientsHandlerImpl); //remove client socket

    void listentoClientsMsgsImgs(); //listen clients to get imgs and msgs

    void broadcastMsgsToClients(String msg, String senderUsername);  //send msg to all clients

    void broadcastImagesToClients(String senderUsername); //send img to all clients
}
