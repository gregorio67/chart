package dymn.chart.service;

import java.util.List;
import java.util.Map;

public interface StudentService {

	public List<Map<String, Object>> selectStudents(Map<String, Object> param) throws Exception;
}
