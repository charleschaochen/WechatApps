package org.wechatapps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wechatapps.pojo.mapper.HistoryMapper;
import org.wechatapps.factory.EventEntity;
import org.wechatapps.factory.ExecutorFactory;
import org.wechatapps.pojo.Message;
import org.wechatapps.pojo.db.History;
import org.wechatapps.pojo.event.ClickEvent;
import org.wechatapps.pojo.recieve.*;
import org.wechatapps.utils.WechatUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @Description Wechat Message Proccessor
 * @author Charles Chen
 * @date 14-2-19
 * @version 1.0
 */
@Service
public class WechatProcessorImpl implements WechatProcessor {
	@Autowired(required = true)
	private HistoryMapper historyMapper;
	@Autowired(required = true)
	private ExecutorFactory executorFactory;

	/**
	 * Process message according to the type
	 * 
	 * @param message
	 * @return
	 */
	public String process(Message message) {
		String type = message.getType();
		if (type == null) {
			return null;
		}
		if (message.getMess() == null) {
			return null;
		}
		String processedResult;
		switch (WechatUtils.MESS_TYPE.valueOf(type.toUpperCase())) {
		case TEXT:
			processedResult = processText((ReceiveTextMessage) message
					.getMess());
			break;
		case IMAGE:
			processedResult = processPic((ReceivePicMessage) message.getMess());
			break;
		case VOICE:
			processedResult = processVoice((ReceiveVoiceMessage) message
					.getMess());
			break;
		case VIDEO:
			processedResult = processVideo((ReceiveVideoMessage) message
					.getMess());
			break;
		case LOCATION:
			processedResult = processLocation((ReceiveLocationMessage) message
					.getMess());
			break;
		case LINK:
			processedResult = processLink((ReceiveLinkMessage) message
					.getMess());
			break;
		case EVENT:
			processedResult = processClickEvent((ClickEvent) message.getMess());
			break;
		default:
			processedResult = null;
			break;
		}
		return processedResult;
	}

	/**
	 * Process text message
	 * 
	 * @param text
	 * @return
	 */
	public String processText(ReceiveTextMessage text) {
		/* Start: Check if the text content is a command */
		EventEntity entity = executorFactory.findByKey(text.getContent());
		// If the message is a event command
		if (entity != null) {
			if (entity.getType() == 1) {
				// If type is 1, execute the event action immediately
				return entity.getExecutor().execute(text);
			}
			if (entity.getType() == 2) {
				// If type is 2, save the event as history
				History history = new History();
				history.setHistoryUserId(text.getFromUserName());
				history.setHistoryEventKey(text.getContent());
				historyMapper.add(history);

				// Respond the description of the event to client
				return entity.getExecutor().desc(text);
			}
		}
		/* End: Check if the text content is a command */

		/*
		 * Start: If text content is not a command, check if current user has a
		 * previous history command
		 */
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("userId", text.getFromUserName());
		params.put("seconds", 300);
		// Get user event history in 300 seconds
		List<History> nonTimeoutHistories = historyMapper
				.getNonTimeoutByUserId(params);
		if (nonTimeoutHistories != null && nonTimeoutHistories.size() > 0) {
			// Get the event entity by event key
			entity = executorFactory.findByKey(nonTimeoutHistories.get(0)
					.getHistoryEventKey());
			if (entity == null)
				return null;
			return entity.getExecutor().execute(text);
		}
		/*
		 * End: If text content is not a command, check if current user has a
		 * previous history command
		 */

		return WechatUtils.buildTextMessageForResp(text.getToUserName(), text
				.getFromUserName(), "请求内容无效");
	}

	/**
	 * Process click event
	 * 
	 * @param event
	 * @return
	 */
	public String processClickEvent(ClickEvent event) {
		EventEntity entity = executorFactory.findByKey(event.getEventKey());
		// If event key is found
		if (entity != null) {
			if (entity.getType() == 1) {
				// If type is 1, execute the event action immediately
				return entity.getExecutor().execute(event);
			}
			if (entity.getType() == 2) {
				// If type is 2, save the event as history
				History history = new History();
				history.setHistoryUserId(event.getFromUserName());
				history.setHistoryEventKey(event.getEventKey());
				historyMapper.add(history);

				// Respond the description of the event to client
				return entity.getExecutor().desc(event);
			}
		}
		return WechatUtils.buildTextMessageForResp(event.getToUserName(), event
				.getFromUserName(), "找不到该指令");
	}

	public String processVoice(ReceiveVoiceMessage voice) {
		return null;
	}

	public String processVideo(ReceiveVideoMessage video) {
		return null;
	}

	public String processPic(ReceivePicMessage picture) {
		return null;
	}

	public String processLink(ReceiveLinkMessage link) {
		return null;
	}

	public String processLocation(ReceiveLocationMessage location) {
		return null;
	}
}
