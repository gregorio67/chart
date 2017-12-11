package dymn.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T> String object2Json(T clazz) throws Exception {
		String jsonData = objectMapper.writeValueAsString(clazz);
		return jsonData;
	}
	
	public static Map<String, Object> json2Map(String data) throws Exception {
		Map<String, Object> jsonData = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
		return jsonData;
	}
	
	public static <T> T json2Vo(String data, Class<T> clazz) throws Exception {
		byte[] mapData = data.getBytes();
		T returnVo = objectMapper.readValue(mapData, clazz);
		return returnVo;
	}
	
	public static <T> List<T> json2ListVo(String data, final Class<T> clazz) throws Exception {
		byte[] mapData = data.getBytes();
		@SuppressWarnings("deprecation")
		JavaType valueType = objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, null, clazz);
		return objectMapper.readValue(mapData, valueType);
	}
}
