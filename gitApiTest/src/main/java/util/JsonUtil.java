package util;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import static com.jayway.restassured.RestAssured.given;

public class JsonUtil {


	public static String jsonPostWithoutAuth(String apiurl, String apiBody ){

		RequestSpecBuilder builder = new RequestSpecBuilder();

		builder.setBody(apiBody);

		builder.setContentType("application/json; charset=UTF-8");

		RequestSpecification requestSpec = builder.build();

		Response response = given().spec(requestSpec).post(apiurl);

		String res = response.body().asString();
		return res;
	}

	public static String jsonGet(String url)
	{
		Response response=given().get(url);
		String res = response.body().asString();
		return res;
	}

	public static String jsonDelete(String url)
	{
		int response=given().delete(url).getStatusCode();
		return Integer.toString(response);
	}

	public static String jsonDeleteResponse(String url)
	{
		Response resp= given().delete(url);
		String response=resp.body().asString();
		return response;
	}

}
