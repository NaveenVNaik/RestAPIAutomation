package main;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
import files.ReusableMethods;

public class Basics {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String placeID = null;
		
		//POST method to add place
		String addPlaceResp = given().log().all()
			   .queryParam("key", "qaclick123")
			   .header("Content-Type","application/json")
			   .body(Payload.addplaceJsonPayload())
		.when().log().all()
			   .post("maps/api/place/add/json")
		.then().log().all()
			   .assertThat().statusCode(200) //success
			   //.then().log().all().assertThat().statusCode(209); //failed cz status code is not 209 but is 200
			   .body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)")
			   .extract().response().asString();
		
		System.out.println("******-----Response from AddPlce API-----******");
		System.out.println(addPlaceResp);
		
		JsonPath js= new JsonPath(addPlaceResp);
		placeID = js.getString("place_id");
		
		System.out.println("place_id = "+placeID);
		
		
		//PUT method to update the API
		
		String newAddress = "Summer walk, USA";
		System.out.println();
		System.out.println("------------Update API Starts here---------");
		
		given()//.log().all()
			.queryParam("key", "qaclick123")
			.header("Content-Type", "application/json")
			.body(Payload.updatePlaceJsonPayload(placeID,newAddress))
		.when()//.log().all()
			.put("maps/api/place/update/json")
		.then().log().all()
			.assertThat().statusCode(200)
			.body("msg", equalTo("Address successfully updated"));
			
		
		//GET method to retrieve info from API
		System.out.println();
		System.out.println("-------------GET API starts here----------");
		String getAPIRes = given()//.log().all()
		 	.queryParam("key", "qaclick123")
		 	.queryParam("place_id", placeID)
		 .when().log().all()
		 	.get("maps/api/place/get/json")
		 .then()//.log().all()
		 	.assertThat().statusCode(200)
		 	.extract().response().asString();
		System.out.println("-----------GET API Response----------");
		System.out.println(getAPIRes);
		
		JsonPath js1 = ReusableMethods.rawJsonToString(getAPIRes);
		String actualAddress  = js1.getString("address");
		System.out.println(actualAddress);
		
		Assert.assertEquals(actualAddress, newAddress);
	}

}
