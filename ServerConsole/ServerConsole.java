package ServerConsole;
import java.io.*;

import java.util.Scanner;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;


public class ServerConsole implements ChatIF {
	//Class variables *************************************************
	  
	  /**
	   * The default port to listen on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  /**
	   * The instance of the server that creates server console
	   */
	  
	  EchoServer server;
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromServerConsole; 
	  
	//Constructors ****************************************************

	  /**
	   * Constructs an instance of the ServerConsole UI.
	   *
	   * @param port The port to connect on.
	   */
	  public ServerConsole( int port) 
	  {
	    try 
	    {
	      server = new EchoServer( port, this);
	      
	      
	    } 
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Terminating server.");
	      System.exit(1);
	    }
	    
	    // Create scanner object to read from console
	    fromServerConsole = new Scanner(System.in); 
	  }
	  
	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromServerConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }



	  //Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of 
	   * the server instance (there is no UI in this phase).
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
	  public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		ServerConsole sv = new ServerConsole(port);
		sv.accept();
	   // server = new EchoServer(port,sv);
	    
	   
	  }

	@Override
	public void display(String message) {
		System.out.println(message);
	
	}

}
