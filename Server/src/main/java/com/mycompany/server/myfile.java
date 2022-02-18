/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server;


public class myfile {
    private int id;
    private String name;
    private byte[] data;
    private String fileExtension;

    public myfile(int id, String name, byte[] data, String extension){
        this.id=id;
        this.name = name;
        this.data = data;
        this.fileExtension = extension;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setData(byte[] data){
        this.data = data;
    }
    public void setExtension(String ext){
        this.fileExtension = ext;
    }
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public byte[] getData(){
        return this.data;
    }
    public String getFileExtention(){
        return this.fileExtension;
    }
}

