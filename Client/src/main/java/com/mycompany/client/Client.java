/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.client;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author gaime
 */
public class Client {

     public static File file;
    public static ArrayList<myfile> syncedFiles = new ArrayList<myfile>();

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("Gaimes' Cloud Storage");
        jFrame.setSize(700, 700);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JLabel jlTitle = new JLabel("File Manager");
        jlTitle.setFont(new Font("Times New Roman", Font.BOLD, 30));
        jlTitle.setBorder(new EmptyBorder(20, 0, 0, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel jPanel = new JPanel();
        jPanel.setAlignmentX(Component.TOP_ALIGNMENT);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(5, 0, 10, 0));
        JButton jbSendFile = new JButton("Add File");

        jpButton.add(jbSendFile);

        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose file to add");

                if (jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
                    file = jFileChooser.getSelectedFile();
                    if (file == null) {
                        return;
                    }
                    try {
                        Socket socket = new Socket("localhost", 1000);
                        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        dataOutputStream.writeUTF("ADD");
                        String fileName = file.getName();
                        byte[] fileNameBytes = fileName.getBytes();

                        byte[] fileContentBytes = new byte[(int) file.length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(fileNameBytes.length);
                        dataOutputStream.write(fileNameBytes);
                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);
                        socket.close();
                    } catch (IOException err) {
                        err.getStackTrace();
                    }

                }
            }
        });

        jFrame.add(jlTitle);
        jFrame.add(jpButton);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
        Timer time = new Timer();
        syncCall st = new syncCall();
        time.schedule(st, 0, 1000);
        while (true) {
            try {
                jPanel.removeAll();
                for (myfile file : syncedFiles) {
                    JPanel jpfilerow = new JPanel();

                    jpfilerow.setLayout(new BoxLayout(jpfilerow, BoxLayout.Y_AXIS));

                    JLabel jpFileName = new JLabel(file.getName());

                    jpfilerow.add(jpFileName);
                    jPanel.add(jpfilerow);

                }
                jFrame.validate();
            }catch(ConcurrentModificationException err){
            }
        }
    }
    
}
