import java.io.*;
import java.net.*;
import java.util.*;
class Serverside
{

    static Vector<clienthander >ad=new Vector<clienthander>();
    static int i=0;
    public static void main(String[] args)throws Exception 
    {
        System.out.println("ppp");
        ServerSocket s=new ServerSocket(1008);
        System.out.println("ppp");
        Socket ss;
        while(true)
        {
                ss=s.accept();
                DataInputStream din =new DataInputStream(ss.getInputStream());
                DataOutputStream dout =new DataOutputStream(ss.getOutputStream());
                System.out.println("one more client request accepted"+ss);
                String s1=din.readUTF();
                clienthander cr=new clienthander(din,dout,ss,s1);
                
                Thread t=new Thread(cr);
                
                for( clienthander c:ad)
                {
                    c.dout.writeUTF("you have connected"+" "+s1);
                }
                 
                for( clienthander c:ad)
                {
                    dout.writeUTF("you are connected to "+c.name);
                }
                ad.add(cr);
                i++;
                t.start(); 
        }   
      
    }    
}
class clienthander implements Runnable
{
   DataInputStream din;
   DataOutputStream dout;
   Socket s;
   String name;
   boolean  logginout;
   clienthander(DataInputStream din,DataOutputStream dout,Socket s,String name)
   {
       this.din=din;
       this.dout=dout;
       this.name=name;
       this.s=s;
       logginout=true;
   }  
   public void run()
   {
       String recive;
       while(true)
       {
           try {
               recive=din.readUTF();
               System.out.println(recive);
            //    StringTokenizer st=new StringTokenizer(recive,"#");
            //    String msgtosend=st.nextToken();
            //    String recipient=st.nextToken();
               if(recive.equals("logout"))
               {
                   this.logginout=false;
                //    Vector<clienthander>::iterator it;
                  // int p=Serverside.ad.indexOf(this)-Serverside.ad.begin();
                  // int p=it-Serverside.ad.begin();
                   //Serverside.ad.remove(p);
                   int p=0;
                   for( clienthander c:Serverside.ad)
                    {  
                       if(this==c)
                       {
                           
                        Serverside.ad.remove(p);
                        break;
                       }
                       p++;
                    }  
                    this.s.close(); 
                    for( clienthander c:Serverside.ad)
                    {  
                       
                       c.dout.writeUTF(name+"is logout now you are  not connected ");
                    }
                   
                   break;
               }
               StringTokenizer st=new StringTokenizer(recive,"#");
               String msgtosend=st.nextToken();
               String recipient=st.nextToken();
               for(clienthander mc: Serverside.ad)
               {
                   if(mc.name.equals(recipient)&& mc.logginout==true)
                   {
                       mc.dout.writeUTF(this.name+" "+msgtosend);
                       break;

                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       try{
        this.din.close();
        this.dout.close();
    }
    catch(IOException e) {
        e.printStackTrace();
    }
   }
   
}