import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;
public class ClientHandler implements Runnable{
	Socket connectionSocket;InetAddress ip;
	Thread t;
	int cPort;
	public static ArrayList<PeerList> plist = new ArrayList<PeerList>();
	public static int  cNum=12;

	public ClientHandler(Socket s,InetAddress ipa)
	  {
		//cNum=cookie;
		connectionSocket=s; ip= ipa;
		t= new Thread(this,"client");
		System.out.println("client" + t);
		t.start();
      }

	

	public void run (){
		try{ 
			String req="sil",cmd,Hname=""; 
		
		  
			InetAddress ipa = connectionSocket.getInetAddress();

			ObjectInputStream serverInputStream = new    
            ObjectInputStream(connectionSocket.getInputStream());

         ObjectOutputStream serverOutputStream = new 
            ObjectOutputStream(connectionSocket.getOutputStream());
		
		
		req= serverInputStream.readObject().toString();
			String[] result = req.split("#");
			cmd= result[0];
			System.out.println(cmd);
			Hname=result[1];
			cPort=Integer.parseInt(result[2]);
			
			MyTimerTask Task;
			Timer myTimer = new Timer(); 
			
			if(cmd.equals("REG")){
				//Calendar now = Calendar.getInstance();   
				//Date date1= now.getTime();
				//Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//String s = formatter.format(date1);
				cNum=cNum + 1;
				PeerList p1 = new PeerList();
				p1.Hname=Hname;
				p1.Pnum=cPort;
				p1.ttl=7200;
				p1.noA=0;
				p1.Flag=true;
				p1.ip= ipa;
				p1.Cookie=cNum;
				p1.ip=ip;
			//	p1.date = s;
				plist.add(p1);
				
				Task = new MyTimerTask(p1.Hname);
				myTimer.schedule(Task,7200000);
				/*try{
					Thread.sleep(5000);
				//	System.out.println("hi silpa");
					
				}catch (InterruptedException exc){
					myTimer.cancel();
				}  */
				
				System.out.println("after silpa ");
				String a = Integer.toString(cNum);
				serverOutputStream.writeObject(a);
				connectionSocket.close();
			
			} //if
			
			else if(cmd.equals("REQP")){
				
				 ArrayList<PeerList> ActivePeers = new ArrayList<PeerList>();				
				System.out.println("got 2nd request for peerlist ");
				Iterator<PeerList> I = plist.iterator();
				 System.out.println("in REQP");
				while(I.hasNext()){
					PeerList p = I.next();
					if((p.Flag=true)&&!(p.Hname.equals(Hname)))
					{  
						ActivePeers.add(p);
						
					}
					
				}
	
	         serverOutputStream.writeObject(ActivePeers);
	         System.out.println("sent peerlist");
	         connectionSocket.close();
				
			} //if
			else if(cmd.equals("LEAVE")){
				int i=0;
				Iterator<PeerList> I = plist.iterator();
				 System.out.println("in leave");
				while(I.hasNext()){
					PeerList p = I.next();
					if(p.Hname.equals(Hname))
					{
						plist.remove(i);
						
						//p.Flag=false;
					//	plist.set(1,p);
						//youradapater.NotifyDataSetChanged();
						break;
					}
					++i;
									
				}// while 
				
				serverOutputStream.writeObject("Done");	
				
			}
			else if(cmd.equals("KeepAlive")){
				int i=0;
				Iterator<PeerList> I = plist.iterator();
				 System.out.println("in keepalive");
				while(I.hasNext()){
					PeerList p = I.next();
					if(p.Hname.equals(Hname))
					{
						
						p.Flag=true;
					//	plist.set(1,p);
						//youradapater.NotifyDataSetChanged();
						break;
					}
					++i;
				
							
						}//while
				serverOutputStream.writeObject("Done");	
				
		      }//if
		}//try
		
		catch(IOException e){
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}//run
		
		
		
	}//class
	

	
class MyTimerTask extends TimerTask{
	
	String Hname;
	public MyTimerTask(String host)
	{
		Hname=host;
		
	}
	
	public void run (){
		//System.out.println("hi manasa !!");
		Iterator<PeerList> I = ClientHandler.plist.iterator();
		int i =0;
		while(I.hasNext()){
			PeerList p = I.next();
			if(p.Hname.equals(Hname))
			{
				p.Flag=false;
				ClientHandler.plist.set(i,p);				
				System.out.println("Flag of "+ Hname +"made false");
				break;
			}
			i++;
			
		} 
		
		return;
	}
}
	
	
	
	

