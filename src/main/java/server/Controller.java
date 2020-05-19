package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import java.util.ArrayList;
import java.util.LinkedList;

import client.*;
import protocol.Peer;
import RequestHandler.RequestReceiver;
import RequestHandler.RequestSender;

public class Controller {

    public static ArrayList<Peer> online_list = null;
    //public static LinkedList<Peer> friend_list = null;
    //public static ArrayList<Peer> friend_list = new ArrayList<>() ;

    //private static boolean friended = false;

    private InetAddress server_ip_addr;
    private int server_port = 8080;

    private int peer_port = 10000;
    private String username = "";

    private Socket client_socket;
    private boolean isStop = false;
    private ClientChat server;

    private ObjectInputStream listener;
    private ObjectOutputStream sender;

    public Controller(String server_ip, int server_port, int peer_port, String username, String dataUser) throws Exception {
        this.server_ip_addr = InetAddress.getByName(server_ip);
        this.server_port = server_port;
        this.username = username;
        this.peer_port = peer_port;
        online_list = RequestReceiver.get_all_info(dataUser);

        new Thread(new Runnable(){

            @Override
            public void run() {
                update_online_list();
            }
        }).start();
        server = new ClientChat(username, peer_port);
        (new Request()).start();
    }

    public class Request extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isStop) {
                try {
                    Thread.sleep(1500);

                    // Connect to server
                    SocketAddress addressServer = new InetSocketAddress(server_ip_addr, server_port);
                    client_socket = new Socket(); client_socket.connect(addressServer);

                    // Send online message
                    String msg = RequestSender.send_online_status(username);
                    sender = new ObjectOutputStream(client_socket.getOutputStream());
                    sender.writeObject(msg); sender.flush();

                    // Get acknowledgement (new online list)
                    listener = new ObjectInputStream(client_socket.getInputStream());
                    msg = (String) listener.readObject(); listener.close();
                    online_list = RequestReceiver.get_all_info(msg);

                    // Update online list
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            update_online_list();
                            //update_friend_list();
                        }
                    }).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public void add_friend(String IP, int host, String guest) throws Exception {
//        //TODO add friend func
//        final Socket friendSocket = new Socket(InetAddress.getByName(IP), host);
//        ObjectOutputStream send_friend_request = new ObjectOutputStream(friendSocket.getOutputStream());
//        send_friend_request.writeObject(RequestSender.send_friend_req(username));
//        send_friend_request.flush();
//        ObjectInputStream receiver = new ObjectInputStream(friendSocket.getInputStream());
//        String message = (String) receiver.readObject();

//        if (message.equals("<FRIEND_DENY>")) {
//
//            ControllerGUI.showDialog("This person doesn't want to be friend with you", false);
//            friendSocket.close();
//            return;
//        }
//        else {
//            new ClientChatGUI(username, guest, friendSocket, peer_port);
//        }
//    }
//
//    public void remove_friend(String IP, int host, String guest) {
//
//    }

    public void chat_request(String IP, int host, String guest) throws Exception {

        System.out.println("88");
        final Socket peerSocket = new Socket(InetAddress.getByName(IP), host);
        System.out.println("90");
        ObjectOutputStream sendchat_request = new ObjectOutputStream(peerSocket.getOutputStream());
        sendchat_request.writeObject(RequestSender.send_chat_req(username));
        sendchat_request.flush();
        ObjectInputStream receivedChat = new ObjectInputStream(peerSocket.getInputStream());
        String msg = (String) receivedChat.readObject();
        if (msg.equals("<CHAT_DENY />")) {
            ControllerGUI.showDialog("Your friend said deo", false);
            peerSocket.close();
            return;
        } else {
            new ClientChatGUI(username, guest, peerSocket, peer_port);
        }
    }

    public void stop_request() throws IOException, ClassNotFoundException {
        isStop = true;
        client_socket = new Socket();
        SocketAddress addressServer = new InetSocketAddress(server_ip_addr, server_port);
        client_socket.connect(addressServer);
        // Send offline message
        String msg = RequestSender.send_offline_status(username);
        sender = new ObjectOutputStream(client_socket.getOutputStream());
        sender.writeObject(msg); sender.flush(); sender.close();
        // Close chat room
        server.exit();
    }

    public void update_online_list() {
        int online_size = online_list.size();
        ControllerGUI.clear_online_list();
        for (int i = 0; i < online_size; i++)
            if (!online_list.get(i).getName().equals(username))
                ControllerGUI.update_online_list_UI(online_list.get(i).getName());
    }

//    public void update_friend_list() {
//        int friend_size = friend_list.size();
//        ControllerGUI.clear_friend_list();
//
//        for (int i = 0; i < friend_size; i++) {
//            if (!friend_list.get(i).getName().equals(username)) {
//                ControllerGUI.update_friend_list_UI(friend_list.get(i).getName());
//            }
//        }
//    }
}
