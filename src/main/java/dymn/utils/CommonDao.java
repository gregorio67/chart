package dymn.utils;

import org.mybatis.spring.SqlSessionTemplate;

import com.sun.istack.internal.NotNull;

public class CommonDao {

	@NotNull
	private SqlSessionTemplate sqlSession;
	
	public <T> int insert(String sqlId, T params) throws Exception {
		return sqlSession.insert(sqlId, params);
	}
	
	public <T> int update(String sqlId, T params) throws Exception {
		return sqlSession.update(sqlId, params);
	}

	public <T> int delete(String sqlId, T params) throws Exception {
		return sqlSession.delete(sqlId, params);
	}

	public <T, V> T select(String sqlId, V param) throws Exception {
		return sqlSession.selectOne(sqlId, param);
	}

	@SuppressWarnings("unchecked")
	public <T, V> T selectList(String sqlId, V param) throws Exception {
		return (T) sqlSession.selectList(sqlId, param);
	}

	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	
}
