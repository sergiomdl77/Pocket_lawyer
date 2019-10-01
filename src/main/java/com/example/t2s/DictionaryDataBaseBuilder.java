package com.example.t2s;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Scanner;

public class DictionaryDataBaseBuilder 
{
    Context context;
	public DictionaryDataBaseBuilder(Context context)
	{
		this.context = context;
	}
	
	public Hashtable<String, KeyWord> build(String filename) throws IOException 
	{
		Hashtable<String, KeyWord> dictionary = new Hashtable<String, KeyWord>();
		Scanner inFile;
		String[][] phrases = new String[10][10];
		String[] indexes = new String[10];
		int phraseCount = 0;
		KeyWord keyWordInfo;
		String[] keyWordsSplit;
		String textLine = "";
		int category = 0;
		
		
		try
		{
            File file = convertInputStreamToFile(context.getAssets().open(filename));
			inFile = new Scanner(file /*new File(filename)*/);
			// while there is a token to read from file
			while (inFile.hasNext())
			{	
				textLine = inFile.nextLine();				//
				keyWordsSplit = textLine.split("\\s+");		//  Reads line with similar keywords
				
				textLine = inFile.nextLine();				//  Reads line with **

				while (!textLine.equals("****"))			// while that nextLine is still part of group of keywords
				{		
					
					if (textLine.equals("**"))					// if it found a new category
					{
						indexes[category] = inFile.nextLine();	// reads its corresponding subsection
						
						textLine = inFile.nextLine();			// starts storing phrases for that category
						
						while (!textLine.equals("****") && !textLine.equals("**")) 	// so, while not end of category (**)
						{															//
							phrases[category][phraseCount] = textLine;				// stores a phrase in current
							phraseCount++;											// category of phrases
							textLine = inFile.nextLine();
						}
						
					
					}
					
					category++;									// sets variables for a new category
					phraseCount = 0;							// with a new set of phrases
				}												

				keyWordInfo = new KeyWord(indexes, phrases);	//
				for (String kw : keyWordsSplit){				// once there is no more categories 
					dictionary.put(kw, keyWordInfo);			// creates new entries for dictionary (one close of the
				}												// element just built for each keyword in this group

				phrases = new String[10][10];	// creating pointer to a new array of phrases
				indexes = new String[10];		// creating pointer to a new array of indexes
				
				category = 0;				// sets variable for a new set of categories of a new group of keywords
			}
			
			inFile.close();
		}
		
		catch(IOException e)
		{
			System.out.println(e.toString());
			e.printStackTrace();			
	
		}
		
		return dictionary;
	}

	private File convertInputStreamToFile(InputStream is) throws IOException {
		FileOutputStream fos = null;
		File file = new File(context.getCacheDir()+"/dictionaryTextFile.txt");

		try {
			byte[] data = new byte[2048];
			int nbread = 0;
			fos = new FileOutputStream(file);
			while((nbread=is.read(data))>-1){
				fos.write(data,0,nbread);
			}
		}
		catch (Exception ex) {
			System.out.println("Error converting input stream to file object\n");
		}
		finally{
			if (fos!=null){
				fos.close();
			}
		}

		return file;
	}

	/*public static void main(String[] args) throws IOException
	{
        String dictionaryFileName = "C:\\Users\\nyzad\\AndroidStudioProjects\\T2S\\app\\src\\main\\java\\com\\example\\t2s\\dictionaryTextFile.txt";
        DictionaryDataBaseBuilder builder = new DictionaryDataBaseBuilder();        
        builder.build(dictionaryFileName);
	}*/
}
























