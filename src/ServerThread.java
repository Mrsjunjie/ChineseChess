import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread
{ 
	Server father;//����Server��
	ServerSocket ss;//����ServerSocket������      --�ͻ���
	boolean flag=true;
	public ServerThread(Server father){
		this.father=father;
		ss=father.ss;
	}
   public void run(){
	   while(flag){
		   try{
			   Socket sc=ss.accept();//�ȴ��ͻ�����   �����˿�    ʱʱ�̼̿���  û�пͻ��˷��ʲ��������һ��
			   ServerAgentThread  sat=new ServerAgentThread(father,sc);
			   sat.start();
		   }catch(Exception ee){
			   ee.printStackTrace();
		   }
	   }
   }
}
