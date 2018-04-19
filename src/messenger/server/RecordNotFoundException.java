package messenger.server;

public class RecordNotFoundException extends Exception {
	
	public RecordNotFoundException (String msg) {
		super(msg);
	}
	
	public RecordNotFoundException () {
		this("조회 내용 없음!");
	}
	
}