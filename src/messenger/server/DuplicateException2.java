package messenger.server;

public class DuplicateException2 extends Exception {

	public DuplicateException2 (String message) {
		super (message);
	}

	public DuplicateException2 () {
		this ("중복오류!");
	}

}