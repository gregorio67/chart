package dymn.chart.service;

import java.util.Map;

public interface ApiService {
	
	public Map<String, Object> selectClientId(Map<String, Object> param) throws Exception;
	
	public int insertApiUser(Map<String, Object> param) throws Exception;
	
	public int updateApiUser(Map<String, Object> param) throws Exception;

}
