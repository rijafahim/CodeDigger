package ui;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

public class CodeEditorExample extends Application 
{
	private static Stage main_stage;
	 static VBox layout;
	public static String editingCode = "import javafx.application.Application;\n" + 
		    "import javafx.scene.Scene;\n" + 
		    "import javafx.scene.web.WebView;\n" + 
		    "import javafx.stage.Stage;\n" + 
		    "\n" + 
		    "/** Sample code editing application wrapping an editor in a WebView. */\n" + 
		    "public class CodeEditorExample extends Application {\n" + 
		    "  public static void main(String[] args) { launch(args); }\n" + 
		    "  @Override public void start(Stage stage) throws Exception {\n" + 
		    "    WebView webView = new WebView();\n" + 
		    "    webView.getEngine().load(\"http://codemirror.net/mode/groovy/index.html\");\n" + 
		    "    final Scene scene = new Scene(webView);\n" + 
		    "    webView.prefWidthProperty().bind(scene.widthProperty());\n" + 
		    "    webView.prefHeightProperty().bind(scene.heightProperty());\n" + 
		    "    stage.setScene(scene);\n" + 
		    "    stage.show();\n" + 
		    "  }\n" + 
		    "}"; 
			

	public static void main(String[] args) 
	{ 
		launch(args); 
	}
	@SuppressWarnings("deprecation")
	@Override public void start(Stage stage) throws Exception 
	{
	 
		Label title = new Label("Editing: CodeEditor.java");
		title.setStyle("-fx-font-size: 20;");
		final Label labeledCode = new Label(editingCode);
		final CodeEditor editor = new CodeEditor(editingCode,1);
    
		// layout the scene.
		layout = VBoxBuilder.create().spacing(10).children(
				title,
				editor
				).build();
		layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");

		
		// display the scene.
		final Scene scene = new Scene(layout);
		stage.setScene(scene);
		Platform.setImplicitExit(false);
		
		main_stage = stage;
		stage.setTitle("Code Digger");
		stage.setOnCloseRequest(e -> {
			main_stage.hide();

		});
		stage.show();
	}
	public static void showStage(String code1) 
	{
		CodeEditor editor = new CodeEditor(code1,1);
		layout.getChildren().set(1, editor);
		System.out.println("Showing stage...");
		main_stage.show();

	}
	

}
