package util;
import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JDKClassReader 
{
	public static List<String> allClasses = new ArrayList<String>();
	public static List<String> getAllClasses() throws IOException {	
		FileReader inputStream = new FileReader("/Users/daanyalmalik/Desktop/CodeDigger 2/src/util/data/classes.txt");
		BufferedReader in = new BufferedReader(inputStream);
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			allClasses.add(inputLine.replace("\n", ""));
		}
		in.close();
		return allClasses;
	}
}
