package org.wechatapps.service;

import org.wechatapps.pojo.Message;
import org.wechatapps.pojo.event.ClickEvent;
import org.wechatapps.pojo.recieve.*;

/*
 * @Description Wechat Message Proccessor Interface 
 * @author Charles Chen 
 * @date 14-2-19
 * 
 * @version 1.0
 */
public interface WechatProcessor {
	String process(Message message);

	String processText(ReceiveTextMessage text);

	String processClickEvent(ClickEvent event);

	String processVoice(ReceiveVoiceMessage voice);

	String processVideo(ReceiveVideoMessage video);

	String processPic(ReceivePicMessage picture);

	String processLink(ReceiveLinkMessage link);

	String processLocation(ReceiveLocationMessage location);
}
