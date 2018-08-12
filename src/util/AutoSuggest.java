package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.text.ITextSelection;

import org.eclipse.jface.viewers.ISelection;

public class AutoSuggest implements IJavaCompletionProposalComputer {

	// Stores tasks from the task database file.
	static HashMap<String, String> queries_map = new HashMap<String, String>();
	// Stores the list of recommendation tasks/queries for each invocation of the
	// content assist tool.
	static ArrayList<String> queries = new ArrayList<String>();
	// Ddefines whether we are querying via a task or not.
	static boolean query_task = true;
	// Defines which types of content assist to load (Developer use only).
	private boolean task_auto_completes = true;
	private boolean title_auto_completes = false;
	// Defines the maximum number of recommendations to include in the content
	// assist window (less recommendations for greater performance).
	private int MAX_NUM_RECOMMENDATIONS = 100;

	String lastToken;

	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context,
			IProgressMonitor monitor) {
		if ((queries_map instanceof HashMap<?, ?> && queries instanceof ArrayList<?>)) {
			System.out.println("in if condition");
			queries_map = new HashMap<String, String>();
			queries = new ArrayList<String>();
			// Chooses which type of autocompletes to load (see class variables).
			if (task_auto_completes)
				loadTasks();
			if (title_auto_completes)
				loadTitles();
			// If neither auto-completes are selected to load, load the task auto completes.
			if (!task_auto_completes && !title_auto_completes)
				loadTasks();
		}

		List<String> testProps = new ArrayList<String>();
		testProps.add("suggestion1");

		// System.out.println(testProps);
		// TODO Auto-generated method stub
		// int invocationOffset = context.getInvocationOffset();
		// IDocument doc=context.getDocument();
		// get the last token on which ctrl+space or '.' is pressed.
		// String pgm;
		// System.out.println("Hee");
		// Pre-define some variables
		String line = "";
		int line_num = 0;
		IDocument doc;

		// Get the active editor.
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		// This code block gets the current selection in the active editor.
		// If we are dealing with a text editor.
		if (editor instanceof ITextEditor) {
			// Get the current selection of the document (for determining what line a query
			// is on).
			ISelectionProvider selectionProvider = ((ITextEditor) editor).getSelectionProvider();
			if (selectionProvider.equals(null))
				return new ArrayList<ICompletionProposal>();
			ISelection selection = selectionProvider.getSelection();
			if (selection.equals(null))
				return new ArrayList<ICompletionProposal>();
			ITextEditor ite = (ITextEditor) editor;
			if (ite.equals(null))
				return new ArrayList<ICompletionProposal>();
			// Get the current document (for isolating substring of text in document using
			// line number from selection).
			doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
			if (doc.equals(null))
				return new ArrayList<ICompletionProposal>();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				try {
					// Get the line number we are currently on.
					line_num = textSelection.getStartLine();
					// Get the string on the current line and use that as the query line to be
					// auto-completed.
					line = doc.get(doc.getLineOffset(doc.getLineOfOffset(textSelection.getOffset())),
							doc.getLineLength(doc.getLineOfOffset(textSelection.getOffset())));
				} catch (BadLocationException e) {
					// System.out.println("Error with getting input query.");
					e.printStackTrace();
					return null;
				}
			}
		} else {
			// If we are not dealing with a text editor.
			return null;
		}

		// If the last character of the selection is a end-of-line character, make sure
		// we don't insert end-of-line characters in the content assist.
		boolean eol = false;
		if (line.endsWith("\n"))
			eol = true;

		// Remove leading and trailing whitespace for the partial query (save the
		// leading whitespace for later).
		String whitespace_before;
		if (line.indexOf(line.trim()) < 0) {
			whitespace_before = line;
		} else {
			whitespace_before = line.substring(0, line.indexOf(line.trim()));
		}

		// Calculate offset of partial query. Used when inserting a recommendation to
		// ensure the query is inserted correctly.
		int extra_offset = whitespace_before.length();
		line = line.trim();
		if (line.startsWith("?", 0)) {
			if (line.length() > 1)
				line = line.substring(1, line.length());
			else
				line = "";
		}
		line = line.toLowerCase();

		;
		// creating the list of proposals/suggestions to be returned to UI editor
		List<ICompletionProposal> props = new ArrayList<ICompletionProposal>();
		// System.out.println(line);

		int line_length = 0;
		int line_offset = 0;

		// Get the line length and line offset to ensure we replace the correct location
		// with any completion.
		try {
			line_length = doc.getLineLength(line_num);
			line_offset = doc.getLineOffset(line_num);
			if (eol)
				line_length -= 1;
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		for (String searchResult : findQueries(line,0)) {
			props.add(
					new CompletionProposal(searchResult.substring(0, 1).toUpperCase() + searchResult.substring(1) + "?", // replacement
																															// text,
																															// first
																															// letter
																															// is
																															// capital
							line_offset + extra_offset, // replacement offset
							line_length - extra_offset, // replace the full text
							searchResult.length())); // length of string to replace
		}

		return props;
	}

public List<String> findQueries(String search, int triggeredBy)  
	{
		List<String> result = new ArrayList<String>();
		if (triggeredBy == 0)
		{
			//System.out.println("findQueries: " + search);
			for (String query : queries) {
				// System.out.println("findQueries");
				if (query.contains(search) || search == "") {
					// If we reach the maximum number of queries to recommend, stop.
					if (result.size() == MAX_NUM_RECOMMENDATIONS)
						return result;
					result.add(query);
					//System.out.print("Result");
					//ystem.out.println(query);
				}
			}
			/*FlaskConn modelPredict= new FlaskConn();
			try {
				result.add(modelPredict.QueryModel(search));
				//result.add(modelPredict.QueryModel(search));
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}*/
			
		}
		else if (triggeredBy == 1)
		{
			//System.out.println("findQueries: " + search);
			if ((queries_map instanceof HashMap<?, ?> && queries instanceof ArrayList<?>)) {
				//System.out.println("in if condition");
				queries_map = new HashMap<String, String>();
				queries = new ArrayList<String>();
				// Chooses which type of autocompletes to load (see class variables).
				if (task_auto_completes)
					loadTasks();
				if (title_auto_completes)
					loadTitles();
				// If neither auto-completes are selected to load, load the task auto completes.
				if (!task_auto_completes && !title_auto_completes)
					loadTasks();
			}
			
			for (String query : queries) {
				// System.out.println("findQueries");
				if (query.contains(search) || search == "") {
					// If we reach the maximum number of queries to recommend, stop.
					if (result.size() == MAX_NUM_RECOMMENDATIONS)
						return result;
					result.add(query);
					//System.out.print("Result");
					//ystem.out.println(query);
				}
			}
			/*FlaskConn modelPredict= new FlaskConn();
			try {
				result.add(modelPredict.QueryModel(search));
				//result.add(modelPredict.QueryModel(search));
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}*/
		}
		return result;
	}

	public static void loadTasks() {
		@SuppressWarnings("unused")
		URL url;
		try {
			//System.out.println("Load Tasks");
			// Using this url assumes nlp2code exists in the 'plugin' folder for eclipse.
			// This is true when testing the plugin (in a temporary platform) and after
			// installing the plugin.
			url = new URL("platform:/testanotherone/data/task,id.txt");
			// InputStream inputStream = url.openConnection().getInputStream();
			FileReader inputStream = new FileReader(
					"/Users/daanyalmalik/Desktop/CodeDigger 2/src/util/data/task,id.txt");
			BufferedReader in = new BufferedReader(inputStream);
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				String task = inputLine.substring(0, inputLine.indexOf(","));
				String ids = inputLine.substring(inputLine.indexOf(",") + 1);
				// System.out.println(task);
				// System.out.println(ids);
				queries_map.put(task, ids);
				queries.add(task);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Function loadTitles Loads the Stack Overflow title to question ID mapping
	 * database into a map. Purpose: Loads Question Title->Id mapping into
	 * queries_map.
	 */
	private void loadTitles() {
		@SuppressWarnings("unused")
		URL url;
		try {
			System.out.println("Load Titles");
			// Using this url assumes nlp2code exists in the 'plugin' folder for eclipse.
			// This is true when testing the plugin (in a temporary platform) and after
			// installing the plugin.
			url = new URL("platform:/TestAnotherone/data/title,id.txt");
			// InputStream inputStream = url.openConnection().getInputStream();
			FileReader inputStream = new FileReader(
					"/Users/daanyalmalik/Desktop/CodeDigger 2/src/util/data/title,id.txt");
			BufferedReader in = new BufferedReader(inputStream);
			// BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				String query = inputLine.substring(0, inputLine.indexOf(","));
				String id = inputLine.substring(inputLine.indexOf(",") + 1);
				queries_map.put(query, id);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext arg0,
			IProgressMonitor arg1) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sessionEnded() {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionStarted() {
		// TODO Auto-generated method stub
	}
}