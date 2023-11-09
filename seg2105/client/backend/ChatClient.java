// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID ,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
  
  public String getID() {
	  return this.loginID;
  }
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }
  public void handleCommand(String msg) throws IOException {
	  if(msg.equals("#quit")) {
		 quit(); 
	  }
	  else if(msg.equals("#logoff")) {
		  closeConnection();		  
	  }
	  else if(msg.equals("#sethost < host >")) {
		  String[] parts = msg.split(" ");
		  if (parts.length >= 2) {
		        String action = parts[0];
		        if (action.equals("#sethost")) {
		            if (!isConnected()) {
		                // Client is not logged in; allow setting the host
		                if (parts.length >= 3) {
		                    String newHost = parts[2];
		                    setHost(newHost);
		                
		                } else {
		                    // Display an error message for missing arguments
		                	clientUI.display( "Usage: #sethost < host >");
		                }
		            }
		            
		            else {
		                // Client is already logged in; display an error message
		            	clientUI.display( "You are already logged in. Log out to set the host.");
		            }
		        } 
		      
		  }
	  }
	  
	  
	  else if (msg.equals("#setport <port>")) {
		  String[] parts = msg.split(" ");
		  if (parts.length >= 2) {
		        String action = parts[0];
		        if (action.equals("#setport")) {
		            if (!isConnected()) {
		                // Client is not logged in; allow setting the host
		                if (parts.length >= 2) {
		                    String newPort = parts[1];
		                    setPort(Integer.parseInt(newPort));
		                
		                } else {
		                    // Display an error message for missing arguments
		                	clientUI.display( "Correct Usage: #setport <port>");
		                }
		            }
		            
		            else  {
		                // Client is already logged in; display an error message
		            	clientUI.display( "You are already logged in. Log out to set the port.");
		            }
		        } 
		      
		  }
		  
	  }
	  
	  else if(msg.equals("#login")) {
		  if (!isConnected()) {
			  openConnection();
			  clientUI.display( "You are logged in.");
		  }
		  else {
			  clientUI.display( "You are already logged in.");
		  }
		  
	  }
	  else if(msg.equals("#gethost")) {
		  if (isConnected()) {
			  clientUI.display( "You are connected to " + getHost());
		  }
		  else {
			  clientUI.display( "You are not connected to server" );
		  }
		  
	  }
	  else if(msg.equals("#getport")) {
		  if (isConnected()) {
			  clientUI.display( "You are connected to " + getPort());
		  }
		  else {
			  clientUI.display( "You are not connected to server" );
		  }
		  
	  }
	  
	  
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#"))
    	{handleCommand(message);}
    	else { sendToServer(message);}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Implements the Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
		clientUI.display("Connection to the client has been closed");
		
	}
  
  @Override
  /**
	 * Implements Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
 * @throws IOException 
	 */
	protected void connectionEstablished()  {
	  String message = "#login <" + getID() + ">";
	  try {
		sendToServer(message);
		clientUI.display("Connection is established between client and server");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		connectionException(e);
	}
	 
	}

	/**
	 * Implements Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
		clientUI.display("Server has shutdown");
		quit();
	}
}
//End of ChatClient class
