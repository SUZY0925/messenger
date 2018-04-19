package messenger.client;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import messenger.common.MemberDTO;
import messenger.server.DuplicateException;
import messenger.server.DuplicateException2;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;




public class JoinFrame implements ActionListener{
	private Toolkit tk;
	private Dimension screenSize,size;
	
	private MemberDTO s;
	private Container contentPane;
	private JFrame f;	
	private JPanel p2, combopanel;
	private JLabel joinus, idlb, passlb,passlb2, loclb, sexlb, agelb,birthlb, aliaslb, namelb, phonelb;
	private JTextField idtf,birthtf, aliastf,nametf, phonetf;
	private JComboBox<String> age;
	private JPasswordField passtf,passtf2;
	private JComboBox<String> location;
	private JRadioButton women, men;
	private ButtonGroup grp;
	private JButton ok, cancel;
	private String sex;
	JoinFrame() {
		f = new JFrame();
		contentPane = f.getContentPane();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.setTitle("JOIN");
		f.setResizable(false);
		joinus = new JLabel("JOIN",SwingConstants.CENTER);
		f.add(joinus, BorderLayout.NORTH);
		
		p2 = new JPanel();
		p2.setLayout(new GridLayout(11,1));

		idlb = new JLabel("ID(EMAIL)*");
		idtf = new JTextField(10);
		passlb = new JLabel("PASSWORD*");
		passtf = new JPasswordField(10);
		passlb2 = new JLabel("PASSWORD*");
		passtf2 = new JPasswordField(10);
		namelb = new JLabel("NAME*");
		nametf = new JTextField(10);
		aliaslb = new JLabel("ALIAS");
		aliastf = new JTextField(10);
		
		loclb = new JLabel("LOCATION*");
		location = new JComboBox<String>();
		location.addItem("선택");
		location.addItem("서울");	location.addItem("대전");	location.addItem("인천");	location.addItem("광주");	location.addItem("대구");
		location.addItem("부산");	location.addItem("울산");	location.addItem("경기도");	location.addItem("경상도");	location.addItem("전라도");
		location.addItem("충청도");	location.addItem("강원도");	location.addItem("제주도");

		agelb = new JLabel("AGE*");
		age = new JComboBox<String>();
		age.addItem("선택");
		for(int i =1; i<100; i++) {
			age.addItem(String.valueOf(i));
		}
		
		sexlb = new JLabel("SEX*");
		
		combopanel = new JPanel();
		women = new JRadioButton("여");
		women.addActionListener(this);
		men = new JRadioButton("남");
		men.addActionListener(this);
		grp = new ButtonGroup();
		grp.add(women);
		grp.add(men);
		combopanel.add(women); combopanel.add(men);
		
		birthlb = new JLabel("BIRTH*");
		birthtf = new JTextField(8);
		phonelb = new JLabel("PHONE*");
		phonetf = new JTextField(11);
		
		ok = new JButton("OK");
		ok.addActionListener(this);
		cancel = new JButton("CANCEL");
		cancel.addActionListener(this);
	
		p2.add(idlb); p2.add(idtf);
		p2.add(passlb); p2.add(passtf);
		p2.add(passlb2); p2.add(passtf2);
		p2.add(namelb); p2.add(nametf);
		p2.add(aliaslb); p2.add(aliastf);
		p2.add(loclb); p2.add(location);
		p2.add(sexlb); p2.add(combopanel);
		p2.add(agelb); p2.add(age);
		p2.add(birthlb); p2.add(birthtf);
		p2.add(phonelb); p2.add(phonetf);
		p2.add(ok); p2.add(cancel);
		
		contentPane.add(p2, BorderLayout.SOUTH);
		
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		f.setSize(280, 450);
		size = f.getSize();
		f.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
		f.pack();
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancel) {	// 취소
			new LoginFrame();
			f.setVisible(false);
		} else if(e.getSource() == ok) {	// 확인
			
			if (women.isSelected()) {		// 성별 라디오버튼 String
				sex = "여";
			} else if(men.isSelected()) {
				sex = "남";
			} 

			String agestr = (String) age.getSelectedItem();	// 나이 String
			String locstr = (String)location.getSelectedItem();	// 지역 String
			String passwd = String.valueOf(passtf.getPassword());	// 비밀번호 String
			String passwd2 = String.valueOf(passtf2.getPassword());	// 비밀번호 확인 String

			// 유효성체크
		
			if (idtf.getText().trim().equals("") ||		// ID 체크
					passwd.equals("") ||								// PASSWD 체크
					nametf.getText().trim().equals("") ||		// NAME 체크
					locstr.equals("선택") ||						// 지역 체크
					sex == null || agestr.equals("선택") ||	// 성별, 나이 체크
					birthtf.getText().trim().equals("") ||		// 생년월일 체크
					phonetf.getText().trim().equals("")) {		// 전화번호 체크
				
				JOptionPane.showMessageDialog(null,"필수정보(*) 누락!", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!Pattern.matches("[\\w,_\\-\\.]+@\\w+\\.\\w+(\\.\\w+)?", idtf.getText().trim())||idtf.getText().length()<4 || idtf.getText().length()>20) {
				JOptionPane.showMessageDialog(null,"아이디를 4-20자리의 이메일 형식으로 입력하세요.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!Pattern.matches("[\\w,!?@#$%^&*+\\-_=]{4,20}", passwd) | !Pattern.matches("[a-zA-Z0-9,!?@#$%^&*+\\-_=]{4,20}", passwd2)) {
				JOptionPane.showMessageDialog(null,"비밀번호는 4-20자리의 영어,숫자,특수문자( !@#$%^&*?+-_= )만 가능합니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!Pattern.matches("[\\w가-힣]{2,10}",nametf.getText())) {
				JOptionPane.showMessageDialog(null,"이름은 2-10자리의 영어,한글,숫자만 가능합니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!Pattern.matches("[\\wㄱ-힣]{0,10}",aliastf.getText().trim())) {
				JOptionPane.showMessageDialog(null,"별명은 10자리 이하의 영어,한글(+자음),숫자만 가능합니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!Pattern.matches("\\d{8}",birthtf.getText())) {
				JOptionPane.showMessageDialog(null,"생년월일은 8자리의 숫자만 입력가능합니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!Pattern.matches("\\d{10,11}",phonetf.getText())) {
				JOptionPane.showMessageDialog(null,"전화번호는 10-11자리의 숫자만 입력가능합니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(!passwd.equals(passwd2)) {	// 비밀번호, 비밀번호 확인칸 체크
				JOptionPane.showMessageDialog(null,"비밀번호가 다릅니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
			else {	// 유효성 검사 후 가입시작
					if(aliastf.getText().equals("")) {	// alias가 빈칸일때
						s = new MemberDTO(
								idtf.getText(), passwd, nametf.getText(), locstr, sex, agestr, birthtf.getText(),phonetf.getText());
					} else {											// alias가 빈칸이 아닐때
						s = new MemberDTO(idtf.getText(), passwd, nametf.getText(), aliastf.getText(), locstr, sex, agestr, birthtf.getText(),phonetf.getText());
					}
				try {
					MessengerDAO dim = new MessengerDAOimpl();
					dim.insertMember(s);
					
					JOptionPane.showMessageDialog(null,"회원가입이 완료되었습니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
					f.setVisible(false);
					new LoginFrame();
					
				} catch (DuplicateException e2) {	// 아이디가 존재할 때
					JOptionPane.showMessageDialog(null,"이미 존재하는 아이디입니다!", "=알림=", JOptionPane.WARNING_MESSAGE);
				} catch (DuplicateException2 e1) {  // 전화번호가 존재할 때
					JOptionPane.showMessageDialog(null,"이미 존재하는 전화번호입니다!", "=알림=", JOptionPane.WARNING_MESSAGE);
				} catch (SQLException e1) {
						e1.printStackTrace();
				} 
			}
		} 
	}
}
