package main;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class MockJsonParser {

	public static void main(String[] args) {
		JsonPath js = new JsonPath(Payload.getMockJson());
		
		System.out.println("JSON string is as below");
		System.out.println(js.get().toString());
		
		//1. Print No of courses returned by API
		int noOfCourse = js.getInt("courses.size()");
		System.out.println("1. noOfCourse = "+noOfCourse);
		System.out.println();
		
		//2.Print Purchase Amount
		System.out.println("2. purchaseAmount = "+js.getInt("dashboard.purchaseAmount"));
		System.out.println();

		//3. Print Title of the first course
		System.out.println("3. Title of the first course = "+js.getString("courses[0].title"));
		System.out.println();

		//4. Print All course titles and their respective Prices
		System.out.println("4. All course titles and their respective Prices");
		for(int i=0; i < noOfCourse;i++){
			System.out.print("Title = "+js.getString("courses["+i+"].title"));
			System.out.println("  Price = "+js.getInt("courses["+i+"].price"));
		}
		System.out.println();
		
		//5. Print no of copies sold by RPA Course
		
		for(int i=0; i < noOfCourse;i++){
			if(js.getString("courses["+i+"].title").equalsIgnoreCase("RPA")){
				System.out.print("no of copies sold by RPA Course = "+js.getInt("courses["+i+"].copies"));
				break;
			}
		}
		System.out.println();

		//6. Verify if Sum of all Course prices matches with Purchase Amount
		// this implementation is done using Testng as well. Refer class 
		float sumOfAllCourse = 0;
		for(int i=0; i < noOfCourse;i++){
			sumOfAllCourse += (js.getInt("courses["+i+"].copies") * js.getFloat("courses["+i+"].price"));
		}
		System.out.println("Sum of all Course prices = "+sumOfAllCourse);
		
		if(sumOfAllCourse == js.getFloat("dashboard.purchaseAmount")){
			System.out.println("Sum of all Course prices matches with Purchase Amount");
		}else{
			System.out.println("Sum of all Course prices doesn't matches with Purchase Amount");
		}
	}

}
