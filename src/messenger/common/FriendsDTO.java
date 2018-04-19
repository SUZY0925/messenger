package messenger.common;

public class FriendsDTO {
	private String myid, friendid, groupname;

	public FriendsDTO () {
		
	}

	public FriendsDTO(String myid, String friendid, String groupname) {
		this.myid = myid;
		this.friendid = friendid;
		this.groupname = groupname;
	}
	
	
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public String getMyid() {
		return myid;
	}
	public void setMyid(String myid) {
		this.myid = myid;
	}
	
	public String getFriendid() {
		return friendid;
	}
	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	
	@Override
	public String toString() {
		return "FriendsDTO [myid=" + myid + ", friendid=" + friendid + ", groupname=" + groupname + "]";
	}

	
}
