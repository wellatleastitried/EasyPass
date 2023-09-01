package com.walit.pass;

import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * This is the file containing the main method for PasswordManager. This program allows the user to generate, store,
 * test, and view passwords by implementing an easy-to-use interface.
 *
 * @author Jackson Swindell
 * */
public class PasswordManager implements Runnable {

	public final String bSlash = File.separator;
	public final String dubEsc = ".." + File.separator + ".." + File.separator;
	public int lengthOfPassword = -1;
	public int specialChars = -1;
	public int capitals = -1;
	public int numbers = -1;
	private final Logger logger = Logger.getLogger("ManagerLog");
	public Scanner s = new Scanner(System.in);

	/**
	 * Main method of PasswordManager, creates new PasswordManager object to run.
	 * @param args Any command line arguments (not evaluated if given)
	 * */
	public static void main(String[] args) {
		PasswordManager pM = new PasswordManager();
		pM.run();
	}

	/**
	 * Overridden run function that starts the program's execution.
	 * */
	@Override
	public void run() {
		String os = System.getProperty("os.name");
        os = os.toLowerCase();
        if (!(os.contains("win"))) {
        	System.out.println("Not a windows machine.");
        	System.exit(1);
        }
		initializeFilesForProgram();
		File logFile = new File("resources" + bSlash + "Utilities" + bSlash + "log" + bSlash + "PassMan.log");
		System.out.println(logFile.getName());
		FileHandler fH;
		try {
			if (logFile.exists() && logFile.isFile()) {
				new FileWriter(logFile, false).close();
			}
			fH = new FileHandler("resources" + bSlash + "Utilities" + bSlash + "log" + bSlash + "PassMan.log", true);
			while (logger.getHandlers().length > 0) {
				logger.removeHandler(logger.getHandlers()[0]);
			}
			logger.addHandler(fH);
			fH.setLevel(Level.INFO);
			XMLFormatter xF = new XMLFormatter();
			fH.setFormatter(xF);
			logger.log(Level.INFO, "Successful startup.");
		} catch (IOException e) {
			System.out.println("Logger could not be initialized.\n\nPlease restart program.");
		}
		//PasswordGUI pGUI = new PasswordGUI();
		poundSeparate();
		printLogo();
		poundSeparate();
		int x = startup();
		System.out.println();
		while (x != 7) {
			switch (x) {
				case 1 -> {
					tempMethod();
					checkParams();
					String[] temp = getInformation();
					finalizeName(temp);
					dashSeparate();
					resetParams();
					x = startup();
				}
				case 2 -> {
					searchFor();
					dashSeparate();
					x = startup();
				}
				case 3 -> {
					extractInfoFromList();
					dashSeparate();
					x = startup();
				}
				case 4 -> {
					strengthTest();
					dashSeparate();
					x = startup();
				}
				case 5 -> {
					System.out.println("""
						Would you like to:
						 - Change an existing password
						 - Remove an existing password
						Enter "c" to change and "r" to remove.
						""");
					boolean choice = changeOrRem();
					if (choice) changeInfo();
					else removeInfo();
					dashSeparate();
					x = startup();
				}
				case 6 -> {
					String[] tempStr = getPassFromUser();
					finalizeName(tempStr);
					dashSeparate();
					x = startup();
				}
			}
		}
		shutdown();
		logger.log(Level.INFO, "Successful termination.");
		poundSeparate();
		System.exit(0);
	}

	private void printLogo() {
		System.out.println("""
			
			|||||||     |||     |||||||  ||   ||  |||||||     |||     |||||||  |||||||
			||         || ||    ||        || ||   ||   ||    || ||    ||       ||
			|||||     |||||||   |||||||    |||    |||||||   |||||||   |||||||  |||||||
			||       ||     ||       ||    |||    ||       ||     ||       ||       ||
			|||||||  ||     ||  |||||||    |||    ||       ||     ||  |||||||  |||||||
			""");
	}

	private void dashSeparate() {
		System.out.println("\n--------------------------------------------------------------------------\n");
	}

	private void poundSeparate() {
		System.out.println("\n##########################################################################\n");
	}

	/**
	 * Resets the password parameters to their initial values to prepare for a new password to be generated.
	 */
	private void resetParams() {
		lengthOfPassword = -1;
		specialChars = -1;
		capitals = -1;
		numbers = -1;
	}

	/**
	 * Closes any resources that remain open.
	 * */
	private void shutdown() {
		//pGUI.dispose();
		s.close();
	}

	/**
	 * Gets the response from the user for whether they want to change or remove a specific password.
	 * @return Returns true if the user chooses 'change' and false if the user chooses 'remove'.
	 * */
	private boolean changeOrRem() {
		boolean choiceMade = false;
		boolean isChange =false;
		while (!choiceMade) {
			String ans = s.nextLine().trim().toLowerCase();
			if (ans.equals("c")) {
				isChange = true;
				choiceMade = true;
			} else if (ans.equals("r")) {
				choiceMade = true;
			} else System.out.println("\nThat was not a valid option, enter \"c\" for change or \"r\" for remove.");
		}
		return isChange;
	}

	/**
	 * Handles changing the user's password by getting the name the password belongs to and allowing them to enter a new password.
	 * */
	private void changeInfo() {
		System.out.println("You chose to change an existing password.");
		String name = getUserNameForAlter(0);
		PasswordStorage pS = new PasswordStorage(logger);
		List<String> strings = pS.findNameToAlter();
		List<String> acceptedStrings = new ArrayList<>();
		String[] splitStrings;
		if (strings.get(0).equals("There was an error.")) {
			logger.log(Level.WARNING, "Unable to access file for names.");
		} else {
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
			String[] temp = getPassFromUser();
			String newPass = temp[1];
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(chosenIndex - 1))) index = i;
			}
			strings.set(index, splitStrings[0] + ", " + newPass);
			pS.storeNameFromLists(strings);
		} else if (acceptedStrings.size() == 1) {
			splitStrings = acceptedStrings.get(0).split(", ");
			System.out.println("You chose to change the password: "
					+ splitStrings[1] + " for " + splitStrings[0]
					+ ".\nWhat would you like the new password to be? (ENTER BELOW)");
			String[] temp;
			String newPass;
			temp = getPassFromUser();
			newPass = temp[1];
			int index = -1;
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(0))) {
					index = i;
				}
			}
			strings.set(index, splitStrings[0] + ", " + newPass);
			pS.storeNameFromLists(strings);
			System.out.println("\nPassword successfully changed.");
		} else {
			System.out.println("\nName unable to be found, enter a valid name.\n");
			changeInfo();
		}
	}

	/**
	 * Handles removing the user's password by allowing them to enter the name associated with their password.
	 */
	private void removeInfo() {
		System.out.println("You chose to remove an existing password.");
		String name = getUserNameForAlter(1);
		PasswordStorage pS = new PasswordStorage(logger);
		List<String> strings = pS.findNameToAlter();
		List<String> acceptedStrings = new ArrayList<>();
		String[] splitStrings;
		if (strings.get(0).equals("There was an error.")) {
			logger.log(Level.WARNING, "Unable to access file for names.");
		} else {
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
				if (strings.get(i).equals(acceptedStrings.get(chosenIndex - 1))) index = i;
			}
			strings.remove(index);
			pS.storeNameFromLists(strings);
		} else if (acceptedStrings.size() == 1) {
			splitStrings = acceptedStrings.get(0).split(", ");
			System.out.println("You chose to remove the password: " + splitStrings[1] + " for " + splitStrings[0] + ".");
			int index = -1;
			for (int i = 0; i < strings.size(); i++) {
				if (strings.get(i).equals(acceptedStrings.get(0))) {
					index = i;
				}
			}
			strings.remove(index);
			pS.storeNameFromLists(strings);
		} else {
			System.out.println("\nName unable to be found, enter a valid name.\n");
			removeInfo();
		}
	}

	/**
	 * Gets the name that the password being changed or removed belongs to.
	 * @param choice The variable that holds whether the user chose to change or remove their password.
	 * @return Returns a string containing the name that the password belongs to.
	 */
	private String getUserNameForAlter(int choice) {
		if (choice == 0) System.out.println("Enter the name for the password you would like to change.");
		else if (choice == 1) System.out.println("Enter the name for the password you would like to remove.");
		else logger.log(Level.INFO, "I genuinely have no idea how this happened.");
		return s.nextLine().trim().toLowerCase();
	}

	/**
	 * Gets the password from the user to be stored.
	 * @return Returns the password in the second index of the two index array. The first slot is for the name, which
	 * will be initialized separately.
	 */
	private String[] getPassFromUser() {
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
				} else {
					fixed = false;
					break;
				}
			}
			if (fixed) problem = false;
		}
		return userPass;
	}

	/**
	 * Gets the name for the password to be stored with and then calls the storeInformation() function to store the user's data.
	 * @param arr A two index array holding the user's password and the name associated with it.
	 */
	private void finalizeName(String[] arr) {
		System.out.println("\nWhat is this password for? If you don't want it saved, type STOP");
		arr[0] = s.nextLine().toLowerCase().trim();
		boolean problem = false;
		char[] checker;
		if (arr[0].equals("stop")) {
			System.out.println("ok, fair enough\n");
		} else {
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
					} else {
						fixed = false;
						break;
					}
				}
				if (fixed) problem = false;
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
				tempMethod();
			} 
			while (lengthOfPassword == specialChars) {
				logger.log(Level.INFO, "Cannot make password that is made up of only specialCharacters.");
				//make user reenter params
				tempMethod();
			} 
			while (lengthOfPassword == capitals) {
				logger.log(Level.INFO, "Cannot make password that is made up of only capital letters.");
				//make user reenter params
				tempMethod();
			} 
			while (lengthOfPassword == numbers) {
				logger.log(Level.INFO, "Cannot make password that is made up of only numbers.");
				//make user reenter params
				tempMethod();
			} 
			while (lengthOfPassword < (specialChars + capitals + numbers)) {
				logger.log(Level.INFO, "Invalid parameters for the password.");
				//make user reenter params
				tempMethod();
			}
		} catch (NumberFormatException e) {
			logger.log(Level.INFO, "User input an illegal character in place of an integer.");
			tempMethod();
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
	private int startup() {
		startupText();
		String x;
		int y;
		try {
			x = s.nextLine().trim();
			y = Integer.parseInt(x);
			while (y < 1 || y > 7) {
				logger.log(Level.WARNING, "User input an illegal integer for the prompt.");
				System.out.println("Pick an option numbered 1 through 7.");
				x = s.nextLine();
				y = Integer.parseInt(x);
			}
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "User input an illegal character in place of an integer.");
			y = startup();
		}
		return y;
	}

	/**
	 * Generates a new password for the user.
	 * @return Returns the new password in a string array that can be handled by other methods to store it.
	 */
	private String[] getInformation() {
		PasswordGenerator pGen = new PasswordGenerator(logger);
		String[] params = new String[2];
		params[1] = pGen.generatePassword(lengthOfPassword, specialChars, capitals, numbers);
		System.out.println(params[1]);
		return params;
	}

	/**
	 * Creates a new PasswordStorage object to handle storing the user's password and the name associated with it into a secure file.
	 * @param info The array holding the user's password and the name associated with it.
	 */
	private void storeInformation(String[] info) {
		PasswordStorage pS = new PasswordStorage(logger);
		String[] transferable = new String[2];
		String encodedName = Base64.getEncoder().encodeToString(info[0].getBytes());
		String encodedPwd = Base64.getEncoder().encodeToString(info[1].getBytes());
		transferable[0] = encodedName;
		transferable[1] = encodedPwd;
		pS.storeInfo(transferable);
	}

	/**
	 * Prints the user's passwords and the names associated with them for the user to see.
	 */
	private void extractInfoFromList() {
		PasswordStorage pS = new PasswordStorage(logger);
		pS.getInfo();
	}

	/**
	 * Gets the parameters for password generation from the user.
	 */
	private void tempMethod() {
		//getParams();
		resetParams();
		while (lengthOfPassword == -1 || specialChars == -1 || capitals == -1 || numbers == -1) {
			System.out.println("Enter desired password length: ");
			try {
				String x = s.nextLine().trim();
				lengthOfPassword = Integer.parseInt(x);
				if (lengthOfPassword < 0 || lengthOfPassword > 100) {
					logger.log(Level.WARNING, "User input an invalid number.");
					System.out.println("Negative numbers and lengths above 10000 cannot be used in the input.");
					tempMethod();
				}
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "User input an illegal character.");
				lengthOfPassword = -1;
				System.out.println("You must enter a number here.");
				break;
			}
			System.out.println("Enter num of special chars:");
			try {
				String x = s.nextLine().trim();
				specialChars = Integer.parseInt(x);
				if (specialChars < 0) {
					logger.log(Level.WARNING, "User input a negative number.");
					System.out.println("Negative numbers cannot be used in the input.");
					tempMethod();
				}
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "User input an illegal character.");
				specialChars = -1;
				System.out.println("You must enter a number.");
				break;
			}
			System.out.println("Enter num of capitals");
			try {
				String x = s.nextLine().trim();
				capitals = Integer.parseInt(x);
				if (capitals < 0) {
					logger.log(Level.WARNING, "User input a negative number.");
					capitals = -1;
					System.out.println("Negative numbers cannot be used in the input.");
					tempMethod();
				}
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "User input an illegal character.");
				System.out.println("You must enter a number.");
				break;
			}
			System.out.println("Enter num of numbers:");
			try {
				String x = s.nextLine().trim();
				numbers = Integer.parseInt(x);
				if (numbers < 0) {
					logger.log(Level.WARNING, "User input a negative number.");
					System.out.println("Negative numbers cannot be used in the input.");
					tempMethod();
				}
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "User input an illegal character.");
				numbers = -1;
				System.out.println("You must enter a number.");
				break;
			}
		}
		if (lengthOfPassword == -1 || specialChars == -1 || capitals == -1 || numbers == -1) {
			tempMethod();
		}
	}
	/**
	 * Creates any directories and files that have either not been made yet or have been deleted and need to be remade.
	 */
	private void initializeFilesForProgram() {

		String ls = System.getProperty("line.separator");
		File storeDir = new File("resources" + bSlash + "Utilities" + bSlash + "log");
		File logDir = new File("resources" + bSlash + "Utilities" + bSlash + "data");
		File wordLists = new File("resources" + bSlash + "WordLists");
		File[] dirs = new File[3];
		dirs[0] = storeDir;
		dirs[1] = logDir;
		dirs[2] = wordLists;
		try {
			for (File directory : dirs) {
				if (!(directory.exists())) {
					boolean checkDirCreation = directory.mkdirs();
					if (!checkDirCreation) logger.log(Level.SEVERE, "Error creating directory.");
				}
			}
		} catch (SecurityException sE) {
			logger.log(Level.WARNING, "IO exception while making directories.");
		} catch (NullPointerException nPE) {
			logger.log(Level.WARNING, "Null pointer exception while initializing directories.");
		}
		File info = new File("resources" + bSlash + "Utilities" + bSlash + "data" + bSlash + "info.csv");
		File vectors = new File("resources" + bSlash + "Utilities" + bSlash + "data" + bSlash + "vectors.txt");
		File passMan = new File("resources" + bSlash + "Utilities" + bSlash + "log" + bSlash + "PassMan.log");
		File[] files = new File[3];
		files[0] = info;
		files[1] = vectors;
		files[2] = passMan;
		try {
			for (int i = 0; i < files.length; i++) {
				if (!(files[i].exists() && files[i].isFile())) {
					if (!(files[i].exists()) && i != 2) {
						boolean checkFileCreation = files[i].createNewFile();
						if (!checkFileCreation) logger.log(Level.SEVERE, "Could not initialize files for program.");
					} else if (i == 2 && !(files[i].exists())) {
						try {
							BufferedWriter bW = new BufferedWriter(new FileWriter(files[i]));
							bW.write("~#WARNING#~" + ls);
							bW.write("DO NOT EDIT THIS FILE" + ls);
							bW.write("~#WARNING#~" + ls);
							bW.flush();
							bW.close();
						} catch (IOException e) {
							logger.log(Level.SEVERE, "Error initializing info file.");
						}
					}
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO exception while creating new files for program.");
		}
	}

	/**
	 * Allows the user to test how strong their password is.
	 */
	private void strengthTest() {
		PasswordGenerator pG = new PasswordGenerator(logger);
		//change this block
		System.out.print("Enter the password to test here: ");
		String password = s.nextLine().trim();
		System.out.println();
		int score = pG.passwordStrengthScoring(password);
		if (score == 0) System.out.println("Your password is very weak -> " + score + "/" + 10 + "\n{....................}");
		else if (score >= 1 && score <= 5) System.out.println("Your password is quite weak -> " + score + "/" + 10 + "\n{[][][]..............}");
		else if (score > 5 && score <= 8) System.out.println("Your password is quite strong -> " + score + "/" + 10 + "\n{[][][][][][][]......}");
		else if (score == 9) System.out.println("Your password is very strong -> " + score + "/" + 10 + "\n{[][][][][][][][][]..}");
		else System.out.println("Your password is very strong -> " + score + "/" + 10 + "\n{[][][][][][][][][][]}");
	}

	/**
	 * Searches for a specific password by looking for the name associated with it.
	 */
	private void searchFor() {
		PasswordStorage pS = new PasswordStorage(logger);
		//change this block
		System.out.println("Enter the name for the password you are looking for:");
		String name = s.nextLine().trim().toLowerCase();
		System.out.println();
		ArrayList<String> acceptedStrings = pS.findInfo(name);
		if (acceptedStrings.size() == 1) {
			String[] values = acceptedStrings.get(0).split(", ", 2);
			System.out.println("The password for " + name + " is: " + values[1]);
		} else if (acceptedStrings.size() > 1) {
			System.out.println("Here's a list of the passwords that match the name you entered:");
			for (String matchingPasswords : acceptedStrings) {
				System.out.println(matchingPasswords.replace(",", ":"));
				
			}
		} else {
			System.out.println("""
					No passwords matched your search.
					Would you like to try a different search?
					Enter "y" for yes, anything else for "no\"""");
			String retryString = s.nextLine();
			if (retryString.toLowerCase().trim().equals("y")) {
				searchFor();
			}
		}
	}
}