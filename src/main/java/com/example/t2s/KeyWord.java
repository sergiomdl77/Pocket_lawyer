package com.example.t2s;

public class KeyWord
{
	private String[] indexes;
	private String[][] phrases;
	
	public KeyWord(String[] i, String p[][])
	{
		indexes= i;
		phrases = p;
	}

	public String[] getIndexes()
	{
		return indexes;
	}
	
	public String[][] getPhrases()
	{
		return phrases;
	}
	
	public String[] phraseMatch(String conversationString)
	{
		int phraseCounter = 0;
		int indexCounter = 0;
		String[] idxArray = new String[10];
		int idxCounter = 0;
		
		
		while (indexes[indexCounter] != null)
		{
			while (phrases[indexCounter][phraseCounter] != null)
			{
				if ( conversationString.indexOf( phrases[indexCounter][phraseCounter] ) != -1 ) // if a phrase matches
				{
					idxArray[idxCounter] = indexes[indexCounter];
				}
				
				phraseCounter++;
			}
			phraseCounter = 0;
			indexCounter++;
		}
		
		return idxArray;
	}
	

}
