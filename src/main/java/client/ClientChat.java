package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import RequestHandler.RequestReceiver;
import server.*;

public class ClientChat {

	private String username = "";
	private int port = 0;
	private ServerSocket serverPeer;
	private boolean isStop = false;

	public ClientChat(String username, int port) throws Exception {
		this.username = username;
		this.port = port;
		serverPeer = new ServerSocket(port);
		(new WaitPeerConnect()).start();
	}
	
	public void exit() throws IOException {
		isStop = true;
		serverPeer.close();
	}

	class WaitPeerConnect extends Thread {

		Socket connection;
		ObjectInputStream getRequest;

		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					connection = serverPeer.accept();
					getRequest = new ObjectInputStream(connection.getInputStream());
					String msg = (String) getRequest.readObject();
					String name = RequestReceiver.get_request_name(msg);

					int result = ControllerGUI.showDialog("Would you like chat with " + name, true);
					ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
					if (result == 0) {
						send.writeObject("<CHAT_ACCEPT />");
						new ClientChatGUI(username, name, connection, port);
					} else if (result == 1) {
						send.writeObject("<CHAT_DENY />");
					}

//					int friend_res = ControllerGUI.showDialog("Would you like to add " + name +" as your friend", true);
//					if (friend_res == 0) {
//						send.writeObject("<FRIEND_ACCEPT>");
//						System.out.println("something");
//						//TODO CALL ADD FRIEND HERE
//					}
//					else if (friend_res == 1) {
//						send.writeObject("<FRIEND_DENY>");
//					}
//					send.flush();
				}
				catch (Exception e) {
					break;
				}
			}
			try {
				serverPeer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
