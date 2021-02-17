package testcases;

import org.testng.annotations.Test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class MockJsonTestCases {
	@Test
	public void comparePriceAmt(){
		JsonPath js = new JsonPath(Payload.getMockJson());
		int noOfCourse = js.getInt("courses.size()");
		
		float sumOfAllCourse = 0;
		for(int i=0; i < noOfCourse;i++){
			sumOfAllCourse += (js.getInt("courses["+i+"].copies") * js.getFloat("courses["+i+"].price"));
		}
		System.out.println("Sum of all Course prices = "+sumOfAllCourse);
		
		if(sumOfAllCourse == js.getFloat("dashboard.purchaseAmount")){
			System.out.println("Sum of all Course prices matches with Purchase Amount");
		}else{
			
		}
	}
		
}
