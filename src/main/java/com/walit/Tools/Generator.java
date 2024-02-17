package com.walit.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.security.SecureRandom;

import java.util.*;
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
	private final char[] specialCharacters = new char[] {'!', '?', '-', '#'};
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
				Thread[] fileThreads = new Thread[wordLists.length];
				for (int i = 0; i < fileThreads.length; i++) {
					final int valForThread = i;
					fileThreads[i] = new Thread(() -> {
						File file = wordLists[valForThread];
						if (file.getName().endsWith(".txt")) {
							String line;
							try {
								BufferedReader bR = new BufferedReader(new FileReader(file));
								while ((line = bR.readLine()) != null) {
									if (wordWasFound.get()) {
										return;
									}
									if (pass.equals(line)) {
										wordWasFound.set(true);
										break;
									}
								}
							}
							catch (IOException e) {
								logger.log(Level.INFO, "Error checking wordlist for matching password.");
							}
						}
						else {
							System.out.println("[!] Word lists must be text files!");
						}
					});
					fileThreads[i].start();
				}
				for (Thread thread : fileThreads) {
					thread.join();
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
		if (wordWasFound.get()) {
			System.out.println("[*] This password was previously found in a data breach, you may want to change it.");
			return true;
		}
		return false;
	}

	/**
	 * Checks the strength of a given password by grading it by using specific criteria.
	 * @param pass The password to test the strength of.
	 * @return Returns the score on a scale of 1-10 based on how strong the password appears to be, returns 0 if the password is found in a provided word-list.
	 */
	public int passwordStrengthScoring(String pass) {
		boolean foundInList = checkForPassInLists(pass);
		if (foundInList) {
			return 0;
		}
		int score = 0;
		int passLen = pass.length();
		char[] splitPass = pass.toCharArray();
		int numCount = 0;
		int specCount = 0;
		int capCount = 0;
		for (char c : splitPass) {
			if (isUppercase(c)) {
				capCount++;
			}
			else if (isSpecialChar(c)) {
				specCount++;
			}
			else if (isNumber(c)) {
				numCount++;
			}
		}
		if (passLen == capCount || passLen == numCount ||
				passLen == specCount || passLen == capCount + 1 ||
				passLen == numCount + 1 || passLen == specCount + 1 ||
				passLen == numCount + 2 || passLen == specCount + 2 || passLen == capCount + 2) {
			return 2;
		}
		score = (passLen <= 5) ? (score - 2) : (passLen < 8) ? (score - 1) : (passLen <= 10) ? (score + 1) : (score + 2);
		score = (capCount == 0) ?  score : (capCount < 3) ? (score + 1) : (capCount < 5) ? (score + 2) : (score + 3);
		score = (specCount == 0) ? score : (specCount < 2) ? (score + 1) : (specCount < 5) ? (score + 2) : (score + 3);
		score = (numCount == 0) ? score : (numCount < 2) ? (score + 1) : (numCount < 4) ? (score + 2) : (score + 3);
		return score < 0 ? 0 : Math.min(score, 10);
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