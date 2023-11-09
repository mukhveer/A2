package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.lang.ArithmeticException;
import ocsf.server.*;

import java.io.IOException;

import edu.seg2105.client.common.*;



/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */



public class EchoServer extends AbstractServer 
{
  String loginKey ="loginID";
	ChatIF serverUI;
	
	ConnectionToClient client;
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverConsole) throws IOException
  {
    super(port);
    this.serverUI = serverConsole;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  { 
	  String message = (String)msg;
	  if (!client.getInfo("isLoggedIn").equals(Boolean.TRUE))
	  {
	   if(message.startsWith("#login")) {
		  String loginId = message.split(" ")[1];
		  client.setInfo(loginKey, loginId);
		  client.setInfo("isLoggedIn", Boolean.TRUE);
	  
        }
	  
	    else {
		 
		  String newMesg = (String) client.getInfo(loginKey) + message;
		  this.sendToAllClients((Object)newMesg);
	    }  
      }
	  
	  else if(message.startsWith("#login")) {
		  try {
              client.sendToClient("Error: Already logged in. Cannot perform another login.");
              client.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
	  }
	  
  }
  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Implementing Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client Connected " + client);
	  
  }

  /**
   * Implementing Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(
    ConnectionToClient client) { System.out.println("Client is disconnected " + client);}
  
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
 * @throws IOException 
   */
  public void handleMessageFromServerUI(String message) throws IOException
  {
    try
    {
    	if(message.startsWith("#"))
    	{handleCommand(message);}
    	else { 
    		String newMessage = "Server Mesg>" + message ;
    		
    	    serverUI.display(newMessage);
    		sendToAllClients(newMessage);}
    }
    catch(IOException e)
    {
      serverUI.display
        ("Could not process the request, server not reponding , Terminating Server");
    
      quit();
    }
  }
  
  /**
   * This method terminates the server.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public void handleCommand(String msg) throws IOException {
	  if(msg.equals("#quit")) {
		quit(); 
	  }
	  else if(msg.equals("#stop")) {
		  stopListening()	;	
		  
	  }
	  else if(msg.equals("#close")) {
		  close();
		  
	  }
	  else if(msg.equals("#setport < port >")) {
		  String[] parts = msg.split(" ");
		  if (parts.length >= 2) {
		        String action = parts[0];
		        if (action.equals("#setport")) {
		            if (!isListening()) {
		                // Server is closed 
		                if (parts.length >= 3) {
		                    int newPort = Integer.parseInt(parts[2]);
		                    setPort(newPort);
		                
		                } else {
		                    // Display an error message for missing arguments
		                	serverUI.display( "Correct Usage:#setport < port >");
		                }
		            }
		            
		            else {
		                // Server is running on other port
		            	serverUI.display( "Server port is already set. Log out to set the port again.");
		            }
		        } 
		      
		  }
	  }
	  
	  
	
	  
	  else if(msg.equals("#start")) {
		  if (!isListening()) {
			  listen();
			  serverStarted();
		  }
		 
		  
	  }
	 
	  else if(msg.equals("#getport")) {
		  if (isListening()) {
			  serverUI.display( "You are connected to " + getPort());
		  }
		  else {
			 serverStopped();
		  }
		  
	  }
	  
	  
  }


  /**
   * Hook method called when the server is clased.
   * The default implementation does nothing. This method may be
   * overriden by subclasses. When the server is closed while still
   * listening, serverStopped() will also be called.
   */
  protected void serverClosed() {}
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /*
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
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  */
}
//End of EchoServer class
