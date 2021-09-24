import java.util.*;
import java.io.*;
import java.net.*;
class ClientSide
{
   final static int port=1008;
   public static void main(String []args)throws Exception
   {
      Scanner sc=new Scanner (System.in);
      InetAddress ip=InetAddress.getByName("localhost");
      Socket s=new Socket(ip,port);
      
      DataInputStream din=new DataInputStream(s.getInputStream());
      DataOutputStream dout=new DataOutputStream(s.getOutputStream());
      System.out.println("enetr your name");
      String m=sc.nextLine();
      dout.writeUTF(m);
      Thread sendmessage=new Thread(new Runnable()
      {
          public void run()
          {
              while(true)
              {
                  String message=sc.nextLine();
                  try {
                      dout.writeUTF(message);
                      if(message.equals("logout"))
                      break;
                  } catch (IOException e) {
                      //TODO: handle exception
                     System.out.println("at sendmessage");
                  }
              }
          }
      });
      Thread readmessage=new Thread(new Runnable()
      {
          public void run()
          {
              while(true)
              {
                  
                  try {
                    String message=din.readUTF();
                    System.out.println(message);
                  } catch (IOException e) {
                      //TODO: handle exception
                      System.out.println("now you logout");
                      break;
                      
                  }
              }
          }
      });
      sendmessage.start();
      readmessage.start();

   }
}