/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 6:29 PM
*/
package util;

import java.io.File;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 6:29 PM
 */
public interface ClientsObservable {

    void addClient(ClientObserver clientObserver);  //add client to hold

    void removeClient(ClientObserver clientObserver); //remove client from holdin array

    void broadcastMsgsToClients(String msg, String senderUsername); //send massage into all holding clients

    void broadcastImagesToClients(File file, String senderUsername); //send images into all holding clients
}
