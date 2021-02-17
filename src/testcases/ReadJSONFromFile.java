package testcases;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.testng.annotations.Test;

import files.ReusableMethods;
import io.restassured.RestAssured;

public class ReadJSONFromFile {
	@Test
	public void addPlaceByJSONFile(){
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//POST method to add place
		String addPlaceResp = null;
		try {
			addPlaceResp = given().log().all()
				   .queryParam("key", "qaclick123")
				   .header("Content-Type","application/json")
				   .body(ReusableMethods.readFileAndReturnString("G:\\Interview Preparation\\Rest API Automation\\RestAPIProjects\\FirstProject\\TestData\\AddPlaceAPIPayload.json"))
			.when().log().all()
				   .post("maps/api/place/add/json")
			.then().log().all()
				   .assertThat().statusCode(200) //success
				   //.then().log().all().assertThat().statusCode(209); //failed cz status code is not 209 but is 200
				   .body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)")
				   .extract().response().asString();
			System.out.println("******-----Response from AddPlce API inside addPlaceByJSONFile-----******");
			System.out.println(addPlaceResp);
		} catch (IOException e) {
			System.out.println("Exception whuile reading file.");
			e.printStackTrace();
		}
	}
}
