package com.example.t2s;

import android.content.Context;

import java.io.IOException;
import java.util.Hashtable;

public class LawyerAppMaster
{
    Context context;
	protected ConversationParser curConversation;
//	private Updater updateAction;
	
	
	public LawyerAppMaster(Context context) throws IOException
	{	
		this.context =  context;
		String lawFileName = "lawTextFile.txt";
		String dictionaryFileName = "dictionaryTextFile.txt";
		
		LawDataBaseBuilder ldbBuilder = new LawDataBaseBuilder(context);
		DictionaryDataBaseBuilder ddbBuilder = new DictionaryDataBaseBuilder(context);
		
		Hashtable<String, String> lawDB = ldbBuilder.build(lawFileName);
		Hashtable<String, KeyWord> dictionaryDB = ddbBuilder.build(dictionaryFileName);

		curConversation = new ConversationParser(lawDB, dictionaryDB);
		
		
	}
	
	
	// This method simple updates the databases if more updated data files are available and and update
	// has been requested from the app activities.
	
	public void regionSwitcher(String lawFileName, String dictionaryFileName) throws IOException
	{
		LawDataBaseBuilder ldbBuilder = new LawDataBaseBuilder(context);
		DictionaryDataBaseBuilder ddbBuilder = new DictionaryDataBaseBuilder(context);
		
		Hashtable<String, String> lawDB = ldbBuilder.build(lawFileName);
		Hashtable<String, KeyWord> dictionaryDB = ddbBuilder.build(dictionaryFileName);	
		curConversation = new ConversationParser(lawDB, dictionaryDB);
		
	}
	
	
	
	/*public static void main(String[] args) throws IOException
	{
		// I think this whole block should be in a loop in the Main Activity of the app.  The loop won't stop until app is closed.
		String conversationString = "Whatever conversation text with had a drink or ran over a stop sign got from voice recognizer.";

		LawyerAppMaster appUtils = new LawyerAppMaster();
		
		System.out.println("This is the law relevalt to conversation: ");
		System.out.println( appUtils.curConversation.parse(conversationString) );
		
	}*/
}










