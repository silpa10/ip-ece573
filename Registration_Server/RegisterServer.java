import java.util.*;

import java.io.*;
import java.net.*;

public class RegisterServer  {
	//String Hname,cPort;
	// public static ArrayList<PeerList> plist = new ArrayList<PeerList>();
	  
	 
	public static void main (String args[])
	{ //String req="sil",cmd,Hname=""; 
	  //int cPort=0;
	  
		
		try{
		ServerSocket welcomeSocket=new ServerSocket(65423);
		while(true)
		{  				

			Socket connectionSocket=welcomeSocket.accept();
			System.out.println("in server");
			
			//cookie=cookie + 1;
			InetAddress ipa = connectionSocket.getInetAddress();
			
			System.out.println(connectionSocket.getInetAddress());
			ClientHandler CH = new ClientHandler(connectionSocket,ipa);
			
		
	}//while
	}// try
	catch(IOException e){
		e.printStackTrace();
		
	} 
	
	
	}//main
	
	
}//class
		
	
	

