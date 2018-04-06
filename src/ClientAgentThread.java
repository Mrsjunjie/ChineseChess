import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread
{ 
	XiangQi father;//����XiangQi������
	boolean flag=true;
	DataInputStream  din;
	DataOutputStream  dout;
	
	String tiaoZhanZhe;//���ڼ�¼������ս�Ķ���
	public ClientAgentThread(XiangQi father){
		this.father=father;
		try{
			din=new DataInputStream(father.sc.getInputStream());//������������  ����������
			dout=new DataOutputStream(father.sc.getOutputStream());//
			String name=father.jtfNickName.getText().trim();//����س�
			dout.writeUTF("<#NICK_NAME#>"+name);//�����سƵ�������
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
public void run(){
	while(flag){
		try{
			String msg=din.readUTF().trim();//��÷�������������Ϣo[
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
			}else if(msg.startsWith("<#SERVER_DOWN#>")){//���յ��������뿪����Ϣ
				this.server_down();
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
}
private void renshu() {
	
	JOptionPane.showMessageDialog(this.father,"��ϲ�㣬���ʤ���Է�����","��ʾ",
			JOptionPane.ERROR_MESSAGE);//������ʤ��Ϣ
	this.tiaoZhanZhe=null;//����ս����Ϊ��
	this.father.next();//������һ��
	this.father.caiPan=false;//��caipan��Ϊfalse
	this.father.color=0;//��color��Ϊ0
	this.father.jtfHost.setEnabled(false);//�������������������ı������Ϊ������
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(true);//���Ͽ���Ϊ������
	this.father.jbChallenge.setEnabled(true);//����ս��Ϊ������
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	
}

private void move(String msg) {
	int length=msg.length();
	int startI=Integer.parseInt(msg.substring(length-4,length-3));//������ӵ�ԭʼλ��
	int startJ=Integer.parseInt(msg.substring(length-3,length-2));
	int endI=Integer.parseInt(msg.substring(length-2,length-1));//����ߺ��λ��
	int endJ=Integer.parseInt(msg.substring(length-1));
	this.father.jpz.move(startI,startJ,endI,endJ);//���÷�������
	this.father.caiPan=true;//��caipan��Ϊtrue  
	
}

private void busy() {
	this.father.caiPan=false;//��caipan��Ϊfalse
	this.father.color=0;//��color��Ϊ0
	this.father.jtfHost.setEnabled(false);//�������������������ı������Ϊ������
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(true);//���Ͽ���Ϊ������
	this.father.jbChallenge.setEnabled(true);//����ս��Ϊ������
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	JOptionPane.showMessageDialog(this.father,"�Է���æ��","��ʾ",
			JOptionPane.ERROR_MESSAGE);
	this.tiaoZhanZhe=null;
	
}

private void buyong_yi(String msg) {
	this.father.caiPan=false;//��caipan��Ϊfalse
	this.father.color=0;//��color��Ϊ0
	this.father.jtfHost.setEnabled(false);//�������������������ı������Ϊ������
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(true);//���Ͽ���Ϊ������
	this.father.jbChallenge.setEnabled(true);//����ս��Ϊ������
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	JOptionPane.showMessageDialog(this.father,"�Է��ܾ������ս��","��ʾ",
			JOptionPane.ERROR_MESSAGE);
	this.tiaoZhanZhe=null;
}

private void tong_yi() {
	this.father.jtfHost.setEnabled(false);//�������������������ı������Ϊ������
	this.father.jtfPort.setEnabled(false);
	this.father.jtfNickName.setEnabled(false);
	this.father.jbConnect.setEnabled(false);
	this.father.jbDisconnect.setEnabled(!true);//���Ͽ���Ϊ������
	this.father.jbChallenge.setEnabled(!true);//����ս��Ϊ������
	this.father.jbYChallenge.setEnabled(false);
	this.father.jbNChallenge.setEnabled(false);
	this.father.jbFail.setEnabled(false);
	JOptionPane.showMessageDialog(this.father,"�Է����������ս����������(����)","��ʾ",
			JOptionPane.ERROR_MESSAGE);
	
}

private void tiao_zhan(String msg) {
	try{
		String name=msg.substring(13);
		if(this.tiaoZhanZhe==null){
			tiaoZhanZhe=msg.substring(13);//��tiaozhanzhe��ֵ��Ϊ��ս�ߵ�necheng
			this.father.jtfHost.setEnabled(false);//�������������������ı������Ϊ������
			this.father.jtfPort.setEnabled(false);
			this.father.jtfNickName.setEnabled(false);
			this.father.jbConnect.setEnabled(false);
			this.father.jbDisconnect.setEnabled(!true);//���Ͽ���Ϊ������
			this.father.jbChallenge.setEnabled(!true);//����ս��Ϊ������
			this.father.jbYChallenge.setEnabled(!false);
			this.father.jbNChallenge.setEnabled(!false);
			this.father.jbFail.setEnabled(false);
			JOptionPane.showMessageDialog(this.father,tiaoZhanZhe+"������ս","��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}else{
			this.dout.writeUTF("<#BUSY#>"+name);
		}
	}catch(IOException e){
		e.printStackTrace();
	}
	
}

public void server_down(){
	this.father.jtfHost.setEnabled(!false);//�������������������ı�����Ϊ����
	this.father.jtfPort.setEnabled(!false);;//����������˿ںŵ��ı�����Ϊ����
	this.father.jtfNickName.setEnabled(!false);//�����������ǳƵ��ı�����Ϊ����
	this.father.jbConnect.setEnabled(!false);//��"����"��ť��Ϊ����
	this.father.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
	this.father.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
	this.father.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
	this.father.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
	this.father.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
	this.flag=false;//��ֹ�ÿͻ��˴����߳�
	father.cat=null;
	JOptionPane.showMessageDialog(this.father,"������ֹͣ������","��ʾ",
	           JOptionPane.INFORMATION_MESSAGE);//�����������뿪����ʾ��Ϣ
}
private void nick_list(String msg) {
	String s=msg.substring(13);//�ֽⲢ�õ��û���Ϣ
	String[] na=s.split("\\|"); //�ָ�Ϊ���ַ���  תΪ����
	Vector v=new Vector();//����Vector����
   for(int i=0;i<na.length;i++){
	   if(na[i].trim().length()!=0&&(!na[i].trim().equals(father.jtfNickName.getText().trim())));
	   v.add(na[i]);//���س�ȫ����ӵ�Vector
   }
	father.jcbNickList.setModel(new DefaultComboBoxModel(v));//��������������
}

private void name_chongming() {
	try{//����ͬ����ս����Ϣ
		JOptionPane.showMessageDialog(this.father,"����������Ѿ���ռ�ã���������д","����",
				JOptionPane.ERROR_MESSAGE);
		this.father.jtfHost.setEnabled(!false);//�������������������ı������Ϊ������
		this.father.jtfPort.setEnabled(!false);
		this.father.jtfNickName.setEnabled(!false);
		this.father.jbConnect.setEnabled(!false);
		this.father.jbDisconnect.setEnabled(!true);//���Ͽ���Ϊ������
		this.father.jbChallenge.setEnabled(!true);//����ս��Ϊ������
		this.father.jbYChallenge.setEnabled(false);
		this.father.jbNChallenge.setEnabled(false);
		this.father.jbFail.setEnabled(false);
		din.close();//�ر�����������
		dout.close();//�ر����������
		flag=false;//��ֹ�ÿͻ��˴����߳�
		father.sc=null;//�ر�socket
		father.sc.close();
		father.cat=null;
		
	}catch(Exception ee){
		ee.printStackTrace();
	
}
	
}
}
