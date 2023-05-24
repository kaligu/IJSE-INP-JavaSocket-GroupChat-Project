/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 7:57 PM
*/
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 7:57 PM
 */
public interface ClientObserver {
    String getClientUsername();

    BufferedWriter getBufferedWriter();

    BufferedReader getBufferedReader();

    Socket getSocket();
}
