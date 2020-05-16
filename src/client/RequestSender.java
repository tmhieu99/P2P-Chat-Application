package client;
//TODO DONE
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RequestSender {

    /* Function to generate and send AccountRequest */
    public static String send_acc_req(String name, String port) {

        return "<SESSION>" + "<PEER_NAME>" + name
               + "</PEER_NAME>" + "<PORT>" + port
               + "</PORT>" + "</SESSION>" ;
    }

    /* Function to check online */
    public static String send_onl_status(String name) {

        return  "<SESSION_KEEP_ALIVE>" + "<PEER_NAME>" + name
                + "</PEER_NAME>" + "<STATUS>" + "<ALIVE>" + "</STATUS>"
                + "<SESSION_KEEP_ALIVE>";
    }

    /* Function to check offline */
    public static String send_off_status(String name) {

        return "<SESSION_KEEP_ALIVE>" + "<PEER_NAME>" + name
                + "</PEER_NAME>" + "<STATUS>" + "<DIED>" + "</STATUS>"
                + "<SESSION_KEEP_ALIVE>";
    }

    /* Function to generate peer list from request */

    public static String peer_list_request(ArrayList<Peer> peer_list) {
        String msg = "<SESSION_ACCEPT>";
        for (int i = 0; i < peer_list.size(); i++) {
            Peer peer = peer_list.get(i);
            msg += "<PEER>";
            msg += "<PEER_NAME>";
            msg += peer.getName();
            msg += "</PEER_NAME>";
            msg += "<IP>";
            msg += peer.getHost();
            msg += "</IP>";
            msg += "<PORT>";
            msg += peer.getPort();
            msg += "</PORT>";
            msg += "</PEER>";
        }
        //return msg += "</SESSION_ACCEPT>";
        msg += "</SESSION_ACCEPT>";
        return msg;
    }

    /* Function to send ChatRequest */
    public static String send_request(String name) {
        String request = "<CHAT_REQ>" + "<PEER_NAME>" + name + "</PEER_NAME>" + "</CHAT_REQ>";
        return request;
    }

    /* Function to send text message */
    public static String send_text_msg(String msg) {

        String res = "";
        String f_res = "";
        Pattern ptr = Pattern.compile("[^<>]*(<|>)]");
        Matcher finder = ptr.matcher(msg);
        int msg_size = msg.length();


        while(finder.find()) {
            String sub_msg = finder.group(0);
            int tail = sub_msg.length();
            //char next = msg.charAt(sub_msg.length() - 1);
            char next = msg.charAt(tail - 1);

            System.out.println(res);
            System.out.println(sub_msg);
            System.out.println(tail);

            res += sub_msg + next;
            sub_msg = msg.substring(tail, msg_size);
            msg = sub_msg;
            finder = ptr.matcher(msg);
        }
        res += msg;
        f_res = "<CHAT_MSG>" + res + "</CHAT_MSG>";
        return f_res;
    }

    /* Function to send FileRequest */
    public static String send_file_req (String name) {
        String file_request = "<FILE_REQ>" + name + "</FILE_REQ>";
        return file_request;
    }
}
