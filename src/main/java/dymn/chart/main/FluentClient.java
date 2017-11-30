package dymn.chart.main;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class FluentClient {

	public static void main(String args[]) throws Exception {
		Request.Post("https://openapi.kyobo.co.kr:1443")
				.connectTimeout(1000)
				.socketTimeout(1000)
				.addHeader("X-Custom-header", "stuff")
				.bodyString("", ContentType.APPLICATION_JSON)
				.execute().returnContent().asString();
		
	}
}
