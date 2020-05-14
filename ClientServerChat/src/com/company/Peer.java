package com.company;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;
public class Peer {
    ServerSocket ss;
    Socket s;
    Socket sclient;
    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
    String name;
    String clientName;
    public Peer(){}
    private void ServerActive(String name) throws IOException{
        ss = new ServerSocket(1234);
        s = ss.accept();
        System.out.println("New client request received : " + s);
        this.name = name;
    };
    private void ClientActive(int Port) throws IOException{
        sclient = new Socket("127.0.0.1", Port);
    }
    private void SendMess(Scanner scn, DataOutputStream dos) throws IOException{
        while (true) {

            // read the message to deliver.
            String msg = scn.nextLine();

            try {
                // write on the output stream
                dos.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void ReadMess(Scanner scn, DataInputStream dis) throws IOException{
        while (true) {
            try {
                // read the message sent to this client
                String msg = dis.readUTF();
                System.out.println(clientName+": "+msg);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        Peer peer = new Peer();
        DataInputStream dis;
        DataOutputStream dos;
        System.out.println("Choose [Server/Client]: ");
        Scanner scn = new Scanner(System.in);;
        String mode = keyboard.readLine();
        if( mode != null){
            switch(mode){
                case "Server":
                    System.out.println("Waiting for client...");
                    peer.ServerActive("Server1");
                    dis = new DataInputStream(peer.s.getInputStream());
                    dos = new DataOutputStream(peer.s.getOutputStream());
                    System.out.println("Waiting for client name...");
                    String msg = dis.readUTF();
                    peer.clientName = msg;

                    System.out.println("Enter server name:");
                    String mess2 = scn.nextLine();
                    peer.name = mess2;
                    dos.writeUTF(mess2);

                    System.out.println("Start chatting!");
                    Thread sendMessage = new Thread(new Runnable()
                    {
                        @Override
                        public void run() {
                            try {
                                peer.SendMess(scn, dos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    // readMessage thread
                    Thread readMessage = new Thread(new Runnable()
                    {
                        @Override
                        public void run() {

                            try {
                                peer.ReadMess(scn, dis);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    sendMessage.start();
                    readMessage.start();
                    break;
                case "Client":
                    peer.ClientActive(1234);
                    dis = new DataInputStream(peer.sclient.getInputStream());
                    dos = new DataOutputStream(peer.sclient.getOutputStream());

                    System.out.println("Enter client name:");
                    String mess = scn.nextLine();
                    peer.name = mess;
                    dos.writeUTF(mess);

                    System.out.println("Waiting for server name...");
                    String msg2 = dis.readUTF();
                    peer.clientName = msg2;

                    System.out.println("Start chatting!");
                    //peer.clientName = "Khoa";
                    Thread senMessage = new Thread(new Runnable()
                    {
                        @Override
                        public void run() {
                            try {
                                peer.SendMess(scn, dos);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Thread reaMessage = new Thread(new Runnable()
                    {
                        @Override
                        public void run() {
                            try {
                                peer.ReadMess(scn, dis);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    senMessage.start();
                    reaMessage.start();
                    break;
            }
        }
    }
}
