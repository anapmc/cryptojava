/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ssi_aula1_aula2;
import java.io.*;
import java.net.*;
import java.nio.file.*;


import javax.crypto.CipherInputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.InputStream;

/**
 *
 * @author User
 */
public class Server {
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */

    public static void main(String[] args) throws IOException {
	   ServerSocket ss = new ServerSocket(4567);
	   int client_id = 0;

	   while (true) {
	     Socket cs = ss.accept();
	     Thread t1 = new Thread(new ReadMessage(cs,client_id));
	     t1.start();
	     client_id++;
	   }
    }
}



class ReadMessage implements Runnable {
    
    private Socket client;
    private int id;

    
    public ReadMessage(Socket client,int id) {
	   this.client = client;
	   this.id=id;
    }

    public void run() {
    	Boolean firstTime=true;
    	String cipherMode="";
	try {
		if (firstTime){


	
	    BufferedReader in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

	    String msg;
            while ((msg = in.readLine()) != null){
            	cipherMode=(String) msg;
                firstTime=false;
                break;    
           } 
		}
		else{


		InputStream is = this.client.getInputStream();
        String mode="RC4";
		Cipher e = Cipher.getInstance("RC4");
		byte[] keyfile= Files.readAllBytes(Paths.get("../rc4/chave"));
		SecretKey key = new SecretKeySpec(keyfile,"RC4");
		e.init(Cipher.DECRYPT_MODE,key);
        System.out.println("Using cipher "+cipherMode+"for client ["+id+"]"); 

        CipherInputStream cis = new CipherInputStream(is,e);
        int test;
        while((test=cis.read())!=-1){
            System.out.println("Client ["+id+"]:"); 
        	System.out.println((char) test);
        }
        System.out.println("["+id+"]: "+"Client disconnected");
    }

            
            
	   
	} catch (Exception e) {System.out.println(e);}
    }
    
}
