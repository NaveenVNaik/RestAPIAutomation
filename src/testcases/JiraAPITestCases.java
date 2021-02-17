package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

public class JiraAPITestCases {
	private SessionFilter session=new SessionFilter();
	String issueIdOrKey = "10001";//LAN-2
	String comment = "Adding comment 89077 for verification";
	String expectedComment;
	int commentId;
	
	@Test(priority =1)
	public void loginToJira(){
		RestAssured.baseURI = "http://localhost:8080";
		
		given() 
			.header("Content-Type","application/json")
			.body("{ \"username\": \"naveennaikhnr\", \"password\": \"Chandani@1\" }")
			.filter(session)
		.when()
			.post("rest/auth/1/session")
		.then().log().all()
			.assertThat().statusCode(200);
	}
	
	@Test(priority =2)
	public void createDefect(){
		RestAssured.baseURI = "http://localhost:8080";
		
		String res = given()
			.header("Content-Type","application/json")
			.body("{\r\n" + 
					"    \"fields\": {\r\n" + 
					"       \"project\":\r\n" + 
					"       {\r\n" + 
					"          \"key\": \"LAN\"\r\n" + 
					"       },\r\n" + 
					"       \"summary\": \"second  defect using eclipse.\",\r\n" + 
					"       \"description\": \"Eclipse - Creating of an issue using the REST API\",\r\n" + 
					"       \"issuetype\": {\r\n" + 
					"          \"name\": \"Bug\"\r\n" + 
					"       }\r\n" + 
					"   }\r\n" + 
					"}")
			.filter(session)
		.when()
			.post("rest/api/2/issue")
		.then().log().all()
			.assertThat().statusCode(201)
			.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawJsonToString(res);
		issueIdOrKey = js.getString("id");
	}
	
	@Test(priority = 3)
	public void addcomment(){
		RestAssured.baseURI = "http://localhost:8080";
		
		String res = given()
			.header("Content-Type","application/json")
			.pathParam("IdOrKey", issueIdOrKey)
			.body("{\r\n" + 
					"    \"body\": \""+comment+"\"\r\n" + 
					"}")
			.filter(session)
		.when()
			.post("rest/api/2/issue/{IdOrKey}/comment")
		.then().log().all()
			.assertThat().statusCode(201)
			.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawJsonToString(res);
		commentId = js.getInt("id");
		expectedComment = js.getString("body");
		System.out.println("***********************************************************************");
		System.out.println("coomentID = "+commentId);
		System.out.println("expectedComment = "+expectedComment);
		System.out.println("***********************************************************************");
	}
	
	@Test(priority = 4)
	public void addAttachment(){
		RestAssured.baseURI = "http://localhost:8080";
		
		given().log().all()
			.header("X-Atlassian-Token","no-check")
			.header("Content-Type","multipart/form-data")
			.pathParam("issueIdOrKey", issueIdOrKey)
			.filter(session)
			.multiPart(new File("G:\\Interview Preparation\\Rest API Automation\\RestAPIProjects\\FirstProject\\TestData\\testAttachment.txt"))
		.when().log().all()
			.post("/rest/api/2/issue/{issueIdOrKey}/attachments")
		.then().log().all()
			.assertThat().statusCode(200)
			.extract().response().asString();
	}
	
	@Test(priority = 5)
	public void getIssueDetails(){
		RestAssured.baseURI = "http://localhost:8080";
		String actualComment = null;
		
		String issueDetailResp = given()
			.pathParam("key",issueIdOrKey)
			.queryParam("fields", "comment")
			.filter(session)
		.when().log().all()
			.get("rest/api/2/issue/{key}")
		.then().log().all()
			.assertThat().statusCode(200)
			.extract().response().asString();
		
		System.out.println("Starts comment verification");
		JsonPath js = ReusableMethods.rawJsonToString(issueDetailResp);
		int noOfComments = js.getInt("fields.comment.comments.size()");
		System.out.println("noOfComments = "+noOfComments);
		
		for(int i=0;i<noOfComments;i++){
			if(js.getInt("fields.comment.comments["+i+"].id") == commentId){
				actualComment = js.getString("fields.comment.comments["+i+"].body");
				break;
			}
		}
		System.out.println("***********************************************************************");
		System.out.println("actualComment = "+actualComment);
		System.out.println("expectedComment = "+expectedComment);
		System.out.println("commentId = "+commentId);
		Assert.assertEquals(actualComment, expectedComment);
		System.out.println("***********************************************************************");
		
	}
}





