package main;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;

import javax.annotation.meta.When;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;


public class SpecBuildersAddPlaceAPI {

	public static void main(String[] args) {
		Location loc = new Location();
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		
		ArrayList<String> type = new ArrayList<String>();
		type.add("shoe test");
		type.add("shop");
		
		AddPlace addPlace = new AddPlace();
		addPlace.setLocation(loc);
		addPlace.setAccuracy(50);
		addPlace.setName("Frontline house");
		addPlace.setPhone_number("(+91) 983 893 3937");
		addPlace.setAddress("29, side layout, cohen 09");
		addPlace.setTypes(type);
		addPlace.setWebsite("http://google.com");
		addPlace.setLanguage("French-IN");
		
		
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Content-Type","application/json")
				.addQueryParam("key", "qaclick123").build();
		RequestSpecification req = given()
				.spec(reqSpec)
				.body(addPlace);
		
		ResponseSpecification respSpec = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();

		Response resp = req.when()
				.post("/maps/api/place/add/json")
				.then().spec(respSpec)
				.body("scope",equalTo("APP"))
				.extract().response();
		
		String respString = resp.asString();
		
		System.out.println("****************Response from ADD place API******************");
		System.out.println(respString);
					

	}

}
