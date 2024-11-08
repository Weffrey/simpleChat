// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.common.*;
import edu.seg2105.edu.server.backend.EchoServer;

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
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    if (msg.toString().startsWith("SERVER MSG>")) {
		System.out.println(msg.toString());
	}
	else 
	{
		clientUI.display(msg.toString());
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
    	if(message.startsWith("#")) {
    		handleUserCommand(message);
    	} else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  
  private void handleUserCommand(String commandLine) {
	  try {
		  if(commandLine.equals("#quit")) 
		  {
			  quit();
		  }
		  
		  else if(commandLine.equals("#logoff")) 
		  {
			  closeConnection();
		  }
		  
		  else if(commandLine.startsWith("#sethost")) 
		  {
			  if(!(isConnected())) {
				  String[] command = commandLine.split(" ");
			        if (command.length > 1) {
			            String host = command[1];
			            setHost(host);  // Call setHost with the specified host
			        } else {
			            System.out.println("ERROR: No host specified.");
			        }
			  }else {
				  System.out.println("You are already connected");
			  }
		  }
		  
		  else if(commandLine.startsWith("#setport")) 
		  {
			  if(!(isConnected())) {
				  String[] command = commandLine.split(" ");
			        if (command.length > 1) {
			        	try {
			                int port = Integer.parseInt(command[1]);
			                setPort(port);  // Assuming you have a setPort method
			            } catch (NumberFormatException e) {
			                System.out.println("ERROR: Invalid port number.");
			            }
			        } else 
			        {
			            System.out.println("ERROR: No port specified.");
			        }
			  } else 
			  {
				  System.out.println("You are already connected");
			  }
		  }
		  
		  else if(commandLine.equals("#login")) 
		  {
			  if(!(isConnected())) {
				  openConnection();
			  } else {
		            System.out.println("ERROR: Already connected.");
			  }
		  }

		  else if(commandLine.equals("#gethost")) 
		  {
			  System.out.println("Host: " + getHost());
			  
		  }
		  
		  else if(commandLine.equals("#getport")) 
		  {
			  System.out.println("Port: " + getPort());
		  }
		  
		  else {
			  
			  System.out.println("Command not found");
		  }
	} catch (Exception e) {
		 	System.out.println("ERROR: Can not execute command" + e.getMessage());
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
  
  //Exercise 1 Client Side a) *****************************************************

  /**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
	  clientUI.display("Server has shut down");
	  quit();
	}

  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
		clientUI.display("Connection Closed");
		
	}
}
//End of ChatClient class
