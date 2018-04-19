package messenger.client;





import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import messenger.common.MemberDTO;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;
import messenger.server.RecordNotFoundException;


public class Find implements ActionListener{
	private Toolkit tk;
	private Dimension screenSize, size;
	private JFrame f;
	private JPanel p1,p2, p3;
	private JLabel  nameLb, birthLb, phone1Lb,      idLb, name2Lb,phone2Lb;
	private JTextField nameTf, birthTf, phone1Tf,   idTf, name2Tf,phone2Tf;
	private JButton idOk, passwdOk, cancel;
	private TitledBorder idt, pst;
	private JDialog jDialog;
	private MessengerDAO dim;
	private MemberDTO s;
	
	Find() {
		f = new JFrame("Find ID/PASSWD");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		
		f.setLayout(new FlowLayout());

		p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		
		p1.setPreferredSize(new Dimension(190, 135));
		
		idt = new TitledBorder(new LineBorder(Color.BLACK,1),"FIND ID");
		p1.setBorder(idt);
		
		nameLb = new JLabel("NAME");
		nameTf = new JTextField(10);
		nameTf.addActionListener(this);
		birthLb = new JLabel("BIRTH");
		birthTf = new JTextField(10);
		birthTf.addActionListener(this);
		phone1Lb = new JLabel("PHONE");
		phone1Tf = new JTextField(10);
		phone1Tf.addActionListener(this);
		idOk = new JButton("FIND ID");
		idOk.addActionListener(this);		
		p1.add(nameLb); p1.add(nameTf);
		p1.add(birthLb); p1.add(birthTf);
		p1.add(phone1Lb); p1.add(phone1Tf);
		p1.add(idOk);
		
		p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		p2.setPreferredSize(new Dimension(190, 135));
		pst = new TitledBorder(new LineBorder(Color.BLACK,1),"FIND PASSWD");
		p2.setBorder(pst);

		idLb = new JLabel("ID 　");
		idTf = new JTextField(10);
		idTf.addActionListener(this);
		name2Lb = new JLabel("NAME");
		name2Tf = new JTextField(10);
		name2Tf.addActionListener(this);
		phone2Lb = new JLabel("PHONE");
		phone2Tf = new JTextField(10);
		phone2Tf.addActionListener(this);
		passwdOk = new JButton("FIND PASSWORD");
		passwdOk.addActionListener(this);
		
		p2.add(idLb);
		p2.add(idTf);
		p2.add(name2Lb);
		p2.add(name2Tf);
		p2.add(phone2Lb);
		p2.add(phone2Tf);
		p2.add(passwdOk);
		
		p3 = new JPanel();
		cancel = new JButton("CANCEL");
		cancel.addActionListener(this);
		
		p3.add(cancel);
		
		f.add(p1);
		f.add(p2);
		f.add(p3);
		
		f.setSize(235, 360);
		size = f.getSize();
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		f.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 아이디 찾기를 눌렀을때
		if (e.getSource() == idOk) {
			s = new MemberDTO();
			s.setName(nameTf.getText());
			s.setBirth(birthTf.getText());
			s.setPhone(phone1Tf.getText());
			try {
				dim = new MessengerDAOimpl();
				
				JOptionPane.showMessageDialog(jDialog,"당신의 ID는 "+dim.findId(s.getName(),  s.getBirth(), s.getPhone())+"입니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} catch (RecordNotFoundException e1) {
				JOptionPane.showMessageDialog(jDialog,"해당하는 정보를 찾을 수 없습니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
			
		// 비밀번호 찾기를 눌렀을때
		} else if(e.getSource() == passwdOk) {
			s = new MemberDTO();
			s.setId(idTf.getText());
			s.setName(name2Tf.getText());
			s.setPhone(phone2Tf.getText());
			try {
				dim = new MessengerDAOimpl();
				
				JOptionPane.showMessageDialog(jDialog,"당신의 PASSWORD는 "+dim.findPasswd(s.getId(),  s.getName(), s.getPhone())+"입니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);

			} catch (RecordNotFoundException e1) {
				JOptionPane.showMessageDialog(jDialog,"해당하는 정보를 찾을 수 없습니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
		// 돌아가기를 눌렀을때
		} else if(e.getSource() == cancel) {
			f.setVisible(false);
			new LoginFrame();
		}
	}
}
