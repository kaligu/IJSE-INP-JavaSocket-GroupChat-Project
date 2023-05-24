/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 6:29 PM
*/
package util;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 6:29 PM
 */
public interface ClientsObservable {

    public void addClient(ClientObserver clientObserver);

    public void removeClient(ClientObserver clientObserver);

    public void broadcastMsgsToClients(String msg, String senderUsername);
}
