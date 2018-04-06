import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class XiangQi extends JFrame  implements ActionListener
{  
	public static final Color bgColor=new Color(245,250,160);//���̵ı���ɫ
	public static final Color focusbg=new Color(242,242,242);//����ѡ�к�ı���ɫ
	public static final Color focuschar=new Color(96,95,91);//����ѡ�к���ַ���ɫ
	public static final Color color1=new Color(249,183,173);//�췽����ɫ
	public static final Color color2=Color.white;//�׷�����ɫ
	JLabel jlHost=new JLabel("������");//������ʾ�����������ı�ǩ
	JLabel jlPort=new JLabel("�˿ں�");////������ʾ����˿ںű�ǩ
	JLabel jlNickName=new JLabel("��    ��");//������ʾ�����ǳƵı�ǩ
	JTextField jtfHost=new JTextField("127.0.0.1");//�����������������ı���Ĭ��ֵ��"127.0.0.1"
	JTextField jtfPort=new JTextField("9999");//��������˿ںŵ��ı���Ĭ��ֵ��9999
	JTextField jtfNickName=new JTextField("Play1");//���������ǳƵ��ı���Ĭ��ֵ��Play1
	JButton jbConnect=new JButton("��  ��");//����"����"��ť
	JButton jbDisconnect=new JButton("��  ��");//����"�Ͽ�"��ť
	JButton jbFail=new JButton("��  ��");//����"����"��ť
	JButton jbChallenge=new JButton("��  ս");//����"��ս"��ť
	JComboBox jcbNickList=new JComboBox();//������ŵ�ǰ�û��������б��
	JButton jbYChallenge=new JButton("������ս");//����"������ս"��ť
	JButton jbNChallenge=new JButton("�ܾ���ս");//����"�ܾ���ս"��ť
	int width=60;//������������֮��ľ���
	QiZi[][] qiZi=new QiZi[9][10];//������������
	QiPan jpz=new QiPan(qiZi,width,this);//��������
	//JPanel jpz=new JPanel();//����һ��JPanel����ʱ��������
	JPanel jpy=new JPanel();//����һ��JPanel
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//����һ��JSplitPane
	
	boolean caiPan=false;//�ɷ�����ı�־λ
	int color=0;//0 ������壬1�������
	Socket sc;//����Socket����
	ClientAgentThread cat;//�����ͻ��˴����̵߳�����
	public XiangQi()
	{
		this.initialComponent();//��ʼ���ؼ�
		this.addListener();//Ϊ��Ӧ�ؼ�ע���¼�������
		this.initialState();//��ʼ��״̬
		this.initialQiZi();//��ʼ������
		this.initialFrame();//��ʼ������
	}
	public void initialComponent()
	{
		jpy.setLayout(null);//��Ϊ�ղ���
		this.jlHost.setBounds(10,10,50,20);
		jpy.add(this.jlHost);//���"������"��ǩ
		this.jtfHost.setBounds(70,10,80,20);
		jpy.add(this.jtfHost);//��������������������ı���
		this.jlPort.setBounds(10,40,50,20);
		jpy.add(this.jlPort);//���"�˿ں�"��ǩ
		this.jtfPort.setBounds(70,40,80,20);
		jpy.add(this.jtfPort);//�����������˿ںŵ��ı���
		this.jlNickName.setBounds(10,70,50,20);
		jpy.add(this.jlNickName);//���"�ǳ�"��ǩ
		this.jtfNickName.setBounds(70,70,80,20);
		jpy.add(this.jtfNickName);//������������ǳƵ��ı���
		this.jbConnect.setBounds(10,100,80,20);
		jpy.add(this.jbConnect);//���"����"��ť
		this.jbDisconnect.setBounds(100,100,80,20);
		jpy.add(this.jbDisconnect);//���"�Ͽ�"��ť
		this.jcbNickList.setBounds(20,130,130,20);
		jpy.add(this.jcbNickList);//���������ʾ��ǰ�û��������б��
		this.jbChallenge.setBounds(10,160,80,20);
		jpy.add(this.jbChallenge);//���"��ս"��ť
		this.jbFail.setBounds(100,160,80,20);
		jpy.add(this.jbFail);//���"����"��ť
		this.jbYChallenge.setBounds(5,190,86,20);
		jpy.add(this.jbYChallenge);//���"������ս"��ť
		this.jbNChallenge.setBounds(100,190,86,20);
		jpy.add(this.jbNChallenge);//���"�ܾ���ս"��ť
		jpz.setLayout(null);//��������Ϊ�ղ���
		jpz.setBounds(0,0,700,700);//���ô�С
	}
	public void addListener()
	{
		this.jbConnect.addActionListener(this);//Ϊ"����"��ťע���¼�������
		this.jbDisconnect.addActionListener(this);//Ϊ"�Ͽ�"��ťע���¼�������
		this.jbChallenge.addActionListener(this);//Ϊ"��ս"��ťע���¼�������
		this.jbFail.addActionListener(this);//Ϊ"����"��ťע���¼�������
		this.jbYChallenge.addActionListener(this);//Ϊ"ͬ����ս"��ťע���¼�������
		this.jbNChallenge.addActionListener(this);//Ϊ"�ܾ���ս"��ťע���¼�������
	}
	public void initialState()
	{
		this.jbDisconnect.setEnabled(false);//��"�Ͽ�"��ť��Ϊ������
		this.jbChallenge.setEnabled(false);//��"��ս"��ť��Ϊ������
		this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
		this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
		this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
	}
	public void initialQiZi()
	{//��ʼ����������
		qiZi[0][0]=new QiZi(color1,"܇",0,0);
		qiZi[1][0]=new QiZi(color1,"�R",1,0);
		qiZi[2][0]=new QiZi(color1,"��",2,0);
		qiZi[3][0]=new QiZi(color1,"��",3,0);
		qiZi[4][0]=new QiZi(color1,"��",4,0);
		qiZi[5][0]=new QiZi(color1,"��",5,0);
		qiZi[6][0]=new QiZi(color1,"��",6,0);
		qiZi[7][0]=new QiZi(color1,"�R",7,0);
		qiZi[8][0]=new QiZi(color1,"܇",8,0);
		qiZi[1][2]=new QiZi(color1,"�h",1,2);
		qiZi[7][2]=new QiZi(color1,"�h",7,2);
		qiZi[0][3]=new QiZi(color1,"��",0,3);
		qiZi[2][3]=new QiZi(color1,"��",2,3);
		qiZi[4][3]=new QiZi(color1,"��",4,3);
		qiZi[6][3]=new QiZi(color1,"��",6,3);
		qiZi[8][3]=new QiZi(color1,"��",8,3);
		qiZi[0][9]=new QiZi(color2,"܇",0,9);
		qiZi[1][9]=new QiZi(color2,"�R",1,9);
		qiZi[2][9]=new QiZi(color2,"��",2,9);
		qiZi[3][9]=new QiZi(color2,"ʿ",3,9);
		qiZi[4][9]=new QiZi(color2,"��",4,9);
		qiZi[5][9]=new QiZi(color2,"ʿ",5,9);
		qiZi[6][9]=new QiZi(color2,"��",6,9);
		qiZi[7][9]=new QiZi(color2,"�R",7,9);
		qiZi[8][9]=new QiZi(color2,"܇",8,9);
		qiZi[1][7]=new QiZi(color2,"��",1,7);
		qiZi[7][7]=new QiZi(color2,"��",7,7);
		qiZi[0][6]=new QiZi(color2,"��",0,6);
		qiZi[2][6]=new QiZi(color2,"��",2,6);
		qiZi[4][6]=new QiZi(color2,"��",4,6);
		qiZi[6][6]=new QiZi(color2,"��",6,6);
		qiZi[8][6]=new QiZi(color2,"��",8,6);
	}
	public void initialFrame()
	{
		this.setTitle("�й�����--�ͻ���");//���ô������
		Image image=new ImageIcon("ico.gif").getImage(); 
		this.setIconImage(image); //����ͼ��
		this.add(this.jsp);//���JSplitPane
		jsp.setDividerLocation(730);//���÷ָ���λ�ü����
		jsp.setDividerSize(4);
		this.setBounds(100,30,930,730);//���ô����С
		this.setVisible(true);//���ÿɼ���
		this.addWindowListener(//Ϊ������Ӽ�����
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(cat==null)//�ͻ��˴����߳�Ϊ�գ�ֱ���˳�
					{
						System.exit(0);//�˳�
						return;
					}
					try
					{
						if(cat.tiaoZhanZhe!=null)//����������
						{
							try
							{
								//����������Ϣ
								cat.dout.writeUTF("<#RENSHU#>"+cat.tiaoZhanZhe);
							}
							catch(Exception ee)
							{
								ee.printStackTrace();
							}
						}
						cat.dout.writeUTF("<#CLIENT_LEAVE#>");//������������뿪��Ϣ
						cat.flag=false;//��ֹ�ͻ��˴����߳�
						cat=null;
						
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
		if(e.getSource()==this.jbConnect)
		{//����"����"��ť
			this.jbConnect_event();
		}
		else if(e.getSource()==this.jbDisconnect)
		{//������"�Ͽ�"��ťʱ
			this.jbDisconnect_event();
		}
		else if(e.getSource()==this.jbChallenge)
		{//������"��ս"��ťʱ
			this.jbChallenge_event();
		}
		else if(e.getSource()==this.jbYChallenge)
		{//������"ͬ����ս"��ťʱ
			this.jbYChallenge_event();
		}
		else if(e.getSource()==this.jbNChallenge)
		{//������"�ܾ���ս"��ťʱ
			this.jbNChallenge_event();
		}
		else if(e.getSource()==this.jbFail)
		{//������"����"��ťʱ
			this.jbFail_event();
		}
	}
	public void jbConnect_event()
	{//�Ե���"����"��ť�¼���ҵ�������
		int port=0;
		try
		{//����û�����ĶϿںŲ�ת��Ϊ����
			port=Integer.parseInt(this.jtfPort.getText().trim());
		}
		catch(Exception ee)
		{//��������������������ʾ
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ��������","����",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(port>65535||port<0)
		{//�˿ںŲ��Ϸ�������������ʾ
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ����0-65535������","����",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		String name=this.jtfNickName.getText().trim();//����ǳ�
		if(name.length()==0)
		{//�ǳ�Ϊ�գ�����������ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"�����������Ϊ��","����",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			sc=new Socket(this.jtfHost.getText().trim(),port);//����Socket����
			cat=new ClientAgentThread(this);//�����ͻ��˴����߳�	
			cat.start();//�����ͻ��˴����߳�	
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(true);//��"�Ͽ�"��ť��Ϊ����
			this.jbChallenge.setEnabled(true);//��"��ս"��ť��Ϊ����
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
			JOptionPane.showMessageDialog(this,"�����ӵ�������","��ʾ",
			            JOptionPane.INFORMATION_MESSAGE);//���ӳɹ���������ʾ��Ϣ	
		}
		catch(Exception ee)
		{
			JOptionPane.showMessageDialog(this,"���ӷ�����ʧ��","����",
			        JOptionPane.ERROR_MESSAGE);//����ʧ�ܣ�������ʾ��Ϣ
			return;
		}
	}
	public void jbDisconnect_event()
	{//�Ե���"�Ͽ�"��ť�¼���ҵ�������
		try
		{
			this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");//������������뿪����Ϣ
			this.cat.flag=false;//��ֹ�ͻ��˴����߳�
			this.cat=null;
			this.jtfHost.setEnabled(!false);//�������������������ı�����Ϊ����
			this.jtfPort.setEnabled(!false);//����������˿ںŵ��ı�����Ϊ����
			this.jtfNickName.setEnabled(!false);//�����������ǳƵ��ı�����Ϊ����
			this.jbConnect.setEnabled(!false);//��"����"��ť��Ϊ����
			this.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
			this.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	public void jbChallenge_event(){
		//����û�ѡ�е���ս����
		Object o=this.jcbNickList.getSelectedItem();
		if(o==null||((String)o).equals("")) {
			JOptionPane.showMessageDialog(this,"��ѡ��Է�����","����",
			JOptionPane.ERROR_MESSAGE);//��δѡ����ս���󣬸���������ʾ��Ϣ
		}
		else{
			String name2=(String)this.jcbNickList.getSelectedItem();//�����ս����
			try{
				this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
				this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
				this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
				this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
				this.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
				this.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
				this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
				this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
				this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
				this.cat.tiaoZhanZhe=name2;//������ս����
				this.caiPan=true;//��caiPan��Ϊtrue
				this.color=0;//��color��Ϊ0
				this.cat.dout.writeUTF("<#TIAO_ZHAN#>"+name2);//������ս��Ϣ
				JOptionPane.showMessageDialog(this,"�������ս,��ȴ��ָ�...","��ʾ",
				           JOptionPane.INFORMATION_MESSAGE);//������Ϣ��������ʾ��Ϣ
			}
			catch(Exception ee){ee.printStackTrace();}
		}
	}
	public void jbYChallenge_event(){
		try{	//����ͬ����ս����Ϣ
			this.cat.dout.writeUTF("<#TONG_YI#>"+this.cat.tiaoZhanZhe);
			this.caiPan=false;//��caiPan��Ϊfalse
			this.color=1;//��color��Ϊ1
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
			this.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(!false);//��"����"��ť��Ϊ����
		}
		catch(Exception ee){ee.printStackTrace();}
	}
	public void jbNChallenge_event(){
		try{   //���;ܾ���ս����Ϣ
			this.cat.dout.writeUTF("<#BUTONG_YI#>"+this.cat.tiaoZhanZhe);
			this.cat.tiaoZhanZhe=null;//��tiaoZhanZhe��Ϊ��
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(true);//��"�Ͽ�"��ť��Ϊ����
			this.jbChallenge.setEnabled(true);//��"��ս"��ť��Ϊ����
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
		}
		catch(Exception ee){ee.printStackTrace();}
	}
	public void jbFail_event(){
		try{   //�����������Ϣ
			this.cat.dout.writeUTF("<#RENSHU#>"+this.cat.tiaoZhanZhe);
			this.cat.tiaoZhanZhe=null;//��tiaoZhanZhe��Ϊ��
			this.color=0;//��color��Ϊ0
			this.caiPan=false;//��caiPan��Ϊfalse
			this.next();//��ʼ����һ��
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(true);//��"�Ͽ�"��ť��Ϊ����
			this.jbChallenge.setEnabled(true);//��"��ս"��ť��Ϊ����
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
		}
		catch(Exception ee){ee.printStackTrace();}	
	}
	public void next(){
		for(int i=0;i<9;i++){//���������鶼�ÿ�
			for(int j=0;j<10;j++){
				this.qiZi[i][j]=null;
			}
		}
		this.caiPan=false;
		this.initialQiZi();//���³�ʼ������ 
		this.repaint();//�ػ�
	}
	public static void main(String[] arges){
		new XiangQi();
	}

}
