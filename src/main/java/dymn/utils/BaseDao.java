package sehati.util.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.InitializingBean;

public class BaseDao extends SqlSessionDaoSupport implements InitializingBean {
	

	public <T> int insert(String sqlId, T params) throws Exception {
		return getSqlSession().insert(sqlId, params);
	}
	
	public <T> int update(String sqlId, T params) throws Exception {
		return getSqlSession().update(sqlId, params);
	}

	public <T> int delete(String sqlId, T params) throws Exception {
		return getSqlSession().delete(sqlId, params);
	}

	public <T, V> T select(String sqlId, V param) throws Exception {
		return getSqlSession().selectOne(sqlId, param);
	}

	@SuppressWarnings("unchecked")
	public <T, V> T selectList(String sqlId, V param) throws Exception {
		return (T) getSqlSession().selectList(sqlId, param);
	}
}
