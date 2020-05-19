package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import protocol.Peer;
import RequestHandler.RequestReceiver;
import RequestHandler.RequestSender;

public class Server {

	private ArrayList<Peer> peer_list = null;	
	private ServerSocket server;						
	private Socket connection;
	private ObjectOutputStream sender;		
	private ObjectInputStream listener;			
	public boolean isStop = false, isExit = false;		
	
	
	public Server(String ip, int port) throws Exception {
		server = new ServerSocket(port, 50, InetAddress.getByName(ip));		
		peer_list = new ArrayList<Peer>();
		(new WaitForConnect()).start();			
	}

	public void stop() throws Exception {
		isStop = true;
		server.close();							
		connection.close();						
	}
	
	private boolean listen() throws Exception {
		/* Get data by TCP */

		// Waiting for connection
		connection = server.accept();
		// Listening the client
		listener = new ObjectInputStream(connection.getInputStream());	
		// Get data from the client
		String message = (String) listener.readObject();

		/* Analyze message*/
		// CASE 1 : Client request a new account
		ArrayList<String> acc_info = RequestReceiver.get_one_info(message);
		if (acc_info != null) {
			// Prepare data for new peer
			String acc_ip = connection.getInetAddress().getHostAddress();
			Integer acc_port = Integer.parseInt(acc_info.get(1));
			String acc_name = acc_info.get(0);
			
			if (getIndexByName(acc_name) == -1)	 {
				// Add new peer to peer list			
				saveNewPeer(acc_name, acc_ip,acc_port);			
				// Update message to application console
				ServerGUI.updateMessage(message);
			}	
			else return false;
		} 
		// CASE 2 : Client send a keep alive message
		if (acc_info == null) {		
			String acc_name = RequestReceiver.get_inactive_info(message);
			int index = getIndexByName(acc_name);
			if(index != -1) {peer_list.remove(index); isExit = true;}
		}

		return true;
	}
	public class WaitForConnect extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				while (!isStop) {
					String message = "";

					if (listen()) {
						if (isExit) {
							isExit = false;
							continue;
						}
						message = RequestSender.send_peerList(peer_list);
					} else {
						message = "<SESSION_DENY />";
					}

					sender = new ObjectOutputStream(connection.getOutputStream());
					sender.writeObject(message);
					sender.flush(); sender.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// Tool function of this.peer_list: Save a new peer
	private void saveNewPeer(String user, String ip, int port) throws Exception {
		Peer new_peer = new Peer();		
		if (peer_list.size() == 0)				
			peer_list = new ArrayList<Peer>();
		new_peer.setPeer(user, ip, port);		
		peer_list.add(new_peer);					
	}
	// Tool function of this.peer_list: get peer index
	private Integer getIndexByName(String name) throws Exception {
		if (peer_list == null) return -1;
		
		int size = peer_list.size();

		for (int i = 0; i < size; i++) {
			Peer peer = peer_list.get(i);
			if (peer.getName().equals(name)) return i;
		}

		return -1;
	}

}

