import java.io.*;

import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread
{

	Server father;//声明Server的引用
	Socket sc;//声明Socket的引用
	DataInputStream din;//声明数据输入流与输出流的引用
	DataOutputStream dout;
	boolean flag=true;//控制线程的标志位
	public ServerAgentThread(Server father,Socket sc)
	{
		this.father=father;
		this.sc=sc;
		try
		{
			din=new DataInputStream(sc.getInputStream());//创建数据输入流
			dout=new DataOutputStream(sc.getOutputStream());//创建数据输出流
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void run()
	{
		while(flag)
		{
			try
			{
				String msg=din.readUTF().trim();//接收客户端传来的信息
				if(msg.startsWith("<#NICK_NAME#>"))//收到新用户的信息
				{
					this.nick_name(msg);
				}
				else if(msg.startsWith("<#CLIENT_LEAVE#>")){//收到用户离开的信息
					this.client_leave(msg);
				}
				else if(msg.startsWith("<#TIAO_ZHAN#>")){//收到用户发出的挑战信息
					this.tiao_zhan(msg);
				}
				else if(msg.startsWith("<#TONG_YI#>")){//受到接受挑战的信息
					this.tong_yi(msg);
				}
				else if(msg.startsWith("<#BUTONG_YI#>")){//受到拒绝挑战的信息
					this.butong_yi(msg);
				}
				else if(msg.startsWith("<#BUSY#>")){//收到被挑战者忙的信息
					this.busy(msg);
				}
				else if(msg.startsWith("<#MOVE#>")){//收到走棋的信息
					this.move(msg);
				}
				else if(msg.startsWith("<#RENSHU#>")){//收到某用户认输的信息
					this.renshu(msg);
				}	
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public void nick_name(String msg)
	{
		try
		{
			String name=msg.substring(13);//获得用户的昵称
			this.setName(name);//用该昵称给该线程取名
			Vector v=father.onlineList;//获得在线用户列表
			boolean isChongMing=false;
			int size=v.size();//获得用户列表的大小
			for(int i=0;i<size;i++)
			{//遍历列表，查看是否已经有该用户名
				ServerAgentThread tempSat=(ServerAgentThread)v.get(i);
				if(tempSat.getName().equals(name))
				{
					isChongMing=true;//有重名，将标志位设为true
					break;
				}
			}
			if(isChongMing==true)//如果重名
			{
				dout.writeUTF("<#NAME_CHONGMING#>");//将重名信息发送给客户端
				din.close();//关闭数据输入流
				dout.close();//关闭数据输出流
				sc.close();//关闭Socket
				flag=false;//终止该服务器代理线程
			}
			else//如果不重名
			{
				v.add(this);//将该线程添加到在线列表
				father.refreshList();//刷新服务器在线信息列表
				String nickListMsg="";
				size=v.size();//获得在线列表大小
				for(int i=0;i<size;i++)
				{
					ServerAgentThread tempSat=(ServerAgentThread)v.get(i);
					nickListMsg=nickListMsg+"|"+tempSat.getName();
				}//将在线列表内容住组织成字符串
				nickListMsg="<#NICK_LIST#>"+nickListMsg;
				Vector tempv=father.onlineList;
				size=tempv.size();
				for(int i=0;i<size;i++)
				{//遍历在线列表
					ServerAgentThread satTemp=(ServerAgentThread)tempv.get(i);
					satTemp.dout.writeUTF(nickListMsg);//将最新的列表信息发送到各个客户端
					if(satTemp!=this)
					{//给其他客户端发送新用户上线的信息
						satTemp.dout.writeUTF("<#MSG#>"+this.getName()+"上线了...");
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public void client_leave(String msg){
		try{
			Vector tempv=father.onlineList;//获得在线列表
			tempv.remove(this);//移除该用户
			int size=tempv.size();
			String nl="<#NICK_LIST#>";
			for(int i=0;i<size;i++){//遍历在线列表
				ServerAgentThread satTemp=(ServerAgentThread)tempv.get(i);
				//向各个客户端发送用户离线信息
				satTemp.dout.writeUTF("<#MSG#>"+this.getName()+"离线了...");
				//组织信息的在线用户列表
				nl=nl+"|"+satTemp.getName();
			}
			for(int i=0;i<size;i++){//将最新的列表信息发送到各个客户端
				ServerAgentThread satTemp=(ServerAgentThread)tempv.get(i);
				satTemp.dout.writeUTF(nl);
			}
			this.flag=false;//终止该服务器代理线程
			father.refreshList();//更新服务器在线用户列表
		}
		catch(IOException e){e.printStackTrace();}
	}
	public void tiao_zhan(String msg)
	{
		try
		{
			String name1=this.getName();//获得发出挑战信息用户的名字
			String name2=msg.substring(13);//获得被挑战的用户名字
			Vector v=father.onlineList;//获得在线用户列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++)
			{//遍历列表，搜索被挑战的用户
				ServerAgentThread satTemp=(ServerAgentThread)v.get(i);
				if(satTemp.getName().equals(name2))
				{//向该用户发送挑战信息，附带提出挑战用户的名字
					satTemp.dout.writeUTF("<#TIAO_ZHAN#>"+name1);
					break;
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void tong_yi(String msg){
		try{
			String name=msg.substring(11);//获得提出挑战的用户的名字
			Vector v=father.onlineList;//获得在线用户列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++){//遍历列表，搜索提出挑战的用户
				ServerAgentThread satTemp=(ServerAgentThread)v.get(i);
				if(satTemp.getName().equals(name)){//向该用户发送对方接受挑战的信息
					satTemp.dout.writeUTF("<#TONG_YI#>");
					break;
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void butong_yi(String msg){
		try{
			String name=msg.substring(13);//获得提出挑战的用户的名字
			Vector v=father.onlineList;//获得在线用户列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++)
			{//遍历列表，搜索提出挑战的用户
				ServerAgentThread satTemp=(ServerAgentThread)v.get(i);
				if(satTemp.getName().equals(name)){//向该用户发送对方拒绝挑战的信息
					satTemp.dout.writeUTF("<#BUTONG_YI#>");
					break;
				}
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
	public void busy(String msg){
		try{
			String name=msg.substring(8);//获得提出挑战的用户的名字
			Vector v=father.onlineList;//获得在线用户列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++){//遍历列表，搜索提出挑战的用户
				ServerAgentThread satTemp=(ServerAgentThread)v.get(i);
				if(satTemp.getName().equals(name)){//向该用户发送对方正在忙的信息
					satTemp.dout.writeUTF("<#BUSY#>");
					break;
				}
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
	public void move(String msg){
		try{
			String name=msg.substring(8,msg.length()-4);//获得接收方的名字
			Vector v=father.onlineList;//获得在线用户列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++){//遍历列表，搜索接收方
				ServerAgentThread satTemp=(ServerAgentThread)v.get(i);
				if(satTemp.getName().equals(name)){//将该信息转发给接收方
					satTemp.dout.writeUTF(msg);
					break;
				}
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
	public void renshu(String msg){
		try{
			String name=msg.substring(10);//获得接收方的名字
			Vector v=father.onlineList;//获得在线用户列表
			int size=v.size();//获得在线用户列表的大小
			for(int i=0;i<size;i++){//遍历列表，搜索接收方
				ServerAgentThread satTemp=(ServerAgentThread)v.get(i);
				if(satTemp.getName().equals(name)){//将该信息转发给接收方
					satTemp.dout.writeUTF(msg);
					break;
				}
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
}
