package messenger.server;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataBaseUtil {

	private static BasicDataSource dataSource= new BasicDataSource();
	private static DataBaseUtil instance = new DataBaseUtil();
	
	
	private static final String driverName = "oracle.jdbc.driver.OracleDriver";
	private static final String serverIP = "127.0.0.1";
	private static final String userName = "testsuzy";
	private static final String userPass = "testsuzy";
	
	private DataBaseUtil() { // 싱글톤패턴으로 하기위해 private로 생성자 만듬~

		// jdbc 접속 설정을 dataSource 객체에 해준다.
		dataSource.setDriverClassName(driverName);
		System.out.println("드라이버 로딩 성공");
		
		// db연결
		dataSource.setUrl("jdbc:oracle:thin:@"+serverIP+":1521:xe");
		dataSource.setUsername(userName);
		dataSource.setPassword(userPass);
		System.out.println("데이터베이스 연결 성공");

		// Connection Pool설정 : 설정하지않으면 default값으로 셋팅됨
		dataSource.setInitialSize(5); // 최초 생성할 Connection수 : default - 0
		dataSource.setMaxTotal(10); // 최대 생성할 Connection수 : default - 8
												// 무제한 - 음수(-1)
		dataSource.setMaxWaitMillis(1000);// Connection 반납을 기다리는 최대시간 : default 무제한
		dataSource.setMaxIdle(5); // 최대 대여 가능한 Connection 수 : default - 8, 음수-무제한
		dataSource.setMaxIdle(5); // 최소 대여 가능한 Connection 수 : default - 0
	}

	public static DataBaseUtil getInstance() {
		return instance;
	}
	
	public static Connection getConnection() throws SQLException {
		Connection con = dataSource.getConnection();
		return con;
	}
	
	// close 하는거 메소드로 만들기!
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null; rs = null; pstmt = null;		
	}
	
	public static void close(PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 rs = null; pstmt = null;		
	}
	
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null; rs = null; stmt = null;		
	}
	
	public static void close(Statement stmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 rs = null; stmt = null;		
	}
	
	// update할때~
	public static void close(Connection conn, PreparedStatement pstmt) {
		try {
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null; pstmt = null;		
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if (pstmt != null) pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		pstmt = null;		
	}
	
	
	public static void close(Connection conn) {
		try {
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null; 
	}

	
	public static void printSQLException(SQLException e) {
		while (e != null) {
			System.err.println("SQLstate : " + e.getSQLState());
			System.err.println("ErrorCode : " + e.getErrorCode());
			System.err.println("Message : " + e.getMessage());
			
			Throwable t = e.getCause();
			
			while(t != null) {
				System.err.println("Cause : "+t);
				t = t.getCause();
			}
			e = e.getNextException();
		}
	}
	
	public static void printSQLException(SQLException e, String errLoc) {
		System.err.println("ErrLoc : "+errLoc);
		while (e != null) {
			System.err.println("SQLstate : " + e.getSQLState());
			System.err.println("ErrorCode : " + e.getErrorCode());
			System.err.println("Message : " + e.getMessage());
			
			Throwable t = e.getCause();
			
			while(t != null) {
				System.err.println("Cause : "+t);
				t = t.getCause();
			}
			e = e.getNextException();
		}
	}

	
	
}
