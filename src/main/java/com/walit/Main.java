package com.walit;

import com.walit.Application.CLI;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
		CLI consoleInstance = new CLI();
		int argCount = args.length;
		if (argCount == 0) consoleInstance.callInterface();
		String argument = args[0];
		if (argCount > 1 && !(argument.equals("--add") || argument.equals("--get") || argument.equals("--test"))) {
			System.out.println("\nOnly one argument allowed!\n");
			exit(0);
		}
		else {
			if (argCount > 2) {
				System.out.println("\nOnly two arguments allowed when using the '" + argument + "' command.\n");
				exit(0);
			}
			switch (args[0]) {
				case "--console" -> consoleInstance.run();
				case "--help" -> System.out.println("""
						
						Welcome to the help menu for EasyPass!
						
						If no argument is passed to the program, the full UI application will launch.
						
						Commands:
							--help       -> Bring up the help menu
							--version    -> Output version information and exit
						
							--console    -> Starts the command line interface
						
							--add        -> Add a username and password to be stored
								Usage: --add 'username password'
						
							--generate   -> Generate a password
							--display    -> Display all usernames and passwords
						
							--get        -> Displays password for given username
								Usage: --get username
						
							--test       -> Tests the strength of the given password
								Usage: --test password
						
						""");
				case "--add" -> {
					if (argCount == 2) {
						String data = args[1];
						if (data.contains(" ")) {
							String[] userPass = data.split(" ");
							if (userPass.length == 2 && !userPass[0].isEmpty() && !userPass[1].isEmpty()) {
								consoleInstance.storeInformation(userPass);
								System.out.println("\n[*] Username and password have been stored.\n");
							}
							else {
								System.out.println("""
									
									There was a syntax error in your command!
									Usage for --add:
									
									easypass --add 'username password'
									
									""");
							}
						}
						else {
							System.out.println("""
								
								There was a missing space in your command!
								Usage for --add:
								
								easypass --add 'username password'
								
								""");
						}
					}
					else {
						System.out.println("""
							
							There was a missing space in your command!
							Usage for --add:
							
							easypass --add 'username password'
							
							""");
					}
				}
				case "--version" -> System.out.println(consoleInstance.getVersionInfo());
				case "--generate" -> {
					consoleInstance.getParams();
					consoleInstance.checkParams();
					System.out.println("[*] Your password is: " + consoleInstance.quickGenerate());
				}
				case "--display" -> consoleInstance.extractInfoFromList();
				case "--get" -> {
					if (argCount == 2) {
						consoleInstance.findNamePassCombos(args[1]);
					}
					else {
						System.out.println("""
							
							There were an incorrect number of arguments in your command.
							Usage for --get:
							
							easypass --get username
							
							""");
					}
				}
				case "--test" -> {
					if (argCount == 2) {
						consoleInstance.strengthTest(args[1]);
					} else {
						System.out.println("""
							
							There were an incorrect number of arguments in your command.
							Usage for --test:
							
							easypass --test password
							
							""");
					}
				}
				default ->
						System.out.println("\nAn illegal argument was input, use '--help' to display the help menu!\n");
			}
		}
		exit(0);
	}
}