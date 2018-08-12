package ui;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.imageio.ImageIO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.ErrorLookup;
import util.JDKClassReader;
import util.Searcher;

public class SearchViewController implements Initializable 
{
	@FXML private AnchorPane errorSearchView;
    @FXML private JFXButton copyButton2;
    @FXML private JFXButton copyButton1;
    @FXML private JFXButton viewButton1;
    @FXML private Pane mainScene;
    @FXML private JFXButton viewButton2;
    @FXML private JFXButton viewButton3;
    @FXML private JFXButton viewButton4;
    @FXML private JFXButton copyButton5;
    @FXML private JFXButton copyButton4;
    @FXML private JFXButton viewButton5;
    @FXML private JFXButton copyButton3;
    @FXML private AnchorPane snippet1;
    @FXML private AnchorPane snippet2;
    @FXML private AnchorPane snippet3;
    @FXML private AnchorPane snippet4;
    @FXML private AnchorPane snippet5;
    @FXML private JFXButton noButton2;
    @FXML private JFXButton noButton1;
    @FXML private JFXButton yesButton1;
    @FXML private JFXButton yesButton2;
    @FXML private JFXButton yesButton3;
    @FXML private JFXButton yesButton4;
    @FXML private JFXButton noButton4;
    @FXML private Pane box5;
    @FXML private JFXButton yesButton5;
    @FXML private JFXButton noButton3;
    @FXML private Pane box3;
    @FXML private Pane box4;
    @FXML private JFXButton noButton5;
    @FXML private Pane box1;
    @FXML private Text title1;
    @FXML private Pane box2;
    @FXML private Text title2;
    @FXML private Text title3;
    @FXML private Text title4;
    @FXML private Text title5;
    @FXML private ImageView badge1;
    @FXML private ImageView badge2;
    @FXML private Text codeContent2;
    @FXML private ImageView badge3;
    @FXML private Text codeContent3;
    @FXML private ImageView badge4;
    @FXML private ImageView badge5;
    @FXML private Text codeContent1;
    @FXML private Text codeContent4;
    @FXML private Text codeContent5;
    @FXML private JFXSpinner loader;
    @FXML private TilePane bar;
    ObservableList<String> items = FXCollections.observableArrayList();
    public static ObservableList<String> retrievedCodeSnippets = FXCollections.observableArrayList();
    List<String> allClasses = new ArrayList<String>();
    private int viewCounter1 = 0;
    private int viewCounter2 = 0;
    private int viewCounter3 = 0;
    private int viewCounter4 = 0;
    private int viewCounter5 = 0;
    public static int func = -1;
    @FXML void copySnippet1(ActionEvent event) {	copytoClipboard(codeContent1.getText());	}
   	@FXML void copySnippet2(ActionEvent event) {	copytoClipboard(codeContent2.getText());	}
    @FXML void copySnippet3(ActionEvent event) {	copytoClipboard(codeContent3.getText());	}
	@FXML void copySnippet4(ActionEvent event) {	copytoClipboard(codeContent4.getText());	}
   	@FXML void copySnippet5(ActionEvent event) {	copytoClipboard(codeContent5.getText());	}
    private final Node errorIcon = new ImageView(new Image(getClass().getResourceAsStream("images/error.png")));
    public static List<ErrorLookup> errorSuggestions = new ArrayList<ErrorLookup>();
    @FXML private TreeView<String> treeView;
    @FXML private AnchorPane errorSearchVew;
    private int errorCount = 0;
    
    void copytoClipboard(String string) //function that copies a given string to system clipboard
    {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(string);
		clipboard.setContents(strSel, null);
    }
    
    public WritableImage setLogos(String file) //Loads an Image and converts it into a Writable Image to be set to an Image View
    {
        BufferedImage BufferedImg = null;
        try {
			BufferedImg = ImageIO.read(getClass().getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
        WritableImage wr = null;
        if (BufferedImg!=null){
            wr = new WritableImage(BufferedImg.getWidth(),BufferedImg.getHeight());
            PixelWriter px = wr.getPixelWriter();
            for (int i=0; i<BufferedImg.getWidth(); i++){
                for (int j=0; j<BufferedImg.getHeight(); j++){
                    px.setArgb(i, j, BufferedImg.getRGB(i,j));
                }
            }
        }
        return wr;
    }
    
    private void setStackOverflowIcons() //set Stack Overflow icons in every code snippet pane
    {
    	 WritableImage W1 = setLogos("images/stack.png");
    	 badge1.setImage(W1); badge2.setImage(W1); badge3.setImage(W1); badge4.setImage(W1); badge5.setImage(W1);
    }
    public boolean searchCodeQuery() //Executed when the user presses 'Search' button for an enetered query
    {
    	Searcher obj = new Searcher();
    	Vector<String> stackURLS = obj.getThreads(CodeDiggerHomeController.Query); //fetches top Stack Overflow URLs for the given query
    	Vector<String> codeSnippets = obj.getCodeSnippets(stackURLS); //sends the URLS to get code snippets from them
        
        //Add them to an observable array 
        retrievedCodeSnippets.add(codeSnippets.get(0));
        retrievedCodeSnippets.add(codeSnippets.get(1));
        retrievedCodeSnippets.add(codeSnippets.get(2));
        retrievedCodeSnippets.add(codeSnippets.get(3));
        retrievedCodeSnippets.add(codeSnippets.get(4));
        return true;
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
					}
				}));
		updateGUI.setCycleCount(Timeline.INDEFINITE);
		updateGUI.play();
	}

	public ObservableList<String> readFile() throws IOException 
	{
		ArrayList<String> stringList = new ArrayList<String>();
		ObservableList<String> ExceptionsList = FXCollections.observableArrayList();
		File file = new File("/Users/daanyalmalik/Desktop/CodeDigger 2/src/util/data/output.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));

		String st = null;
		while ((st = br.readLine()) != null) 
		{
			st = st.replace("\t", "");
			if (!st.startsWith("at") && !st.startsWith(".") && !st.startsWith("/") && st.length() > 2) {
				stringList.add(st);
				ExceptionsList.add(st);
			}

		}
		br.close();
		return ExceptionsList;
	}
    public boolean searchErrorQuery() throws IOException
    {
		items = readFile();		
    	Searcher obj = new Searcher();	 //fetches top Stack Overflow URLs for the given query
    	Vector<String> All_Answers = new Vector<String>();
    	for (int i=0; i<items.size(); i++) {
    		Vector<String> stackURLS = obj.getThreads(items.get(i)); 
    		for (int j=0; j<stackURLS.size(); j++) {
    			errorSuggestions.add(new ErrorLookup(items.get(i),stackURLS.get(j)));
    		}
    	}
    	return true;
    }


    public List<String> findClassFromCode() //reads the retrieved code word by word and identifies the JDK 8 classes from it
    {    	
    	String[] CodeSplit_1 = CodeEditor.code1.split("\\s+"); //Finds classes from the first code snippet
    	for (int i=0; i<CodeSplit_1.length; i++) {
    		if (JDKClassReader.allClasses.contains(CodeSplit_1[i])) {
    				allClasses.add(CodeSplit_1[i]);
    		}
    	}
    	
    	String[] CodeSplit_2 = CodeEditor.code2.split("\\s+"); //Finds classes from the first code snippet
    	for (int i=0; i<CodeSplit_2.length; i++) {
    		if (JDKClassReader.allClasses.contains(CodeSplit_2[i])) {
    				allClasses.add(CodeSplit_2[i]);
    		}
    	}
    	
    	String[] CodeSplit_3 = CodeEditor.code3.split("\\s+"); //Finds classes from the first code snippet
    	for (int i=0; i<CodeSplit_3.length; i++) {
    		if (JDKClassReader.allClasses.contains(CodeSplit_3[i])) {
    				allClasses.add(CodeSplit_3[i]);
    		}
    	}
    	
    	String[] CodeSplit_4 = CodeEditor.code4.split("\\s+"); //Finds classes from the first code snippet
    	for (int i=0; i<CodeSplit_4.length; i++) {
    		if (JDKClassReader.allClasses.contains(CodeSplit_4[i])) {
    				allClasses.add(CodeSplit_4[i]);
    		}
    	}
    	
    	String[] CodeSplit_5 = CodeEditor.code5.split("\\s+"); //Finds classes from the first code snippet
    	for (int i=0; i<CodeSplit_5.length; i++) {
    		if (JDKClassReader.allClasses.contains(CodeSplit_5[i])) {
    				allClasses.add(CodeSplit_5[i]);
    		}
    	}
    	
    	allClasses = new ArrayList<String>(new LinkedHashSet<String>(allClasses));// delete duplicates (if any) from 'myArrayList'
    	return allClasses;
    }
    public void setImagesonButtons() //load images and write them to ImageView 
    {
    	 Image copy = new Image(getClass().getResourceAsStream("images/copy.png"));
    	 ImageView temp1 = new ImageView(copy);
    	 ImageView temp2 = new ImageView(copy);
    	 ImageView temp3 = new ImageView(copy);
    	 ImageView temp4 = new ImageView(copy);
    	 ImageView temp5 = new ImageView(copy);
    	 copyButton1.setGraphic(temp1);
    	 copyButton2.setGraphic(temp2);
    	 copyButton3.setGraphic(temp3);
    	 copyButton4.setGraphic(temp4);
    	 copyButton5.setGraphic(temp5);
    	 
    	 Image yes = new Image(getClass().getResourceAsStream("images/Yes.png"));
    	 ImageView temp6 = new ImageView(yes);
    	 ImageView temp7 = new ImageView(yes);
    	 ImageView temp8 = new ImageView(yes);
    	 ImageView temp9 = new ImageView(yes);
    	 ImageView temp10 = new ImageView(yes);
    	 yesButton1.setGraphic(temp6);
    	 yesButton2.setGraphic(temp7);
    	 yesButton3.setGraphic(temp8);
    	 yesButton4.setGraphic(temp9);
    	 yesButton5.setGraphic(temp10);
    	 
    	 Image no= new Image(getClass().getResourceAsStream("images/No.png"));
    	 ImageView temp11 = new ImageView(no);
    	 ImageView temp12 = new ImageView(no);
    	 ImageView temp13 = new ImageView(no);
    	 ImageView temp14 = new ImageView(no);
    	 ImageView temp15 = new ImageView(no);
    	 noButton1.setGraphic(temp11);
    	 noButton2.setGraphic(temp12);
    	 noButton3.setGraphic(temp13);
    	 noButton4.setGraphic(temp14);
    	 noButton5.setGraphic(temp15);
    	 
    	 Image view = new Image(getClass().getResourceAsStream("images/eye.png"));
    	 ImageView temp16 = new ImageView(view);
    	 ImageView temp17 = new ImageView(view);
    	 ImageView temp18 = new ImageView(view);
    	 ImageView temp19 = new ImageView(view);
    	 ImageView temp20 = new ImageView(view);
    	 viewButton1.setGraphic(temp16);
    	 viewButton2.setGraphic(temp17);
    	 viewButton3.setGraphic(temp18);
    	 viewButton4.setGraphic(temp19);
    	 viewButton5.setGraphic(temp20);
    }
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
				System.out.println(errorCount  +" boboboboboob");
				Platform.runLater(() -> {
					ErrorView.editingCode = codeSnippets;
		           	ErrorView.showStage(codeSnippets);
				
				});
			}
           	

        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {  
    	 if (func == 1){ //if the user entered a code snippet query
    		 Task task = new Task<Void>(){ //A listener that controls UI during/after the results have been fetched from Google API
    			 @Override
    			 protected Void call() throws Exception {
    				 searchCodeQuery();
    				 while(true) {
    					 Platform.runLater(new Runnable() {
    						 @Override
    						 public void run() {
    							 if (retrievedCodeSnippets.size() == 5){
    								 loader.setVisible(false);
    								 box1.setVisible(true);
    								 box2.setVisible(true);
    								 box3.setVisible(true);
    								 box4.setVisible(true);
    								 box5.setVisible(true);
    								 
    								 snippet1.getChildren().add(new CodeEditor(retrievedCodeSnippets.get(0),1));
    								 snippet2.getChildren().add(new CodeEditor(retrievedCodeSnippets.get(1),1));
    								 snippet3.getChildren().add(new CodeEditor(retrievedCodeSnippets.get(2),1));
    								 snippet4.getChildren().add(new CodeEditor(retrievedCodeSnippets.get(3),1));
    								 snippet5.getChildren().add(new CodeEditor(retrievedCodeSnippets.get(4),1));
    								 CodeEditor.code1 = retrievedCodeSnippets.get(0);
    							     CodeEditor.code2 = retrievedCodeSnippets.get(1);
    							     CodeEditor.code3 = retrievedCodeSnippets.get(2);
    							     CodeEditor.code4 = retrievedCodeSnippets.get(3);
    							     CodeEditor.code5 = retrievedCodeSnippets.get(4);
    								 
    					    		 findClassFromCode(); //search for classes
    					        	 for (int i = 0; i<allClasses.size(); i++) {
    					        		 bar.getChildren().add((new JFXButton(allClasses.get(i))));
    					        	 }
    					        	 	bar.setPrefColumns(3);
    							 }
    						 }
    					 });
    					 Thread.sleep(2000);
    					 if (retrievedCodeSnippets.size() == 5){
    						 retrievedCodeSnippets.clear();
    						 break;
    					 }
    				 }
    				 return null;
    			 }
    		 };           
    		 Thread thread = new Thread(task);
    		 thread.setDaemon(true);
    		 thread.start();
    		 
    	 }
    	 else if (func == 2) //if the user pressed on the error lookup button
    	 {
    		 Task task = new Task<Void>(){ //A listener that controls UI during/after the results have been fetched from Google API
    			 @Override
    			 protected Void call() throws Exception {
    				 searchErrorQuery();
    				 while(true) {
    					 Platform.runLater(new Runnable() {
    						 @Override
    						 public void run() {
    							 if (errorSuggestions.size() != 0){
    								 loader.setVisible(false);
    								 if (box1.isVisible()) { 	box1.setVisible(false);	}
    								 if (box2.isVisible()) { 	box2.setVisible(false);	}
    								 if (box3.isVisible()) { 	box3.setVisible(false);	}
    								 if (box4.isVisible()) { 	box4.setVisible(false);	}
    								 if (box5.isVisible()) { 	box5.setVisible(false);	}
    								 
    						         Pane newLoadedPane = null;
    						         try {
    						            newLoadedPane = FXMLLoader.load(getClass().getResource("ErrorSearchView.fxml"));
    						         } catch (IOException e) {
    						            e.printStackTrace();
    						         }
    						         errorSearchView.getChildren().add(newLoadedPane); 
    						         
    								 /*TreeItem<String> rootNode = new TreeItem<String> ("Stack Overflow Error Suggestions", errorIcon);
    							     rootNode.setExpanded(true);
    							     for (int i = 0; i<errorSuggestions.size(); i++) 
    							     {
    							         TreeItem<String> errorSuggestion = new TreeItem<String>(errorSuggestions.get(i).getErrorSuggestion());
    							         boolean found = false;
    							            for (TreeItem<String> errorNode : rootNode.getChildren()) {
    							                if (errorNode.getValue().contentEquals(errorSuggestions.get(i).getErrorName())){
    							                    errorNode.getChildren().add(errorSuggestion);
    							                    found = true;
    							                    break;
    							                }
    							            }
    							            if (!found) {
    							                TreeItem<String> errorNode = new TreeItem<String>(
    							                	errorSuggestions.get(i).getErrorName(), 
    							                    errorIcon
    							                );
    							                rootNode.getChildren().add(errorNode);
    							                errorNode.getChildren().add(errorSuggestion);
    							            }*/
    							            errorSearchView.setVisible(true);
    								 /*}
    							     treeView.setRoot(rootNode);
							         for (TreeItem<String> errorNode : rootNode.getChildren()) {
							        	 errorNode.setExpanded(true);
							         }*/
    							 }
    						 }
    					 });
    					 Thread.sleep(2000);
    					 if (errorSuggestions.size() > 0){
    						 errorSuggestions.clear();
    						 break;
    					 }
    				 }
    				 return null;
    			 }
    		 };           
    		 Thread thread = new Thread(task);
    		 thread.setDaemon(true);
    		 thread.start();
    	 }
        
    	setStackOverflowIcons(); //setting Stack Overflow icons on every code snippet pane 
    	setImagesonButtons();
    	viewButton1.setOnAction(new EventHandler<ActionEvent>(){ //defines code snippet viewing action for code snippet # 1
        	@Override 
        		public void handle(ActionEvent event) { 
        			if (viewCounter1 == 0){
        				viewCounter1++;
        				viewCounter2++;
        				viewCounter3++;
        				viewCounter4++;
        				viewCounter5++;
        				
        				System.out.println("in viewCounter1");

        				Thread runFxApp = new Thread(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code1;
        					System.out.println("in viewCounter1 " + CodeEditor.code1);
        					CodeEditorExample.launch(CodeEditorExample.class, null);
        				});
        				runFxApp.start(); }
        			else if (viewCounter1 > 0){
        				System.out.println("in viewCounter1++");
        				Platform.runLater(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code1;
        					CodeEditorExample.showStage(CodeEditor.code1);
        				
        				});}}}); 
    	
    	viewButton2.setOnAction(new EventHandler<ActionEvent>(){ //defines code snippet viewing action for code snippet # 2
        	@Override 
        		public void handle(ActionEvent event) { 
        			if (viewCounter2 == 0){
        				viewCounter1++;
        				viewCounter2++;
        				viewCounter3++;
        				viewCounter4++;
        				viewCounter5++;
        				
        				Thread runFxApp = new Thread(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code2;
        					CodeEditorExample.launch(CodeEditorExample.class, null);
        				});
        				runFxApp.start(); }
        			else if (viewCounter2 > 0){
        				Platform.runLater(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code2;
        					CodeEditorExample.showStage(CodeEditor.code2);
        				
        				});}}}); 
    	
    	viewButton3.setOnAction(new EventHandler<ActionEvent>(){ //defines code snippet viewing action for code snippet # 3
        	@Override 
        		public void handle(ActionEvent event) { 
        			if (viewCounter3 == 0){
        				viewCounter1++;
        				viewCounter2++;
        				viewCounter3++;
        				viewCounter4++;
        				viewCounter5++;
        				
        				Thread runFxApp = new Thread(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code3;
        					CodeEditorExample.launch(CodeEditorExample.class, null);
        				});
        				runFxApp.start(); }
        			else if (viewCounter3 > 0){
        				
        				Platform.runLater(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code3;
        					CodeEditorExample.showStage(CodeEditor.code3);
        				
        				});}}}); 
    	
    	viewButton4.setOnAction(new EventHandler<ActionEvent>(){ //defines code snippet viewing action for code snippet # 4
        	@Override 
        		public void handle(ActionEvent event) { 
        			if (viewCounter4 == 0){
        				viewCounter1++;
        				viewCounter2++;
        				viewCounter3++;
        				viewCounter4++;
        				viewCounter5++;
        				
        				Thread runFxApp = new Thread(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code4;
        					CodeEditorExample.launch(CodeEditorExample.class, null);
        				});
        				runFxApp.start(); }
        			else if (viewCounter4 > 0){
        				Platform.runLater(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code4;
        					CodeEditorExample.showStage(CodeEditor.code4);
        				
        				});}}}); 
    	
    	viewButton5.setOnAction(new EventHandler<ActionEvent>(){ //defines code snippet viewing action for code snippet # 5
        	@Override 
        		public void handle(ActionEvent event) { 
        			if (viewCounter5 == 0){
        				viewCounter1++;
        				viewCounter2++;
        				viewCounter3++;
        				viewCounter4++;
        				viewCounter5++;
        				
        				Thread runFxApp = new Thread(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code5;
        					CodeEditorExample.launch(CodeEditorExample.class, null);
        				});
        				runFxApp.start(); }
        			else if (viewCounter5 > 0){
        				Platform.runLater(() -> {
        					CodeEditorExample.editingCode = CodeEditor.code5;
        					CodeEditorExample.showStage(CodeEditor.code5);
        				
        				});}}}); 

    	/*
        
   EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> 
   {
       	handleMouseClicked(event);
   };
   //treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);*/
    	}
 
}
