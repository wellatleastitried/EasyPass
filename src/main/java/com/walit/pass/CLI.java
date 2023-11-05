package com.walit.pass;

import java.io.*;

import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * This program allows the user to generate, store, test, and view passwords by implementing an easy-to-use
 * console interface.
 *
 * @author Jackson Swindell
 */
class CLI implements Runner {

	public int lengthOfPassword = -1;
	public int specialChars = -1;
	public int capitals = -1;
	public int numbers = -1;
	private final Logger logger = Logger.getLogger("ManagerLog");
	public Scanner s = new Scanner(System.in);

	protected void callInterface() { new UI(logger).run(); }

	/**
	 * Prints the version information of the program.
	 */
	protected String getVersionInfo() {
		String version;
		try {
			Parsed parser = new Parsed();
			version = parser.getVersion();
			System.out.println(version);

		}
		catch (Exception ignored) {
			version = "Error parsing version info.";
			System.err.println(version);
		}
		return version;
	}
	/**
	 * Run function that starts the program's execution.
	 */
	public void run() {
		String os = System.getProperty("os.name");
        os = os.toLowerCase();
        if (!(os.contains("win"))) {
        	System.err.println("Not a windows machine.");
        	System.exit(1);
        }
		initializeMissingFilesForProgram();
		File logFile = new File(logFilePath);
		FileHandler fH;
		try {
			if (logFile.exists() && logFile.isFile()) {
				new FileWriter(logFile, false).close();
			}
			fH = new FileHandler(logFilePath, true);
			while (logger.getHandlers().length > 0) {
				logger.removeHandler(logger.getHandlers()[0]);
			}
			logger.addHandler(fH);
			fH.setLevel(Level.INFO);
			XMLFormatter xF = new XMLFormatter();
			fH.setFormatter(xF);
			logger.log(Level.INFO, "Successful startup.");
		}
		catch (IOException e) {
			System.out.println("Error in startup.\n\nPlease restart program.");
		}
		poundLine();
		printLogo();
		poundLine();
		int x = displayMenu();
		System.out.println();
		while (x != 7) {
			switch (x) {
				case 1 -> {
					getParams();
					checkParams();
					String[] temp = getUserInformation();
					getPassIdentifierFromUser(temp);
					dashLine();
					resetParams();
					x = displayMenu();
				}
				case 2 -> {
					findNamePassCombos();
					dashLine();
					x = displayMenu();
				}
				case 3 -> {
					extractInfoFromList();
					dashLine();
					x = displayMenu();
				}
				case 4 -> {
					strengthTest();
					dashLine();
					x = displayMenu();
				}
				case 5 -> {
					System.out.println("""
						Would you like to:
						 - Change an existing password
						 - Remove an existing password
						Enter "c" to change and "r" to remove.
						""");
					boolean choice = getChangeOrRemoveDecision();
					if (choice) changeData();
					else removeData();
					dashLine();
					x = displayMenu();
				}
				case 6 -> {
					String[] tempStr = getPasswordFromUser();
					getPassIdentifierFromUser(tempStr);
					dashLine();
					x = displayMenu();
				}
			}
		}
		shutdown();
		logger.log(Level.INFO, "Successful termination.");
		poundLine();
		System.exit(0);
	}

	/**
	 * Prints logo at startup.
	 */
	private void printLogo() {
		System.out.println("""			
				 ______     __     __________    __________    __     _______   _______
				| _____|   /  \\   | ______\\  \\  /  /|   _  |  /  \\   | ______| | ______|
				| |___    / /_\\\\  | |_____ \\  \\/  / |  |_| | / /_\\\\  | |_____  | |_____
				| ____|  /      \\ |_____  | |    |  |   ___|/      \\ |_____  | |_____  |
				| |_____/   /\\   \\ _____| | |    |  |   |  /   /\\   \\ _____| |  _____| |
				|______/___/  \\___|_______| |____|  |___| /___/  \\___|_______| |_______|
				""");
	}

	/**
	 * Line separator during program runtime.
	 */
	private void dashLine() {
		System.out.println("\n--------------------------------------------------------------------------\n");
	}

	/**
	 * Line separator during program runtime.
	 */
	private void poundLine() {
		System.out.println("\n##########################################################################\n");
	}

	/**
	 * Resets the password parameters to their initial values to prepare for a new password to be generated.
	 */
	public void resetParams() {
		lengthOfPassword = -1;
		specialChars = -1;
		capitals = -1;
		numbers = -1;
	}

	/**
	 * Closes any resources that remain open.
	 */
	@Override
	public void shutdown() { s.close(); }

	/**
	 * Gets the response from the user for whether they want to change or remove a specific password.
	 * @return Returns true if the user chooses 'change' and false if the user chooses 'remove'.
	 */
	@Override
	public boolean getChangeOrRemoveDecision() {
		boolean choiceMade = false;
		boolean isChange = false;
		while (!choiceMade) {
			String ans = s.nextLine().trim().toLowerCase();
			if (ans.equals("c") || ans.equals("change")) {
				isChange = true;
				choiceMade = true;
			}
			else if (ans.equals("r") || ans.equals("remove")) {
				choiceMade = true;
			}
			else {
				System.out.println("\nThat was not a valid option, enter \"c\" or \"change\" for change OR \"r\" or \"remove\" for remove.");
			}
		}
		return isChange;
	}

	/**
	 * Handles changing the user's password by getting the name the password belongs to and allowing them to enter
	 * a new password.
	 */
	@Override
	public void changeData() {
		System.out.println("You chose to change an existing password.");
		String name = getPassIdentifierForChangeOrRemove(0);
		Storage store = new Storage(logger);
		List<String> strings = store.findNameToAlter();
		List<String> acceptedStrings = new ArrayList<>();
		String[] splitStrings;
		if (strings.get(0).equals("There was an error.")) {
			logger.log(Level.WARNING, "Error retrieving file, please restart.");
		}
		else {
			for (String x : strings) {
				splitStrings = x.split(", ");
				splitStrings[0] = splitStrings[0].toLowerCase();
				if (splitStrings[0].equals(name)) {
					acceptedStrings.add(x);
				}
			}
		}
		if (acceptedStrings.size() > 1) {
			int index = 1;
			System.out.println("\nThere were multiple passwords with the same name, which would you like to change?");
			for (String x : acceptedStrings) {
				System.out.println(index + ") " + x);
				index++;
			}
			System.out.print("\nEnter the number that corresponds to the password you would like to change from the list: ");
			String choice = s.nextLine();
			int chosenIndex = Integer.parseInt(choice);
			splitStrings = acceptedStrings.get(chosenIndex - 1).split(", ");
			System.out.println("You chose to change the password: "
					+ splitStrings[1] + " for " + splitStrings[0]
					+ ".\nWhat would you like the new password to be? (ENTER BELOW)");
			String[] temp = getPasswordFromUser();
			String newPass = temp[1];
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(chosenIndex - 1))) {
					index = i;
				}
			}
			strings.set(index, splitStrings[0] + ", " + newPass);
			store.storeNameFromLists(strings);
		}
		else if (acceptedStrings.size() == 1) {
			splitStrings = acceptedStrings.get(0).split(", ");
			System.out.println("You chose to change the password: "
					+ splitStrings[1] + " for " + splitStrings[0]
					+ ".\nWhat would you like the new password to be? (ENTER BELOW)");
			String[] temp;
			String newPass;
			temp = getPasswordFromUser();
			newPass = temp[1];
			int index = -1;
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(0))) {
					index = i;
				}
			}
			strings.set(index, splitStrings[0] + ", " + newPass);
			store.storeNameFromLists(strings);
			System.out.println("\nPassword successfully changed.");
		}
		else {
			System.out.println("\nName unable to be found, enter a valid name.\n");
			changeData();
		}
	}

	/**
	 * Handles removing the user's password by allowing them to enter the name associated with their password.
	 */
	@Override
	public void removeData() {
		System.out.println("You chose to remove an existing password.");
		String name = getPassIdentifierForChangeOrRemove(1);
		Storage store = new Storage(logger);
		List<String> strings = store.findNameToAlter();
		List<String> acceptedStrings = new ArrayList<>();
		String[] splitStrings;
		if (strings.get(0).equals("There was an error.")) {
			logger.log(Level.WARNING, "Error retrieving file, please restart.");
		}
		else {
			for (String x : strings) {
				splitStrings = x.split(", ");
				splitStrings[0] = splitStrings[0].toLowerCase();
				if (splitStrings[0].equals(name)) {
					acceptedStrings.add(x);
				}
			}
		}
		if (acceptedStrings.size() > 1) {
			int index = 1;
			System.out.println("\nThere were multiple passwords with the same name, which would you like to remove?");
			for (String x : acceptedStrings) {
				System.out.println(index + ") " + x);
				index++;
			}
			System.out.print("\nEnter the number that corresponds to the password you would like to remove from the list: ");
			String choice = s.nextLine().trim();
			int chosenIndex = Integer.parseInt(choice);
			splitStrings = acceptedStrings.get(chosenIndex - 1).split(", ");
			System.out.println("You chose to change the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(chosenIndex - 1))) {
					index = i;
				}
			}
			strings.remove(index);
			store.storeNameFromLists(strings);
		}
		else if (acceptedStrings.size() == 1) {
			splitStrings = acceptedStrings.get(0).split(", ");
			System.out.println("You chose to remove the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
			int index = -1;
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(0))) {
					index = i;
				}
			}
			strings.remove(index);
			store.storeNameFromLists(strings);
		}
		else {
			System.out.println("\nName unable to be found, enter a valid name.\n");
			removeData();
		}
	}

	/**
	 * Gets the name that the password being changed or removed belongs to.
	 * @param choice The variable that holds whether the user chose to change or remove their password.
	 * @return Returns a string containing the name that the password belongs to.
	 */
	@Override
	public String getPassIdentifierForChangeOrRemove(int choice) {
		if (choice == 0) {
			System.out.println("Enter the name for the password you would like to change.");
		}
		else if (choice == 1) {
			System.out.println("Enter the name for the password you would like to remove.");
		}
		else {
			logger.log(Level.INFO, "There has been an error that I didn't think was possible.");
		}
		return s.nextLine().trim().toLowerCase();
	}

	/**
	 * Gets the password from the user to be stored.
	 * @return Returns the password in the second index of the two index array. The first slot is for the name, which
	 * will be initialized separately.
	 */
	public String[] getPasswordFromUser() {
		System.out.println("Enter the password you would like to store: ");
		String[] userPass = new String[2];
		userPass[1] = s.nextLine().trim();
		char[] checker = userPass[1].toCharArray();
		boolean problem = false;
		boolean fixed = true;
		for (char x : checker) {
			if (x == ',' || x == ' ') {
				problem = true;
				fixed = false;
				break;
			}
		}
		while (problem) {
			System.out.println("\nThere cannot be a comma \",\" or space in the password you are saving, " +
					"please try a password without a comma.");
			userPass[1] = s.nextLine().trim();
			checker = userPass[1].toCharArray();
			for (char x : checker) {
				if (!((x == ',') || (x == ' '))) {
					fixed = true;
				}
				else {
					fixed = false;
					break;
				}
			}
			if (fixed) {
				problem = false;
			}
		}
		return userPass;
	}

	/**
	 * Gets the name for the password to be stored with and then calls the storeInformation() function to store the
	 * user's data.
	 * @param arr A two index array holding the user's password and the name associated with it.
	 */
	@Override
	public void getPassIdentifierFromUser(String[] arr) {
		System.out.println("\nWhat is this password for? If you don't want it saved, type STOP");
		arr[0] = s.nextLine().toLowerCase().trim();
		boolean problem = false;
		char[] checker;
		if (arr[0].equals("stop")) {
			System.out.println("\nReturning to menu...\n");
		}
		else {
			checker = arr[0].toCharArray();
			for (char q : checker) {
				if (q == ',') {
					problem = true;
					break;
				}
			}
			boolean fixed = false;
			while (problem) {
				System.out.println("\nThere cannot be a comma \",\" in the name you are saving, please try a name without a comma.");
				System.out.println("What is this password for? If you don't want it saved, type STOP");
				arr[0] = s.nextLine().toLowerCase().trim();
				checker = arr[0].toCharArray();
				for (char x : checker) {
					if (!(x == ',')) {
						fixed = true;
					}
					else {
						fixed = false;
						break;
					}
				}
				if (fixed) {
					problem = false;
				}
			}
			if (!arr[0].equals("stop")) {
				storeInformation(arr);
			}
		}
	}

	/**
	 * Checks whether the entered parameters for generating a new password are valid.
	 */
	private void checkParams() {
		try {
			while (lengthOfPassword < specialChars || lengthOfPassword < capitals || lengthOfPassword < numbers) {
				logger.log(Level.INFO, "Unable to generate password with the given parameters.\n" +
						"Make sure the length of the password is long enough.");
				//make user reenter params
				getParams();
			} 
			while (lengthOfPassword == specialChars) {
				logger.log(Level.INFO, "Cannot make password that is made up of only specialCharacters.");
				//make user reenter params
				getParams();
			} 
			while (lengthOfPassword == capitals) {
				logger.log(Level.INFO, "Cannot make password that is made up of only capital letters.");
				//make user reenter params
				getParams();
			} 
			while (lengthOfPassword == numbers) {
				logger.log(Level.INFO, "Cannot make password that is made up of only numbers.");
				//make user reenter params
				getParams();
			} 
			while (lengthOfPassword < (specialChars + capitals + numbers)) {
				logger.log(Level.INFO, "Invalid parameters for the password.");
				//make user reenter params
				getParams();
			}
		}
		catch (NumberFormatException e) {
			logger.log(Level.INFO, "User input an illegal character in place of an integer.");
			getParams();
		}
	}

	/**
	 * Generates the menu for the user to interact with in the terminal.
	 */
	private void startupText() {
		System.out.print("""
			CHOOSE AN OPTION!
			1) Generate a new password
			2) Find password by search
			3) View all passwords
			4) Check password strength
			5) Change/remove password
			6) Store existing password
			7) Exit program
			Choose an option:""");
		System.out.print(" ");
	}

	/**
	 * Gets the response from the user in regard to the selection they make from the startupText() menu.
	 * @return Returns the number they chose from the menu in startupText().
	 */
	private int displayMenu() {
		startupText();
		String x;
		int y;
		try {
			x = s.nextLine().trim();
			y = Integer.parseInt(x);
			while (y < 1 || y > 7) {
				logger.log(Level.WARNING, "You have input an illegal integer for the prompt.");
				System.out.println("Pick an option numbered 1 through 7.");
				x = s.nextLine();
				y = Integer.parseInt(x);
			}
		}
		catch (NumberFormatException e) {
			logger.log(Level.WARNING, "You have input an illegal character in place of an integer.");
			y = displayMenu();
		}
		return y;
	}

	/**
	 * Generates a new password for the user.
	 * @return Returns the new password in a string array that can be handled by other methods to store it.
	 */
	@Override
	public String[] getUserInformation() {
		Generator gen = new Generator(logger);
		String[] params = new String[2];
		params[1] = gen.generatePassword(lengthOfPassword, specialChars, capitals, numbers);
		System.out.println(params[1]);
		return params;
	}

	/**
	 * Creates a new Storage object to handle storing the user's password and the name associated with it
	 * into a secure file.
	 * @param info The array holding the user's password and the name associated with it.
	 */
	@Override
	public void storeInformation(String[] info) {
		Storage store = new Storage(logger);
		String[] transferable = new String[] {
				Base64.getEncoder().encodeToString(info[0].getBytes()),
				Base64.getEncoder().encodeToString(info[1].getBytes())
		};
		store.storeData(transferable);
	}

	/**
	 * Prints the user's passwords and the names associated with them for the user to see.
	 */
	@Override
	public void extractInfoFromList() {
		Storage store = new Storage(logger);
		store.displayUserPassCombos();
	}

	/**
	 * Gets the parameters for password generation from the user.
	 */
	private void getParams() {
		resetParams();
		while (lengthOfPassword == -1 || specialChars == -1 || capitals == -1 || numbers == -1) {
			System.out.println("Enter desired password length: ");
			try {
				String x = s.nextLine().trim();
				lengthOfPassword = Integer.parseInt(x);
				if (lengthOfPassword < 0 || lengthOfPassword > 1000) {
					logger.log(Level.WARNING, "You have input an invalid number.");
					System.out.println("Negative numbers and lengths above 1000 cannot be used in the input.");
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				lengthOfPassword = -1;
				System.out.println("You must enter a number here.");
				break;
			}
			System.out.println("Enter num of special chars:");
			try {
				String x = s.nextLine().trim();
				specialChars = Integer.parseInt(x);
				if (specialChars < 0) {
					logger.log(Level.WARNING, "You have input a negative number.");
					System.out.println("Negative numbers cannot be used in the input.");
					specialChars = -1;
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				specialChars = -1;
				System.out.println("You must enter a number.");
				break;
			}
			System.out.println("Enter num of capitals");
			try {
				String x = s.nextLine().trim();
				capitals = Integer.parseInt(x);
				if (capitals < 0) {
					logger.log(Level.WARNING, "You have input a negative number.");
					capitals = -1;
					System.out.println("Negative numbers cannot be used in the input.");
					capitals = -1;
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				System.out.println("You must enter a number.");
				break;
			}
			System.out.println("Enter num of numbers:");
			try {
				String x = s.nextLine().trim();
				numbers = Integer.parseInt(x);
				if (numbers < 0) {
					logger.log(Level.WARNING, "You have input a negative number.");
					System.out.println("Negative numbers cannot be used in the input.");
					numbers = -1;
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				numbers = -1;
				System.out.println("You must enter a number.");
				break;
			}
		}
		if (lengthOfPassword == -1 || specialChars == -1 || capitals == -1 || numbers == -1) {
			getParams();
		}
	}
	/**
	 * Allows the user to test how strong their password is.
	 */
	@Override
	public void strengthTest() {
		Generator gen = new Generator(logger);
		//change this block
		System.out.print("Enter the password to test here: ");
		String password = s.nextLine().trim();
		System.out.println();
		int score = gen.passwordStrengthScoring(password);
		if (score == 0) {
			System.out.println("Your password is very weak -> " + score + "/" + 10 + "\n{....................}");
		}
		else if (score >= 1 && score <= 5) {
			System.out.println("Your password is somewhat weak -> " + score + "/" + 10 + "\n{[][][]..............}");
		}
		else if (score > 5 && score <= 8) {
			System.out.println("Your password is somewhat strong -> " + score + "/" + 10 + "\n{[][][][][]..........}");
		}
		else if (score == 9) {
			System.out.println("Your password is very strong -> " + score + "/" + 10 + "\n{[][][][][][][][]....}");
		}
		else {
			System.out.println("Your password is very strong -> " + score + "/" + 10 + "\n{[][][][][][][][][][]}");
		}
	}

	/**
	 * Searches for a specific password by looking for the name associated with it.
	 */
	@Override
	public void findNamePassCombos() {
		Storage store = new Storage(logger);
		System.out.println("Enter the name for the password you are looking for:");
		String name = s.nextLine().trim().toLowerCase();
		System.out.println();
		ArrayList<String> acceptedStrings = store.checkStoredDataForName(name);
		if (acceptedStrings.size() == 1) {
			String[] values = acceptedStrings.get(0).split(", ", 2);
			System.out.println("The password for " + name + " is: " + values[1]);
		}
		else if (acceptedStrings.size() > 1) {
			System.out.println("Here's a list of the passwords that match the name you entered:");
			for (String matchingPasswords : acceptedStrings) {
				System.out.println(matchingPasswords.replace(",", ":"));
			}
		}
		else {
			System.out.println("""
					No passwords matched your search.
					Would you like to try a different search?
					Enter "y" for yes, anything else for "no\"""");
			String retryString = s.nextLine();
			if (retryString.toLowerCase().trim().equals("y")) {
				findNamePassCombos();
			}
		}
	}
}