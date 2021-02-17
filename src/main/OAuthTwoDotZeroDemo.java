package main;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.Courses;
import pojo.GetCourses;
import pojo.Mobile;
import pojo.WebAutomation;

public class OAuthTwoDotZeroDemo {
	public static void main(String[] args) throws InterruptedException {

		/*Hit below URL in browser and login with Gmail.
		 * https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php
		 * -----Below are the Params used in above URL------
		 * scope: https://www.googleapis.com/auth/userinfo.email
		 * auth_url: https://accounts.google.com/o/oauth2/v2/auth
		 * client_id: 692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com
		 * response_type: code
		 * redirect_uri: https://rahulshettyacademy.com/getCourse.php
		 * 
		 * After login it will be redirected to a page (blank).
		 * Copy that page URL and paste it in variable "url" mentioned below.
		 * This URL will hold the "code" required to get Access Token.
		 * 
		 */
		String url ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AY0e-g4TTCdWfEhjxUohHId7AdYAS1SUnIFgRvX5zjloE4hEZluGToPviuLXkL_Y1UIpRg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialcode=url.split("code=")[1];
		String code=partialcode.split("&scope")[0];
		
		System.out.println("code = "+code);
		
		String response =given()
		                .urlEncodingEnabled(false)
		                .queryParams("code",code)
		                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		                .queryParams("grant_type", "authorization_code")
		                .queryParams("state", "verifyfjdss")
		                .queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
		                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		                .when().log().all()
		                .post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		
		JsonPath jsonPath = new JsonPath(response);
		String accessToken = jsonPath.getString("access_token");
		System.out.println("token = "+accessToken);
		
		
		//------------------Deserialization using POJO class----------------------//
		GetCourses gc=given()
				 .queryParams("access_token", accessToken).expect().defaultParser(Parser.JSON)
				 .when()
				 .get("https://rahulshettyacademy.com/getCourse.php")
				 .as(GetCourses.class);
		
		System.out.println(gc.getExpertise());
		
		System.out.println(gc.getCourses().toString());
		
		//accessing the webautomation object (List object) using get(index)
		System.out.println(gc.getCourses().getWebAutomation().get(1).getCourseTitle());
		System.out.println(gc.getCourses().getWebAutomation().get(1).getPrice());
		
		List<API> api = gc.getCourses().getApi();
		for(API a: api){
			System.out.println(a.getCourseTitle());
			System.out.println(a.getPrice());
		}
		
		List<Mobile> mobile = gc.getCourses().getMobile();
		for(Mobile m: mobile){
			System.out.println(m.getCourseTitle());
			System.out.println(m.getPrice());
		}
		
		//comparing expected courses with actual courses
		String[] expectedWebCourses = {"Selenium Webdriver Java","Cypress","Protractor"};
		ArrayList<String> actualWebCourses = new ArrayList<String>();
		
		List<WebAutomation> w = gc.getCourses().getWebAutomation();
		
		for(int i=0; i<w.size();i++){
			actualWebCourses.add(w.get(i).getCourseTitle());
		}
	
		List<String> expectedCourseList = Arrays.asList(expectedWebCourses);
		
		Assert.assertTrue(actualWebCourses.equals(expectedCourseList));
	}
}