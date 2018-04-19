package messenger.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import messenger.common.FriendsDTO;
import messenger.common.MemberDTO;



// Refactor -> Extract Interface -> Select All -> OK  : 인터페이스화 함
public class MessengerDAOimpl implements MessengerDAO {
	Connection conn;
	Statement stmt;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public MessengerDAOimpl() throws SQLException {
		conn = DataBaseUtil.getConnection();
	}

	
	// 회원조회
	@Override
	public MemberDTO getMember(String id) {
		MemberDTO member = new MemberDTO();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT id, passwd, name, alias, loc, sex, age, birth, phone FROM member WHERE id = ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member.setId(rs.getString("id"));
				member.setPasswd(rs.getString("passwd"));
				member.setName(rs.getString("name"));
				member.setAlias(rs.getString("alias"));
				member.setLoc(rs.getString("loc"));
				member.setSex(rs.getString("sex"));
				member.setAge(rs.getString("age"));
				member.setBirth(rs.getString("birth"));
				member.setPhone(rs.getString("phone"));
			} 
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "MemberDTO getMember(String id)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		return member;
	}
	
	
	// 전화번호 존재 유무 체크
	@Override
	public boolean isExistPhone(String phone){
		boolean isExistPhone = false;
		MemberDTO member = new MemberDTO();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT phone FROM member WHERE phone = ?");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, phone);
			rs = pstmt.executeQuery();

			if (rs.next())
				isExistPhone = true;

		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "boolean isExistPhone(String phone)");
		} finally {
			DataBaseUtil.close(pstmt, rs);
		}
		return isExistPhone;
	}
	
	// 회원 존재 유무 체크
	@Override
	public boolean isExistMember(String id){
		boolean isExistMember = false;
		MemberDTO member = new MemberDTO();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT id FROM member WHERE id = ?");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next())
				isExistMember = true;

		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "boolean isExistMember(String id)");
		} finally {
			DataBaseUtil.close(pstmt, rs);
		}
		return isExistMember;
	}
	
	
	// 친구 존재 유무 체크
	@Override
	public boolean isExistFriend(String myId, String friendId){
		boolean isExistFriend = false;
		MemberDTO member = new MemberDTO();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT Friendid FROM member, friends WHERE ")
			.append("friends.myid = ? and friends.friendId = ?");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, myId);
			pstmt.setString(2, friendId);
			rs = pstmt.executeQuery();

			if (rs.next()) 
				isExistFriend = true;
			

		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "boolean isExistMember(String id)");
		} finally {
			DataBaseUtil.close(pstmt, rs);
		}
		
		return isExistFriend;

	}
	
	
	// 로그인 
	public int login(String id, String passwd) throws RecordNotFoundException{
		MemberDTO member = new MemberDTO();
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id FROM member WHERE id = ? and passwd = ?");
			
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				cnt = 1;
			} else if(!rs.next()){
				cnt = 0;
				throw new RecordNotFoundException();
			}
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "int login(String id, String passwd)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		return cnt;
	}

	// 회원등록
	@Override
	public int insertMember(MemberDTO member) throws DuplicateException, DuplicateException2 {
		
		if(isExistMember(member.getId())) {		// 아이디 중복 체크
			throw new DuplicateException();
		}
		
		if(isExistPhone(member.getPhone())) {	// 핸드폰번호 중복 체크
			throw new DuplicateException2();
		}
		
		int cnt = 0;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("insert into member ")
			.append("(id,passwd,name,alias,loc,sex,age,birth,phone,cdate) ")
			.append("values (?,?,?,?,?,?,?,?,?,sysdate)");
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getAlias());
			pstmt.setString(5, member.getLoc());
			pstmt.setString(6, member.getSex());
			pstmt.setString(7, member.getAge());
			pstmt.setString(8, member.getBirth());
			pstmt.setString(9, member.getPhone());
			
			cnt = pstmt.executeUpdate();	//executeUpdate : 업데이트한 row 수를 반환
			 
		} catch (SQLException e ) {
			DataBaseUtil.printSQLException(e, "int insertMember(MemberDTO member)");
		} finally {
			DataBaseUtil.close(conn, pstmt);
		}
		
		return cnt;
	}
	

	// 회원 수정
	@Override
	public int updateMember(MemberDTO member){

		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("update member set ")
			.append("passwd=? , name=?, alias= ?, loc= ?, age=?, birth=?, phone=? where id=?");
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			
				pstmt.setString(1, member.getPasswd());
				pstmt.setString(2, member.getName());
				pstmt.setString(3, member.getAlias());
				pstmt.setString(4, member.getLoc());
				pstmt.setString(5, member.getAge());
				pstmt.setString(6, member.getBirth());
				pstmt.setString(7, member.getPhone());
				pstmt.setString(8, member.getId());
			
			cnt = pstmt.executeUpdate();

		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "int updateMember(MemberDTO member)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		
		return cnt;
	}

	// 회원 탈퇴
	@Override
	public int deleteMember(String id, String passwd) {
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("delete from member where id = ? and passwd = ?");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, passwd);
			cnt = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseUtil.close(conn,pstmt);
		}
		
		return cnt;
	}

	
	
	// 아이디 찾기
	@Override
	public String findId(String name, String birth, String phone) throws RecordNotFoundException{
		MemberDTO member = new MemberDTO();
		String findId = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select id from member where name=? and birth=? and phone=?");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			pstmt.setString(2, birth);
			pstmt.setString(3, phone);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				findId = rs.getString(1);
			} else if(!rs.next()){
				throw new RecordNotFoundException();
			}
			
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "String findId(String name, String birth, String phone)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		
		return findId;
	}
	
	
	// 비밀번호 찾기
	@Override
	public String findPasswd(String id, String name, String phone) throws RecordNotFoundException{
		MemberDTO member = new MemberDTO();
		String findPasswd = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select passwd from member where id=? and name=? and phone=?");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, phone);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				findPasswd = rs.getString(1);
			} else if(!rs.next()){
				throw new RecordNotFoundException();
			}
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "String findPasswd(String id, String name, String phone)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		
		return findPasswd;
	}
	

	// 친구 등록
	@Override
	public int insertFriend(FriendsDTO friends) throws DuplicateException {
		
		if(isExistFriend(friends.getMyid(), friends.getFriendid())) {		
			throw new DuplicateException();
		}
		
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into friends values(?,?,null,sysdate)");
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, friends.getMyid());
			pstmt.setString(2, friends.getFriendid());
			
			cnt = pstmt.executeUpdate();	//executeUpdate : 업데이트한 row 수를 반환

		} catch (SQLException e ) {
			DataBaseUtil.printSQLException(e, "int insertFriend(FriendsDTO friends)");
		} finally {
			DataBaseUtil.close(conn, pstmt);
		}
		
		return cnt;
	}

	// 친구 삭제
	@Override
	public int deleteFriend(FriendsDTO friends) {
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("delete from friends where myid = ? and friendid = ?");
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, friends.getMyid());
			pstmt.setString(2, friends.getFriendid());
			
			cnt = pstmt.executeUpdate();	//executeUpdate : 업데이트한 row 수를 반환

		} catch (SQLException e ) {
			DataBaseUtil.printSQLException(e, "int deleteFriend(FriendsDTO friends)");
		} finally {
			DataBaseUtil.close(conn, pstmt);
		}
		
		return cnt;
	}
	
	// 친구 찾기
	@Override
	public FriendsDTO getFriend(String myid, String friendId)   throws RecordNotFoundException{
		FriendsDTO friend = null;
		
		return friend;
	}
	
// 친구 목록 가져오기
	@Override
	public ArrayList<FriendsDTO> getFriends(String id)  throws RecordNotFoundException{
		ArrayList<FriendsDTO> friends = new ArrayList<FriendsDTO>();
		FriendsDTO friend;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT friendid FROM friends WHERE myid = ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			 while(rs.next()) {
				 friend =  new FriendsDTO();
				 friend.setFriendid(rs.getString("friendid"));
				 friends.add(friend);
	        }
			
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "FriendsDTO getFriends(String id)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		return friends;
	}

// 아이디로 친구 검색
	@Override
	public MemberDTO getFriendID(String friendId) {
		MemberDTO member = new MemberDTO();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT id, name, alias FROM member WHERE id like ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, friendId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setAlias(rs.getString("alias"));
			} 
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "MemberDTO getFriendID(String friendId)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		return member;
	}

	// 이름으로 친구 검색
	@Override
	public ArrayList<MemberDTO> getFriendName(String friendName) {
		ArrayList<MemberDTO> friends = new ArrayList<MemberDTO>();
		MemberDTO member;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT id, name, alias FROM member WHERE name like ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, friendName);
			rs = pstmt.executeQuery();
			
			 while(rs.next()) {
	               member = new MemberDTO();
	               member.setId(rs.getString("id"));
	               member.setName(rs.getString("name"));
	               member.setAlias(rs.getString("alias"));
	               friends.add(member);
	        }
			
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "MemberDTO getFriendName(String friendName)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		return friends;
	}


	// 별명으로 친구 검색
	@Override
	public ArrayList<MemberDTO> getFriendAlias(String friendAlias) {
		ArrayList<MemberDTO> friends = new ArrayList<MemberDTO>();
		MemberDTO member;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT id, name, alias FROM member WHERE alias like ?");
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, friendAlias);
			rs = pstmt.executeQuery();
			
			 while(rs.next()) {
	               member = new MemberDTO();
	               member.setId(rs.getString("id"));
	               member.setName(rs.getString("name"));
	               member.setAlias(rs.getString("alias"));
	               friends.add(member);
	        }
			
		} catch (SQLException e) {
			DataBaseUtil.printSQLException(e, "MemberDTO getFriendAlias(String friendAlias)");
		} finally {
			DataBaseUtil.close(conn, pstmt, rs);
		}
		return friends;
	}


}
