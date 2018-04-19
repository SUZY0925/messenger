package messenger.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Server extends Thread implements ActionListener {
	private ExecutorService executorService;
	private ServerSocket serverSocket;
	private Socket socket;
	private HashMap<String, DataOutputStream> connection;
	private int PORT = 5500;
	private String IP = "localhost";
	private JFrame f;
	private JTextArea ta;
	private JButton btn;
	public String clientID;

	public Server() {
		f = new JFrame("서버");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ta = new JTextArea();
		ta.setEditable(false);
		btn = new JButton("ServerStart");
		btn.addActionListener(this);
		f.add(new JScrollPane(ta), BorderLayout.CENTER);
		f.add(btn, BorderLayout.SOUTH);
		f.setSize(300, 300);
		f.setVisible(true);

		connection = new HashMap();
		Collections.synchronizedMap(connection);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn) {
			if (btn.getText().equals("ServerStart")) { // 서버시작
				btn.setText("ServerStop");
				startServer();
			}
//			else if (btn.getText().equals("ServerStop")) { // 서버종료
//				btn.setText("ServerStart");
//				stopServer();
//			}
		}
	}

	void startServer() { // 서버 시작
		// 코어수만큼 쓰레드풀 생성
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, PORT));
		} catch (IOException e1) {
			if (!serverSocket.isClosed()) {
				System.out.println("bind 부분 에러");
//				stopServer();
			}
			return;
		}
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ta.append("[서버 시작]\n");
				while (true) {
					try {
						socket = serverSocket.accept();
						ta.append("[" + socket.getInetAddress() + " : " + socket.getPort() + "]" + "에서 접속하였습니다.\n");
					} catch (IOException e) {
						System.out.println("accept 부분 에러");
//						stopServer();
					}
				} // while
			}
		};
		executorService.submit(runnable);
	}
//
//	void stopServer() {
//		Iterator<Client> iterator = connection.keySet().iterator();
//		try {
//			// client 접속해제
//			while (iterator.hasNext()) {
//				Client client = iterator.next();
//				client.socket.close();
//				iterator.remove();
//			}
//
//			if (serverSocket != null && !serverSocket.isClosed()) {
//				serverSocket.close();
//			}
//
//			if (executorService != null && !executorService.isShutdown()) {
//				executorService.shutdownNow();
//			}
//			System.out.println("[서버 멈춤]");
//			ta.append("[서버 멈춤]" + "\r\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) {
		new Server();
	}
	
}
