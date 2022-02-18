/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Server {
    static ArrayList<myfile> syncFiles = new ArrayList<>();

    public static void main(String[] args) {
        int fileId = 0;

        JFrame jFrame = new JFrame("Server Contents");
        jFrame.setSize(500, 500);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTitle = new JLabel("Server Folder");
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        try {
            ServerSocket servers = new ServerSocket(1000);
            while (true) {
                try {
                    Socket socket = servers.accept();

                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String action = dataInputStream.readUTF();
                    switch (action) {
                        case "SYNC":
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            if(syncFiles.size()==0){
                                dataOutputStream.writeUTF("NO");
                                continue;
                            }
                            dataOutputStream.writeUTF("OK");
                            dataOutputStream.writeInt(syncFiles.size());
                            
                            for (myfile file : syncFiles) {
                                byte[] data = file.getData();
                                dataOutputStream.writeUTF(file.getName());
                                dataOutputStream.writeInt(data.length);
                                dataOutputStream.write(data);
                            }
                            socket.close();
                            break;

                        case "ADD":
                            int fileNameLength = dataInputStream.readInt();

                            if (fileNameLength > 0) {
                                byte[] fileNameBytes = new byte[fileNameLength];
                                dataInputStream.readFully(fileNameBytes, 0, fileNameLength);
                                String fileName = new String(fileNameBytes);

                                int fileContentLength = dataInputStream.readInt();

                                if (fileContentLength > 0) {
                                    byte[] fileContentBytes = new byte[fileContentLength];
                                    dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                                    JPanel jpfilerow = new JPanel();

                                    jpfilerow.setLayout(new BoxLayout(jpfilerow, BoxLayout.Y_AXIS));

                                    JLabel jpFileName = new JLabel(fileName);

                                    jpfilerow.setName(fileName);
                                    jpfilerow.add(jpFileName);
                                    jPanel.add(jpfilerow);
                                    jFrame.validate();
                                    syncFiles.add(
                                            new myfile(fileId, fileName, fileContentBytes, getFileExtension(fileName))
                                            );
                                }
                            }
                            socket.close();
                            break;
                    }
                } catch (IOException err) {
                    System.out.print(err.getMessage());
                }
            }
        } catch (IOException err) {

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