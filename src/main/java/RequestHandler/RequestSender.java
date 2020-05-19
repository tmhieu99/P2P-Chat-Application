package RequestHandler;

import protocol.Peer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class RequestSender {

	public static String send_acc_req(String name, String port) {
		return "<SESSION>" + "<PEER_NAME>"
			+ name + "</PEER_NAME>" + "<PORT>"
			+ port + "</PORT>" + "</SESSION>";
	}

	public static String send_online_status(String name) {
		return "<SESSION_KEEP_ALIVE>" + "<PEER_NAME>"
			+ name + "</PEER_NAME>" + "<STATUS>"
			+ "ONLINE" + "</STATUS>"
			+ "</SESSION_KEEP_ALIVE>";
	}

	public static String send_offline_status(String name) {
		return "<SESSION_KEEP_ALIVE>" + "<PEER_NAME>" + name
			+ "</PEER_NAME>" + "<STATUS>" + "OFFLINE"
			+ "</STATUS>" + "</SESSION_KEEP_ALIVE>";
	}
	
	public static String send_peerList(ArrayList<Peer> peer_list) throws Exception {
		String msg = "<SESSION_ACCEPT>";
		int size = peer_list.size();				
		for (int i = 0; i < size; i++) {		
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
		msg += "</SESSION_ACCEPT>";
		return msg;
	}

	public static String send_chat_req(String name) {
		return "<CHAT_REQ>" + "<PEER_NAME>" + name
				+ "</PEER_NAME>" + "</CHAT_REQ>";
	}

//	public static String send_friend_req(String name) {
//		return "<FRIEND_REQ>" + "<PEER_NAME>" + name
//				+ "</PEER_NAME>" + "</FRIEND_REQ>";
//	}

	public static String send_text_content(String message) {
		Pattern checkMessage = Pattern.compile("[^<>]*(<|>)");
		Matcher findMessage = checkMessage.matcher(message);
		String result = "";
		while (findMessage.find()) {
			String subMessage = findMessage.group(0);
			int begin = subMessage.length();
			char nextChar = message.charAt(subMessage.length() - 1);
			System.out.println(result);
			result += subMessage + nextChar;
			subMessage = message.substring(begin, message.length());
			message = subMessage;
			findMessage = checkMessage.matcher(message);
		}
		result += message;
		return "<CHAT_MSG>" + result + "</CHAT_MSG>";
	}

	public static String send_file_req(String name) {
		return "<FILE_REQ>" + name + "</FILE_REQ>";
	}

}

