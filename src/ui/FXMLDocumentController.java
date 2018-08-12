/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.util.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.Searcher;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class FXMLDocumentController implements Initializable 
{

	@FXML private TextField addressbar;
	@FXML private TextField ex_list;
	@FXML private ListView<String> excpListView = new ListView<String>();
	ObservableList<String> items = FXCollections.observableArrayList();
	@FXML private Button prev_btn = new Button();
	@FXML private Button exit_button1 = new Button();
	@FXML private AnchorPane ac_pain;
	@FXML private static Button button = new Button();
	@FXML private Label label;
	@FXML private VBox vbox;

	private WebView browser = new WebView();
	private Searcher search = new Searcher();
	private WebEngine webEngine = browser.getEngine();
	public ArrayList<String> stringList = new ArrayList<String>();
	private Vector<String> threads;
	private int indx = 0;


	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		threads = search.getThreads("string not found");
		webEngine.load(threads.get(indx));
		addressbar.setText(threads.get(indx));
		addressbar.setEditable(false);
		ex_list.setText("List of Exceptions");
		ex_list.setEditable(false);
		vbox.getChildren().addAll(browser);// ,button,prev_btn, exit_button1);

		try {
			items = readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		excpListView.setItems(items);

		excpListView.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) {
				String selectedString = excpListView.getSelectionModel().getSelectedItem();
				selectedString += " in java";
				System.out.println(selectedString);
				indx = 0;
				threads = search.getThreads(selectedString);
				webEngine.load(threads.get(indx));
			}
		});

		updateExcepList();
	}

	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException {

		int n = threads.size();
		indx += 1;
		if (indx >= n) {
			indx = 0;
		}
		webEngine.load(threads.get(indx));
		addressbar.setText(threads.get(indx));

	}

	@FXML
	void prevSnippet(ActionEvent event) {
		int n = threads.size();
		indx -= 1;
		if (indx < 0) {
			indx = n - 1;
		}
		webEngine.load(threads.get(indx));
		addressbar.setText(threads.get(indx));

		int N = stringList.size();
		for (int i = 0; i < N; i++) {
			System.out.println(stringList.get(i));
		}

	}

	@FXML
	void exit(ActionEvent event) {
		//Platform.exit();
	}

	public ObservableList<String> readFile() throws IOException {
		ObservableList<String> excep_list = FXCollections.observableArrayList();
		File file = new File("/Users/daanyalmalik/eclipse-workspace/CodeDigger/data/output.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st = null;
		System.out.println("dani: " + st);
		while ((st = br.readLine()) != null) {

			st = st.replace("\t", "");

			if (!st.startsWith("at") && !st.startsWith(".") && !st.startsWith("/") && st.length() > 2) {
				stringList.add(st);
				System.out.println("dani: " + st.length());
				excep_list.add(st);
			}

		}
		br.close();
		return excep_list;
	}

	public void updateExcepList()
	{
		Timeline updateGUI = new Timeline(
				new KeyFrame(javafx.util.Duration.seconds(5), new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						//System.out.println("this is called every 5 seconds on UI thread");
						try {
							items = readFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						excpListView.setItems(items);

					}
				}));
		updateGUI.setCycleCount(Timeline.INDEFINITE);
		updateGUI.play();
	}

}
