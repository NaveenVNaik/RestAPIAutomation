package testcases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethods;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath; 

public class LibraryAPITestCases {
	
	@Test(priority = 1, dataProvider = "Books")
	public void addBookToLibrary(String isbn,String aisle){
		RestAssured.baseURI = "http://216.10.245.166";
		
		//adding bookto library
		String rep = given()
			.header("Content-Type", "application/json")
			.body(Payload.addBookJsonPayload(isbn,aisle))
		.when()
			.post("Library/Addbook.php")
		.then() 
			.assertThat().statusCode(200)
			.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawJsonToString(rep);
		
		String id = js.getString("ID");
		System.out.println("ID = "+id);
		
		//delete book from library
		given()
			.header("Content-Type", "application/json")
			.body(Payload.deleteBookJsonPayload(id))
		.when()
			.post("Library/DeleteBook.php")
		.then().log().all()
			.assertThat().statusCode(200)
			.extract().response().toString();
	}
	
	
	@DataProvider(name = "Books")
	public Object[][] provideDataToAddBook(){
		return new Object [][] {{"7538h","495"},{"ssrty","8574"},{"ppoouu","866"}};
	}
}
