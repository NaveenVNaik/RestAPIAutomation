package main;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;

import io.restassured.RestAssured;
import pojo.AddPlace;
import pojo.Location;


public class AddPlaceAPISerialization {

	public static void main(String[] args) {
		Location loc = new Location();
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		
		ArrayList<String> type = new ArrayList<String>();
		type.add("shoe test");
		type.add("shop");
		
		/*OR
		ArrayList<String> type = new ArrayList<String>(){
				{
					add("shoe park");
					add("shop");
				}
		};
		*/
		AddPlace addPlace = new AddPlace();
		addPlace.setLocation(loc);
		addPlace.setAccuracy(50);
		addPlace.setName("Frontline house");
		addPlace.setPhone_number("(+91) 983 893 3937");
		addPlace.setAddress("29, side layout, cohen 09");
		addPlace.setTypes(type);
		/*OR
		addPlace.setTypes(Arrays.asList("shoe park","shop"));
		*/
		addPlace.setWebsite("http://google.com");
		addPlace.setLanguage("French-IN");
		
		
		
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String resp = given().log().all()
				.header("Content-Type","application/json")
				.queryParam("key", "qaclick123")
				.body(addPlace)
		.when().log().all()
				.post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200)
				.header("Server", "Apache/2.4.18 (Ubuntu)")
				.body("scope",equalTo("APP"))
				.extract().response().asString();
		
		System.out.println("****************Response from ADD place API******************");
		System.out.println(resp);
					

	}

}
