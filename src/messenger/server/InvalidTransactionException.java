package messenger.server;

public class InvalidTransactionException extends Exception {
	
	public InvalidTransactionException (String msg) {
		super(msg);
	}
	
	public InvalidTransactionException() {
		this("유효하지 않은 트랜잭션!");
	}
	
}
