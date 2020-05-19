package protocol;

public class Peer {

	private String namePeer = "";
	private String hostPeer = "";
	private int portPeer = 0;
	//private String status ;
	//private boolean isFriend;

	public void setPeer(String name, String host, int port) {
		namePeer = name;
		hostPeer = host;
		portPeer = port;

	}

//	public void setPeer(String name, String host, int port, boolean friended) {
//		namePeer = name;
//		hostPeer = host;
//		portPeer = port;
//		isFriend = friended;
//	}

	public void setName(String name) {
		namePeer = name;
	}

	public void setHost(String host) {
		hostPeer = host;
	}

	public void setPort(int port) {
		portPeer = port;
	}

//	public void getOnline() {
//		this.status = "online";
//	}
//
//	public void getOffline() {
//		this.status = "offline";
//	}
//
//	public void setFriend() {
//		this.isFriend = true;
//	}

	public String getName() {
		return namePeer;
	}

	public String getHost() {
		return hostPeer;
	}

	public int getPort() {
		return portPeer;
	}


}

