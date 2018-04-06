package jiemian;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class zhujie extends JFrame implements ActionListener
{   JLabel jb1,jb2;
    JTextField jf1,jf2;
    JPasswordField tp1;
    JButton btn1,btn2;
    
    public zhujie(){
    	super("µÇÂ¼");
    	this.setLayout(null);
    	jb1=new JLabel("ÕËºÅ£º");
    	
    	jb2=new JLabel("ÃÜÂë£º");
    	jf1=new JTextField();
    	tp1=new JPasswordField();
    	jb1.setBounds(30, 40, 40, 20);
    	jb2.setBounds(30, 80, 40, 20);
    	jf1.setBounds(85, 40, 80, 20);
    	tp1.setBounds(85, 80, 80, 20);
    	this.add(jb1);this.add(jf1);
    	this.add(jb2);this.add(tp1);
    	btn1=new JButton("È·¶¨");
    	btn2=new JButton("×¢²á");
    	btn1.setBounds(50, 110, 60, 20);
    	btn2.setBounds(120, 110, 60, 20);
    	this.add(btn1);this.add(btn2);
    	btn1.addActionListener(this);
    	btn2.addActionListener(this);
    	this.setVisible(true);
    	this.setSize(250, 230);
    	this.setLocation(200, 200);
    	
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		String j1=jf1.getText().toString();
		String j2=jf2.getText().toString();
		if(e.getSource().equals(btn1)){
			this.btn2.setEnabled(false);
			this.dispose();
		}
		if(e.getSource().equals(btn2)){
			this.btn1.setEnabled(false);
		}
		
	}
   public static void main(String arges[]){
	   new zhujie();
   }
}
