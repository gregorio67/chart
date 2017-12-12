package dymn.chart.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dymn.utils.CommonDao;

@Service("apiService")
public class ApiServiceImpl implements ApiService {

	@Resource(name="commonDao")
	private CommonDao commonDao;
	
	public Map<String, Object> selectClientId(Map<String, Object> param) throws Exception {
		return commonDao.select("apiUser.selApiUser", param);
	}

	public int insertApiUser(Map<String, Object> param) throws Exception {
		return commonDao.insert("apiUser.insApiUser", param);
	}

	public int updateApiUser(Map<String, Object> param) throws Exception {
		return commonDao.update("apiUser.updApiUser", param);
	}


}
