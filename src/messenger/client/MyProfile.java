package messenger.client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import messenger.common.MemberDTO;
import messenger.server.DuplicateException;
import messenger.server.DuplicateException2;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;


public class MyProfile implements ActionListener{
	private Toolkit tk;
	private Dimension screenSize,size;
	private JDialog d;
	private JPanel p1,combopanel;
	private JLabel idlb, passlb,passlb2, loclb, sexlb, agelb,birthlb, aliaslb, namelb, phonelb;
	private JTextField idtf,birthtf, aliastf,nametf, phonetf;
	private JPasswordField passtf,passtf2;
	private JComboBox<String> age;
	private JComboBox<String> location;
	private JRadioButton women, men;
	private ButtonGroup grp;
	private JButton withdraw,ok;
	private MessengerDAO dim;
	private MemberDTO s;
	private String id, sex;
	
	private JFrame f;
	
	MyProfile(JFrame frame, String id) {
		this.id = id;
		f = frame;
		d = new JDialog(f, "회원정보 확인", true);
		
		try {
			dim = new MessengerDAOimpl();
			s = dim.getMember(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setResizable(false);
		
		p1 = new JPanel();
		p1.setLayout(new GridLayout(11,1));
		idlb = new JLabel("ID(EMAIL)*");
		idtf = new JTextField(10);
		idtf.setText(s.getId());
		idtf.setEditable(false);
		passlb = new JLabel("PASSWODD*");
		passtf = new JPasswordField(10);
		passlb2 = new JLabel("PASSWORD*");
		passtf2 = new JPasswordField(10);
		namelb = new JLabel("NAME*");
		nametf = new JTextField(10);
		nametf.setText(s.getName());
		aliaslb = new JLabel("ALIAS");
		aliastf = new JTextField(10);
		aliastf.setText(s.getAlias());
		
		loclb = new JLabel("LOCATION*");
		location = new JComboBox<String>();
		location.addItem("선택");
		location.addItem("서울");	location.addItem("대전");	location.addItem("인천");	location.addItem("광주");	location.addItem("대구");
		location.addItem("부산");	location.addItem("울산");	location.addItem("경기도");	location.addItem("경상도");	location.addItem("전라도");
		location.addItem("충청도");	location.addItem("강원도");	location.addItem("제주도");
		location.setSelectedItem(s.getLoc());
		
		agelb = new JLabel("AGE*");
		age = new JComboBox<String>();
		age.addItem("선택");
		for(int i =1; i<100; i++) {
			age.addItem(String.valueOf(i));
		}
		age.setSelectedIndex(Integer.valueOf(s.getAge()));
		
		sexlb = new JLabel("SEX*");
		
		combopanel = new JPanel();
		women = new JRadioButton("여");
		men = new JRadioButton("남");
		if (s.getSex().equals("여")) {
			women.setSelected(true);
		} else if (s.getSex().equals("남")) {
			men.setSelected(true);
		}
		grp = new ButtonGroup();
		grp.add(women);
		grp.add(men);
		combopanel.add(women); combopanel.add(men);
		
		birthlb = new JLabel("BIRTH*");
		birthtf = new JTextField(8);
		birthtf.setText(s.getBirth());
		phonelb = new JLabel("PHONE*");
		phonetf = new JTextField(11);
		phonetf.setText(s.getPhone());
		
		withdraw = new JButton("회원탈퇴");
		withdraw.addActionListener(this);
		ok = new JButton("EDIT");
		ok.addActionListener(this);
		

		
		
		p1.add(idlb); p1.add(idtf); 
		p1.add(passlb); p1.add(passtf); 
		p1.add(passlb2); p1.add(passtf2);
		p1.add(namelb); p1.add(nametf);
		p1.add(aliaslb); p1.add(aliastf);
		p1.add(loclb); p1.add(location);
		p1.add(sexlb); p1.add(combopanel);
		p1.add(agelb); p1.add(age);
		p1.add(birthlb); p1.add(birthtf);
		p1.add(phonelb); p1.add(phonetf);
		p1.add(withdraw); p1.add(ok);
		d.add(p1);
		
		
		
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		d.setSize(320,450);
		size = d.getSize();
		d.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
		d.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (women.isSelected()) { // 성별 라디오버튼 String
				sex = "여";
			} else if (men.isSelected()) {
				sex = "남";
			}

			String agestr = (String) age.getSelectedItem(); // 나이 String
			String locstr = (String) location.getSelectedItem(); // 지역 String
			String passwd = String.valueOf(passtf.getPassword()); // 비밀번호 String
			String passwd2 = String.valueOf(passtf2.getPassword()); // 비밀번호 확인 String

			// 유효성체크
			if (	passwd.equals("") || // PASSWD 체크
					nametf.getText().trim().equals("") || // NAME 체크
					locstr.equals("선택") || // 지역 체크
					agestr.equals("선택") || // 나이 체크
					birthtf.getText().trim().equals("") || // 생년월일 체크
					phonetf.getText().trim().equals("")) { // 전화번호 체크

				JOptionPane.showMessageDialog(null, "필수정보(*) 누락!", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} else if (!Pattern.matches("[\\w,!?@#$%^&*+\\-_=]{4,20}", passwd)
					| !Pattern.matches("[a-zA-Z0-9,!?@#$%^&*+\\-_=]{4,20}", passwd2)) {
				JOptionPane.showMessageDialog(null, "비밀번호는 4-20자리의 영어,숫자,특수문자( !@#$%^&*?+-_= )만 가능합니다.", "=알림=",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (!Pattern.matches("[a-zA-Z가-힣]{3,10}", nametf.getText())) {
				JOptionPane.showMessageDialog(null, "이름은 3-10자리의 영어,한글만 가능합니다.", "=알림=",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (!Pattern.matches("[\\wㄱ-힣]{0,10}", aliastf.getText().trim())) {
				JOptionPane.showMessageDialog(null, "별명은 10자리 이하의 영어,한글(+자음),숫자만 가능합니다.", "=알림=",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (!Pattern.matches("\\d{8}", birthtf.getText())) {
				JOptionPane.showMessageDialog(null, "생년월일은 8자리의 숫자만 입력가능합니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} else if (!Pattern.matches("\\d{10,11}", phonetf.getText())) {
				JOptionPane.showMessageDialog(null, "전화번호는 10-11자리의 숫자만 입력가능합니다.", "=알림=",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (!passwd.equals(passwd2)) { // 비밀번호, 비밀번호 확인칸 체크
				JOptionPane.showMessageDialog(null, "비밀번호가 다릅니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			} else { // 유효성 검사 후 가입시작
				if (aliastf.getText().equals("")) { // alias가 빈칸일때
					s = new MemberDTO(idtf.getText(), passwd, nametf.getText(), locstr, sex, agestr, birthtf.getText(),
							phonetf.getText());
				} else { // alias가 빈칸이 아닐때
					s = new MemberDTO(idtf.getText(), passwd, nametf.getText(), aliastf.getText(), locstr, sex, agestr,
							birthtf.getText(), phonetf.getText());
				}
				try {
					MessengerDAO dim = new MessengerDAOimpl();
					if (dim.updateMember(s) == 1) {
						JOptionPane.showMessageDialog(null, "회원수정이 완료되었습니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} 
				
				d.setVisible(false);
				
			}
		}

		if (e.getSource() == withdraw) {
			new WithDraw(f, id);
		}

	}
}
