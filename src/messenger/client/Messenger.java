package messenger.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import messenger.common.FriendsDTO;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;
import messenger.server.RecordNotFoundException;

public class Messenger implements ActionListener, MouseListener{
	private JFrame f;
	private Toolkit tk;
	private Dimension screenSize, size;
	private JPanel p1;
	private JMenuBar menubar;
	private JMenu Option;
	private JMenuItem Logoff;
	private JMenuItem Exit;	// file
	private JMenu Profile; // Option
	private JMenuItem MyProfile, ReFresh;
	private static DefaultMutableTreeNode FriendList;
	private DefaultMutableTreeNode selectedNode;
	private static DefaultTreeModel model;
	private JTree jt;
	private JButton friendAdd, chat;
	private String id;
	private MessengerDAO dim;
	private FriendsDTO friend;
	ArrayList<FriendsDTO> temp;
	JPopupMenu popupMenu;
	JMenuItem deleteFriendItem;
	 
	Messenger() {
		
	}
	
	Messenger(String id) {
		this.id = id;
		f = new JFrame("Suzy's Messenger");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menubar = new JMenuBar();

		f.add(menubar, BorderLayout.NORTH);
		
		Option = new JMenu("Option");
		Profile = new JMenu("Profile");
		MyProfile = new JMenuItem("MyProfile");
		Logoff = new JMenuItem("Logoff");
		Exit = new JMenuItem("Exit");

		menubar.add(Option);
		Option.add(Profile);
		Profile.add(MyProfile);
		Option.addSeparator();
		Option.add(Logoff);
		Option.add(Exit);
		
		
		FriendList = new DefaultMutableTreeNode("Friend List");
		
		try {
			dim = new MessengerDAOimpl();
			temp = dim.getFriends(id);
			for(int i = 0; i<temp.size(); i ++) {
				FriendList.add(new DefaultMutableTreeNode(temp.get(i).getFriendid()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
		}
		model = new DefaultTreeModel(FriendList);
		jt = new JTree(model);

		f.add(new JScrollPane(jt), BorderLayout.CENTER);
		
		jt.addMouseListener(this);
		
		p1 = new JPanel();
		friendAdd = new JButton("Search Friend");
		friendAdd.addActionListener(this);
		chat = new JButton("Chatting");
		chat.addActionListener(this);

		p1.add(friendAdd); p1.add(chat); 
		f.add(p1, BorderLayout.SOUTH);
		
		popupMenu = new JPopupMenu();
		deleteFriendItem = new JMenuItem("친구삭제");
		popupMenu.add(deleteFriendItem);
		deleteFriendItem.addActionListener(this);
		
		f.setSize(400, 600);
		size = f.getSize();
		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
		f.setLocation(screenSize.width-size.width, 0);
		f.setVisible(true);

		Logoff.addActionListener(this);
		Exit.addActionListener(this);

		MyProfile.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// 메뉴 액션
		if(e.getSource() == Exit) {
			System.exit(0);
		}
		if(e.getSource() == Logoff) {
			f.setVisible(false);
			new LoginFrame();
		}
		if(e.getSource() == MyProfile) {
			new MyProfile(f, id);
		}

		
		// 버튼 액션
		if(e.getSource() == friendAdd) {
			new friendAdd(id);
		}
		if(e.getSource() == chat) {
			selectedNode = (DefaultMutableTreeNode)jt.getLastSelectedPathComponent();
			
			if(jt.getSelectionPaths()==null) {
				JOptionPane.showMessageDialog(null, "친구를 선택하지 않았습니다.", "=알림=",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				if(!selectedNode.isRoot()) {
					new Chatting(id,String.valueOf(selectedNode));
				}
			}
		}

		if (e.getSource()==deleteFriendItem) {
			selectedNode = (DefaultMutableTreeNode)jt.getLastSelectedPathComponent();
				if(!selectedNode.isRoot()) {
					if (0 == JOptionPane.showConfirmDialog(null, selectedNode + "님을 삭제하시겠습니까?", "=알림=",
							JOptionPane.OK_CANCEL_OPTION)) {
						friend = new FriendsDTO(id, String.valueOf(selectedNode), null);
						try {
							dim = new MessengerDAOimpl();
							int cnt = dim.deleteFriend(friend);
	
							if (cnt == 1) {
								JOptionPane.showMessageDialog(null,selectedNode+"님이 삭제되었습니다.", "=알림=",
										JOptionPane.INFORMATION_MESSAGE);
								model.removeNodeFromParent(selectedNode);	//트리에서 삭제
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (jt.getRowForLocation(e.getX(), e.getY()) != -1) {
			selectedNode = (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();
			if (selectedNode != null) {
				if (!selectedNode.isRoot()) {
					if (e.getClickCount() == 2) {
						new Chatting(id, String.valueOf(selectedNode));
					}
					if (e.getButton() == 3) {
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}
		

	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public static void insertFriend(String FriendID) {	// 트리에서 추가
		model.insertNodeInto(new DefaultMutableTreeNode(FriendID), FriendList, model.getChildCount(FriendList));
	}
	
}
