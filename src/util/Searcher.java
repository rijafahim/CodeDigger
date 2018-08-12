package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;
import util.URLReader;
import javax.swing.JOptionPane;

/**
 * 	 Implements the required functionality to search for, and scrape Stack Overflow webpages
 *   to retrieve code snippets.
 *   Uses a combination of Goggle Custom Search Engine API and Jsoup to retrieve snippets.
 */
public class Searcher 
{
	static int NUM_URLS = 5; // Defaults for the number of snippets to receive, and how many pages to look at for snippets.
	static int NUM_ANSWERS_PER_URL = 1; 
	static String key = "AIzaSyDBHQvmQAr6aAi1p2sATXx3ih6InOPDMMI"; // API key and custom search engine key defaults.
	static String cx = "006916996761576124389:3wk3li1vgvq";

	public static Vector<String> getThreads(String query) //Given a string query to search for, retrieve NUM_URLS from Stack Overflow
	{ 
			if (query.equals("")) {
				return new Vector<String>();
			}
			query = setTargetLanguage(query);
			
			//Create a string vector holding all of the URLS we will find.
			Vector<String> urls = new Vector<String>();
			
	        //This is the input query to do.
			String qry= query;
			System.out.println("Query: "+ qry);
			
	        //Convert spaces to http-like-spaces (%20).
			qry = qry.replaceAll(" ", "%20");
	        URL url;
	        try {
	        	//The url is structured to do a custom search which only looks at StackOverflow sites.
				url = new URL("https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=" + cx + "&q="+ qry + "&alt=json" + "&num="+NUM_URLS);
				HttpURLConnection conn;
				
				//Connect to the URL and set properties of the connection.
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
		        conn.setRequestProperty("Accept", "application/json");
		        
		        //Get the stream of json data from the search response.
		        BufferedReader br;
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        String output; //For each line in the response,
				while ((output = br.readLine()) != null) 
				{
					if(output.contains("\"link\": \""))	//If we find the format for a link to the search result, get the link's substring and add it to the vector of urls.
					{                
						String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
						urls.addElement(link);       //Will add google search links to vector.
					}
				}				
				conn.disconnect(); //Disconnect for safety.
	        } 
	        catch (ProtocolException e1) //Handle all types of errors possible without crashing.
	        {
	        	e1.printStackTrace();
	        	return new Vector<String>();
	        } 
	        catch (MalformedURLException e1) {
	        	e1.printStackTrace();
	        	return new Vector<String>();
	        } 
	        catch (IOException e) {
	        	JOptionPane.showMessageDialog(null, "Query Failed - Couldn't resolve connection.", "Warning", JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
				return new Vector<String>();
			}
	        return urls;
		}
	
	//Given a vector of StackOverflow forum thread URLs, retrieve the top NUM_ANSWERS_PER_URL answers from each thread (based on upvotes).
	public static Vector<String> getCodeSnippets(Vector<String> urls) 
	{
		Vector<String> code = new Vector<String>();
		for (int i=0; i<urls.size(); i++) 
		{
			// Create a new url and open using jsoup so we can do easy queries on the results (formats code for us nicely at cost of time).
	        URLReader ur = new URLReader();
	        
	        ur.openHtml(urls.elementAt(i));
	        Vector<String> top_n_answers = ur.getTopN(NUM_ANSWERS_PER_URL);
	        if (top_n_answers.size() == 0) 
	        {
	        	System.out.println("ERROR, could not get code from url: " + urls.elementAt(i));
	        } 
	        else 
	        {
	        	for (int j=0; j<top_n_answers.size(); j++) {
	        		code.add(top_n_answers.get(j));
	        	}
	        }
		}
		//System.out.println("Code: "+ code);
		return code;
	}
	
	//Given a vector of StackOverflow forum thread URLs, retrieve the top NUM_ANSWERS_PER_URL answers from each thread (based on upvotes).
	public static String getErrorSnippet(String url) 
	{
		// Create a new url and open using jsoup so we can do easy queries on the results (formats code for us nicely at cost of time).
	    URLReader ur = new URLReader();      
	    ur.openHtml(url);
	    String code = ur.getTopN_Error(NUM_ANSWERS_PER_URL);
	    if (code == "") 
	    {
	        System.out.println("ERROR, could not get code from url: " + url);
	    } 
		return code;
	}
	
	//Given a query to search for, add the text " in java" to the end of the text if it isn't already there. 
	private static String setTargetLanguage(String text) {
		String language = "java";
		if (text.contains(" in ")) {
        	return text;
        }
		return text + " in " + language;
	}
	
}