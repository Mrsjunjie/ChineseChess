import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread
{ 
	Server father;//声明Server的
	ServerSocket ss;//声明ServerSocket的引用      --客户端
	boolean flag=true;
	public ServerThread(Server father){
		this.father=father;
		ss=father.ss;
	}
   public void run(){
	   while(flag){
		   try{
			   Socket sc=ss.accept();//等待客户链接   监听端口    时时刻刻监听  没有客户端访问不会进入下一条
			   ServerAgentThread  sat=new ServerAgentThread(father,sc);
			   sat.start();
		   }catch(Exception ee){
			   ee.printStackTrace();
		   }
	   }
   }
}
