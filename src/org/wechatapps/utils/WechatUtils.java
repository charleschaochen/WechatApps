package org.wechatapps.utils;

import org.dom4j.DocumentException;
import org.wechatapps.pojo.Message;
import org.wechatapps.pojo.event.ClickEvent;
import org.wechatapps.pojo.recieve.*;
import org.wechatapps.pojo.send.SendTextMessage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

/*
 * @Description Wechat API Utilities
 * @author Charles Chen
 * @date 14-2-19
 * @version 1.0
 */
public class WechatUtils {
	/**
	 * Message type enum
	 */
	public enum MESS_TYPE {
		TEXT, IMAGE, VOICE, VIDEO, LOCATION, LINK, EVENT
	}

	/* End: Wechat Message Types */

	public static final String UTF8 = "utf-8";

	/**
	 * Retrieve wechat message details
	 * 
	 * @param request
	 *            HTTP Request
	 * @return
	 * @throws java.io.IOException
	 * @throws DocumentException
	 * @throws org.dom4j.DocumentException
	 */
	public static Message retrieveMessage(HttpServletRequest request)
			throws IOException, DocumentException {
		Message message = new Message();
		InputStream is = request.getInputStream();
		Map<String, String> data = XMLUtils.parseXmlToMap(is, UTF8);

		// Build up message object according to the type
		String type = data.get("MsgType");
		if (type == null) {
			return null;
		}
		// Build specific kind of message according to the type
		switch (MESS_TYPE.valueOf(type.toUpperCase())) {
		case TEXT:
			message.setType(MESS_TYPE.TEXT.toString());
			message.setMess(buildTextMessage(data));
			break;
		case IMAGE:
			message.setType(MESS_TYPE.IMAGE.toString());
			message.setMess(buildPicMessage(data));
			break;
		case VOICE:
			message.setType(MESS_TYPE.VOICE.toString());
			message.setMess(buildVoiceMessage(data));
			break;
		case VIDEO:
			message.setType(MESS_TYPE.VIDEO.toString());
			message.setMess(buildVideoMessage(data));
			break;
		case LOCATION:
			message.setType(MESS_TYPE.LOCATION.toString());
			message.setMess(buildLocationMessage(data));
			break;
		case LINK:
			message.setType(MESS_TYPE.LINK.toString());
			message.setMess(buildLinkMessage(data));
			break;
		case EVENT:
			message.setType(MESS_TYPE.EVENT.toString());
			message.setMess(buildClickEvent(data));
			break;
		default:
			message = null;
			break;
		}
		return message;
	}

	/**
	 * Build text message from data map
	 * 
	 * @param data
	 *            Data Map
	 * @return
	 */
	public static ReceiveTextMessage buildTextMessage(Map<String, String> data) {
		ReceiveTextMessage text = new ReceiveTextMessage();
		for (Enum en : ReceiveTextMessage.AttributeEnum.values()) {
			text.setAttribute(en.toString(), data.get(en.toString()));
		}
		return text;
	}

	/**
	 * Build link message from data map
	 * 
	 * @param data
	 * @return
	 */
	public static ReceiveLinkMessage buildLinkMessage(Map<String, String> data) {
		ReceiveLinkMessage link = new ReceiveLinkMessage();
		for (Enum en : ReceiveLinkMessage.AttributeEnum.values()) {
			link.setAttribute(en.toString(), data.get(en.toString()));
		}
		return link;
	}

	/**
	 * Build location message from data map
	 * 
	 * @param data
	 * @return
	 */
	public static ReceiveLocationMessage buildLocationMessage(
			Map<String, String> data) {
		ReceiveLocationMessage location = new ReceiveLocationMessage();
		for (Enum en : ReceiveLocationMessage.AttributeEnum.values()) {
			location.setAttribute(en.toString(), data.get(en.toString()));
		}
		return location;
	}

	/**
	 * Build picture message
	 * 
	 * @param data
	 * @return
	 */
	public static ReceivePicMessage buildPicMessage(Map<String, String> data) {
		ReceivePicMessage pic = new ReceivePicMessage();
		for (Enum en : ReceivePicMessage.AttributeEnum.values()) {
			pic.setAttribute(en.toString(), data.get(en.toString()));
		}
		return pic;
	}

	/**
	 * Build video message
	 * 
	 * @param data
	 * @return
	 */
	public static ReceiveVideoMessage buildVideoMessage(Map<String, String> data) {
		ReceiveVideoMessage video = new ReceiveVideoMessage();
		for (Enum en : ReceiveVideoMessage.AttributeEnum.values()) {
			video.setAttribute(en.toString(), data.get(en.toString()));
		}
		return video;
	}

	/**
	 * Build voice message
	 * 
	 * @param data
	 * @return
	 */
	public static ReceiveVoiceMessage buildVoiceMessage(Map<String, String> data) {
		ReceiveVoiceMessage voice = new ReceiveVoiceMessage();
		for (Enum en : ReceiveVoiceMessage.AttributeEnum.values()) {
			voice.setAttribute(en.toString(), data.get(en.toString()));
		}
		return voice;
	}

	/**
	 * Build event message
	 * 
	 * @param data
	 * @return
	 */
	public static ClickEvent buildClickEvent(Map<String, String> data) {
		ClickEvent event = new ClickEvent();
		for (Enum en : ClickEvent.AttributeEnum.values()) {
			event.setAttribute(en.toString(), data.get(en.toString()));
		}
		return event;
	}

	/**
	 * Build text message for response
	 * 
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public static String buildTextMessageForResp(String fromUserName,
			String toUserName, String content) {
		SendTextMessage message = new SendTextMessage();
		message.setAttribute(SendTextMessage.AttributeEnum.FromUserName
				.toString(), fromUserName);
		message.setAttribute(SendTextMessage.AttributeEnum.ToUserName
				.toString(), toUserName);
		message.setAttribute(SendTextMessage.AttributeEnum.CreateTime
				.toString(), System.currentTimeMillis());
		message.setAttribute(SendTextMessage.AttributeEnum.MsgType.toString(),
				MESS_TYPE.TEXT.toString().toLowerCase());
		message.setAttribute(SendTextMessage.AttributeEnum.Content.toString(),
				content);
		return XMLUtils.objectToXml(message);
	}

	/**
	 * Get token for verification
	 * 
	 * @return
	 */
	public static String getVerifyToken() {
		ResourceBundle bundle = ResourceBundle.getBundle("token");
		if (bundle == null)
			return null;
		return bundle.getString("verify_token");
	}

	/**
	 * Get access token
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		ResourceBundle bundle = ResourceBundle.getBundle("token");
		if (bundle == null)
			return null;
		return bundle.getString("access_token");
	}

	public static void main(String[] args) {
		// System.out
		// .println(buildTextMessageForResp("hello", "123213", "213213"));
	}
}
