package com.example.t2s;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class ConversationParser
{
	private Hashtable<String, String> lawDB;
	private Hashtable<String, KeyWord> dictionaryDB;
	
	public ConversationParser(Hashtable<String,String> lDB, Hashtable<String, KeyWord> dDB)
	{
		lawDB = lDB;
		dictionaryDB = dDB;
	}
	
	
	
	// This method gets a String (conversationString) as a parameter and looks for the VA traffic
	// law subsections and returns them as String value.
	public String parse(String conversationString)
	{
		String relevantLaw = "";
		String currentToken = "";
		KeyWord keyWordInfo;
		TreeSet<String> relevantIndexesTree = new TreeSet<String>();
		String[] indexes;
		int counter;
		
		StringTokenizer ourTokenizer = new StringTokenizer(conversationString);
		 
		// Reference:  https://howtodoinjava.com/java/string/4-ways-to-split-tokenize-strings-in-java/#StringTokenizer
		while (ourTokenizer.hasMoreTokens())
		{
		    currentToken = ourTokenizer.nextToken();
	
	    	keyWordInfo = dictionaryDB.get(currentToken);
	    	
		    if (keyWordInfo != null)
		    {
		    	indexes = keyWordInfo.phraseMatch(conversationString);  // looks for any possible phrases that are 
		    															// part of the keyword's category and returns all
		    															// relevant law codes in the array (indexes)
		    	counter = 0;
			    while (indexes[counter] != null)		// goes through all indexes returned by keyWordInfo.phraseMatch()
			    {
			    	relevantIndexesTree.add(indexes[counter]);	// we use a tree to store the indexes so that they
			    	counter++;									// get sorted, and duplicates eliminated.
			    }
		    }   
		}
		 
		Iterator<String> treeIterator = relevantIndexesTree.iterator();
	    while (treeIterator.hasNext())		// traverses the tree of indexes and collects law text from each relevant index
	    {
	    	String idx = treeIterator.next();
	    	
	    	relevantLaw = relevantLaw + lawDB.get(idx);
	    }
	    
		return relevantLaw;
	}
	
	/*public static void main(String[] args) throws IOException
	{
        String lawFileName = "C:\\Users\\nyzad\\AndroidStudioProjects\\T2S\\app\\src\\main\\java\\com\\example\\t2s\\lawTextFile.txt";
        String dictionaryFileName = "C:\\Users\\nyzad\\AndroidStudioProjects\\T2S\\app\\src\\main\\java\\com\\example\\t2s\\dictionaryTextFile.txt";

        LawDataBaseBuilder ldbBuilder = new LawDataBaseBuilder();
		DictionaryDataBaseBuilder ddbBuilder = new DictionaryDataBaseBuilder();
		
		Hashtable<String, String> lawDB = ldbBuilder.build(lawFileName);
		Hashtable<String, KeyWord> dictionaryDB = ddbBuilder.build(dictionaryFileName);
		
		ConversationParser curConversation = new ConversationParser(lawDB, dictionaryDB);
		
		String conversationString = "Whatever conversation text with had a drink or ran over a stop sign got from voice recognizer.";
		String foundLaw = curConversation.parse(conversationString);
		
		System.out.println("This is the law relevalt to conversation: ");
		System.out.println(foundLaw);
		
	}*/
}
