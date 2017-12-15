package dymn.chart.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dymn.utils.CommonDao;

@Service("studentService")
public class StudenetServiceImpl implements StudentService {

	@Resource(name="commonDao")
	private CommonDao commonDao;

	public List<Map<String, Object>> selectStudents(Map<String, Object> param) throws Exception {
		
		List<Map<String, Object>> resultList = commonDao.selectList("students.selStudents", param);
		return resultList;
	}
}
