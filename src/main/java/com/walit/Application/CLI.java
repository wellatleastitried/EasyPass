package com.walit.Application;

import com.walit.Tools.Generator;
import com.walit.Tools.Parsed;
import com.walit.Tools.Storage;

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
public non-sealed class CLI implements Runner {

	public int lengthOfPassword = -1;
	public int specialChars = -1;
	public int capitals = -1;
	public int numbers = -1;
	private final Logger logger = Logger.getLogger("ManagerLog");
	public Scanner s = new Scanner(System.in);

	public Logger getLogger() {
		return logger;
	}

	public void callInterface() {
		new UI(logger).run();
	}

	public CLI() {
		String os = System.getProperty("os.name");
        os = os.toLowerCase();
		if (os.contains("win")) {
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
				System.out.println("[!] Error in startup.\n\n[!] Please restart program.");
			}
		}
		else if (os.contains("nix") || os.contains("nux")) {
			System.out.println("[!] Support for Linux is not yet available.");
			System.exit(0);
		}
		else if (os.contains("mac")) {
			System.out.println("[!] Support for MacOS is not yet available.");
		}
	}
	/**
	 * Prints the version information of the program.
	 */
	public String getVersionInfo() {
		String version;
		try {
			Parsed parser = new Parsed();
			version = parser.getVersion();
			return version;
		}
		catch (Exception ignored) {
			version = "[!] Error parsing version info.";
			System.err.println(version);
		}
		return version;
	}
	/**
	 * Run function that starts the program's execution.
	 */
	public void run() {
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
					findNamePassCombos(null);
					dashLine();
					x = displayMenu();
				}
				case 3 -> {
					extractInfoFromList();
					dashLine();
					x = displayMenu();
				}
				case 4 -> {
					strengthTest(null);
					dashLine();
					x = displayMenu();
				}
				case 5 -> {
					System.out.println("""
						[*] Would you like to:
						[*]  - Change an existing password
						[*]  - Remove an existing password
						[*] Enter "c" to change and "r" to remove.
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
	protected void printLogo() {
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
	protected void dashLine() {
		System.out.println("\n--------------------------------------------------------------------------\n");
	}

	/**
	 * Line separator during program runtime.
	 */
	protected void poundLine() {
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
				System.out.println("\n[!] That was not a valid option, enter \"c\" or \"change\" for change OR \"r\" or \"remove\" for remove.");
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
		// TODO: FIX THIS METHOD
		System.out.println("[*] You chose to change an existing password.");
		String name = getPassIdentifierForChangeOrRemove(0);
		try (Storage store = new Storage(logger)){

			List<String> strings = store.findNameToAlter();
			List<String> acceptedStrings = new ArrayList<>();
			String[] splitStrings;
			for (String x : strings) {
				splitStrings = x.split("~~SEPARATOR~~");
				splitStrings[0] = splitStrings[0].toLowerCase();
				if (splitStrings[0].equals(name)) {
					acceptedStrings.add(x);
				}
			}
			if (acceptedStrings.size() > 1) {
				int index = 1;
				System.out.println("\n[*] There were multiple passwords with the same name, which would you like to change?");
				for (String x : acceptedStrings) {
					splitStrings = x.split("~~SEPARATOR~~");
					System.out.println(index + ") " + splitStrings[0] + ": " + splitStrings[1]);
					index++;
				}
				System.out.print("\n[*] Enter the number that corresponds to the password you would like to change from the list: ");
				int chosenIndex = getValidNumber(1, acceptedStrings.size());
				splitStrings = acceptedStrings.get(chosenIndex - 1).split("~~SEPARATOR~~");
				System.out.println("[*] You chose to change the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
				store.changeRow(splitStrings[0], splitStrings[1], splitStrings[0], getPasswordFromUser()[1]);
			}
			else if (acceptedStrings.size() == 1) {
				splitStrings = acceptedStrings.get(0).split("~~SEPARATOR~~");
				System.out.println("[*] You chose to change the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
				System.out.println(splitStrings[0] + " " + splitStrings[1]);
				store.changeRow(splitStrings[0], splitStrings[1], splitStrings[0], getPasswordFromUser()[1]);
			}
			else {
				System.out.println("\n[!] Name unable to be found, enter a valid name.\n");
				changeData();
			}
		}
		catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error connecting to database.");
			System.exit(1);
		}
	}
	/**
	 * Handles removing the user's password by allowing them to enter the name associated with their password.
	 */
	@Override
	public void removeData() {
		System.out.println("[*] You chose to remove an existing password.");
		String name = getPassIdentifierForChangeOrRemove(1);
		try (Storage store = new Storage(logger)) {
			List<String> strings = store.findNameToAlter();
			List<String> acceptedStrings = new ArrayList<>();
			String[] splitStrings;
			for (String x : strings) {
				splitStrings = x.split("~~SEPARATOR~~");
				splitStrings[0] = splitStrings[0].toLowerCase();
				if (splitStrings[0].equals(name)) {
					acceptedStrings.add(x);
				}
			}
			if (acceptedStrings.size() > 1) {
				int index = 1;
				System.out.println("\n[*] There were multiple passwords with the same name, which would you like to remove?");
				for (String x : acceptedStrings) {
					splitStrings = x.split("~~SEPARATOR~~");
					System.out.println(index + ") " + splitStrings[0] + ": " + splitStrings[1]);
					index++;
				}
				System.out.print("\n[*] Enter the number that corresponds to the password you would like to remove from the list: ");
				int chosenIndex = getValidNumber(1, acceptedStrings.size());
				splitStrings = acceptedStrings.get(chosenIndex - 1).split("~~SEPARATOR~~");
				System.out.println("[*] You chose to remove the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
				store.removeRow(splitStrings[0], splitStrings[1]);
			}
			else if (acceptedStrings.size() == 1) {
				splitStrings = acceptedStrings.get(0).split("~~SEPARATOR~~");
				System.out.println("[*] You chose to remove the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
				store.removeRow(splitStrings[0], splitStrings[1]);
			}
			else {
				System.out.println("\n[!] Name unable to be found, enter a valid name.\n");
				removeData();
			}
		}
		catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error connecting to database.");
			System.exit(1);
		}
	}

	public int getValidNumber(int start, int end) {
		int index;
		try {
			String choice = s.nextLine().trim();
			index = Integer.parseInt(choice);
			if (index >= start && index <= end) {
				return index;
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e) {
			System.out.println("[!] The number that was entered was invalid. Enter a number within " + start + " and " + end + ".");
			index = getValidNumber(start, end);
		}
		return index;
	}

	/**
	 * Gets the name that the password being changed or removed belongs to.
	 * @param choice The variable that holds whether the user chose to change or remove their password.
	 * @return Returns a string containing the name that the password belongs to.
	 */
	@Override
	public String getPassIdentifierForChangeOrRemove(int choice) {
		if (choice == 0) {
			System.out.println("[*] Enter the name for the password you would like to change.");
		}
		else if (choice == 1) {
			System.out.println("[*] Enter the name for the password you would like to remove.");
		}
		else {
			logger.log(Level.INFO, "[!] There has been an error that I didn't think was possible.");
		}
		return s.nextLine().trim().toLowerCase();
	}

	/**
	 * Gets the password from the user to be stored.
	 * @return Returns the password in the second index of the two index array. The first slot is for the name, which
	 * will be initialized separately.
	 */
	public String[] getPasswordFromUser() {
		System.out.println("[*] Enter the password you would like to store: ");
		String[] userPass = new String[2];
		userPass[1] = s.nextLine().trim();
		return userPass;
	}

	/**
	 * Gets the name for the password to be stored with and then calls the storeInformation() function to store the
	 * user's data.
	 * @param arr A two index array holding the user's password and the name associated with it.
	 */
	@Override
	public void getPassIdentifierFromUser(String[] arr) {
		System.out.println("\n[*] What is this password for? If you don't want it to be saved, type STOP");
		arr[0] = s.nextLine().toLowerCase().trim();
		if (!arr[0].equals("stop")) {
			storeInformation(arr);
		}
		else {
			System.out.println("\n[*] Returning to menu...\n");
		}
	}

	/**
	 * Checks whether the entered parameters for generating a new password are valid.
	 */
	public void checkParams() {
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
			[*] CHOOSE AN OPTION!
			[*] 1) Generate a new password
			[*] 2) Find password by search
			[*] 3) View all passwords
			[*] 4) Check password strength
			[*] 5) Change/remove password
			[*] 6) Store existing password
			[*] 7) Exit program
			[*] Choose an option:""");
		System.out.print(" ");
	}

	/**
	 * Gets the response from the user in regard to the selection they make from the startupText() menu.
	 * @return Returns the number they chose from the menu in startupText().
	 */
	private int displayMenu() {
		startupText();
		int y;
		try {
			y = getValidNumber(1, 7);
			while (y < 1 || y > 7) {
				logger.log(Level.WARNING, "You have input an illegal integer for the prompt.");
				System.out.println("[*] Pick an option numbered 1 through 7.");
				y = getValidNumber(1, 7);
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
		System.out.println("\n[*] Your password is: " + params[1]);
		return params;
	}

	public String quickGenerate() {
		return new Generator(logger).generatePassword(lengthOfPassword, specialChars, capitals, numbers);
	}

	/**
	 * Creates a new Storage object to handle storing the user's password and the name associated with it
	 * into a secure file.
	 * @param info The array holding the user's password and the name associated with it.
	 */
	@Override
	public void storeInformation(String[] info) {
		info[0] = info[0].toLowerCase();
		try (Storage store = new Storage(logger)) {
			store.storeData(info);
		}
		catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error connecting to database.");
			System.exit(1);
		}
	}

	/**
	 * Prints the user's passwords and the names associated with them for the user to see.
	 */
	@Override
	public void extractInfoFromList() {
		try (Storage store = new Storage(logger)) {
			store.displayUserPassCombos();
		}
		catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error connecting to database.");
			System.exit(1);
		}
	}

	/**
	 * Gets the parameters for password generation from the user.
	 */
	public void getParams() {
		resetParams();
		while (lengthOfPassword == -1 || specialChars == -1 || capitals == -1 || numbers == -1) {
			System.out.print("\n[*] Enter desired password length: ");
			try {
				lengthOfPassword = getValidNumber(0, 1000);
				if (lengthOfPassword < 0 || lengthOfPassword > 1000) {
					logger.log(Level.WARNING, "You have input an invalid number.");
					System.out.print("[!] Negative numbers and lengths above 1000 cannot be used in the input.");
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				lengthOfPassword = -1;
				System.out.println("[!] You must enter a number here.");
				break;
			}
			System.out.print("\n[*] Enter the number of special characters: ");
			try {
				specialChars = getValidNumber(0, lengthOfPassword);
				if (specialChars < 0) {
					logger.log(Level.WARNING, "You have input a negative number.");
					System.out.println("[!] Negative numbers cannot be used in the input.");
					specialChars = -1;
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				specialChars = -1;
				System.out.println("[!] You must enter a number.");
				break;
			}
			System.out.print("\n[*] Enter the number of capitals: ");
			try {
				capitals = getValidNumber(0, lengthOfPassword);
				if (capitals < 0) {
					logger.log(Level.WARNING, "You have input a negative number.");
					capitals = -1;
					System.out.println("[!] Negative numbers cannot be used in the input.");
					capitals = -1;
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				System.out.println("[!] You must enter a number.");
				break;
			}
			System.out.print("\n[*] Enter the number of digits: ");
			try {
				numbers = getValidNumber(0, lengthOfPassword);
				if (numbers < 0) {
					logger.log(Level.WARNING, "You have input a negative number.");
					System.out.println("[!] Negative numbers cannot be used in the input.");
					numbers = -1;
					break;
				}
			}
			catch (NumberFormatException e) {
				logger.log(Level.WARNING, "You have input an illegal character.");
				numbers = -1;
				System.out.println("[!] You must enter a number.");
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
	public void strengthTest(String pass) {
		Generator gen = new Generator(logger);
		System.out.print("[*] Enter the password to test here: ");
		String password = s.nextLine().trim();
		System.out.println();
		int score = gen.passwordStrengthScoring(password);
		if (score == 0) {
			System.out.println("[*] Your password is very weak -> " + score + "/" + 10 + "\n{....................}");
		}
		else if (score >= 1 && score <= 5) {
			System.out.println("[*] Your password is somewhat weak -> " + score + "/" + 10 + "\n{[][][]..............}");
		}
		else if (score > 5 && score <= 8) {
			System.out.println("[*] Your password is somewhat strong -> " + score + "/" + 10 + "\n{[][][][][]..........}");
		}
		else if (score == 9) {
			System.out.println("[*] Your password is very strong -> " + score + "/" + 10 + "\n{[][][][][][][][]....}");
		}
		else {
			System.out.println("[*] Your password is very strong -> " + score + "/" + 10 + "\n{[][][][][][][][][][]}");
		}
	}

	/**
	 * Searches for a specific password by looking for the name associated with it.
	 */
	public void findNamePassCombos(String passedName) {
		try (Storage store = new Storage(logger)) {
			String name;
			if (passedName != null) {
				name = passedName;
			}
			else {
				System.out.print("[*] Enter the name for the password you are looking for:");
				name = s.nextLine().trim().toLowerCase();
			}
			System.out.println();
			List<String> acceptedStrings = store.checkStoredDataForName(name);
			if (acceptedStrings.size() == 1) {
				String[] values = acceptedStrings.get(0).split("~~SEPARATOR~~", 2);
				System.out.println("[*] The password for " + name + " is: " + values[1]);
			}
			else if (acceptedStrings.size() > 1) {
				System.out.println("[*] Here's a list of the passwords that match the name you entered:");
				for (String matchingPasswords : acceptedStrings) {
					System.out.println(matchingPasswords.replace("~~SEPARATOR~~", ": "));
				}
			}
			else {
				System.out.println("""
						[*] No passwords matched your search.
						[*] Would you like to try a different search?
						[*] Enter "y" for yes, anything else for "no\"""");
				String retryString = s.nextLine();
				if (retryString.toLowerCase().trim().equals("y")) {
					findNamePassCombos(null);
				}
			}
		}
		catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error connecting to database.");
			System.exit(1);
		}
	}
}