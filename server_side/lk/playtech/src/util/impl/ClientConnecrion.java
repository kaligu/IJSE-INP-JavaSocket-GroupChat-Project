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
    private String userName;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;


    public ClientConnecrion(Socket socket) {  //assign holded socket into create conection
        try {
            this.socket = socket;
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.userName = dataInputStream.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getClientUsername() {
        return this.userName;
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return this.dataOutputStream;
    }

    @Override
    public DataInputStream getDataInputStream() {
        return this.dataInputStream;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }


}
