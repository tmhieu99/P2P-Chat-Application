package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mdlaf.MaterialLookAndFeel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ControllerGUI {

    static {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
            UIManager.put("Button.mouseHoverEnable", true);
            JFrame.setDefaultLookAndFeelDecorated(false);

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static String URL_DIR = System.getProperty("user.dir");

    private Controller client_node;
    private String server_ip = "";
    private int server_port = 8080;

    private int peer_port = 0;
    private String username = "";
    private String message = "";

    private JFrame fmMenu;
    private JTextField txtUsername, txtOnlineName, txtFriendName;
    private static JTextArea txtPeerList;

    private static JTextArea txtFriendList;

    private JButton btnChat, btnExit, btnAdd;

    public ControllerGUI(String server_ip, int server_port, int peer_port, String username, String message) throws Exception {
        this.server_ip = server_ip;
        this.server_port = server_port;
        this.peer_port = peer_port;
        this.username = username;
        this.message = message;

        initializeFrame();
        initializeLabel();
        initializeTextBox();
        initializeButton();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    fmMenu.setVisible(true);
                    client_node = new Controller(server_ip, server_port, peer_port, username, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ControllerGUI() throws Exception {
        initializeFrame();
        initializeLabel();
        initializeTextBox();
        initializeButton();
        client_node = new Controller(server_ip, server_port, peer_port, username, message);
    }

    private void initializeFrame() {
        fmMenu = new JFrame();
        fmMenu.setTitle("Menu");
        ImageIcon image = new ImageIcon(URL_DIR + "/src/main/resources/icons8-menu-64.png");
        fmMenu.setIconImage(image.getImage());
        fmMenu.setResizable(false);
        fmMenu.setBounds(100, 100, 400, 600);
        fmMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fmMenu.getContentPane().setLayout(null);
    }

    private void initializeLabel() {
        JLabel lbUsername = new JLabel("Username: ");
        lbUsername.setBounds(10, 17, 100, 16);
        fmMenu.getContentPane().add(lbUsername);

        JLabel lbOnlineList = new JLabel("Online list:");
        lbOnlineList.setBounds(10,50,100,16);
        fmMenu.getContentPane().add(lbOnlineList);

        JLabel lbFriendList = new JLabel("Friend list:");
        lbFriendList.setBounds(200,50,100,16);
        fmMenu.getContentPane().add(lbFriendList);

        JLabel lbFriendName = new JLabel("Add Friend: ");
        lbFriendName.setBounds(10, 445, 110, 16);
        fmMenu.getContentPane().add(lbFriendName);

        JLabel lbFriendText = new JLabel("Chat with: ");
        lbFriendText.setBounds(10, 476, 110, 16);
        fmMenu.getContentPane().add(lbFriendText);
    }

    private void initializeTextBox() {
        //User area
        txtUsername = new JTextField(this.username);
        txtUsername.setEditable(false);
        txtUsername.setColumns(10);
        txtUsername.setBounds(110, 11, 210, 28);
        fmMenu.getContentPane().add(txtUsername);

        //Online listing area
        txtPeerList = new JTextArea();
        txtPeerList.setText("");
        txtPeerList.setEditable(false);
        txtPeerList.setBounds(10, 80, 170, 340);
        fmMenu.getContentPane().add(txtPeerList);

        //Friend listing area
        txtFriendList = new JTextArea();
        txtFriendList.setText("");
        txtFriendList.setEditable(false);
        txtFriendList.setBounds(200, 80, 170, 340);
        fmMenu.getContentPane().add(txtFriendList);

        //Add friend area
        txtOnlineName = new JTextField("Type name to add");
        txtPeerList.setEditable(true);
        txtOnlineName.setColumns(10);
        txtOnlineName.setBounds(125, 439, 192, 28);
        fmMenu.getContentPane().add(txtOnlineName);

        //Chat with area
        txtFriendName = new JTextField("Type name to chat");
        txtFriendList.setEditable(true);
        txtFriendName.setColumns(10);
        txtFriendName.setBounds(125, 470, 192, 28);
        fmMenu.getContentPane().add(txtFriendName);
    }

    private void initializeButton() {

        btnChat = new JButton("Chat");
        btnChat.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent arg0) {
                System.out.println("here1");
                String name = txtOnlineName.getText();
                if (name.equals("") || Controller.online_list == null) {
                    JOptionPane.showMessageDialog(fmMenu, "Name 's friend mistake!");
                    return;
                }
                if (name.equals(username)) {
                    JOptionPane.showMessageDialog(fmMenu, "You can't chat with yourself !");
                    return;
                }
                int size = Controller.online_list.size();
                System.out.println("here2");
                System.out.println(size);
                //System.out.println(toString(online_list));
                for (int i = 0; i < size; i++) {
                    System.out.println("here3");
                    System.out.println(size);
                    if (name.equals(Controller.online_list.get(i).getName())) {
                        System.out.println("here4");

                        try {
                            System.out.println("here");
                            client_node.chat_request(Controller.online_list.get(i).getHost(),Controller.online_list.get(i).getPort(), name);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                JOptionPane.showMessageDialog(fmMenu, "Can't found your friend!");
            }
        });
        btnChat.setBounds(150, 510, 113, 29);
        fmMenu.getContentPane().add(btnChat);

        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pressed action");
//                int result = JOptionPane.showConfirmDialog(
//                        fmMenu, "Do you want exit now?", null,
//                        JOptionPane.YES_NO_OPTION
//                );
//                if (result == 0) {
//                    try {
//                        client_node.stop_request();
//                        fmMenu.dispose();
//                    } catch (Exception e) {
//                        fmMenu.dispose();
//                    }
//                }
            }
        });
        btnAdd.setBounds(10, 510, 113, 29);
        fmMenu.getContentPane().add(btnAdd);



        btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int result = JOptionPane.showConfirmDialog(
                        fmMenu, "Do you want exit now?", null,
                        JOptionPane.YES_NO_OPTION
                );
                if (result == 0) {
                    try {
                        client_node.stop_request();
                        fmMenu.dispose();
                    } catch (Exception e) {
                        fmMenu.dispose();
                    }
                }
            }
        });
        btnExit.setBounds(290, 510, 113, 29);
        fmMenu.getContentPane().add(btnExit);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ControllerGUI window = new ControllerGUI();
                    window.fmMenu.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static int showDialog(String msg, boolean type) {
        JFrame frameMessage = new JFrame();
        if(type)
            return JOptionPane.showConfirmDialog(
                    frameMessage, msg, null,
                    JOptionPane.YES_NO_OPTION
            );
        else
            JOptionPane.showMessageDialog(frameMessage, msg);
        return -22;

    }

    public static void update_online_list_UI(String msg) {

        txtPeerList.append(msg + "\n");
    }

//    public static void update_friend_list_UI(String msg) {
//
//        txtFriendList.append(msg + "\n");
//    }


    public static void clear_online_list() {
        txtPeerList.setText("");
        txtPeerList.setText("");
    }

//    public static void clear_friend_list() {
//        txtFriendList.setText("");
//        txtFriendList.setText("");
//    }
}
