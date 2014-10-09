package org.wechatapps.pojo.mapper;

import java.util.List;
import java.util.Map;

import org.wechatapps.pojo.db.History;

public interface HistoryMapper {
	void add(History history);

	void delete(int historyId);

	void update(History history);

	List<History> getAllHistories();

	History getHistoryById(int history);

	List<History> getHistoryByKey(String key);

	List<History> getHistoryByUserId(String userId);

	List<History> getNonTimeoutByUserId(Map<Object, Object> params);
}
