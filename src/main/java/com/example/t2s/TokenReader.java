package com.example.t2s;


import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TokenReader
{
	
		
	public static void main(String[] args) throws FileNotFoundException 
	{
                
		String fileName = ("/home/sergio/eclipse-workspace/PocketLawyerComponents/src/lawTextFile.txt"); 

		String lawText = "";
		int starCount = 0;
		String subSectionCode = "";
		char character = 0;
		
		try
		{	// The next 5 lines from:    https://stackoverflow.com/questions/811851/how-do-i-read-input-character-by-character-in-java
			BufferedReader reader = new BufferedReader(
				    new InputStreamReader( new FileInputStream(fileName), Charset.forName("UTF-8")));
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
							character = (char)reader.read();
							if (character == '*')
								starCount++;
							else
							{
								stillStars = false;   // found a non star character before finding 4 in a row
								
								for (int i=0; i<starCount; i++)  // so we copy the 
									lawText = lawText + '*';
								lawText = lawText + character;
							}
						}
							
						if (starCount == 4)   // If it has read the sequence **** we start getting the key
						{
							starCount = 0;
							character = (char)reader.read();
							
							while (character != ' ')  // extracting the subSectionCode for this subsection of VA code
							{
								subSectionCode = subSectionCode + character;
								character = (char)reader.read();
							}
								
						}
					
					// now we get the text from that subsection of the Traffic Code
					}
					
					else      //  adds one more character to the subsection of the VA code being built
						lawText = lawText + character;
					
					/******************************************************************************
					 AT THIS POINT WE HAVE THE KEY subSectionCode AND THE SUBSECTION TEXT lawText
					 *       so now we build element for hash table lasDB and insert it in it
					 *****************************************************************************/
					
					Hashtable<String, String> lawDB = new Hashtable<String, String>(); 
					lawDB.put(subSectionCode, lawText);
					lawText = "";
					subSectionCode = "";
					
		        }       			
			
				//System.out.println(lawText);
			
		}		

		catch(IOException e)
		{
			System.out.println(e.toString());
			e.printStackTrace();			

		}
		

		
	}
	
	
}
