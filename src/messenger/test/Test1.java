package messenger.test;

import java.sql.SQLException;
import org.junit.Test;
import messenger.server.MessengerDAO;
import messenger.server.MessengerDAOimpl;

public class Test1 {
	Boolean b;
	@Test
	public void test1() {
		try {
			MessengerDAO dim = new MessengerDAOimpl();
			b = dim.isExistMember("admin");
			System.out.println(b);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
