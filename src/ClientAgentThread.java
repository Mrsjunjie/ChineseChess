import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread
{ 
	XiangQi father;//声明XiangQi的引用
	boolean flag=true;
	DataInputStream  din;
	DataOutputStream  dout;
	
	String tiaoZhanZhe;//用于记录正在挑战的对手
	public ClientAgentThread(XiangQi father){
		this.father=father;
		try{
			din=new DataInputStream(father.sc.getInputStream());//创建输入数据  数据输入流
			dout=new DataOutputStream(father.sc.getOutputStream());//
			String name=father.jtfNickName.getText().trim();//获得呢称
			dout.writeUTF("<#NICK_NAME#>"+name);//发送呢称到服务器
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
public void run(){
	while(flag){
		try{
			String msg=din.readUTF().trim();//获得服务器发来的消息o[
			if(msg.startsWith("<#NAME_CHONGMING#>")){
				this.name_chongming();
			}else if(msg.startsWith("<#NICK_LIST#>")){
				this.nick_list(msg);
			}else if(msg.startsWith("<#TIAO_ZHAN#>")){
				this.tiao_zhan(msg);
			}else if(msg.startsWith("<#TONG_YI#>")){
				this.tong_yi();
			}else if(msg.startsWith("<#BUTONG_YI#>")){
				this.buyong_yi(msg);
			}else if(msg.startsWith("<#BUSY#>")){
				this.busy();
			}else if(msg.startsWith("<#MOVE#>")){
				this.move(msg);
			}else if(msg.startsWith("<#RENSHU#>")){
				this.renshu();
			}else if(msg.startsWith("<#SERVER_DOWN#>")){//当收到服务器离开的信息
				this.server_down();
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
}
private void renshu() {
	
	JOptionPane.showMessageDialog(this.father,"恭喜你，你获胜，对方认输","提示",
			JOptionPane.ERROR_MESSAGE);//给出获胜信息
	this.tiaoZhanZhe=null;//将挑战者设为空
	this.father.next();//进入下一局
	this.father.caiPan=false;//将caipan设为false
	this.father.color=0;//将color设为0
	this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框架设为不可用
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(true);//将断开设为不可用
	this.father.jbChallenge.setEnabled(true);//将挑战设为不可用
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	
}

private void move(String msg) {
	int length=msg.length();
	int startI=Integer.parseInt(msg.substring(length-4,length-3));//获得棋子的原始位置
	int startJ=Integer.parseInt(msg.substring(length-3,length-2));
	int endI=Integer.parseInt(msg.substring(length-2,length-1));//获得走后的位置
	int endJ=Integer.parseInt(msg.substring(length-1));
	this.father.jpz.move(startI,startJ,endI,endJ);//调用方法走棋
	this.father.caiPan=true;//将caipan设为true  
	
}

private void busy() {
	this.father.caiPan=false;//将caipan设为false
	this.father.color=0;//将color设为0
	this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框架设为不可用
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(true);//将断开设为不可用
	this.father.jbChallenge.setEnabled(true);//将挑战设为不可用
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	JOptionPane.showMessageDialog(this.father,"对方繁忙！","提示",
			JOptionPane.ERROR_MESSAGE);
	this.tiaoZhanZhe=null;
	
}

private void buyong_yi(String msg) {
	this.father.caiPan=false;//将caipan设为false
	this.father.color=0;//将color设为0
	this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框架设为不可用
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(true);//将断开设为不可用
	this.father.jbChallenge.setEnabled(true);//将挑战设为不可用
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	JOptionPane.showMessageDialog(this.father,"对方拒绝你的挑战！","提示",
			JOptionPane.ERROR_MESSAGE);
	this.tiaoZhanZhe=null;
}

private void tong_yi() {
	this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框架设为不可用
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(!true);//将断开设为不可用
	this.father.jbChallenge.setEnabled(!true);//将挑战设为不可用
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	JOptionPane.showMessageDialog(this.father,"对方接受你的挑战！你先走棋(红棋)","提示",
			JOptionPane.ERROR_MESSAGE);
	
}

private void tiao_zhan(String msg) {
	try{
		String name=msg.substring(13);
		if(this.tiaoZhanZhe==null){
			tiaoZhanZhe=msg.substring(13);//将tiaozhanzhe的值赋为挑战者的necheng
			this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框架设为不可用
			this.father.jtfPort.setEnabled(false);
			this.father.jtfNickName.setEnabled(false);
			this.father.jbConnect.setEnabled(false);
			this.father.jbDisconnect.setEnabled(!true);//将断开设为不可用
			this.father.jbChallenge.setEnabled(!true);//将挑战设为不可用
			this.father.jbYChallenge.setEnabled(!false);
			this.father.jbNChallenge.setEnabled(!false);
			this.father.jbFail.setEnabled(false);
			JOptionPane.showMessageDialog(this.father,tiaoZhanZhe+"向你挑战","提示",
					JOptionPane.ERROR_MESSAGE);
		}else{
			this.dout.writeUTF("<#BUSY#>"+name);
		}
	}catch(IOException e){
		e.printStackTrace();
	}
	
}

public void server_down(){
	this.father.jtfHost.setEnabled(!false);//将用于输入主机名的文本框设为可用
	this.father.jtfPort.setEnabled(!false);;//将用于输入端口号的文本框设为可用
	this.father.jtfNickName.setEnabled(!false);//将用于输入昵称的文本框设为可用
	this.father.jbConnect.setEnabled(!false);//将"连接"按钮设为可用
	this.father.jbDisconnect.setEnabled(!true);//将"断开"按钮设为不可用
	this.father.jbChallenge.setEnabled(!true);//将"挑战"按钮设为不可用
	this.father.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
	this.father.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
	this.father.jbFail.setEnabled(false);//将"认输"按钮设为不可用
	this.flag=false;//终止该客户端代理线程
	father.cat=null;
	JOptionPane.showMessageDialog(this.father,"服务器停止！！！","提示",
	           JOptionPane.INFORMATION_MESSAGE);//给出服务器离开的提示信息
}
private void nick_list(String msg) {
	String s=msg.substring(13);//分解并得到用户信息
	String[] na=s.split("\\|"); //分割为子字符窜  转为数组
	Vector v=new Vector();//创建Vector对象
   for(int i=0;i<na.length;i++){
	   if(na[i].trim().length()!=0&&(!na[i].trim().equals(father.jtfNickName.getText().trim())));
	   v.add(na[i]);//将呢称全部添加到Vector
   }
	father.jcbNickList.setModel(new DefaultComboBoxModel(v));//重新设置下拉框
}

private void name_chongming() {
	try{//发送同意挑战的信息
		JOptionPane.showMessageDialog(this.father,"该玩家名称已经被占用，请重新填写","错误",
				JOptionPane.ERROR_MESSAGE);
		this.father.jtfHost.setEnabled(!false);//将用于输入主机名的文本框架设为不可用
		this.father.jtfPort.setEnabled(!false);
		this.father.jtfNickName.setEnabled(!false);
		this.father.jbConnect.setEnabled(!false);
		this.father.jbDisconnect.setEnabled(!true);//将断开设为不可用
		this.father.jbChallenge.setEnabled(!true);//将挑战设为不可用
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		din.close();//关闭数据输入流
		dout.close();//关闭数据输出流
		flag=false;//终止该客户端代理线程
		father.sc=null;//关闭socket
		father.sc.close();
		father.cat=null;
		
	}catch(Exception ee){
		ee.printStackTrace();
	
}
	
}
}
