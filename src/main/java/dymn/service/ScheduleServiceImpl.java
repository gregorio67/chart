package sehati.inf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sehati.framework.web.dao.CommonDao;
import sehati.inf.service.ScheduleService;
import sehati.util.spring.PropertiesUtil;


@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/**
	 * Clear authcode if auth time is less than expired time
	 */
	public void deleteAuthCode() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("api.expired time :: {}", PropertiesUtil.getLong("api.exprired.mills"));
		}
		Map<String, Object> param = new HashMap<String, Object>();
		Long expiredTime = System.currentTimeMillis() - PropertiesUtil.getLong("api.exprired.mills");
		param.put("authTime", expiredTime);
		List<String> userList = commonDao.selectList("apiUser.selElapsedUser", expiredTime);
		LOGGER.info("Expired User at {} :: {}", System.currentTimeMillis(), userList.toArray());
		for (String clientId : userList) {
			Map<String, Object> updParam = new HashMap<String, Object>();
			updParam.put("clientId", clientId);
			updParam.put("authCode", "");
			updParam.put("authTime", 0);
			commonDao.update("apiUser.updApiUser", updParam);
		}
	}
}
