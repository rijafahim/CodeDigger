package ui;

import util.AutoSuggest;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.controlsfx.control.textfield.TextFields;
import javafx.fxml.Initializable;

public class CodeDiggerHomeController implements Initializable 
{
    @FXML private Tab myCodeDigger;
    @FXML private JFXTabPane mainPane;
    @FXML private JFXTextField codeSearchArea;
    @FXML private JFXButton previousQuery_Search;
    @FXML private AnchorPane codeView;
    @FXML private JFXButton nextQuery_Search;
    @FXML private JFXButton errorLookup;
    @FXML private AnchorPane classes;
    ImageView previousQuery_IMAGE = new ImageView();
    ImageView nextQuery_IMAGE = new ImageView();
    ImageView error_IMAGE = new ImageView();
    public static String Query;
    public List<String> possibleSuggestions = new ArrayList<String>();
    public String queryFromIDE = "";

    public void loadImages() //loads Images to ImageViews
    {
    	 Image backarrow = new Image(getClass().getResourceAsStream("images/left-arrow.png"));
    	 previousQuery_IMAGE = new ImageView(backarrow);
    	 Image nextarrow = new Image(getClass().getResourceAsStream("images/right-arrow.png"));
    	 nextQuery_IMAGE = new ImageView(nextarrow);
    	 Image error = new Image(getClass().getResourceAsStream("images/error.png"));
    	 error_IMAGE = new ImageView(error);
    }
    
    public void setImages() //sets ImageViews to buttons
    {
    	previousQuery_Search.setGraphic(previousQuery_IMAGE);
    	nextQuery_Search.setGraphic(nextQuery_IMAGE);
    	errorLookup.setGraphic(error_IMAGE);
    }
    
    public void triggerByIDE(String IDEQuery)
    {
    	queryFromIDE = IDEQuery;
		System.out.println(queryFromIDE);
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable()
        {
        	@Override
            public void run() 
        	{
                   Platform.runLater(new Runnable()
                   {
                         @Override
                         public void run() 
                         {
							 if (queryFromIDE != ""){
								 System.out.println("listedednedmned!!!! ");
								 codeSearchArea.setText(queryFromIDE);
						         Query = codeSearchArea.getText();
						         SearchViewController.func = 1;
						         Pane newLoadedPane = null;
						         try {
						            newLoadedPane = FXMLLoader.load(getClass().getResource("SearchView.fxml"));
						         } catch (IOException e) {
						            e.printStackTrace();
						         }
						         codeView.getChildren().add(newLoadedPane);
						         queryFromIDE = "";
							 }
                         }
                   });
            }
                          
        },1, 1,TimeUnit.SECONDS); 
        
  	/*	 Task task = new Task<Void>(){ //A listener that controls UI during/after the results have been fetched from Google API
			 @Override
			 protected Void call() throws Exception {
				 while(true) {
					 Platform.runLater(new Runnable() {
						 @Override
						 public void run() {
							 if (queryFromIDE != ""){
								 System.out.println("listedednedmned!!!! ");
								 codeSearchArea.setText(queryFromIDE);
						         Query = codeSearchArea.getText();
						         SearchViewController.func = 1;
						         Pane newLoadedPane = null;
						         try {
						            newLoadedPane = FXMLLoader.load(getClass().getResource("SearchView.fxml"));
						         } catch (IOException e) {
						            e.printStackTrace();
						         }
						         codeView.getChildren().add(newLoadedPane);
						         queryFromIDE = "";
							 }
						 }
					 });
					 Thread.sleep(2000);
				 }
			 }
		 };           
		 Thread thread = new Thread(task);
		 thread.setDaemon(true);
		 thread.start();*/
		
		loadImages(); //loads images and converts them into ImageView
		setImages(); //sets images on buttons
		AutoSuggest obj = new AutoSuggest(); //creates object for Auto Suggest

		//defines all actions that take place on the code search text field
		codeSearchArea.setOnKeyPressed(new EventHandler<KeyEvent>() { 
		    @Override 
		    public void handle(KeyEvent keyEvent) {
		        if (keyEvent.getCode() == KeyCode.ENTER) { //If the user writes a query and presses the Enter key
		            Query = codeSearchArea.getText();
		            SearchViewController.func = 1;
		            Pane newLoadedPane = null;
		            try {
		            	newLoadedPane = FXMLLoader.load(getClass().getResource("SearchView.fxml"));
		            } catch (IOException e) {
		            	e.printStackTrace();
		            }
		            codeView.getChildren().add(newLoadedPane);
		            
		        }
		        else { //Keep listening for suggestions as the user presses keys on codeSearchArea
			    	Query = codeSearchArea.getText();
	            	possibleSuggestions = obj.findQueries(Query, 1);
	        		TextFields.bindAutoCompletion(codeSearchArea, possibleSuggestions);
		        }
		    }}); 
		
		//defines action that takes place is the error lookup button is pressed.
    	errorLookup.setOnAction(new EventHandler<ActionEvent>(){ 
			@Override
			public void handle(ActionEvent arg0) {
				SearchViewController.func = 2;
	            Pane newLoadedPane = null;
	            try {
	            	newLoadedPane = FXMLLoader.load(getClass().getResource("SearchView.fxml"));
	            } catch (IOException e) {
	            	e.printStackTrace();
	            }
	            codeView.getChildren().add(newLoadedPane);
				
			}}); 
	}
}



