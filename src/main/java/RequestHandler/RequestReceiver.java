package RequestHandler;

import java.util.ArrayList;
import java.util.regex.Pattern;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import protocol.Peer;

public class RequestReceiver {

	public static ArrayList<String> get_one_info(String message) {
		if (find_account.matcher(message).matches()) {
			ArrayList<String> account_infomation = new ArrayList<String>();
			
			Document document = string_to_xml(message);

			String name = document
				.getElementsByTagName("PEER_NAME")
				.item(0).getTextContent();
			account_infomation.add(name);

			String port = document
				.getElementsByTagName("PORT")
				.item(0).getTextContent();
			account_infomation.add(port);

			return account_infomation;
		} else
			return null;
	}

	public static String get_inactive_info(String message) {
		if (request.matcher(message).matches()) {
			Document document = string_to_xml(message);

			String status = document
				.getElementsByTagName("STATUS")
				.item(0).getTextContent();
			
			if(status.equals("ONLINE")) return null;

			if(status.equals("OFFLINE")) {
				String name = document
				.getElementsByTagName("PEER_NAME")
				.item(0).getTextContent();

				return name;
			}
		}
		return "";
	}

	public static ArrayList<Peer> get_all_info(String message) {
		if (find_accounts.matcher(message).matches()) {
			ArrayList<Peer> account_list = new ArrayList<Peer>();

			Document document = string_to_xml(message);

			NodeList node_list = document.getElementsByTagName("PEER");

			for(int i = 0; i < node_list.getLength(); i = i + 1) {
				Node node = node_list.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;

						String name = element
							.getElementsByTagName("PEER_NAME")
							.item(0).getTextContent();
						String host = element
							.getElementsByTagName("IP")
							.item(0).getTextContent();
						Integer port = Integer.parseInt(element
							.getElementsByTagName("PORT")
							.item(0).getTextContent());
//						Boolean friended = element
//							.getElementsByTagName("FRIEND")
//							.item(0).getTextContent();
//						)
						
						Peer new_peer = new Peer();
						new_peer.setPeer(name, host, port);

						account_list.add(new_peer);
				}	
			}

			return account_list;
		} else
			return null;
	}

	public static String get_text_msg_content(String message) {
		if (find_message.matcher(message).matches()) {
			int begin = "<CHAT_MSG>".length();
			int end = message.length() - "</CHAT_MSG>".length();
			String text_message = message.substring(begin, end);
			return text_message;
		} else
			return "";
	}

	public static String get_request_name(String msg) {
		if (checkRequest.matcher(msg).matches()) {
			int length = msg.length();
			String name = msg
					.substring(
							("<CHAT_REQ>" + "<PEER_NAME>")
									.length(),
							length
									- ("</PEER_NAME>" + "</CHAT_REQ>")
											.length());
			return name;
		}
		return "";
	}

	public static boolean get_file(String name) {
		if (check_file.matcher(name).matches())
			return true;
		else
			return false;
	}

	public static boolean get_ack(String message) {
		if (feed_back.matcher(message).matches())
			return true;
		else
			return false;
	}

	private static Document string_to_xml(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
			Document document = builder.parse(
				new InputSource(new StringReader(xmlString))
			);
			
			//Normalize xml structure
			document.normalize();

            return document;
        }
        catch (Exception e)
        {
            e.printStackTrace();
		}
		
        return null;
    }

	private static final Pattern find_account = Pattern.compile(
			"<SESSION>" + "<PEER_NAME>" + ".*"
			+ "</PEER_NAME>" + "<PORT>" + ".*"
			+ "</PORT>" + "</SESSION>"
		);

	private static final Pattern find_accounts = Pattern.compile(
		"<SESSION_ACCEPT>" + "(" + "<PEER>"
		+ "<PEER_NAME>" + ".+" + "</PEER_NAME>"
		+ "<IP>" + ".+" + "</IP>" + "<PORT>"
		+ "[0-9]+" + "</PORT>" + "</PEER>" + ")*"
		+ "</SESSION_ACCEPT>");

	private static final Pattern find_message = Pattern.compile(
		"<CHAT_MSG>"+ ".*"
		+ "</CHAT_MSG>"
	);

	private static final Pattern checkRequest = Pattern.compile("<CHAT_REQ>"
				+ "<PEER_NAME>" + "[^<>]*" + "</PEER_NAME>"
				+ "</CHAT_REQ>");

//	private static final Pattern get_friend_req = Pattern.compile("<FRIEND_REQ>"
//			+ "<PEER_NAME>" + "[^<>]*" + "</PEER_NAME>"
//			+ "</FRIEND_REQ>");
	
	private static final Pattern check_file = Pattern.compile(
		"<FILE_REQ>" + ".*"
		+ "</FILE_REQ>"
	);

	private static final Pattern feed_back = Pattern.compile(
			"<FILE_REQ_ACK>" + ".*"
			+ "</FILE_REQ_ACK>"
	);

	private static final Pattern request = Pattern.compile(
		"<SESSION_KEEP_ALIVE>" + "<PEER_NAME>"
			+ "[^<>]+" + "</PEER_NAME>"+ "<STATUS>"
			+ "(" + "ONLINE" + "|"+ "OFFLINE" + ")"
			+ "</STATUS>" + "</SESSION_KEEP_ALIVE>");

}

