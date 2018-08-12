package ui;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

/**
 * A syntax highlighting code editor for JavaFX created by wrapping a
 * CodeMirror code editor in a WebView.
 *
 * See http://codemirror.net for more information on using the codemirror editor.
 */
	public class CodeEditor extends StackPane 
	{
		final WebView webview = new WebView(); // a Webview used to encapsulate  the CodeMirror JavaScript     
		private String editingCode; // A snapshor of the code to be edited for easy initialization and reversion of editable code
		public static String code1;
		public static String code2;
		public static String code3;
		public static String code4;
		public static String code5;
		private final String editingTemplate =
				"<!doctype html>" +
				"<html>" +
				"<head>" +
						"  <link rel=\"stylesheet\" href=\"http://codemirror.net/lib/codemirror.css\">" + 
					    "  <script src=\"http://codemirror.net/lib/codemirror.js\"></script>" + 
					    "  <script src=\"http://codemirror.net/mode/clike/clike.js\"></script>" + 
				"</head>" +
				"<body>" +
						"<form><textarea id=\"code\" name=\"code\">\n" +
						"${code}" +
						"</textarea></form>" +
						"<script>" +
						"  var editor = CodeMirror.fromTextArea(document.getElementById(\"code\"), {" +
						"    lineNumbers: true," +
						"    matchBrackets: true," +
						"    mode: \"text/x-java\"" +
						"  });" +
						"</script>" +
				"</body>" +
				"</html>";
		private String errorTemplate = "";

		private String applyEditingTemplate() { // applies the editing template to the editing code to create the html+javascript source for a code editor. 
			return editingTemplate.replace("${code}", editingCode);
		}

		/** sets the current code in the editor and creates an editing snapshot of the code which can be reverted to. */
		public void setCode(String newCode) {
			this.editingCode = newCode;
			webview.getEngine().loadContent(applyEditingTemplate());
		}


		/**
		 * Create a new code editor.
		 * @param editingCode the initial code to be edited in the code editor.
		 */
		CodeEditor(String editingCode, int option) 
		{
			if (option ==1 )
			{
				this.editingCode = editingCode;
				webview.setPrefSize(650, 325);
				webview.setMinSize(650, 325);
				webview.getEngine().loadContent(applyEditingTemplate());
				this.getChildren().add(webview);
			}
			else if(option ==2)
			{
				errorTemplate = editingCode;
				System.out.println(errorTemplate + " lalallalalalala");
				webview.setPrefSize(650, 325);
				webview.setMinSize(650, 325);
				webview.getEngine().loadContent(errorTemplate);
				this.getChildren().add(webview);
			}
		}		
}
