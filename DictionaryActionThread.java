/* Name: Sachin Antony Edwin Earayil
 * Student Id: 947044
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.Reader;
import java.net.Socket;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DictionaryActionThread extends Thread {
	
	private Socket clientSocket;
	private String filePath;
	private DataInputStream reader;
	private DataOutputStream writer;
	
	public DictionaryActionThread(Socket clientSocket, String filePath )
	{
		this.clientSocket = clientSocket;
		this.filePath = filePath;		
	}
	
	@Override
	public void run()
	{
		try {
			reader = new DataInputStream(clientSocket.getInputStream());
			writer = new DataOutputStream(clientSocket.getOutputStream());
			String clientMsg = null;
			String action;
			
			while((clientMsg = reader.readUTF()) != null)
			{
				JSONObject dictionarySet = getDictionary();

				StringTokenizer clientRequest = new StringTokenizer(clientMsg," ");
				action = clientRequest.nextToken();
				String word = clientRequest.nextToken();
				String wordMeaning = null;
				
				if(dictionarySet != null)
					wordMeaning = searchMeaning(word,dictionarySet);
				//if word meaning does not exist, then the word does not exist

				if(action.equals("query"))
				{
					if(wordMeaning != null)
					{			
						writer.writeUTF(wordMeaning);
						writer.flush();		//returns the meaning of the word
					}
					else
					{
						String m = "Dictionary doesnt contain this word!";
						writer.writeUTF(m + "\n");
						writer.flush();
					}
				}
				else if(action.equals("add"))
				{
					if(wordMeaning != null)
					{
						writer.writeUTF("Word already exists!");
						writer.flush();
					}
					else
					{
						String tempNewMeaning = clientRequest.nextToken("\n").trim();
						dictionarySet = AddWord(word, tempNewMeaning, dictionarySet);
									//adds new entry into the dictionary
						writer.writeUTF("New word added to dictionary!");
						writer.flush();
					}
				}
				else if(action.equals("remove"))
				{
					if(wordMeaning != null)
					{
						dictionarySet = RemoveWord(word,dictionarySet);
									//removes and entry from the dictionary
						writer.writeUTF("Word removed from dictionary!");
						writer.flush();
					}
					else
					{
						writer.writeUTF("Dictionary does not contain this word!");
						writer.flush();
					}
				}
				else
				{
					writer.writeUTF("Invalid command!");
					writer.flush();
				}
			}

		}
		catch(FileNotFoundException e)
		{
			System.err.println("Could not locate the file!");
		}
		catch(ParseException e)
		{
			System.err.println("Error! Parsing not a success!");
		}
		catch(IOException e)
		{
			System.out.println("Error in transfer of information! A client may have disconnected! ");
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
		}
		finally
		{			//the socket is attempted to close when activities end
			if(clientSocket != null)
			{
				try
				{
					clientSocket.close();
				}
				catch(IOException e)
				{
					System.err.println("Error! Closing of socket not proper!");
				}	
			}			
		}	
	}
	private void writeToFile(JSONObject dictionarySet)		//function to write the data set back to the file
	{			
		FileWriter file;
		try
		{
			file = new FileWriter(filePath);
			file.write(dictionarySet.toJSONString());
			file.flush();
			file.close();
		}
		catch(IOException e)
		{
			System.err.println("Error in writing to file!");
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized JSONObject AddWord(String word, String newMeaning, JSONObject dictionarySet)
	{				//function to add a new set of word and meaning to the data set
		dictionarySet.put(word, newMeaning);	
		writeToFile(dictionarySet);

		return dictionarySet;
	}
	
	private synchronized JSONObject RemoveWord(String word, JSONObject dictionarySet)
	{				//function to remove a set of word and meaning from the data set
		dictionarySet.remove(word);
		writeToFile(dictionarySet);

		return dictionarySet;
	}
	
	private String searchMeaning(String word, JSONObject dictionarySet)
	{				//function to find and return the meaning of a word if it exists
		if(dictionarySet.containsKey(word))
		{
			String wordMeaning = (String) dictionarySet.get(word);
			return wordMeaning;
		}
		else
		{
			return null;
		}
	}
	private JSONObject getDictionary() throws IOException, ParseException, FileNotFoundException
	{				//function to retrieve the data from the file and store it into a data set
	JSONParser parser = new JSONParser();
	JSONObject dictionarySet = null;
	
		Reader in = new FileReader(filePath);
		if(in.ready())
		{
			dictionarySet = (JSONObject) parser.parse(in);
		}
		in.close();

	return dictionarySet;
	}
}
