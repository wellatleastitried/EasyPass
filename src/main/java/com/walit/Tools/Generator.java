package com.walit.Tools;

import java.io.*;

import java.security.SecureRandom;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generator serves as a helper class to CLI and UI by supplying helpful methods that may be necessary to
 * calculate new passwords or test their strength.
 *
 * @author Jackson Swindell
 */
public class Generator {
	private final char[] numbers = "0123456789".toCharArray();
	private final char[] characters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private final char[] capLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private final char[] specialCharacters = new char[] {'!', '?', '-', '#', '%', '$'};
	private final StringBuilder sB = new StringBuilder();
	private String pwd = "";
	private final Logger logger;

	/**
	 * Constructor for Generator class.
	 */
	public Generator(Logger genLog) { this.logger = genLog; }

	/**
	 * Generates a unique password based on the parameters that are passed to it.
	 * @param length The length of the password to be made.
	 * @param specialChars The number of special characters to be added to the new password.
	 * @param capitals The number of capital letters to be added to the new password.
	 * @param numbers The number of numbers to be added to the new password.
	 * @return Returns the new password for the user.
	 */
	public String generatePassword(int length, int specialChars, int capitals, int numbers) {
		while (!(validateElementCount(numbers, 2))) {
			sB.append(addNumber());
			pwd = sB.toString();
		}
		while (!(validateElementCount(capitals, 0))) {
			sB.append(addCapital());
			pwd = sB.toString();
		}
		while (!(validateElementCount(specialChars, 1))) {
			sB.append(addSpecialChar());
			pwd = sB.toString();
		}
		pwd = sB.toString();
		while (pwd.length() < length) {
			sB.append(characters[new SecureRandom().nextInt(characters.length)]);
			pwd = sB.toString();
		}
		List<String> passwordCharsInList = Arrays.asList(pwd.split(""));
		Collections.shuffle(passwordCharsInList, new SecureRandom());
		sB.setLength(0);
		for (String letter : passwordCharsInList) {
			sB.append(letter);
		}
		pwd = sB.toString();
		char[] test = pwd.toCharArray();
		if (isValidChar(test[0])) {
			return pwd;
		}
		else {
			pwd = swapFirstChar(test);
		}
		return pwd;
	}

	/**
	 * Makes sure that the first character does not start with a special character or number, if it does, swaps it with
	 * a normal letter from a different index in the password.
	 * @param toSwap The new password in the form of a char array.
	 * @return Returns the password with the first character swapped for a normal letter instead of a special character
	 * or number.
	 */
	private String swapFirstChar(char[] toSwap) {
		int indexToNote;
		Set<Character> lowercaseSet = new HashSet<>();
		for (char c : characters) {
			lowercaseSet.add(c);
		}
		Set<Character> uppercaseSet = new HashSet<>();
		for (char c : capLetters) {
			uppercaseSet.add(c);
		}
		char normalChar;
		for (int i = 0; i < toSwap.length; i++) {
			if (lowercaseSet.contains(toSwap[i]) || uppercaseSet.contains(toSwap[i])) {
				indexToNote = i;
				normalChar = toSwap[i];
				toSwap[indexToNote] = toSwap[0];
				toSwap[0] = normalChar;
				break;
			}
		}
		return String.valueOf(toSwap);
	}

	/**
	 * Checks to see if the first letter of the new password is a normal letter instead of a special character
	 * or number.
	 * @param characterToCheck The first character of the new password.
	 * @return Returns 'true' if the first letter is a normal letter, 'false' otherwise.
	 */
	private boolean isValidChar(char characterToCheck) {
		for (int i = 0; i < characters.length; i++) {
			if (characterToCheck == characters[i] || characterToCheck == capLetters[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generates a random single digit number.
	 * @return Returns the number to be added to the password.
	 */
	private char addNumber() { return numbers[new SecureRandom().nextInt(numbers.length)]; }

	/**
	 * Generates a random capital letter.
	 * @return Returns the letter to be added to the password.
	 */
	private char addCapital() { return capLetters[new SecureRandom().nextInt(capLetters.length)];	}

	/**
	 * Generates a random special character.
	 * @return Returns the character to be added to the password.
	 */
	private char addSpecialChar() { return specialCharacters[new SecureRandom().nextInt(specialCharacters.length)]; }

	/**
	 * Checks the number of capital letters, special characters, or digits to make sure it is not more or less than the intended amount.
	 * @param elementCount The number of elements the password SHOULD contain.
	 * @return Returns 'true' if the number of elements is equal to the count and 'false' otherwise.
	 */
	private boolean validateElementCount(int elementCount, int determineElement) {
		char[] passwordStringToCharArray = pwd.toCharArray();
		Set<Character> setOfElements = new HashSet<>();
		switch (determineElement) {
			case 0 -> {
				for (char c : capLetters) {
					setOfElements.add(c);
				}
			}
			case 1 -> {
				for (char c : specialCharacters) {
					setOfElements.add(c);
				}
			}
			case 2 -> {
				for (char c : numbers) {
					setOfElements.add(c);
				}
			}
		}
		int counter = 0;
		for (char characterToCheck : passwordStringToCharArray) {
			if (setOfElements.contains(characterToCheck)) {
				counter += 1;
			}
		}
		if (counter > elementCount || counter == elementCount) {
			int numOfCapitalsToRemove = counter - elementCount;
			if (numOfCapitalsToRemove > 0) {
				logger.log(Level.WARNING, "Function made an error adding elements to password, had to manually remove them.");
				for (int i = 0; i < passwordStringToCharArray.length; i++) {
					if (numOfCapitalsToRemove == 0) {
						break;
					}
					if (setOfElements.contains(passwordStringToCharArray[i])) {
						passwordStringToCharArray[i] = '$';
						numOfCapitalsToRemove -= 1;
					}
				}
			}
			pwd = String.valueOf(passwordStringToCharArray);
			pwd = pwd.replace("$", "");
			return true;
		}
		return false;
	}

	/**
	 * Searches through the provided word-lists for the given password to see if there is a match.
	 * @param pass The password to search for.
	 * @return Returns "true" if the password is found in the word-lists, "false" otherwise.
	 */
	private boolean checkForPassInLists(String pass) {
		//User can add as many word-lists to this folder as they want, but it will impact runtime.
		File wordDirectory = new File("resources\\WordLists");
		AtomicBoolean wordWasFound = new AtomicBoolean(false);
		long start = System.currentTimeMillis();
		try {
			File[] wordLists = wordDirectory.listFiles();
			if (wordLists != null) {
				ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
				for (File file : wordLists) {
					if (file.getName().endsWith(".txt")) {
						threadPool.execute(() -> {
							try (BufferedReader bR = new BufferedReader(new FileReader(file))) {
								wordWasFound.set(bR.lines().parallel().anyMatch(pass::equals));
							}
							catch (IOException e) {
								logger.log(Level.WARNING, "IOException while initializing buffered reader for word-lists.");
							}
						});
					}
				}
				threadPool.shutdown();
				if (!threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
					logger.log(Level.WARNING, "Error in thread pool termination.");
				}
			}
			System.out.println(
					"[*] Searching through previous password leaks took "
							+ (System.currentTimeMillis() - start)
							+ " ms to complete."
			);
		}
		catch (NullPointerException nPE) {
			logger.log(Level.INFO, "No word-lists to search through.");
		}
		catch (InterruptedException iE) {
			logger.log(Level.WARNING, "Error synchronizing threads while searching word-lists");
		}
		boolean found = wordWasFound.get();
		if (found) {
			System.out.println("[*] This password was previously found in a data breach, you may want to change it.");
		}
		return found;
	}

	/**
	 * Checks the strength of a given password by grading it by using specific criteria.
	 * @param pass The password to test the strength of.
	 * @return Returns the score on a scale of 1-10 based on how strong the password appears to be, returns 0 if the password is found in a provided word-list.
	 */
	public int passwordStrengthScoring(String pass) {
		boolean foundInList = checkForPassInLists(pass);
		if (foundInList) return -1;
		double entropy = getPasswordEntropy(pass);
		double maxEntropy = getMaxEntropy();
		return (int) Math.max(0, Math.min(10, (entropy / maxEntropy) * 10));
	}
	public double getMaxEntropy() {
		return Math.log(Math.pow(74, 20)) / Math.log(2);
	}
	public double getPasswordEntropy(String password) {
		int charsetSize = 0;
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasNumber = false;
		boolean hasSpecialChar = false;
		char[] passChars = password.toCharArray();
		for (int i = 0; i < passChars.length; i++) {
			if (isUppercase(passChars[i]) && !hasUpper) {
				hasUpper = true;
			} else if (isSpecialChar(passChars[i]) && !hasSpecialChar) {
				hasSpecialChar = true;
			} else if (isNumber(passChars[i]) && !hasNumber) {
				hasNumber = true;
			} else if (Character.isLetter(passChars[i]) && !isUppercase(passChars[i]) && !hasLower) {
				hasLower = true;
			}
		}
		charsetSize += hasUpper ? 26 : 0;
		charsetSize += hasLower ? 26 : 0;
		charsetSize += hasNumber ? 10 : 0;
		charsetSize += hasSpecialChar ? 12 : 0;
        return Math.log(Math.pow(charsetSize, password.length())) / Math.log(2);
	}
	/**
	 * Checks to see if a given char is uppercase.
	 * @param c The character to check.
	 * @return Returns 'true' if uppercase, 'false' otherwise.
	 */
	public boolean isUppercase(char c) { return ((int) c) >= 65 && ((int) c) <= 90; }

	/**
	 * Checks to see if a given char is a special character.
	 * @param c The character to check.
	 * @return Returns 'true' if the char is a special character, 'false' otherwise.
	 */
	public boolean isSpecialChar(char c) {
		char[] list = "!_?.-@#$%&*+".toCharArray();
		for (char x : list) {
			if (c == x) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if a given char is a number.
	 * @param c The character to check.
	 * @return Returns 'true' if the char is a number, 'false' otherwise.
	 */
	public boolean isNumber(char c) { return Character.isDigit(c); }
}