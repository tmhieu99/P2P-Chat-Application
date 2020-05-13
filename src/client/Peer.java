package client;

public class Peer {
    private String Peer_name = "";
    private String Peer_host = "";
    private int Peer_port = 0;
    //private int Peer_port = null;

    public void setPeer(String name, String host, int port) {
        Peer_name = name;
        Peer_host = host;
        Peer_port = port;
    }
    public void Name(String name) {
        Peer_name = name;
    }
    public void Host(String host) {
        Peer_host = host;
    }
    public void Port(int port) {
        Peer_port = port;
    }

    public String getName() {
        return Peer_name;
    }
    public String getHost() {
        return Peer_host;
    }
    public int getPort() {
        return Peer_port;
    }
}
