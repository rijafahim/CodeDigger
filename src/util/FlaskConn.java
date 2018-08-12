package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL; 
import org.json.JSONObject;

public class FlaskConn 
{
	String url = "http://127.0.0.1:5000/getdata/";	   
	public String QueryModel(String query) throws Exception 
	{ 
		url+=query;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		//     //Read JSON response and print
		//     JSONObject myResponse = new JSONObject(response.toString());
		//     System.out.println("result after Reading JSON Response");
		//     System.out.println("Name: "+myResponse.getString("sentence"));
		//return myResponse.getString("sentence");
		return response.toString();
   }
}