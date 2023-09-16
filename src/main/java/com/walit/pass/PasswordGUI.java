package com.walit.pass;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

class PasswordGUI {

    protected int length;
    protected int specialCharCount;
    protected int capCount;
    protected int numCount;
    // TODO: Initialize local buttons, text-fields, etc for Manager to access
    JTextField lengthField, capField, specField, numField, nameGetter, toStrengthTest;
    JButton genPass, search, displayInfo, strength, changeOrRem, addExist;

    private final Logger logger;

    protected PasswordGUI(Logger guiLog) {
        this.logger = guiLog;
        // TODO: Initialize start screen
        getStartScreen();
    }
    protected void getStartScreen() {
        // TODO: Display logo, action listener on all keys, if pressed -> getHomeScreen()
    }
    protected void getHomeScreen() {
        // TODO: Display home menu where user can choose different functions of manager
    }
    protected int getOption() {
        int choice = 0; // Only initialized so the package compiles
        // TODO: Get choice from JButtons, depending on button clicked, return corresponding int
        return choice;
    }
    protected void getCompleteScreen() {
        // TODO: Display text telling user their task is finished, with back button to home screen
    }
    protected int[] getSpecs() {
        int[] res = new int[4];
        String l = lengthField.getText();
        String cap = capField.getText();
        String special = specField.getText();
        String numbers = numField.getText();
        boolean lSet = false;
        boolean capSet = false;
        boolean specSet = false;
        boolean numSet = false;
        int attempt;
        try {
            attempt = Integer.parseInt(l);
            lSet = validateParams(attempt, 0);
            attempt = Integer.parseInt(cap);
            capSet = validateParams(attempt, 1);
            attempt = Integer.parseInt(special);
            specSet = validateParams(attempt, 2);
            attempt = Integer.parseInt(numbers);
            numSet = validateParams(attempt, 3);
        } catch (NumberFormatException nFE) {
            logger.log(Level.INFO, "Integers must be entered for the parameters.");
        }
        while (!lSet || !capSet || !specSet || !numSet) {
            if (!lSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(0);
                lSet = validateParams(temp, 0);
                if (lSet) res[0] = temp;
            }
            if (!capSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(1);
                capSet = validateParams(temp, 1);
                if (lSet) res[1] = temp;
            }
            if (!specSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(2);
                specSet = validateParams(temp, 2);
                if (lSet) res[2] = temp;
            }
            if (!numSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(3);
                numSet = validateParams(temp, 3);
                if (lSet) res[3] = temp;
            }
        }

        return res;
    }
    protected int fetchNewVal(int index) {
        // TODO: Get new value for index given
        return 0;
    }
    protected boolean validateParams(int val, int index) {
        // TODO: Verify params are ints and within legal range given index in int[]
        return true;
    }
    protected void searchFor() {
		Storage pS = new Storage(logger);
		// Enter the name for the password you are looking for
        String name = ""; // TODO: Change to take value from textField
		ArrayList<String> acceptedStrings = pS.findInfo(name);
		if (acceptedStrings.size() == 1) {
			String[] values = acceptedStrings.get(0).split(", ", 2);
            // TODO: Display password for user
		} else if (acceptedStrings.size() > 1) {

			for (String matchingPasswords : acceptedStrings) {
				matchingPasswords = matchingPasswords.replace(",", ":");
                // TODO: Create list of password-username combos for user
			}

		} else {
            // Prompt user to try a dif search because there were no matches
			String retryString = ""; // TODO: Make button for yes and no
			if (retryString.toLowerCase().trim().equals("y")) {
				searchFor();
			}
		}
	}
    protected String[] getInformation() {
		Generator pGen = new Generator(logger);
		String[] params = new String[2];
		params[1] = pGen.generatePassword(length, specialCharCount, capCount, numCount);
        // Display password -> params[1]
		return params;
	}
    protected void finalizeName(String[] arr) {
        // TODO: Prompt user on whether they want to save the password (Button for save, button for back
		arr[0] = nameGetter.getText(); // TODO: Get val from textField
		boolean problem = false;
		char[] checker;
		if (arr[0].equals("stop")) {
			getCompleteScreen();
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
                // TODO: Tell user they cannot have a comma in the name
                // TODO: Re-prompt user on whether they want to save it
				arr[0] = nameGetter.getText();
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
    private void storeInformation(String[] info) {
		Storage pS = new Storage(logger);
		String[] transferable = new String[2];
		String encodedName = Base64.getEncoder().encodeToString(info[0].getBytes());
		String encodedPwd = Base64.getEncoder().encodeToString(info[1].getBytes());
		transferable[0] = encodedName;
		transferable[1] = encodedPwd;
		pS.storeInfo(transferable);
	}
    private void strengthTest() {
		Generator pG = new Generator(logger);
        // TODO: Prompt user for password to test
		String password = toStrengthTest.getText();
		int score = pG.passwordStrengthScoring(password);
		if (score == 0) {
            // TODO: Display the score of the password
		}
		else if (score >= 1 && score <= 5) {
			// TODO: Display the score of the password
		}
		else if (score > 5 && score <= 8) {
			// TODO: Display the score of the password
		}
		else if (score == 9) {
			// TODO: Display the score of the password
		}
		else {
			// TODO: Display the score of the password
		}
	}
    private void extractInfoFromList() {
		Storage pS = new Storage(logger);
		pS.getInfo(); //pS.getInfoGUI();
	}

}

/*
Methods to create:
p.changeOrRem();
p.changeInfo();
p.removeInfo();
p.getPassFromUser();
*/