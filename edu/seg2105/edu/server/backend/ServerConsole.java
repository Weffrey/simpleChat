package edu.seg2105.edu.server.backend;




import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;


public class ServerConsole implements ChatIF {

	private EchoServer server;
	private Scanner fromConsole;

	public ServerConsole(EchoServer Server) {
		this.server = Server;
		fromConsole = new Scanner(System.in);
	}

	public void accept() {
		try {

			String message;

			while (true) {
				message = fromConsole.nextLine();
				if (message.startsWith("#")) {
					handleCommandLine(message);
				} else {
					
					server.sendToAllClients(message);
					display(message);
				}
			}
		} catch (Exception ex) {
			display("Unexpected error while reading from console!");
		}
	}

	private void handleCommandLine(String commandLine) {
		String[] commandString = commandLine.split(" ");
		switch (commandString[0]) {
		// Causes the server to quit gracefully.
		case "#quit":
			display("Shutting down the server ");
			try {
				server.close();
			} catch (IOException e) {
				display("ERROR: can not quit");
			}
			System.exit(0);
			break;
		// Causes the server to stop listening for new clients
		case "#stop":
			if (server.isListening()) {
				server.stopListening();
				display("Server stopped listening for new clients.");
			} else {
				display("Server is already stopped.");
			}
			break;
		// Causes the server not only to stop listening for new clients, but also to
		// disconnect
		// all existing clients.
		case "#close":
			try {
				server.close();
			} catch (IOException e) {
				display("ERROR: can not close server");
			}
			display("Server closed and all clients disconnected.");
			break;
		// Calls the setPort method in the server. Only allowed if the server is closed.
		case "#setport":
			if (!server.isListening()) {
				String[] setMessage = commandLine.split(" ");
				if (setMessage.length > 1) {
					int port = Integer.parseInt(setMessage[1]);
					server.setPort(port);
					display("Port : " + port);
				} else {
					display("ERROR: No port specified.");
				}
			} else {
				display("ERROR: Server can not be open when setting port.");
			}

			break;
		// Causes the server to start listening for new clients. Only valid if the
		// server is
		// stopped.
		case "#start":

			if (!server.isListening()) {
				try {
					server.listen();
				} catch (IOException e) {
					display("ERROR: socket not created properly");
				}
				display("Server started listening for new clients.");
			} else {
				display("Server is already listening.");
			}

			break;
		// Displays the current port number
		case "#getport":
			display("Port: " + server.getPort());
			break;

		default: // Default message, if the "#" command was not a valid command
			display("ERROR: Invalid command line");
		}
	}

	@Override
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);

	}

}
