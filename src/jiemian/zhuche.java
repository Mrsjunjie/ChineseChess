package jiemian;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class zhuche extends Dialog implements ActionListener
{   JLabel jt1,jt2,jt3;
    JTextField jf1,jf2,jf3;
    JButton bt1,bt2;

	public zhuche(Frame arg0, String arg1, boolean arg2)
	{
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
		this.setLayout(null);
		jt1=new JLabel("呢称：");
		this.jt1.setBounds(20,30,40,40);
		jf1=new JTextField();
		this.jf1.setBounds(75, 30, 80, 40);
		jt2=new JLabel("账号：");
		this.jt2.setBounds(20, 80, 40, 40);
		jf2=new JTextField();
		this.jf2.setBounds(75, 80, 80, 40);
		jt3=new JLabel("密码：");
		this.jt3.setBounds(20, 130, 40, 40);
		jf3=new JTextField();
		this.jf3.setBounds(75, 130, 80, 40);
		this.setTitle("注册表");
		bt1=new JButton("确定");
		bt1.addActionListener(this);
		this.bt1.setBounds(35, 195, 30, 30);
		bt2=new JButton("取消");
		bt2.addActionListener(this);
		this.bt2.setBounds(75, 195, 30, 30);
		Image ima=new ImageIcon().getImage();
		this.setIconImage(ima);
		this.setVisible(true);
		this.setLocation(200,260);
		this.setSize(250, 260);
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String  sj1=jf1.getText().toString();
		String  sj2=jf2.getText().toString();
		String  sj3=jf3.getText().toString();
		 PreparedStatement ps=null;
	     Connection  ct=null;
	     ResultSet rs=null;
		
		if(e.getSource().equals(bt1)){
			try{
			  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	          String url="jdbc:sqlserver://localhost:1433;databaseName=shihu";
	          String ss=("insert into xuesheng values(?,?,?)");
	          String user="sa";//sa超级管理员
	          String password="5282560";//密码
	          ct=DriverManager.getConnection( url,user,password);
			  ps=ct.prepareStatement(ss);
			  ps.setString(1, sj1);
			  ps.setString(2, sj2);
			  ps.setString(3, sj3);
			  ps.executeUpdate();
			  this.dispose();
			}
			catch(Exception e1){
			}  finally{
				  try{
					  if(rs!=null){
						  rs.close();
					  }
					  if(ps!=null){
						  ps.close();
					  }
					  if(ct!=null){
						  ct.close();
					  }
					  
				  }catch(Exception e2){
					  
				  }
			    }
			 
		}
		else if(e.getSource().equals(bt2)){
			 this.dispose();
		}
	}

	

}
