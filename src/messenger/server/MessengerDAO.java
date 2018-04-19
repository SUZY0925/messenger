package messenger.server;

import java.util.ArrayList;

import messenger.common.FriendsDTO;
import messenger.common.MemberDTO;





public interface MessengerDAO {

	// 회원 조회
	MemberDTO getMember(String id);
	
	// 전화번호 존재 유무 조회
	boolean isExistPhone(String phone);
	
	// 회원 존재 유무 조회
	boolean isExistMember(String id);
	
	// 친구 존재 유무 체크
	boolean isExistFriend(String myId, String friendId);
	
	// 로그인
	int login(String id, String passwd) throws RecordNotFoundException;
	
	// 회원 등록
	int insertMember(MemberDTO member) throws DuplicateException, DuplicateException2;
	
	// 회원 수정
	int updateMember(MemberDTO member);
	
	// 회원 탈퇴
	int deleteMember(String id, String passwd);
	
	// 아이디 찾기
	String findId(String name, String birth, String phone) throws RecordNotFoundException;

	// 비밀번호 찾기
	String findPasswd(String id, String name, String phone) throws RecordNotFoundException;
	
	// 친구 등록
	int insertFriend(FriendsDTO friends) throws DuplicateException ;
	
	// 친구 삭제
	int deleteFriend(FriendsDTO friends);
	
	// 친구 검색 아이디
	MemberDTO getFriendID(String friendId);

	// 친구 검색 이름
	ArrayList<MemberDTO> getFriendName(String friendName);

	// 친구 검색 별명
	ArrayList<MemberDTO> getFriendAlias(String friendAlias);

	// 친구 찾기
	FriendsDTO getFriend(String myId, String friendId)  throws RecordNotFoundException;

	// 친구 목록 가져오기
	ArrayList<FriendsDTO> getFriends(String id)  throws RecordNotFoundException;

}