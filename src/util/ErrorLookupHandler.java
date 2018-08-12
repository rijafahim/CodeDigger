package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import ui.*;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class ErrorLookupHandler extends AbstractHandler 
{
	
	public static Vector<String> previous_search = new Vector<String>();
	// Holds the previous query (equivilent to previous_search[last]).
	static String previous_query = "";
	// Offset of the previous query (to re-insert when using undo).
	static int previous_offset = 0;
	// Length of the previous query (to re-insert when using undo).
	static int previous_length = 0;
	// Holds previous queries.
	public static Vector<String> previous_queries = new Vector<String>();

	static Vector<IDocument> documents = new Vector<IDocument>();
	// public static IDocumentListener doclistener;
	public static IDocumentListener doclistener;
	public int chk = 0;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {


		File file = new File("/Users/daanyalmalik/eclipse-workspace/CodeDigger/data/output.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);
			sc.useDelimiter("\\Z");
			System.out.println(sc.next());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String arArgs = null;

		if (chk == 0) {
			chk += 1;
			Thread runFxApp = new Thread(() -> {

				FYP.launch(FYP.class, arArgs);

			});

			runFxApp.start();

		} else if (chk > 0) {
			System.out.println("in chk2");

			Platform.runLater(() -> {
				// Update UI here.
				FYP.showStage();
			
			});
		}

		return null;

	}

	public void QuerySearch(String qry) {
		//Searcher search = new Searcher();
		//System.out.println("dani: " + search.getThreads(qry));
	}

}
