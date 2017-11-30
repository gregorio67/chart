package dymn.chart.main;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// Metadata JSON Example
//{
//	  "IN": [
//	        {"name":"id", "length":"10"},
//	        {"name":"password", "length":"20"}
//	  ],
//	  
//	  "OUT": {"Result1" : [{"name":"id", "length":"10", "order":"1", "visuable":"Y"},
//	  {"name":"name", "length":"20", "order":"2", "visuable":"Y"},
//	            {"name":"age", "length":"3", "order":"3", "visuable":"Y"},
//	            {"name":"gender", "length":"2", "order":"4", "visuable":"Y"},
//	            {"name":"birthday", "length":"10", "order":"5", "visuable":"Y"}
//	        ],
//	          "Result2" : [
//				{"name":"id", "length":"10", "order":"1", "visuable":"Y"},
//				{"name":"name", "length":"20", "order":"2", "visuable":"Y"},
//				{"name":"age", "length":"3", "order":"3", "visuable":"Y"},
//				{"name":"gender", "length":"2", "order":"4", "visuable":"Y"},
//				{"name":"birthday", "length":"10", "order":"5", "visuable":"Y"}
//	        ]
//	  }
//	}

public class Metadata {

	public static void main(String args[]) throws Exception {
		
		String inJsonData = "{\"IN\": [{\"name\":\"id\", \"length\":\"10\"},{\"name\":\"password\", \"length\":\"20\"}]}";

//		String outJsonData = "{\"OUT\": {\"Result1\" : [{\"name\":\"id\", \"length\":\"10\", \"order\":\"1\", \"visuable\":\"Y\"},{\"name\":\"name\", \"length\":\"20\", \"order\":\"2\", \"visuable\":\"Y\"},{\"name\":\"age\", \"length\":\"3\", \"order\":\"3\", \"visuable\":\"Y\"},{\"name\":\"gender\", \"length\":\"2\", \"order\":\"4\", \"visuable\":\"Y\"},{\"name\":\"birthday\", \"length\":\"10\", \"order\":\"5\", \"visuable\":\"Y\"}],\"Result2\" : [{\"name\":\"id\", \"length\":\"10\", \"order\":\"1\", \"visuable\":\"Y\"},{\"name\":\"name\", \"length\":\"20\", \"order\":\"2\", \"visuable\":\"Y\"},{\"name\":\"age\", \"length\":\"3\", \"order\":\"3\", \"visuable\":\"Y\"},{\"name\":\"gender\", \"length\":\"2\", \"order\":\"4\", \"visuable\":\"Y\"},{\"name\":\"birthday\", \"length\":\"10\", \"order\":\"5\", \"visuable\":\"Y\"}]}}";
		String outJsonData = "{\"OUT\": {\"Result1\" : [{\"name\":\"id\", \"length\":\"10\", \"order\":\"1\", \"visuable\":\"Y\"},{\"name\":\"name\", \"length\":\"20\", \"order\":\"2\", \"visuable\":\"Y\"},{\"name\":\"age\", \"length\":\"3\", \"order\":\"3\", \"visuable\":\"Y\"},{\"name\":\"gender\", \"length\":\"2\", \"order\":\"4\", \"visuable\":\"Y\"},{\"name\":\"birthday\", \"length\":\"10\", \"order\":\"5\", \"visuable\":\"Y\"}]}}";
		Gson gson = new GsonBuilder().create();
		final Type typeOf = new TypeToken<Map<String, List<Map<String,String>>>>(){}.getType();
		 
		Map<String, List<Map<String,String>>> inMap = gson.fromJson(inJsonData, typeOf);
		
		List<Map<String,String>> listOfMaps = inMap.get("IN");
		
		/** Check Input Paramter **/
		for (Map<String, String> map : listOfMaps) {
			Iterator<String> itr = map.keySet().iterator();
			while(itr.hasNext()) {
				String key = itr.next();
				String value = map.get(key);
				System.out.println("KEY :: " + key + "::" + "VALUE ::" + value);
				
			}
		}
		
		/** Check output parameter **/
		System.out.println("------------------------------------------------------");
		final Type typeOf1 = new TypeToken<Map<String, Map<String, List<Map<String,String>>>>>(){}.getType();		
		Map<String, Map<String, List<Map<String,String>>>> outMap = gson.fromJson(outJsonData, typeOf1);
		
		Map<String, List<Map<String,String>>> listOfOutMaps = outMap.get("OUT");
		
		Iterator<String> itr1 = listOfOutMaps.keySet().iterator();
		while(itr1.hasNext()) {
			String key = itr1.next();
			List<Map<String, String>> outList1 = listOfOutMaps.get(key);
			for (Map<String, String> map : outList1) {
				Iterator<String> itr3 = map.keySet().iterator();
				while(itr3.hasNext()) {
					String key1 = itr3.next();
					String value = map.get(key1);
					System.out.println("KEY :: " + key1 + "::" + "VALUE ::" + value);
				}
			}
			System.out.println("------------------------------------------------------");
		}
		
		 
		 
	}
}
