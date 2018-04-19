package messenger.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import messenger.common.MemberDTO;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;

public class WithDraw implements ActionListener{
	private Toolkit tk;
	private Dimension screenSize, size;
	private JDialog d;
	private JPanel p1;
	private JTextField idtf;
	private JPasswordField passtf;
	private JButton ok, cancel;
	private String id;
	private MemberDTO s;
	private MessengerDAO dim;
	private JFrame frame;
	
	WithDraw(JFrame frame, String id) {
		this.frame=frame;
		this.id = id;
		
		d = new JDialog(frame, "회원 탈퇴", true);
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setResizable(false);
		p1 = new JPanel();
		p1.setLayout(new GridLayout(3,1));
		
		idtf = new JTextField(10);
		idtf.setText(id);
		idtf.setEditable(false);
		passtf = new JPasswordField(10);
		ok = new JButton("탈퇴하기");
		ok.addActionListener(this);
		cancel = new JButton("뒤로가기");
		cancel.addActionListener(this);
		
		d.add(new JLabel("탈퇴하시려면 비밀번호를 입력하세요.",SwingConstants.CENTER), BorderLayout.NORTH);
		p1.add(new JLabel("아이디")); p1.add(idtf);
		p1.add(new JLabel("비밀번호")); p1.add(passtf);
		p1.add(ok);	p1.add(cancel);
		d.add(p1, BorderLayout.CENTER);
		
		d.setSize(400, 130);
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		
		size = d.getSize();
		d.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
		d.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok) {
			try {
				dim = new MessengerDAOimpl();
				
				if (dim.deleteMember(id, passtf.getText()) == 1) {
					JOptionPane.showMessageDialog(null, "탈퇴가 완료되었습니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
					new LoginFrame();
					d.setVisible(false);
					frame.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "비밀번호를 확인하세요.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		if (e.getSource() == cancel) {
			d.setVisible(false);
		}
	}
}
