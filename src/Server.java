import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Server  extends JFrame implements  ActionListener
{  
	JLabel jlPort=new JLabel("�� �� ��");//������ʾ����˿ںű�ǩ
	JTextField jtfPort=new JTextField("9999");//��������˿ںŵ��ı���
	JButton jbStart=new JButton("����");//����"����"��ť
	JButton jbStop=new JButton("�ر�");//����"�ر�"��ť
	JPanel jps=new JPanel();//����һ��JPanel����
	JList jlUserOnline=new JList();//����������ʾ��ǰ�û���JList
	JScrollPane jspx=new JScrollPane(jlUserOnline);//����ʾ��ǰ�û���JList����JScrollPane��
	JSplitPane jspz=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jspx,jps);//����JSplitPane����
	ServerSocket ss;//����ServerSocket����
	ServerThread st;//����ServerThread����
	Vector onlineList=new Vector();//������ŵ�ǰ�����û���Vector����
	public Server()
	{
		this.initialComponent();//��ʼ���ؼ�
		this.addListener();//Ϊ��Ӧ�Ŀؼ�ע���¼�������
		this.initialFrame();//��ʼ������
	}
	public void initialComponent()
	{
		jps.setLayout(null);//��Ϊ�ղ���
		jlPort.setBounds(20,20,50,20);
		jps.add(jlPort);//���������ʾ����˿ںŵı�ǩ
		this.jtfPort.setBounds(85,20,60,20);
		jps.add(this.jtfPort);//�����������˿ںŵ��ı���
		this.jbStart.setBounds(18,50,60,20);
		jps.add(this.jbStart);//���"��ʼ"��ť
		this.jbStop.setBounds(85,50,60,20);
		jps.add(this.jbStop);//���"�ر�"��ť
		this.jbStop.setEnabled(false);//��"�ر�"��ť��Ϊ������
	}
	public void addListener()
	{
		this.jbStart.addActionListener(this);//Ϊ"��ʼ"��ťע���¼�������
		this.jbStop.addActionListener(this);//Ϊ"�ر�"��ťע���¼�������
	}
	public void initialFrame()
	{
		this.setTitle("����--��������");//���ô������
		Image image=new ImageIcon("ico.gif").getImage();
		this.setIconImage(image);
		this.add(jspz);//��JSplitPane��ӵ�������
		jspz.setDividerLocation(250);
		jspz.setDividerSize(4);//���÷ָ��ߵ�λ�úͿ��
		this.setBounds(20,20,420,320);
		this.setVisible(true);//���ÿɼ���
		this.addWindowListener(//Ϊ����ر��¼�ע�������
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(st==null)//���������߳�Ϊ��ʱֱ���˳�
					{
						System.exit(0);//�˳�
						return;
					}
					try
					{
						Vector v=onlineList;
						int size=v.size();
						for(int i=0;i<size;i++)
						{
							//����Ϊ��ʱ���������û�����������Ϣ
							ServerAgentThread tempSat=(ServerAgentThread)v.get(i);
							tempSat.dout.writeUTF("<#SERVER_DOWN#>");
							tempSat.flag=false;//��ֹ�����������߳�
						}
						st.flag=false;//��ֹ�������߳�
						st=null;
						ss.close();//�ر�ServerSocket
						v.clear();//�������û��б����
						refreshList();//ˢ���б�	
					}
					catch(Exception ee)
					{
						ee.printStackTrace();
					}
					System.exit(0);//�˳�
				}
			}
			);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==this.jbStart)
		{//������"����"��ťʱ
			this.jbStart_event();
		}
		else if(e.getSource()==this.jbStop)
		{//����"�ر�"��ť��
			this.jbStop_event();
		}
	}
	public void jbStart_event()
	{
		//����"����"��ť��ҵ�������
		int port=0;
		try
		{
			//����û�����Ķ˿ںţ���ת��Ϊ����
			port=Integer.parseInt(this.jtfPort.getText().trim());
		}
		catch(Exception ee)
		{//�˿ںŲ���������������ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ��������","����",
			                               JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(port>65535||port<0)
		{//�ϿںŲ��Ϸ���������ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ����0-65535������","����",
			                               JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			this.jbStart.setEnabled(false);//����ʼ��ť��Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jbStop.setEnabled(true);//��ֹͣ��ť��Ϊ����
			ss=new ServerSocket(port);//����ServerSocket����
			st=new ServerThread(this);//�����������߳�
			st.start();//�����������߳�
			//���������������ɹ�����ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"�����������ɹ�","��ʾ",
			                               JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception ee)
		{
			//��������������ʧ�ܵ���ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"����������ʧ��","����",
			                               JOptionPane.ERROR_MESSAGE);
			this.jbStart.setEnabled(true);//����ʼ��ť��Ϊ����
			this.jtfPort.setEnabled(true);//����������˿ںŵ��ı�����Ϊ����
			this.jbStop.setEnabled(false);//��ֹͣ��ť��Ϊ������
		}
	}
	public void jbStop_event()
	{
		//����"�ر�"��ť��ҵ�������
		try
		{
			Vector v=onlineList;
			int size=v.size();
			for(int i=0;i<size;i++)
			{//�������û�����������Ϣ
				ServerAgentThread tempSat=(ServerAgentThread)v.get(i);
				tempSat.dout.writeUTF("<#SERVER_DOWN#>");
				tempSat.flag=false;//�رշ����������߳�
			}
			st.flag=false;//�رշ������߳�
			st=null;
			ss.close();//�ر�ServerSocket
			v.clear();//�������û��б����
			refreshList();//ˢ���б�	
			this.jbStart.setEnabled(true);//����ʼ��ť��Ϊ����
	    	this.jtfPort.setEnabled(true);//����������˿ںŵ��ı�����Ϊ����
		    this.jbStop.setEnabled(false);//��ֹͣ��ť��Ϊ������
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	public void refreshList()
	{
		//���������û��б��ҵ�������
		Vector v=new Vector();
		int size=this.onlineList.size();
		for(int i=0;i<size;i++)
		{//���������б�
			ServerAgentThread tempSat=(ServerAgentThread)this.onlineList.get(i);
			String temps=tempSat.sc.getInetAddress().toString();
			temps=temps+"|"+tempSat.getName();//���������Ϣ
		
			v.add(temps);//��ӵ�Vector��
			
		}
		this.jlUserOnline.setListData(v);//�����б�����
		
	}
	public static void main(String args[]){
		new Server();
	}

}
