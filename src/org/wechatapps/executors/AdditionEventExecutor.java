package org.wechatapps.executors;

import org.wechatapps.annotation.EventComponent;
import org.wechatapps.pojo.BaseMessage;
import org.wechatapps.pojo.recieve.ReceiveTextMessage;
import org.wechatapps.utils.WechatUtils;

/*
 * @Description 
 * @author Charles Chen
 * @date 14-5-3
 * @version 1.0
 */
@EventComponent(key = "ADD", type = 2)
public class AdditionEventExecutor implements EventExecutor {
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
		String[] nums = textMessage.getContent().split(" ");
		if (nums.length != 2) {
			String warning = "��������������";
			return WechatUtils.buildTextMessageForResp(textMessage
					.getToUserName(), textMessage.getFromUserName(), warning);
		}
		try {
			double sum = 0;
			for (String num : nums) {
				sum += Double.parseDouble(num);
			}
			String content = "�ӷ�����ǣ�" + sum;
			return WechatUtils.buildTextMessageForResp(textMessage
					.getToUserName(), textMessage.getFromUserName(), content);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			String warning = "����ļ�������Ϊ����";
			return WechatUtils.buildTextMessageForResp(textMessage
					.getToUserName(), textMessage.getFromUserName(), warning);
		}
	}

	public String desc(BaseMessage message) {
		String content = "�����������������Կո�ָ�";
		return WechatUtils.buildTextMessageForResp(message.getToUserName(),
				message.getFromUserName(), content);
	}
}
