package com.walit.pass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

class UI implements Runner {

    protected int length = -1;
    protected int specialCharCount = -1;
    protected int capCount = -1;
    protected int numCount = -1;
    // TODO: Initialize local buttons, text-fields, etc for Manager to access
    private final Logger logger;
	private final Stage s;

    protected UI(Logger guiLog) {
		s = new Stage();
        this.logger = guiLog;
		s.start();

    }
    @Override
    public void run() {

        String os = System.getProperty("os.name");
		if (!(os.toLowerCase().contains("win"))) {
			System.err.println("Not a windows machine.");
			System.exit(1);
		}
		initializeMissingFilesForProgram();
		File logFile = new File(logFilePath);
		System.err.println(logFile.getName());
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
			System.err.println("Logger could not be initialized.\n\nPlease restart program.");
		}
		getHomeScreen();
		int x = getOption();
		while (x != 7) {
			switch (x) {
				case 1 -> {
					s.passwordGenerate();
					int[] params = getSpecs();
					length = params[0];
					capCount = params[1];
					specialCharCount = params[2];
					numCount = params[3];
					String[] temp = getUserInformation();
					getPassIdentifierFromUser(temp);
					x = getCompleteScreen(); // TODO: Figure this out
				}
				case 2 -> {
					findNamePassCombos();
					x = getCompleteScreen();
				}
				case 3 -> {
					extractInfoFromList();
					x = getCompleteScreen();
				}
				case 4 -> {
					strengthTest();
					x = getCompleteScreen();
				}
				case 5 -> {
					boolean choice = getChangeOrRemoveDecision();
					if (choice) changeData();
					else removeData();
					x = getCompleteScreen();
				}
				case 6 -> {
					String[] tempStr = getPasswordFromUser();
					getPassIdentifierFromUser(tempStr);
					x = getCompleteScreen();
				}
			}
		}
		shutdown();
		logger.log(Level.INFO, "Successful termination.");
		System.exit(0);
    }
    @Override
    public void resetParams() {
        length = -1;
        specialCharCount = -1;
        capCount = -1;
        numCount = -1;
    }
    protected void getHomeScreen() {
		s.home();
        // TODO: Display home menu where user can choose different functions of manager
    }
    protected int getOption() {
		for (int i = 0; i < 6; i++) {
			if (s.checker[i]) {
				Arrays.fill(s.checker, false);
				return i + 1;
			}
		}
        return 7;
    }
    protected int getCompleteScreen() {
		s.complete();
        // TODO: Display text telling user their task is finished, with back button to home screen
		return 0;
    }
    @Override
    public void shutdown() {
		s.dispose();
        //pM.dispose();
    }
    protected int[] getSpecs() {
        int[] res = new int[4];
        String l = s.lengthField.getText();
        String cap = s.capField.getText();
        String special = s.specField.getText();
        String numbers = s.numField.getText();
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
        }
		catch (NumberFormatException nFE) {
            logger.log(Level.INFO, "Integers must be entered for the parameters.");
        }
        while (!lSet || !capSet || !specSet || !numSet) {
            if (!lSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(0);
                lSet = validateParams(temp, 0);
                if (lSet) {
					res[0] = temp;
				}
            }
            if (!capSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(1);
                capSet = validateParams(temp, 1);
                if (lSet) {
					res[1] = temp;
				}
            }
            if (!specSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(2);
                specSet = validateParams(temp, 2);
                if (lSet) {
					res[2] = temp;
				}
            }
            if (!numSet) {
                // TODO: display error for user to see
                int temp = fetchNewVal(3);
                numSet = validateParams(temp, 3);
                if (lSet) {
					res[3] = temp;
				}
            }
        }

        return res;
    }
    protected int fetchNewVal(int index) {
        // TODO: Get new value for index given
        return index;
    }
    protected boolean validateParams(int val, int index) {
        // TODO: Verify params are ints and within legal range given index in int[]
        return true;
    }
    @Override
    public void findNamePassCombos() {
		Storage store = new Storage(logger);
		String search = s.searchForPass();
		ArrayList<String> acceptedStrings = store.checkStoredDataForName(search);
		if (!acceptedStrings.isEmpty()) {
            acceptedStrings.replaceAll(string -> string.replace(",", ":"));
			// TODO: Create list of password-username combos for user
			s.displayResults(acceptedStrings);
		}
		else {
            // Prompt user to try a dif search because there were no matches
			s.noResults(search);
			String retryString = ""; // TODO: Make button for yes and no
			if (retryString.toLowerCase().trim().equals("y")) {
				findNamePassCombos();
			}
		}

	}
    @Override
    public String[] getUserInformation() {
		Generator gen = new Generator(logger);
		String[] params = new String[2];
		params[1] = gen.generatePassword(length, specialCharCount, capCount, numCount);
        // Display password -> params[1]
		return params;
	}
    @Override
    public boolean getChangeOrRemoveDecision() {
        String choice = s.cOrR();
        return choice.equals("c") || choice.equals("change");
    }
    @Override
    public void changeData() {
		String val = s.changeVal();
		// TODO: Finish this
		s.finalizeChange();
    }
    @Override
    public void removeData() {
		String val = s.removeVal();
		// TODO: Finish this
		s.finalizeRemove();
    }
    @Override
    public String getPassIdentifierForChangeOrRemove(int x) {
        return null;
    }
    @Override
    public String[] getPasswordFromUser() {
		String[] userPass = new String[2];
		userPass[1] = s.addExisting();
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
			// TODO: Proper message (no comma or space in password)
			userPass[1] = s.addExisting();
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
    @Override
    public void getPassIdentifierFromUser(String[] arr) {
        // TODO: Prompt user on whether they want to save the password (Button for save, button for back
		arr[0] = s.nameGetter.getText(); // TODO: Get val from textField
		boolean problem = false;
		char[] checker;
		if (arr[0].equals("stop")) {
			getCompleteScreen();
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
                // TODO: Tell user they cannot have a comma in the name
                // TODO: Re-prompt user on whether they want to save it
				arr[0] = s.nameGetter.getText();
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
    @Override
    public void storeInformation(String[] info) {
		Storage store = new Storage(logger);
		String[] transferable = new String[2];
		String encodedName = Base64.getEncoder().encodeToString(info[0].getBytes());
		String encodedPwd = Base64.getEncoder().encodeToString(info[1].getBytes());
		transferable[0] = encodedName;
		transferable[1] = encodedPwd;
		store.storeData(transferable);
	}
    @Override
    public void strengthTest() {
		Generator gen = new Generator(logger);
        // TODO: Prompt user for password to test
		String password = s.strengthText();
		int score = gen.passwordStrengthScoring(password);
		s.strengthDisplay(score);
	}
    @Override
    public void extractInfoFromList() {
		Storage store = new Storage(logger);
		String[] combos = store.getUserPassCombosForUI();
		s.displayInfo(combos);
		// TODO: add values from combos to the interface to be displayed with scroll bar if needed
	}
}