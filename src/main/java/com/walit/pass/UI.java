package com.walit.pass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

class UI implements Runner {

    public String bSlash = File.separator;
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
        // TODO: Initialize start screen
        getStartScreen();
    }
    @Override
    public void run() {
        // TODO: Remove this block, only here while testing
        System.out.println("Called.");
		System.exit(0);

        String os = System.getProperty("os.name");
		if (!(os.toLowerCase().contains("win"))) {
			System.err.println("Not a windows machine.");
			System.exit(1);
		}
		initializeFilesForProgram();
		File logFile = new File("resources" + bSlash + "utilities" + bSlash + "log" + bSlash + "PassMan.log");
		System.err.println(logFile.getName());
		FileHandler fH;
		try {
			if (logFile.exists() && logFile.isFile()) {
				new FileWriter(logFile, false).close();
			}
			fH = new FileHandler("resources" + bSlash + "utilities" + bSlash + "log" + bSlash + "PassMan.log", true);
			while (logger.getHandlers().length > 0) {
				logger.removeHandler(logger.getHandlers()[0]);
			}
			logger.addHandler(fH);
			fH.setLevel(Level.INFO);
			XMLFormatter xF = new XMLFormatter();
			fH.setFormatter(xF);
			logger.log(Level.INFO, "Successful startup.");
		} catch (IOException e) {
			System.err.println("Logger could not be initialized.\n\nPlease restart program.");
		}
		Stage s = new Stage();
		getStartScreen();
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
					String[] temp = getInformation();
					finalizeName(temp);
					x = getCompleteScreen(); // TODO: Figure this out
				}
				case 2 -> {
					searchFor();
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
					boolean choice = changeOrRem();
					if (choice) changeInfo();
					else removeInfo();
					x = getCompleteScreen();
				}
				case 6 -> {
					String[] tempStr = getPassFromUser();
					finalizeName(tempStr);
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
    protected void getStartScreen() {
		s.start();
        // TODO: Display logo, action listener on all keys, if pressed -> run() -> getHomeScreen()
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
        return index;
    }
    protected boolean validateParams(int val, int index) {
        // TODO: Verify params are ints and within legal range given index in int[]
        return true;
    }
    @Override
    public void searchFor() {
		Storage store = new Storage(logger);
		String search = s.searchForPass();
		ArrayList<String> acceptedStrings = store.findInfo(search);
		if (!acceptedStrings.isEmpty()) {
            acceptedStrings.replaceAll(string -> string.replace(",", ":"));
			// TODO: Create list of password-username combos for user
			s.displayResults(acceptedStrings);
		} else {
            // Prompt user to try a dif search because there were no matches
			s.noResults(search);
			String retryString = ""; // TODO: Make button for yes and no
			if (retryString.toLowerCase().trim().equals("y")) {
				searchFor();
			}
		}

	}
    @Override
    public String[] getInformation() {
		Generator gen = new Generator(logger);
		String[] params = new String[2];
		params[1] = gen.generatePassword(length, specialCharCount, capCount, numCount);
        // Display password -> params[1]
		return params;
	}
    @Override
    public boolean changeOrRem() {
        String choice = s.cOrR();
        return choice.equals("c") || choice.equals("change");
    }
    @Override
    public void changeInfo() {
		String val = s.changeVal();
		// TODO: Finish this
		s.finalizeChange();
    }
    @Override
    public void removeInfo() {
		String val = s.removeVal();
		// TODO: Finish this
		s.finalizeRemove();
    }
    @Override
    public String getUserNameForAlter(int x) {
        return null;
    }
    @Override
    public String[] getPassFromUser() {
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
				} else {
					fixed = false;
					break;
				}
			}
			if (fixed) problem = false;
		}
		return userPass;
    }
    @Override
    public void finalizeName(String[] arr) {
        // TODO: Prompt user on whether they want to save the password (Button for save, button for back
		arr[0] = s.nameGetter.getText(); // TODO: Get val from textField
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
				arr[0] = s.nameGetter.getText();
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
    @Override
    public void storeInformation(String[] info) {
		Storage store = new Storage(logger);
		String[] transferable = new String[2];
		String encodedName = Base64.getEncoder().encodeToString(info[0].getBytes());
		String encodedPwd = Base64.getEncoder().encodeToString(info[1].getBytes());
		transferable[0] = encodedName;
		transferable[1] = encodedPwd;
		store.storeInfo(transferable);
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
		String[] combos = store.getInfoUI();
		s.displayInfo(combos);
		// TODO: add values from combos to the interface to be displayed with scroll bar if needed
	}
    @Override
    public void initializeFilesForProgram() {
		String ls = System.getProperty("line.separator");
		File storeDir = new File("resources" + bSlash + "utilities" + bSlash + "log");
		File logDir = new File("resources" + bSlash + "utilities" + bSlash + "data");
		File wordLists = new File("resources" + bSlash + "WordLists");
		File[] dirs = new File[3];
		dirs[0] = storeDir;
		dirs[1] = logDir;
		dirs[2] = wordLists;
		try {
			for (File directory : dirs) {
				if (!(directory.exists())) {
					boolean checkDirCreation = directory.mkdirs();
					if (!checkDirCreation) logger.log(Level.SEVERE, "Error creating directory, please restart now.");
				}
			}
		} catch (SecurityException sE) {
			logger.log(Level.WARNING, "IO exception while making directories.");
		} catch (NullPointerException nPE) {
			logger.log(Level.WARNING, "Null pointer exception while initializing directories.");
		}
		File info = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
		File vec = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "iVSTAH");
		File passMan = new File("resources" + bSlash + "utilities" + bSlash + "log" + bSlash + "PassMan.log");
		File inst = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "vSTAH");
		File[] files = new File[4];
		files[0] = info;
		files[1] = vec;
		files[2] = passMan;
		files[3] = inst;
		try {
			for (int i = 0; i < files.length; i++) {
				if (!(files[i].exists() && files[i].isFile())) {
					boolean checkFileCreation = files[i].createNewFile();
					if (!checkFileCreation) logger.log(Level.SEVERE, "Could not initialize files for program.");
					if (i == 3 && files[i].length() == 0) {
						try {
							BufferedWriter bW = new BufferedWriter(new FileWriter(files[i]));
							String hex = "3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D22796573223F3E0A3C7061727365643E0A202020203C696E666F3E0A20202020202020203C70726F643E45617379506173733C2F70726F643E0A20202020202020203C76657273696F6E3E302E312E303C2F76657273696F6E3E0A20202020202020203C7061643E4145532F4342432F504B43533550414444494E473C2F7061643E0A20202020202020203C7374723E363544343142434145343436304235343138344335393034363644333030304645423742444246324138393841373436453745303642464535333538363846453C2F7374723E0A202020203C2F696E666F3E0A3C2F7061727365643E";
							bW.write(new String(deHex(hex), StandardCharsets.UTF_8));
							bW.write(ls);
							bW.flush();
							bW.close();
						} catch (IOException e) {
							logger.log(Level.SEVERE, "Error initializing data file.");
						}
					}
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO exception while creating new files for program.");
		}
    }
	private byte[] deHex(String string) {
		byte[] cipherText = new byte[string.length() / 2];
		for (int i = 0; i < cipherText.length; i++) {
        	int index = i * 2;
	        int val = Integer.parseInt(string.substring(index, index + 2), 16);
	        cipherText[i] = (byte) val;
        }
        return cipherText;
	}
}