/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author daany
 */
public class FYP extends Application {
	private static Stage main_stage;

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		Image image = new Image("file:C:\\Users\\daany\\eclipse-workspace\\TestAnotherone\\icons\\error.png");
		stage.getIcons().add(image);
		Platform.setImplicitExit(false);
		main_stage = stage;
		stage.setTitle("Error Lookup");
		stage.setOnCloseRequest(e -> {
			main_stage.hide();

		});

		stage.show();

	}

	public static void showStage() {
		System.out.println("Showing stage...");
		main_stage.show();

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
