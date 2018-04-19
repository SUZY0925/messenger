package messenger.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import messenger.common.FriendsDTO;
import messenger.common.MemberDTO;
import messenger.server.DuplicateException;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;

public class friendAdd implements ActionListener{
	private Toolkit tk;
	private Dimension screenSize,size;
	
	private String id;
	private JFrame f;
	private JPanel p0,p1, p2, p3, p4, p5;
	private TitledBorder idt,namet, aliast;
	private JTextField idF, nameF, aliasF;
	private JButton idB, nameB, aliasB, FriendAdd, cancel;
	private JScrollPane sp;
	private JTable table;
	private String columnNames[] = {"ID","NAME","ALIAS"};
    private Object rowData[][]= {};
    private DefaultTableModel defaultTableModel;
    private MemberDTO s;
    private FriendsDTO friends;
    private MessengerDAO dim;
    private Messenger main;
    
	friendAdd(String id) {
		this.id = id;
		f = new JFrame("친구 검색");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		
		p1 = new JPanel();
		p1.setPreferredSize(new Dimension(200, 55));
		idF = new JTextField(10);
		idB = new JButton("검색");
		idt = new TitledBorder(new LineBorder(Color.BLACK,1),"Search By ID");
		p1.setBorder(idt);
		idB.addActionListener(this);
		p1.add(idF); p1.add(idB);
		
		
		p2 = new JPanel();
		p2.setPreferredSize(new Dimension(200, 55));
		nameF = new JTextField(10);
		nameB = new JButton("검색");
		nameB.addActionListener(this);
		namet = new TitledBorder(new LineBorder(Color.BLACK,1),"Search By NAME");
		p2.setBorder(namet);
		nameB.addActionListener(this);
		p2.add(nameF); p2.add(nameB);
		
		
		p3 = new JPanel();
		p3.setPreferredSize(new Dimension(200, 55));
		aliasF = new JTextField(10);
		aliasB = new JButton("검색");
		aliasB.addActionListener(this);
		aliast = new TitledBorder(new LineBorder(Color.BLACK,1),"Search By ALIAS");
		p3.setBorder(aliast);
		aliasB.addActionListener(this);
		p3.add(aliasF); p3.add(aliasB);

		
		p0 = new JPanel();
		p0.setPreferredSize(new Dimension(250, 300));
		p0.add(p1); p0.add(p2); p0.add(p3);
		
		p4 = new JPanel();
		defaultTableModel = new DefaultTableModel(rowData, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(defaultTableModel);
		table.getTableHeader().setReorderingAllowed(false);
		
		sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(300, 180));
		p4.add(sp);
		
		p5 = new JPanel();
		FriendAdd = new JButton("친구추가");
		FriendAdd.addActionListener(this);
		cancel = new JButton("돌아가기");
		cancel.addActionListener(this);
		p5.add(FriendAdd); p5.add(cancel);
		
		f.add(p0,BorderLayout.WEST);
		f.add(p4,BorderLayout.EAST);
		f.add(p5,BorderLayout.SOUTH);
		
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		f.setSize(600, 270);
		size = f.getSize();
		f.setLocation(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2);
//		f.pack();
		f.setVisible(true);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// 아이디 검색
		if(e.getSource() == idB) {
			defaultTableModel = (DefaultTableModel)table.getModel();
			defaultTableModel.setNumRows(0);
			s = new MemberDTO();
			s.setId(idF.getText());
			try {
				dim = new MessengerDAOimpl();
				s = dim.getFriendID(s.getId());
				
				Object[] tempObject ={s.getId(),s.getName(),s.getAlias()};
				defaultTableModel.addRow(tempObject);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		// 이름 검색
		if(e.getSource() == nameB) {
			defaultTableModel= (DefaultTableModel)table.getModel();
			defaultTableModel.setNumRows(0);
			s = new MemberDTO();
            
			s.setName(nameF.getText());
			try {
				dim = new MessengerDAOimpl();
				ArrayList<MemberDTO> temp = dim.getFriendName(s.getName());
				
				 for(int i = 0; i < temp.size(); i++) {
	                  Object [] temporaryObject = { temp.get(i).getId(),
	                                          temp.get(i).getName(),
	                                          temp.get(i).getAlias()};
	                  defaultTableModel.addRow(temporaryObject);
	               }
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		// 별명 검색
		if(e.getSource() == aliasB) {
			defaultTableModel= (DefaultTableModel)table.getModel();
			defaultTableModel.setNumRows(0);
			s = new MemberDTO();
			s.setAlias(aliasF.getText());
			try {
				dim = new MessengerDAOimpl();
				ArrayList<MemberDTO> temp = dim.getFriendAlias(s.getAlias());
				
				 for(int i = 0; i < temp.size(); i++) {
	                  Object [] temporaryObject = { temp.get(i).getId(),
	                                          temp.get(i).getName(),
	                                          temp.get(i).getAlias()};
	                  defaultTableModel.addRow(temporaryObject);
	               }
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		// 친구 추가
		if (e.getSource() == FriendAdd) {
			if (table.getSelectedRow() > -1) { // 목록에서 친구가 선택되어 있을 경우
				int selectRow = table.getSelectedRow(); // 선택된 줄
				String friendID = (String) table.getValueAt(selectRow, 0);

				friends = new FriendsDTO(id, friendID, null);
				
				if(friendID.equals(id)) {		// 본인에게 친구등록을 시도할 경우
					JOptionPane.showMessageDialog(null, "본인은 친구등록을 할 수 없습니다.", "=알림=", JOptionPane.INFORMATION_MESSAGE);
				}else {
						try {
						dim = new MessengerDAOimpl();
	
						if (1 == dim.insertFriend(friends)) {
							JOptionPane.showMessageDialog(null, friends.getFriendid() + "님이 추가되었습니다.", "=알림=",
									JOptionPane.INFORMATION_MESSAGE);
							Messenger.insertFriend(friends.getFriendid());
//							Messenger.f.dispose();
//							new Messenger(id);

						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (DuplicateException e1) {	// 이미 친구인 사람을 친구등록을 시도할 경우
						JOptionPane.showMessageDialog(null, friends.getFriendid() + "님은 이미 친구입니다.", "=알림=",
						JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} else if (table.getSelectedRow() == -1) {	// 목록에서 친구를 선택하지 않고 친구추가 버튼을 눌렀을 경우
				JOptionPane.showMessageDialog(null, "목록에서 친구를 선택하세요!", "=알림=", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		if(e.getSource() == cancel) {
			f.setVisible(false);
		}
	}
}
