package util;

import java.io.IOException;
import java.util.Vector;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import ui.CodeDiggerHomeController;
import ui.MyViewPart;

public class Handler extends AbstractHandler {
	// Holds history of previous code snippets (from previous queries) to enable
	// undo functionality.
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

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		ITextEditor current_editor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IDocument current_doc = current_editor.getDocumentProvider().getDocument(current_editor.getEditorInput());
		if (!Handler.documents.contains(current_doc)) {
			// current_doc.addDocumentListener(Handler.qdl);
		}

		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		String text = null;
		// If the current active editor is a text editor (i.e. not any other workbench
		// component)
		if (part instanceof ITextEditor) {
			// Use text editor context to locate the document of the editor and get the
			// input stream to that editor.
			ITextEditor editor = (ITextEditor) part;
			IDocumentProvider prov = editor.getDocumentProvider();
			IDocument doc = prov.getDocument(editor.getEditorInput());

			// Get the current selection to query on.
			ISelection sel = editor.getSelectionProvider().getSelection();
			// System.out.print("Out");
			System.out.print(sel.toString());

			if (sel instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) sel;
				System.out.print("Inside");

				if (textSelection.equals(null))
					return null;
				int offset = textSelection.getOffset();
				// Get the string on the current line and use that as the query line to be
				// auto-completed.
				if (offset > doc.getLength() || offset < 0)
					return null;
				try {
					text = doc.get(doc.getLineOffset(doc.getLineOfOffset(offset)),
							doc.getLineLength(doc.getLineOfOffset(offset)));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (text.equals("") || text.equals(null))
					return null;
				@SuppressWarnings("unused")
				boolean eol = false;
				if (text.endsWith("\n"))
					eol = true;
		
				String whitespace_before = text.substring(0, text.indexOf(text.trim()));
				text = text.trim();
				System.out.print(text);
			}
			
		}

		try {
			System.out.println("Last Character " + text.substring(text.length() - 1));
			String last = text.substring(text.length() - 1);
			//System.out.println("Question " + last);
			if (last.equals("?")) {
				IDEQuery(text);
			} else {
				IDEQuery(text);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;

	}
	public void IDEQuery(String text) throws IOException 
	{
		CodeDiggerHomeController controller = MyViewPart.mainLoader.getController();
		//loadTasks();
		String Id;
		text = text.toLowerCase();
		text = text.substring(0, text.length() - 1);
		controller.triggerByIDE(text);
	}
}
