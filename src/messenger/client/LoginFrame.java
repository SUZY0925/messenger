package messenger.client;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import messenger.common.MemberDTO;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;
import messenger.server.RecordNotFoundException;

public class LoginFrame implements ActionListener, KeyListener{ 
	private Toolkit tk;
	private Dimension screenSize, size;
	private boolean enter = true;
	private JFrame f;
	private Container contentPane;
	private JPanel p1,p2;
	private JTextField idtf;
	private JPasswordField passtf;
	private JButton ok,join,find;
	private JLabel idlb, passlb, ImageLabel,lbtemp;
	private ImageIcon imageicon;
	private MemberDTO s;
	private JDialog jdialog;
	private Socket socket;

	LoginFrame() {
		f = new JFrame("Messenger Login");
		contentPane = f.getContentPane();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		f.setResizable(false);
		
		p1 = new JPanel();
		
		try {
			imageicon = new ImageIcon(
			new URL("https://stickershop.line-scdn.net/stickershop/v1/product/7382/LINEStorePC/main@2x.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ImageLabel = new JLabel(imageicon);
	//	ImageLabel.setBorder(BorderFactory.createRaisedBevelBorder());	// 튀어나와보이게하기
	//	ImageLabel.setBorder(BorderFactory.createLineBorder(Color.gray,1));	// 겉에 라인긋기
		p2 = new JPanel(new GridLayout(3,3));
		idlb = new JLabel("ID",SwingConstants.CENTER);
		idtf = new JTextField(10);
		idtf.addKeyListener(this);
		
		passlb = new JLabel("PASSWORD",SwingConstants.CENTER);
		passtf = new JPasswordField(10);
		passtf.addKeyListener(this);
		join = new JButton("JOIN");
		join.addActionListener(this);
		find = new JButton("FIND ID/PASSWD");
		find.addActionListener(this);
		ok = new JButton("LOGIN");
		ok.setBackground(Color.PINK);
		ok.addActionListener(this);
		ok.addKeyListener(this);
		
		lbtemp = new JLabel();
		
		p1.add(ImageLabel);
		p2.add(idlb); p2.add(idtf); p2.add(join);
		p2.add(passlb); p2.add(passtf); p2.add(find);
		p2.add(lbtemp);
		p2.add(ok);
		
		contentPane.add(p1, BorderLayout.CENTER); contentPane.add(p2,BorderLayout.SOUTH);
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		
		f.setSize(500, 350);
		size = f.getSize();
		f.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
		f.pack();
		f.setVisible(true);
		idtf.getText().substring(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == join) {	// 회원가입
			f.setVisible(false);
			new JoinFrame();
		} else if (e.getSource() == ok) {
			if (enter) {
				ok();
			}
		}
		 else if(e.getSource() == find) {	//아이디비밀번호 찾기
			f.setVisible(false);
			new Find();
		} 
	}
	
	void ok () {
		char[] passwdch = passtf.getPassword();
		String passwd = new String(passwdch, 0, passwdch.length);

		if (idtf.getText().equals("") || passwd.equals("")) { // 아이디나 비밀번호가 null값일때..!
			JOptionPane.showMessageDialog(jdialog, "아이디/비밀번호를 입력하세요.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
		} else {
			s = new MemberDTO();
			s.setId(idtf.getText());
			s.setPasswd(passwd);
			try {
				MessengerDAO dim = new MessengerDAOimpl();
				if (dim.login(s.getId(), s.getPasswd()) == 1)
					 // cnt가 1이고 RecordNotFoundException이 일어나지 않았을 때
				{
					 JOptionPane.showMessageDialog(jdialog,s.getId()+"님이 접속하셨습니다.", "=알림=",
					 JOptionPane.INFORMATION_MESSAGE);
					 new Messenger(idtf.getText());
					f.setVisible(false);
					enter = false;
				}
			} catch (RecordNotFoundException e1) {
				JOptionPane.showMessageDialog(jdialog, "아이디/비밀번호를 확인하세요.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {// 로그인
			if (enter) {
				ok();
			}
		}
	}
	public void keyReleased(KeyEvent e) {} public void keyTyped(KeyEvent e) {}
	
	
	public static void main(String[] args) {
		new LoginFrame();
	}
	

}
