package ui;
import java.util.Observable;

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

public class ErrorView extends Application 
{
	private static Stage main_stage;
	public static String editingCode;
	 static VBox layout;
	public static void main(String[] args) 
	{ 
		launch(args); 
	}
	@Override public void start(Stage stage) throws Exception 
	{
		Label title = new Label("Editing: CodeEditor.java");
		title.setStyle("-fx-font-size: 20;");
		final Label labeledCode = new Label(editingCode);
		final CodeEditor editor = new CodeEditor(editingCode,2);
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
	public static void showStage(String codeSnippets) 
	{
		CodeEditor editor = new CodeEditor(codeSnippets,2);
		layout.getChildren().set(1, editor);
		System.out.println("Showing stage...");
		main_stage.show();
	}

}
