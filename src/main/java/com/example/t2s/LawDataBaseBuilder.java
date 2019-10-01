package com.example.t2s;

import android.content.Context;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LawDataBaseBuilder
{
    Context context;
	public LawDataBaseBuilder(Context context)
	{
		this.context = context;
	}
			
	public Hashtable<String, String> build(String fileName) throws FileNotFoundException 
	{
		Hashtable<String, String> lawDB = new Hashtable<String, String>(); 

		String lawText = "";
		int starCount = 0;
		String subSectionCode = "";
		char character = 0;
		
		try
		{	// The next 5 lines from:    https://stackoverflow.com/questions/811851/how-do-i-read-input-character-by-character-in-java
            InputStream iS = context.getAssets().open(fileName);
			BufferedReader reader = new BufferedReader(
				    new InputStreamReader( iS/*context.openFileInput(fileName) /*new FileInputStream(fileName)*/, Charset.forName("UTF-8")));
				int c;
				while((c = reader.read()) != -1)
				{
					character = (char) c;
				  
					if (c == '*')     // every time we find a star (*) we need to check for 4 stars in a row
					{	
						starCount = 1;
						boolean stillStars = true;
						while (character != -1 && starCount < 4 && stillStars)
						{
							c = reader.read();
							character = (char)c;
							if (character == '*')
								starCount++;
							else
							{
								stillStars = false;   // found a non star character before finding 4 in a row
								
								for (int i=0; i<starCount; i++)  // so we append the stars we had already read to the lawText
									lawText = lawText + '*';
								lawText = lawText + character;	// and also append to it the last character read that is not star
							}
						}
							
						if (starCount == 4)   // If it has read the sequence **** we start getting the key
						{
							starCount = 0;
							character = (char)reader.read();
							
//							// managing hash table:  https://www.geeksforgeeks.org/hashtable-get-method-in-java/
							lawDB.put(subSectionCode, lawText);
							lawText = "";
							subSectionCode = "";
							
							
							while (character != ' ' && c != -1)  // extracting the subSectionCode for this subsection of VA code
							{
								subSectionCode = subSectionCode + character;
								c = reader.read();
								character = (char)c;
							}
							
							// Trimming the the last '.' off the subSectionCode
							subSectionCode = subSectionCode.substring(0, subSectionCode.length()-1);
							lawText = subSectionCode + "\n";
						}
					
					// now we get the text from that subsection of the Traffic Code
					}
					
					else      //  adds one more character to the subsection of the VA code being built
						lawText = lawText + character;
					
					/******************************************************************************
					 AT THIS POINT WE HAVE THE KEY subSectionCode AND THE SUBSECTION TEXT lawText
					 *       so now we build element for hash table lasDB and insert it in it
					 *****************************************************************************/
//					System.out.println("Key: " + subSectionCode);
//					System.out.println("Text: " + lawText);

					
					
					
		        } 
				
				reader.close();
			
		}		

		catch(IOException e)
		{
			System.out.println(e.toString());
			e.printStackTrace();			

		}
		
		
		return lawDB;
	}
	
	/*public static void main(String[] args) throws FileNotFoundException
	{
        String lawFileName = "C:\\Users\\nyzad\\AndroidStudioProjects\\T2S\\app\\src\\main\\java\\com\\example\\t2s\\lawTextFile.txt";
        LawDataBaseBuilder builder = new LawDataBaseBuilder();        
        Hashtable<String,String> ht = builder.build(lawFileName);
        String s = ht.get("46.2-300");

	}*/
	
}

