package server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import client.Peer;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.regex.*;
import java.io.*;


public class RequestReceiver {

    public static ArrayList<String> get_account_info(String msg) throws IOException, SAXException, ParserConfigurationException {

        if (get_one.matcher(msg).matches()) {
            ArrayList<String> account_info = new ArrayList<String>();

        Document doc = String2Xml(msg);

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

    public static String get_inactive_account(String msg) {
        //TODO
        return "";
    }

    public static ArrayList<Peer> get_all_info(String msg) {
        //TODO
        return null;
    }

    /* Function to convert DOM objects to XML documents */
    private static  Document String2Xml(String XmlString) throws ParserConfigurationException, IOException, SAXException {

            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null; //Get DOM object from API
        try {
            //Init default document builder object
            builder = fac.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(XmlString)));

            document.normalize(); // normalize entry
            return document;
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public static String get_message(String msg) {
        //TODO
        return null;
    }
    public static String get_name(String msg) {
        //TODO
        return null;
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
    private static Pattern get_msg = Pattern.compile(
        "<CHAT_MSG>" + ".*" + "</CHAT_MSG>"
    );

    /* Function using RegEx to get file's content */
    private static Pattern get_file = Pattern.compile(
        "<FILE_REQ>" + ".*" + "</FILE_REQ>"
    );

    /* Function using RegEx to send request to Client */
    private static Pattern send_request = Pattern.compile(
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
