package util;

import java.util.Vector;

public class ErrorLookup 
{
	public String errorName;
	public String Suggestion;
	
	public ErrorLookup(String errorName, String suggestion)
	{
		this.errorName = errorName;
		this.Suggestion = suggestion;
	}
	public String getErrorName() {
		return errorName;
	}
	public String getErrorSuggestion(){
		return Suggestion;
	}
}
