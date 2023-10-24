package com.walit.pass;

public class Main {
    public static void main(String[] args) {
		CLI c = new CLI();
		// Once interface is completed, change to a prompt that guides user to help menu with no args
		if (args.length > 0) {
			switch (args[0]) {
				case "--console", "-c" -> c.run();
				case "--help", "-h" -> System.out.println("""
					
					Welcome to the help menu for EasyPass!
					
					If no argument is passed to the program, the full UI application will launch.
					
					Commands:
						--help           -> Bring up the help menu
						-v, --version    -> Output version information and exit
						-c, --console    -> Starts the command line interface
					
					""");
				case "-v", "--version" -> c.getVersionInfo();
				default -> System.out.println("\nInvalid argument provided. Please try again or use the '--help' command.\n");
			}
		}
		else {
			c.callInterface();
		}
		System.exit(0);
	}
}