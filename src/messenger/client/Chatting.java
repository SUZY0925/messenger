package messenger.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chatting implements ActionListener{
	private Toolkit tk;
	private Dimension screenSize,size;
	static SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
	static String date = format.format(new Date());
	private JFrame f;
	private JPanel p1,p2;
	private JScrollPane jc;
	static private JTextArea ta;
	private JTextField tf;
	private JButton send;
	private String myid, friendid;


	
	Chatting(String myid, String friendid) {
		this.myid = myid;
		this.friendid = friendid;

		
		f = new JFrame(this.friendid+"님과의 채팅방");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		
		f.setLayout(new FlowLayout());
		p1 = new JPanel();
		p1.setPreferredSize(new Dimension(450, 400));
		
		ta = new JTextArea(22,35);
		ta.setEditable(false);
		ta.append(myid+"님이 참가하였습니다. \n");
		jc = new JScrollPane(ta);
		jc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		p2 = new JPanel();
		tf = new JTextField(30);
		tf.requestFocus();
		send = new JButton("보내기");
		send.addActionListener(this);
		p1.add(jc);
		p2.add(tf); p2.add(send);
		f.add(p1);
		f.add(p2);
		
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		f.setSize(500, 500);
		size = f.getSize();
		f.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==send) {
			SimpleDateFormat f = new SimpleDateFormat("hh:mm:ss");
			String date = f.format(new Date());
			ta.append("> [보낸 시간 : "+date+"] 나 : "+tf.getText()+"\n");
			tf.setText("");
		}
	}
	
}
