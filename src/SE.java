import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



public class SE {

	//generate the secret key for AES CBC 256 and ECB 256
	public static SecretKey keyGen(int sp) throws NoSuchAlgorithmException {
		//generate the key wit AES
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    	keyGen.init(sp); 
    	SecretKey key = keyGen.generateKey();
    	return key;
	}
	
	//hash function H(s)
	//if you want to use ECB 256, just remove this 
	public static String hashFunc(String s) throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		sha256.update(s.getBytes());
		byte[] digest = sha256.digest();
		//return the hashed value in hex 
		 StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < digest.length; i++) {
	         sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 2).substring(1));
	        }
		return sb.toString();
	}
	
	
	


 
	//the inverted function
	//the first list contains all the tokens 
	//the rest list contains all the file path for one token
	public static ArrayList<ArrayList<String>> invert(final File folder) throws IOException {
		String filePath;
		ArrayList<ArrayList<String>> tokenList = new ArrayList<ArrayList<String>>();
		for (final File fileEntry : folder.listFiles()) {    
			//read the file in the folder 
			filePath = fileEntry.getPath();
			Scanner sc = new Scanner(new File(filePath));
			while(sc.hasNext()){
			 //read all tokens in each file
			 String token = sc.next();    
			 if(tokenList.get(0).contains(token)) {
				 int i = tokenList.get(0).indexOf(token);
				 tokenList.get(i+1).add(filePath);
			 }else {
				 ArrayList<String> fileList = new ArrayList<String>();
				 fileList.add(filePath);
				 tokenList.add(fileList);
			 }
			}
		}
		return tokenList;
	}
	
	

	public static String ECBFunc(String token) {
		return null;
		//to do
	}

	public static String CBCFunc() {
		return null;
		//to do
	}
	
	public static void PRFToken(ArrayList<ArrayList<String>> tokenList, String indexPath) throws IOException {
		ArrayList<String> tokens = tokenList.get(0);
		//do the token encryption 
     	PrintWriter pw = new PrintWriter(indexPath);	
		for(int i = 0; i< tokens.size();i++) {
			String EncToken = ECBFunc(tokens.get(i));
			pw.println(EncToken);
			for (int j = 0; j < tokenList.get(i+1).size();j++) {
				pw.print(" ");
				pw.print(tokenList.get(i+1).get(j));
			}
		}
		pw.close();
	}
	
	
    public static void main(String[] args) throws Exception {
    	String CBCKeyPath, EBCKeyPath,indexPath,tokenPath, plainTextsPath, cipherTextsPath;
	int sp = 256;
	File test = new File("src/data");
	invert(test);
    	//key generation 
    	//I am not quite sure about the Security parameter
    	if(args[0] == "keygen") {
        	SecretKey key = keyGen(sp);
        	CBCKeyPath = args[1]; 
        	EBCKeyPath = args[2];
        	PrintWriter pw1 = new PrintWriter(CBCKeyPath);	
        	pw1.write(key.toString());
        	PrintWriter	pw2 = new PrintWriter(EBCKeyPath);	
        	pw2.write(key.toString());
        pw1.close();
        pw2.close();
        
    	}else if(args[0] == "enc") {
    	    //the encryption

    		EBCKeyPath = args[1]; 
        	CBCKeyPath = args[2];
        	indexPath = args [3];
        	plainTextsPath = args[4];
        	cipherTextsPath = args[5];
        	ArrayList<ArrayList<String>> tokenList = invert(new File(plainTextsPath));
        	PRFToken(tokenList,	indexPath);

 

    		
    	}else if(args[0] == "token") {
    		//token 
    		String token = args[1];
    		EBCKeyPath = args[2];
    		tokenPath = args[3];
    		
    	}else if(args[1] == "search") {
    		
    	}else {
    		System.out.println("Error: Worng command! ");
    	}
    	
    	

   
    }
}