/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/24/2023
  @ Time         : 7:37 PM
*/
package util.impl;

import util.ClientObserver;

import java.io.*;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/24/2023
 * Time    : 7:37 PM
 */
public class ClientConnecrion implements ClientObserver {
    public String userName;
    public Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;


    public ClientConnecrion(Socket socket) {
        try {

            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getClientUsername() {
        return this.userName;
    }

    @Override
    public BufferedWriter getBufferedWriter() {
        return this.bufferedWriter;
    }

    @Override
    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

}
