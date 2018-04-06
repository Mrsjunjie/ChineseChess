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
	public static final Color bgColor=new Color(245,250,160);//棋盘的背景色
	public static final Color focusbg=new Color(242,242,242);//棋子选中后的背景色
	public static final Color focuschar=new Color(96,95,91);//棋子选中后的字符颜色
	public static final Color color1=new Color(249,183,173);//红方的颜色
	public static final Color color2=Color.white;//白方的颜色
	JLabel jlHost=new JLabel("主机名");//创建提示输入主机名的标签
	JLabel jlPort=new JLabel("端口号");////创建提示输入端口号标签
	JLabel jlNickName=new JLabel("昵    称");//创建提示输入昵称的标签
	JTextField jtfHost=new JTextField("127.0.0.1");//创建输入主机名的文本框，默认值是"127.0.0.1"
	JTextField jtfPort=new JTextField("9999");//创建输入端口号的文本框，默认值是9999
	JTextField jtfNickName=new JTextField("Play1");//创建输入昵称的文本框，默认值是Play1
	JButton jbConnect=new JButton("连  接");//创建"连接"按钮
	JButton jbDisconnect=new JButton("断  开");//创建"断开"按钮
	JButton jbFail=new JButton("认  输");//创建"认输"按钮
	JButton jbChallenge=new JButton("挑  战");//创建"挑战"按钮
	JComboBox jcbNickList=new JComboBox();//创建存放当前用户的下拉列表框
	JButton jbYChallenge=new JButton("接受挑战");//创建"接受挑战"按钮
	JButton jbNChallenge=new JButton("拒绝挑战");//创建"拒绝挑战"按钮
	int width=60;//设置棋盘两线之间的距离
	QiZi[][] qiZi=new QiZi[9][10];//创建棋子数组
	QiPan jpz=new QiPan(qiZi,width,this);//创建棋盘
	//JPanel jpz=new JPanel();//创建一个JPanel，暂时代替棋盘
	JPanel jpy=new JPanel();//创建一个JPanel
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//创建一个JSplitPane
	
	boolean caiPan=false;//可否走棋的标志位
	int color=0;//0 代表红棋，1代表白棋
	Socket sc;//声明Socket引用
	ClientAgentThread cat;//声明客户端代理线程的引用
	public XiangQi()
	{
		this.initialComponent();//初始化控件
		this.addListener();//为相应控件注册事件监听器
		this.initialState();//初始化状态
		this.initialQiZi();//初始化棋子
		this.initialFrame();//初始化窗体
	}
	public void initialComponent()
	{
		jpy.setLayout(null);//设为空布局
		this.jlHost.setBounds(10,10,50,20);
		jpy.add(this.jlHost);//添加"主机名"标签
		this.jtfHost.setBounds(70,10,80,20);
		jpy.add(this.jtfHost);//添加用于输入主机名的文本框
		this.jlPort.setBounds(10,40,50,20);
		jpy.add(this.jlPort);//添加"端口号"标签
		this.jtfPort.setBounds(70,40,80,20);
		jpy.add(this.jtfPort);//添加用于输入端口号的文本框
		this.jlNickName.setBounds(10,70,50,20);
		jpy.add(this.jlNickName);//添加"昵称"标签
		this.jtfNickName.setBounds(70,70,80,20);
		jpy.add(this.jtfNickName);//添加用于输入昵称的文本框
		this.jbConnect.setBounds(10,100,80,20);
		jpy.add(this.jbConnect);//添加"连接"按钮
		this.jbDisconnect.setBounds(100,100,80,20);
		jpy.add(this.jbDisconnect);//添加"断开"按钮
		this.jcbNickList.setBounds(20,130,130,20);
		jpy.add(this.jcbNickList);//添加用于显示当前用户的下拉列表框
		this.jbChallenge.setBounds(10,160,80,20);
		jpy.add(this.jbChallenge);//添加"挑战"按钮
		this.jbFail.setBounds(100,160,80,20);
		jpy.add(this.jbFail);//添加"认输"按钮
		this.jbYChallenge.setBounds(5,190,86,20);
		jpy.add(this.jbYChallenge);//添加"接受挑战"按钮
		this.jbNChallenge.setBounds(100,190,86,20);
		jpy.add(this.jbNChallenge);//添加"拒绝挑战"按钮
		jpz.setLayout(null);//将棋盘设为空布局
		jpz.setBounds(0,0,700,700);//设置大小
	}
	public void addListener()
	{
		this.jbConnect.addActionListener(this);//为"连接"按钮注册事件监听器
		this.jbDisconnect.addActionListener(this);//为"断开"按钮注册事件监听器
		this.jbChallenge.addActionListener(this);//为"挑战"按钮注册事件监听器
		this.jbFail.addActionListener(this);//为"认输"按钮注册事件监听器
		this.jbYChallenge.addActionListener(this);//为"同意挑战"按钮注册事件监听器
		this.jbNChallenge.addActionListener(this);//为"拒绝挑战"按钮注册事件监听器
	}
	public void initialState()
	{
		this.jbDisconnect.setEnabled(false);//将"断开"按钮设为不可用
		this.jbChallenge.setEnabled(false);//将"挑战"按钮设为不可用
		this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
		this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
		this.jbFail.setEnabled(false);//将"认输"按钮设为不可用
	}
	public void initialQiZi()
	{//初始化各个棋子
		qiZi[0][0]=new QiZi(color1,"車",0,0);
		qiZi[1][0]=new QiZi(color1,"馬",1,0);
		qiZi[2][0]=new QiZi(color1,"相",2,0);
		qiZi[3][0]=new QiZi(color1,"仕",3,0);
		qiZi[4][0]=new QiZi(color1,"帥",4,0);
		qiZi[5][0]=new QiZi(color1,"仕",5,0);
		qiZi[6][0]=new QiZi(color1,"相",6,0);
		qiZi[7][0]=new QiZi(color1,"馬",7,0);
		qiZi[8][0]=new QiZi(color1,"車",8,0);
		qiZi[1][2]=new QiZi(color1,"砲",1,2);
		qiZi[7][2]=new QiZi(color1,"砲",7,2);
		qiZi[0][3]=new QiZi(color1,"兵",0,3);
		qiZi[2][3]=new QiZi(color1,"兵",2,3);
		qiZi[4][3]=new QiZi(color1,"兵",4,3);
		qiZi[6][3]=new QiZi(color1,"兵",6,3);
		qiZi[8][3]=new QiZi(color1,"兵",8,3);
		qiZi[0][9]=new QiZi(color2,"車",0,9);
		qiZi[1][9]=new QiZi(color2,"馬",1,9);
		qiZi[2][9]=new QiZi(color2,"象",2,9);
		qiZi[3][9]=new QiZi(color2,"士",3,9);
		qiZi[4][9]=new QiZi(color2,"將",4,9);
		qiZi[5][9]=new QiZi(color2,"士",5,9);
		qiZi[6][9]=new QiZi(color2,"象",6,9);
		qiZi[7][9]=new QiZi(color2,"馬",7,9);
		qiZi[8][9]=new QiZi(color2,"車",8,9);
		qiZi[1][7]=new QiZi(color2,"炮",1,7);
		qiZi[7][7]=new QiZi(color2,"炮",7,7);
		qiZi[0][6]=new QiZi(color2,"卒",0,6);
		qiZi[2][6]=new QiZi(color2,"卒",2,6);
		qiZi[4][6]=new QiZi(color2,"卒",4,6);
		qiZi[6][6]=new QiZi(color2,"卒",6,6);
		qiZi[8][6]=new QiZi(color2,"卒",8,6);
	}
	public void initialFrame()
	{
		this.setTitle("中国象棋--客户端");//设置窗体标题
		Image image=new ImageIcon("ico.gif").getImage(); 
		this.setIconImage(image); //设置图标
		this.add(this.jsp);//添加JSplitPane
		jsp.setDividerLocation(730);//设置分割线位置及宽度
		jsp.setDividerSize(4);
		this.setBounds(100,30,930,730);//设置窗体大小
		this.setVisible(true);//设置可见性
		this.addWindowListener(//为窗体添加监听器
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(cat==null)//客户端代理线程为空，直接退出
					{
						System.exit(0);//退出
						return;
					}
					try
					{
						if(cat.tiaoZhanZhe!=null)//正在下棋中
						{
							try
							{
								//发送认输信息
								cat.dout.writeUTF("<#RENSHU#>"+cat.tiaoZhanZhe);
							}
							catch(Exception ee)
							{
								ee.printStackTrace();
							}
						}
						cat.dout.writeUTF("<#CLIENT_LEAVE#>");//向服务器发送离开信息
						cat.flag=false;//终止客户端代理线程
						cat=null;
						
					}
					catch(Exception ee)
					{
						ee.printStackTrace();
					}
					System.exit(0);//退出
				}
				
			}
			);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==this.jbConnect)
		{//单击"连接"按钮
			this.jbConnect_event();
		}
		else if(e.getSource()==this.jbDisconnect)
		{//当单击"断开"按钮时
			this.jbDisconnect_event();
		}
		else if(e.getSource()==this.jbChallenge)
		{//当单击"挑战"按钮时
			this.jbChallenge_event();
		}
		else if(e.getSource()==this.jbYChallenge)
		{//当单击"同意挑战"按钮时
			this.jbYChallenge_event();
		}
		else if(e.getSource()==this.jbNChallenge)
		{//当单击"拒绝挑战"按钮时
			this.jbNChallenge_event();
		}
		else if(e.getSource()==this.jbFail)
		{//当单击"认输"按钮时
			this.jbFail_event();
		}
	}
	public void jbConnect_event()
	{//对单击"连接"按钮事件的业务处理代码
		int port=0;
		try
		{//获得用户输入的断口号并转化为整型
			port=Integer.parseInt(this.jtfPort.getText().trim());
		}
		catch(Exception ee)
		{//不是整数，给出错误提示
			JOptionPane.showMessageDialog(this,"端口号只能是整数","错误",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(port>65535||port<0)
		{//端口号不合法，给出错误提示
			JOptionPane.showMessageDialog(this,"端口号只能是0-65535的整数","错误",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		String name=this.jtfNickName.getText().trim();//获得昵称
		if(name.length()==0)
		{//昵称为空，给出错误提示信息
			JOptionPane.showMessageDialog(this,"玩家姓名不能为空","错误",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			sc=new Socket(this.jtfHost.getText().trim(),port);//创建Socket对象
			cat=new ClientAgentThread(this);//创建客户端代理线程	
			cat.start();//启动客户端代理线程	
			this.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);//将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);//将"连接"按钮设为不可用
			this.jbDisconnect.setEnabled(true);//将"断开"按钮设为可用
			this.jbChallenge.setEnabled(true);//将"挑战"按钮设为可用
			this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
			this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
			this.jbFail.setEnabled(false);//将"认输"按钮设为不可用
			JOptionPane.showMessageDialog(this,"已连接到服务器","提示",
			            JOptionPane.INFORMATION_MESSAGE);//连接成功，给出提示信息	
		}
		catch(Exception ee)
		{
			JOptionPane.showMessageDialog(this,"连接服务器失败","错误",
			        JOptionPane.ERROR_MESSAGE);//连接失败，给出提示信息
			return;
		}
	}
	public void jbDisconnect_event()
	{//对单击"断开"按钮事件的业务处理代码
		try
		{
			this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");//向服务器发送离开的信息
			this.cat.flag=false;//终止客户端代理线程
			this.cat=null;
			this.jtfHost.setEnabled(!false);//将用于输入主机名的文本框设为可用
			this.jtfPort.setEnabled(!false);//将用于输入端口号的文本框设为可用
			this.jtfNickName.setEnabled(!false);//将用于输入昵称的文本框设为可用
			this.jbConnect.setEnabled(!false);//将"连接"按钮设为可用
			this.jbDisconnect.setEnabled(!true);//将"断开"按钮设为不可用
			this.jbChallenge.setEnabled(!true);//将"挑战"按钮设为不可用
			this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
			this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
			this.jbFail.setEnabled(false);//将"认输"按钮设为不可用
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	public void jbChallenge_event(){
		//获得用户选中的挑战对象
		Object o=this.jcbNickList.getSelectedItem();
		if(o==null||((String)o).equals("")) {
			JOptionPane.showMessageDialog(this,"请选择对方名字","错误",
			JOptionPane.ERROR_MESSAGE);//当未选中挑战对象，给出错误提示信息
		}
		else{
			String name2=(String)this.jcbNickList.getSelectedItem();//获得挑战对象
			try{
				this.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
				this.jtfPort.setEnabled(false);//将用于输入端口号的文本框设为不可用
				this.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
				this.jbConnect.setEnabled(false);//将"连接"按钮设为不可用
				this.jbDisconnect.setEnabled(!true);//将"断开"按钮设为不可用
				this.jbChallenge.setEnabled(!true);//将"挑战"按钮设为不可用
				this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
				this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
				this.jbFail.setEnabled(false);//将"认输"按钮设为不可用
				this.cat.tiaoZhanZhe=name2;//设置挑战对象
				this.caiPan=true;//将caiPan设为true
				this.color=0;//将color设为0
				this.cat.dout.writeUTF("<#TIAO_ZHAN#>"+name2);//发送挑战信息
				JOptionPane.showMessageDialog(this,"已提出挑战,请等待恢复...","提示",
				           JOptionPane.INFORMATION_MESSAGE);//给出信息发出的提示信息
			}
			catch(Exception ee){ee.printStackTrace();}
		}
	}
	public void jbYChallenge_event(){
		try{	//发送同意挑战的信息
			this.cat.dout.writeUTF("<#TONG_YI#>"+this.cat.tiaoZhanZhe);
			this.caiPan=false;//将caiPan设为false
			this.color=1;//将color设为1
			this.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);//将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);//将"连接"按钮设为不可用
			this.jbDisconnect.setEnabled(!true);//将"断开"按钮设为不可用
			this.jbChallenge.setEnabled(!true);//将"挑战"按钮设为不可用
			this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
			this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
			this.jbFail.setEnabled(!false);//将"认输"按钮设为可用
		}
		catch(Exception ee){ee.printStackTrace();}
	}
	public void jbNChallenge_event(){
		try{   //发送拒绝挑战的信息
			this.cat.dout.writeUTF("<#BUTONG_YI#>"+this.cat.tiaoZhanZhe);
			this.cat.tiaoZhanZhe=null;//将tiaoZhanZhe设为空
			this.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);//将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);//将"连接"按钮设为不可用
			this.jbDisconnect.setEnabled(true);//将"断开"按钮设为可用
			this.jbChallenge.setEnabled(true);//将"挑战"按钮设为可用
			this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
			this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
			this.jbFail.setEnabled(false);//将"认输"按钮设为不可用
		}
		catch(Exception ee){ee.printStackTrace();}
	}
	public void jbFail_event(){
		try{   //发送认输的信息
			this.cat.dout.writeUTF("<#RENSHU#>"+this.cat.tiaoZhanZhe);
			this.cat.tiaoZhanZhe=null;//将tiaoZhanZhe设为空
			this.color=0;//将color设为0
			this.caiPan=false;//将caiPan设为false
			this.next();//初始化下一局
			this.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);//将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);//将"连接"按钮设为不可用
			this.jbDisconnect.setEnabled(true);//将"断开"按钮设为可用
			this.jbChallenge.setEnabled(true);//将"挑战"按钮设为可用
			this.jbYChallenge.setEnabled(false);//将"接受挑战"按钮设为不可用
			this.jbNChallenge.setEnabled(false);//将"拒绝挑战"按钮设为不可用
			this.jbFail.setEnabled(false);//将"认输"按钮设为不可用
		}
		catch(Exception ee){ee.printStackTrace();}	
	}
	public void next(){
		for(int i=0;i<9;i++){//将棋子数组都置空
			for(int j=0;j<10;j++){
				this.qiZi[i][j]=null;
			}
		}
		this.caiPan=false;
		this.initialQiZi();//重新初始化棋子 
		this.repaint();//重绘
	}
	public static void main(String[] arges){
		new XiangQi();
	}

}
