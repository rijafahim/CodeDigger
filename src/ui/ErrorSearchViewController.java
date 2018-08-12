package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import util.Searcher;

public class ErrorSearchViewController implements Initializable {
    private final Node errorIcon = new ImageView(new Image(getClass().getResourceAsStream("images/error.png")));
    @FXML private TreeView<String> treeView;
    @FXML private AnchorPane errorSearchVew;
    private int errorCount = 0;
    
    private void handleMouseClicked(MouseEvent event) {
    	Searcher obj = new Searcher();
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue();
           	String codeSnippets = obj.getErrorSnippet(name); //sends the URLS to get code snippets from the
			if (errorCount==0)
			{
				errorCount++;
				
				Thread runFxApp = new Thread(() -> {
		           	ErrorView.editingCode = codeSnippets;
		           	ErrorView.launch(ErrorView.class, null);
				});
				runFxApp.start(); 
			}
			else if (errorCount >0)
			{
				
				Platform.runLater(() -> {
					ErrorView.editingCode = codeSnippets;
		           	ErrorView.showStage(codeSnippets);
				
				});
			}
           	

        }
    }

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stubs
		//System.out.println(x);
		 TreeItem<String> rootNode = new TreeItem<String> ("Stack Overflow Error Suggestions", errorIcon);
	     rootNode.setExpanded(true);
	     for (int i = 0; i<SearchViewController.errorSuggestions.size(); i++) 
	     {
	         TreeItem<String> errorSuggestion = new TreeItem<String>(SearchViewController.errorSuggestions.get(i).getErrorSuggestion());
	         boolean found = false;
	            for (TreeItem<String> errorNode : rootNode.getChildren()) {
	                if (errorNode.getValue().contentEquals(SearchViewController.errorSuggestions.get(i).getErrorName())){
	                    errorNode.getChildren().add(errorSuggestion);
	                    found = true;
	                    break;
	                }
	            }
	            if (!found) {
	                TreeItem<String> errorNode = new TreeItem<String>(
	                	SearchViewController.errorSuggestions.get(i).getErrorName(), 
	                    errorIcon
	                );
	                rootNode.getChildren().add(errorNode);
	                errorNode.getChildren().add(errorSuggestion);
	            }
	            //errorSearchView.setVisible(true);
		 }
	     treeView.setRoot(rootNode);
         for (TreeItem<String> errorNode : rootNode.getChildren()) {
        	 errorNode.setExpanded(true);
         }	
         
         
    EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
        	handleMouseClicked(event);
    };
    treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 
	}
}
