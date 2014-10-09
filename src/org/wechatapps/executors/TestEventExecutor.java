package org.wechatapps.executors;

import org.wechatapps.annotation.EventComponent;
import org.wechatapps.pojo.BaseMessage;
import org.wechatapps.pojo.recieve.ReceiveTextMessage;
import org.wechatapps.utils.WechatUtils;

/*
 * @Description Test Event Executor
 * @author Charles Chen
 * @date 14-5-2
 * @version 1.0
 */
@EventComponent(key = "��дת��", type = 2)
public class TestEventExecutor implements EventExecutor {
	public String execute(BaseMessage message) {
		if (message == null)
			return null;
		ReceiveTextMessage textMessage;
		try {
			textMessage = (ReceiveTextMessage) message;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
		String content = "ת�������" + textMessage.getContent().toUpperCase();
		return WechatUtils.buildTextMessageForResp(textMessage.getToUserName(),
				textMessage.getFromUserName(), content);
	}

	public String desc(BaseMessage message) {
		String content = "��������Ҫת����Сд��ĸ";
		return WechatUtils.buildTextMessageForResp(message.getToUserName(),
				message.getFromUserName(), content);
	}
}
