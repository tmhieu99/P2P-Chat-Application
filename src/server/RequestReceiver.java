package server;

//TODO DONE

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import client.Peer;
import org.w3c.dom.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.regex.*;
import java.io.*;


public class RequestReceiver {

    public static ArrayList<String> get_one_info(String msg) throws IOException, SAXException, ParserConfigurationException {

        if (get_one.matcher(msg).matches()) {
            ArrayList<String> account_info = new ArrayList<String>();

            Document doc = string_to_xml(msg);

            String name = doc
                    .getElementsByTagName("PEER_NAME")
                    .item(0).getTextContent();
            account_info.add(name);

            String port = doc
                    .getElementsByTagName("PORT")
                    .item(0).getTextContent();
            account_info.add(port);

            return account_info;
        }
        else {
            return null;
        }
    }

    public static String get_inactive_info(String msg) throws IOException, SAXException, ParserConfigurationException {
        //TODO DONE
        if (check_status.matcher(msg).matches()) {
            Document doc = string_to_xml(msg);

            String status = doc
                    .getElementsByTagName("STATUS")
                    .item(0).getTextContent();
            if (status.equals("ALIVE")) return null;
            if (status.equals("DIED")) {
                String name = doc
                        .getElementsByTagName("PEER_NAME")
                        .item(0).getTextContent();
                return name;
            }
        }
        return "";
    }
    /* Function to get friend list */
    public static ArrayList<Peer> get_all_info(String msg) throws IOException, SAXException, ParserConfigurationException {
        //TODO DONE
        if (get_all.matcher(msg).matches()) {
            ArrayList<Peer> account_list = new ArrayList<Peer>();
            Document doc = string_to_xml(msg);
            NodeList nodelist = doc.getElementsByTagName("PEER");

            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node = nodelist.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String name = element
                            .getElementsByTagName("PEER_NAME")
                            .item(0).getTextContent();
                    String host = element
                            .getElementsByTagName("IP")
                            .item(0).getTextContent();
                    Integer port = Integer.parseInt(element
                        .getElementsByTagName("PORT")
                        .item(0).getTextContent()
                    );

                    //ADD NEW PEER TO ACCOUNT LIST
                    Peer newpeer = new Peer();
                    newpeer.setPeer(name, host, port);
                    account_list.add(newpeer);
                }
            }
            return account_list;

        }
        else {
            return null;
        }
    }

    /* Function to convert DOM objects to XML documents */
    private static  Document string_to_xml(String xmlstring) throws ParserConfigurationException, IOException, SAXException {

            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null; //Get DOM object from API
        try {
            //Init default document builder object
            builder = fac.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlstring)));

            document.normalize(); // normalize entry
            return document;
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /* Function to get text contain of the message */
    public static String get_message(String msg) {
        //TODO DONE
        if (check_msg.matcher(msg).matches()) {
            int head_pos = "<CHAT_MSG>".length();
            int tail_pos = msg.length() - "</CHAT_MSG>".length();
            //return msg.substring(head_pos, tail_pos);
            String msg_content = msg.substring(head_pos, tail_pos);
            return msg_content;
        }
        else {
            return "";
        }
    }
    public static String get_name(String msg) {
        //TODO DONE
        if (get_request.matcher(msg).matches()) {
        String name = msg.substring(("<CHAT_REQ>" + "<PEER_NAME>").length(),
                    msg.length() -  ("<PEER_NAME>" + "<CHAT_REQ>").length()
            );
        return name;
        }
        return "";
    }

//   /* Function to check if file exist */
    public static boolean check_file_status(String name) {
        //TODO DONE
        if (get_file.matcher(name).matches()) {
            return true;
        }
        else {
            return false;
        }
    }
    /* Function to check if acknowledge from client exist */
    public static boolean check_ack(String msg) {
        //TODO DONE
        if (get_ack.matcher(msg).matches()) {
            return true;
        }
        else
            return false;
    }



    /* Function using RegEx to get an account's information */
    private static Pattern get_one = Pattern.compile(
            "<SESSION>" + "<PEER_NAME>"
            + ".*"      + "</PEER_NAME>" + "<PORT>"
            + ".*"      + "</PORT>"      + "</SESSION>"
    );

    /* Function using RegEx to get all accounts'information */
    private static Pattern get_all = Pattern.compile(
            "<SESSION_ACCEPT>" + "(" + "<PEER>"
            + "<PEER_NAME>" + ".+" + "</PEER_NAME>"
            + "<IP>" + ".+" + "</IP>" + "<PORT>"
            + "[0-9]+" + "</PORT>" + "</PEER>"
            + ")*" + "</SESSION_ACCEPT>"
    );

    /* Function using RegEx to get message's content */
    private static Pattern check_msg = Pattern.compile(
        "<CHAT_MSG>" + ".*" + "</CHAT_MSG>"
    );

    /* Function using RegEx to get file's content */
    private static Pattern get_file = Pattern.compile(
        "<FILE_REQ>" + ".*" + "</FILE_REQ>"
    );

    /* Function using RegEx to check request to Client */
    private static Pattern check_status = Pattern.compile(
            "<SESSION_KEEP_ALIVE>" + "<PEER_NAME>" + "[^<>]+"
            + "</PEER_NAME>" + "<STATUS>" + "(" + "<ALIVE>" + "|"
            + "DIED" + ")" + "</STATUS>" + "</SESSION_KEEP_ALIVE>"
    );
    /* Function to get request from Client*/
    private static Pattern get_request = Pattern.compile(
            "<CHAT_REQ>" + "<PEER_NAME>" + "[^<>]*"
                        + "</PEER_NAME>" + "</CHAT_REQ>"
    );

    /* Function using RegEx to get acknowledgement content */
    private static Pattern get_ack = Pattern.compile(
            "FILE_REQ_ACK" + ".*" + "/FILE_REQ_ACK"
    );


}
