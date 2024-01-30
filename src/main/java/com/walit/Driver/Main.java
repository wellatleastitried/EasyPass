package com.walit.Driver;

import com.walit.Application.CLI;

public class Main {
    public static void main(String[] args) {
		CLI consoleInstance = new CLI();
		int argCount = args.length;
		if (argCount == 0) {
			System.out.println("UI is still in development, use '--help' to view available commands!");
			System.exit(0);
		}
		for (int i = 0; i < argCount; i++) {
			if (args[i].equals("--console") || args[i].equals("-c")) {
				consoleInstance.run();
			}
			else if (i == 0 && args[i].equals("--help")) {
				System.out.println("""
					
					Welcome to the help menu for EasyPass!
					
					If no argument is passed to the program, the full UI application will launch.
					
					Commands:
						--help           -> Bring up the help menu
						-v, --version    -> Output version information and exit
						
						-c, --console    -> Starts the command line interface
						
						-a, --add        -> Add a username and password to be stored
							Usage: --add 'username password'
							
						-g, --generate   -> Generate a password
						-d, --display    -> Display all usernames and passwords
						
					""");
			}
			else if (i == 0 && (args[i].equals("--add") || args[i].equals("-a"))) {
				if (i + 1 < argCount) {
					String data = args[i + 1];
					if (data.contains(" ")) {
						String[] userPass = data.split(" ");
						if (userPass.length == 2 && userPass[0].length() > 0 && userPass[1].length() > 0) {
							consoleInstance.storeInformation(userPass);
						} else {
							System.out.println("""
           								
							There was a syntax error in your command!
							Usage for --add:
							
							easypass --add 'username password'
	
							""");
						}
					} else {
						System.out.println("""
           
							There was a missing space in your command!
							Usage for --add:
							
							easypass --add 'username password'
	
							""");
					}
				}
				else {
					System.out.println("\nNo arguments were provided to '--add'\n");
				}
				i++;
			}
			else if (i == 0 && (args[i].equals("--version") || args[i].equals("-v"))) {
				System.out.println(consoleInstance.getVersionInfo());
			}
			else if (i == 0 && (args[i].equals("--generate") || args[i].equals("-g"))) {
				consoleInstance.getParams();
				consoleInstance.checkParams();
				System.out.println("Your password is: " + consoleInstance.quickGenerate());
			}
			else if (args[i].equals("--display") || args[i].equals("-d")) {
				consoleInstance.extractInfoFromList();
			}
			else {
				System.out.println("\nAn illegal argument was input, use '--help' to display the help menu!\n");
			}
		}
		System.exit(0);
	}
}