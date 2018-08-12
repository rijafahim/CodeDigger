package ui;
import java.io.FileInputStream;
import java.io.IOException;
import org.eclipse.fx.ui.workbench3.FXViewPart;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import util.JDKClassReader;

public class MyViewPart extends FXViewPart 
{
	public static Parent root = null;
	public static FXMLLoader mainLoader;
	//Parent root = loader.load(new FileInputStream("C:/Users/Rija Fahim/Desktop/Code Digger.zip_expanded/CodeDigger/src/ui/CodeDiggerHome.fxml"));
	//CodeDiggerHomeController controller = mainLoader.getController();
	
	@Override
	protected Scene createFxScene() //loads JavaFX as a plugin
	{	
		try {
			JDKClassReader.getAllClasses(); //loads Java classes 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			mainLoader = new FXMLLoader(getClass().getResource("CodeDiggerHome.fxml"));
			root = mainLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		return scene;
	}
	
	public FXMLLoader getMainLoader()
	{
		return mainLoader;
	}
	@Override
	protected void setFxFocus() {		
	}
}