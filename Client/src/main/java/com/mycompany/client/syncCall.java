/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.client;

/**
 *
 * @author gaime
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;

import java.io.IOException;

public class syncCall extends TimerTask {
    public static int state;
    public void run(){
        int fileId = 0;
        try{
        Socket socket = new Socket("localhost", 1000);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        dataOutputStream.writeUTF("SYNC");
        String response = dataInputStream.readUTF();
        if (response.equals("OK")) {
            int numberOfFiles = dataInputStream.readInt();
            ArrayList<myfile> syncedFiles = new ArrayList<myfile>(numberOfFiles);
            
            while (numberOfFiles > 0) {
                    String fileName = dataInputStream.readUTF();

                    int fileContentLength = dataInputStream.readInt();

                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes);

                        syncedFiles.add(new myfile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                        fileId++;
                    }
                    numberOfFiles--;
            }
            Client.syncedFiles = syncedFiles;
            socket.close();
        }
        socket.close();
        }catch(IOException err){
            }
        
    }
    public static String getFileExtension(String FileName) {
        int i = FileName.lastIndexOf('.');

        if (i > 0) {
            return FileName.substring(i + 1);
        } else {
            return "no extension found";
        }
    }
}

