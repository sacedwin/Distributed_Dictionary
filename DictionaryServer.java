/* Name: Sachin Antony Edwin Earayil
 * Student Id: 947044
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DictionaryServer {

	public static void main(String[] args) {
		
		if(args.length != 2)
		{
			System.err.println("Error! Port number or dictionary path missing!");
			System.exit(1);
		}
		
		int portNumber = Integer.parseInt(args[0]);
		ServerSocket serverListeningSocket = null;
		try {
			serverListeningSocket = new ServerSocket(portNumber);		//socket starts listening on the port provided
			System.out.println("Server is now running!");
			while(true)
			{		//server keeps waiting for connections till its terminated
				Socket clientSocket = serverListeningSocket.accept();
					//threads are assigned for each client new client
				Thread t = new Thread(new DictionaryActionThread(clientSocket, args[1]));
				t.start();						
			}		
		}
		catch(IOException e)
		{
			System.err.println("Error! Unable to prepare socket!");
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
