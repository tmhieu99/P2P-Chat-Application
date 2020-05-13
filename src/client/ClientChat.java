package client;

import java.net.*;
import java.io.*;

public class ClientChat {

    /* Client init */
    private String username = "";
    private int port = 0; //private int port = null;
    private ServerSocket serverPeer;
    private boolean stopped = false;

    public Chat(String username, int port) throws IOException {
        this.username = username;
        this.port = port;
        serverPeer = new ServerSocket(port);
        (new PeerConnect()).start();
    }

    public void client_stop() {
        try {
            stopped = true;
            serverPeer.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
