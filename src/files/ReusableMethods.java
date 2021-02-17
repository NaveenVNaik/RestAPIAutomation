package files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
	public static JsonPath rawJsonToString(String getAPIRes){
		return new JsonPath(getAPIRes);
	}
	
	public static String readFileAndReturnString(String filePath) throws IOException{
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}
}
