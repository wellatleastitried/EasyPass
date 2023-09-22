package com.walit.pass;

public class Exec {
    public static void main(String[] args) {
		Console c = new Console();
		if (args.length > 0) {
			switch (args[0]) {
				case "--console", "-c" -> c.run();
				case "--help", "-h" -> System.out.println("""
					
					Welcome to the help menu for EasyPass!
					
					If no argument is passed to the program, the full application will launch.
					
					Commands:
						--help           -> Bring up the help menu
						-v, --version    -> Output version information and exit
						-c, --console    -> Starts the command line interface
					
					""");
				case "-v", "--version" -> c.getVersionInfo();
				default -> System.out.println("Invalid argument provided. Please try again or use the '--help' command.");
			}
		} else {
			c.callInterface();
		}
		System.exit(0);
	}
}
