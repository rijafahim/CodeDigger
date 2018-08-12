package util;

import java.io.IOException;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *   This class uses JSoup to perform JQuery-like statements on StackOverflow forum pages to retrieve
 *   top code snippets from the StackOverflow page.
 */
public class URLReader 
{		
		private static String address; // URL to read HTML from.
		public void openHtml(String address) //this function sets the currently opened address to a new URL. Input: String address - New URL to read HTML from

		{
			URLReader.address = address;
		}
		
		/*
		 * 	 Performs JQuery statements on the currently opened URL address to retrieve the top n code snippets
		 *   from the Stack Overflow forum page. 
		 *   Input: int n - number of code snippets to retrieve from the current webpage.
		 *   Returns: Vector<String> - vector of top n code snippets extracted from the Stack Overflow forum page.
		 */
		public Vector<String> getTopN(int n) 
		{
			Vector<String> top_n_snippets = new Vector<String>(); //makes a vector of top code snippets
			Document doc;
			String code = "", author;
			try 
			{
				doc = Jsoup.connect(address).get(); //Jsoup connects to the provided URL, returns a document
				if (doc.equals(null)) //If Jsoup doesnt return anything, return nothing
				{ 
					return new Vector<String>();
				}

				Elements posts = doc.select("div.answer"); //select where the answers are
				if (!(posts.size() == 0)) 
				{ 
					int counter = 0; // While there are more answers and we haven't retrieved n answers yet.
					while(posts.size() > 0 && counter < n) 
					{
						code = "";
						Element e = posts.first();
						posts.remove(0);
						// Get <pre> tag sections. Code in Stack Overflow posts are surrounded in
						//   <pre><code>  {code here}  </code></pre>
						Elements pres = e.getElementsByTag("pre");
						String code_seg = "";
						
						// Retrieve the text in between the two <code> tags, i.e. the code snippet.
						for (int i=0; i<pres.size(); i++) 
						{
							String elem = pres.get(i).toString();
							if (elem.indexOf("</code>") - elem.indexOf("<code>") - 6 < 0) continue;
							code_seg = elem.substring(elem.indexOf("<code>")+6,elem.indexOf("</code>"));
							if (code_seg.equals("")) continue;
							code_seg = formatResponse(code_seg);
							code += code_seg + "\n";
						}
						
						// Retreive the author of the answer.
						Element user = e.getElementsByClass("user-details").last();
						String text = user.childNode(1).toString();
						text = text.substring(text.indexOf('>')+1);
						text = text.substring(0,text.indexOf('<'));
						author = text;
						
						// Trim whitespace.
						code = code.trim();
						if (code.equals("")) continue;
						
						// Add a reference line to give credit to the original author.
						top_n_snippets.add("// snippet from " + address + " by " + author + "\n" + code + "\n");
						counter++;
					}
				}
			} 
			catch (IOException e) //Incase an Exception occurs
			{
				code = ""; //Initialise the code to null
				author = ""; //Initialise the author to null
				System.out.println("Could not connect to url using jsoup");
			}	
			return top_n_snippets;
		}
		
		
		/*
		 * 	 Performs JQuery statements on the currently opened URL address to retrieve the top n code snippets
		 *   from the Stack Overflow forum page. 
		 *   Input: int n - number of code snippets to retrieve from the current webpage.
		 *   Returns: Vector<String> - vector of top n code snippets extracted from the Stack Overflow forum page.
		 */
		public String getTopN_Error(int n) 
		{
			String top_n_snippets = ""; //makes a vector of top code snippets
			Document doc;
			String code = "", author;
			try 
			{
				doc = Jsoup.connect(address).get(); //Jsoup connects to the provided URL, returns a document
				if (doc.equals(null)) //If Jsoup doesnt return anything, return nothing
				{ 
					return "";
				}
				//String all = doc.html();
				//System.out.println(all);
				
				Elements posts = doc.select("div.answer"); //select where the answers are
				//System.out.println(posts.toString());
				if (!(posts.size() == 0)) 
				{ 
					int counter = 0; // While there are more answers and we haven't retrieved n answers yet.
					while(posts.size() > 0 && counter < n) 
					{
						code = "";
						Element e = posts.first();
						posts.remove(0);
						// Get <pre> tag sections. 	Complete Answeres  in Stack Overflow posts are surrounded in
						//   <pre><code>  {code here}  </code></pre>
						//Elements pres = e.getElementsByTag("pre");
						String code_seg = "";
						
						// Retrieve the text in between the two <code> tags, i.e. the code snippet.
						//for (int i=0; i<pres.size(); i++) 
						//{
							String elem = e.toString();
							code_seg = elem;
							if (code_seg.equals("")) continue;
							code_seg = formatResponse(code_seg);
							code += code_seg + "\n";
						//}
						
						// Retreive the author of the answer.
						Element user = e.getElementsByClass("user-details").last();
						String text = user.childNode(1).toString();
						text = text.substring(text.indexOf('>')+1);
						text = text.substring(0,text.indexOf('<'));
						author = text;
						
						// Trim whitespace.
						code = code.trim();
						if (code.equals("")) continue;
						String cssDeclarations = 
									"<!doctype html>" +
											"<html>" +
											"<head>" +
												"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>\n" + 
												 "<script src=\"https://cdn.sstatic.net/Js/stub.en.js?v=0cdf8dcc3f83\"></script>\n" + 
												 "\n" + 
												 "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.sstatic.net/Shared/stacks.css?v=669f3170a0d5\">\n" + 
												 "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.sstatic.net/Sites/stackoverflow/primary.css?v=cfd8a1566f80\">\n" + 
												 "" + 
											"</head>" + 
											"<body>";
											
						code = cssDeclarations + code;
						code = code + "</body>" + "</html>";
						// Add a reference line to give credit to the original author.
						//System.out.println("Snippet: " + "\n" + code + "\n");
						//top_n_snippets.add("// snippet from " + address + " by " + author + "\n" + code + "\n");
						counter++;
					}
				}
			} 
			catch (IOException e) //Incase an Exception occurs
			{
				code = ""; //Initialise the code to null
				author = ""; //Initialise the author to null
				System.out.println("Could not connect to url using jsoup");
			}	
			return code;
		}
		
		/*
		 * Function formatResponse
		 *   Given a Stack Overflow response post, replace all XML escape character codes with the
		 *   characters they represent.
		 *   
		 *   Input: String post - Stack Overflow answer, or block of text with XML escape character codes.
		 *   Returns: String - formatted post with XML escape character codes removed.
		 */
		private static String formatResponse(String post) {
			//Fix xml reserved escape chars:
			post = post.replaceAll("&;quot;", "\"");
			post = post.replaceAll("&quot;", "\"");
			post = post.replaceAll("&quot", "\"");
			post = post.replaceAll("&;apos;", "'");
			post = post.replaceAll("&apos;", "'");
			post = post.replaceAll("&apos", "'");
			post = post.replaceAll("&;lt;","<");
			post = post.replaceAll("&lt;","<");
			post = post.replaceAll("&lt", "<");
			post = post.replaceAll("&;gt;",">");
			post = post.replaceAll("&gt;", ">");
			post = post.replaceAll("&gt", ">");
			post = post.replaceAll("&;amp;", "&");
			post = post.replaceAll("&amp;", "&");
			post = post.replaceAll("&amp", "&");
			return post;
		}
		
	

}