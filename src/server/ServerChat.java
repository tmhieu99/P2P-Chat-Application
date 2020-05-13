package server;

import java.io.*;
import java.net.*;
import java.util.*;

import client.Peer;

public class ServerChat {

    /* Init server */
    private ArrayList<Peer> peer_list = null;
    private ServerSocket server;
    private Socket socket;
    private ObjectInputStream in; //as listener to client
    private ObjectOutputStream out;//as sender to client
    public boolean stopped = false;
    public boolean exited = false;

    public void server_stop() throws IOException {
        stopped = true;
        server.close();
        socket.close();
    }
    public Server(String ip, int port) throws UnknownHostException, IOException {
        server = new ServerSocket(port, 100, InetAddress.getByName(ip));
        peer_list = new ArrayList<Peer>();
        (new WaitConnection()).start();
    }

    private boolean Listen() throws IOException, ClassNotFoundException {
        /* Fetch data from TCP */

        /* Waiting connections from Client */
        socket = server.accept();

        /* Listening */
        in = new ObjectInputStream(socket.getInputStream());

        /* Receive client's data */
        String msg = (String) in.readObject();

        /* Handling requests */

        //Decode is Request Receiver
        //Encode is Request Sender
        /*
        Implement various shit here
        ArrayList<String> account_info = Decode.
        */
    }

    public class WaitConnection extends Thread {

    }
}
