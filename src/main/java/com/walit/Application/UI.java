package com.walit.Application;

import com.walit.Interface.GeneratorPanel;
import com.walit.Interface.SearchPanel;
import com.walit.Tools.Generator;
import com.walit.Tools.Storage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javax.swing.*;

public non-sealed class UI extends JFrame implements Runner {

    protected int length = -1;
    protected int specialCharCount = -1;
    protected int capCount = -1;
    protected int numCount = -1;
    private final Logger logger;
	private final String resourceFolder = "resources\\images\\";
	private final JPanel startPanel;
    volatile boolean keyWasTyped = false;
    volatile boolean buttonPressed = false;
    volatile boolean backButtonPressed = false;
    volatile boolean submitButtonPressed = false;
    volatile boolean storeButtonPressed = false;
    volatile boolean searchButtonPressed = false;
    Dimension dim = new Dimension(750, 750);
    public boolean[] checker = new boolean[7];
    public UI(Logger logger) {
        this.logger = logger;
		Arrays.fill(checker, false);

        this.setTitle("EasyPass");
        this.setResizable(false);
        this.setPreferredSize(dim);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(getApplicationIcon().getImage());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Error setting the \"look and feel\".");
        }

		startPanel = buildStartPanel();
		start();
    }
	public void start() {
        this.add(startPanel);
        this.pack();
        this.setVisible(true);
        while (!keyWasTyped) {
            Thread.onSpinWait();
        }
        System.out.println("Key was pressed: Application opening.");
        this.remove(startPanel);
    }
	public void home() {
        JPanel homePanel = buildHomePanel();
        this.add(homePanel);
        this.pack();
        this.setVisible(true);
        while (!buttonPressed) {
            Thread.onSpinWait();
        }
        System.out.println("Button pressed.");
        buttonPressed = false;
        this.remove(homePanel);
        homePanel.removeAll();
    }
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	protected JLabel getApplicationLogo() {
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File(resourceFolder + "logo.png"));
        }
        catch (IOException e) {
            System.err.println("Couldn't render images.");
            logger.log(Level.WARNING, "Couldn't render logo.");
        }
        assert logo != null;
        return new JLabel(new ImageIcon(new ImageIcon(logo).getImage().getScaledInstance(450, 450, Image.SCALE_DEFAULT)), SwingConstants.CENTER);
    }
	protected ImageIcon getApplicationIcon() {
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(new File(resourceFolder + "lock.png"));
        }
        catch (IOException e) {
            System.err.println("Couldn't render image.");
            logger.log(Level.WARNING, "Couldn't render icon.");
        }
        assert icon != null;
        return new ImageIcon(icon);
    }
	private JPanel buildStartPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { keyWasTyped = true; }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        JLabel startText = new JLabel("Press any key to continue...");
        startText.setFont(startText.getFont ().deriveFont (36.0f));
        panel.add(startText, BorderLayout.PAGE_END);
        JLabel logo = getApplicationLogo();
        logo.setPreferredSize(new Dimension(500, 500));
        panel.add(logo);
        panel.setVisible(true);
        return panel;
    }
	private JPanel buildHomePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.setBackground(Color.LIGHT_GRAY);
        int ROW = 7;
        int COL = 1;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new GridLayout(ROW, COL));
        buttonPanel.setPreferredSize(new Dimension(450, 500));
        JButton genPass = new JButton("Generate password");
        setButtonStyle(genPass);
        genPass.addActionListener(e -> {
            buttonPressed = true;
            checker[0] = true;
        });
        buttonPanel.add(genPass);
        JButton search = new JButton("Search for existing password");
        setButtonStyle(search);
        search.addActionListener(e -> {
            buttonPressed = true;
            checker[1] = true;
        });
        buttonPanel.add(search);
        JButton displayInfo = new JButton("Display stored passwords");
        setButtonStyle(displayInfo);
        displayInfo.addActionListener(e -> {
            buttonPressed = true;
            checker[2] = true;
        });
        buttonPanel.add(displayInfo);
        JButton strength = new JButton("Test password strength");
        setButtonStyle(strength);
        strength.addActionListener(e -> {
            buttonPressed = true;
            checker[3] = true;
        });
        buttonPanel.add(strength);
        JButton changeOrRem = new JButton("Change or remove existing password");
        setButtonStyle(changeOrRem);
        changeOrRem.addActionListener(e -> {
            buttonPressed = true;
            checker[4] = true;
        });
        buttonPanel.add(changeOrRem);
        JButton addExist = new JButton("Add existing password");
        setButtonStyle(addExist);
        addExist.addActionListener(e -> {
            buttonPressed = true;
            checker[5] = true;
        });
        buttonPanel.add(addExist);
        JButton exitApp = new JButton("Exit");
        setButtonStyle(exitApp);
        exitApp.addActionListener(e -> {
            buttonPressed = true;
            checker[6] = true;
        });
        buttonPanel.add(exitApp);
        buttonPanel.setVisible(true);
        panel.add(buttonPanel, BorderLayout.LINE_START);
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.LIGHT_GRAY);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(750, 150));
        JLabel title = new JLabel("Manage your passwords here!");
        title.setFont(title.getFont().deriveFont(36.0f));
        titlePanel.add(title, BorderLayout.CENTER);
        panel.add(titlePanel, BorderLayout.PAGE_START);
        return panel;
    }
    public void passwordGenerate() {
        GeneratorPanel gP = new GeneratorPanel();

        JPanel genPanel = gP.getPanel();

        JButton backButton = gP.getBackButton();
        backButton.addActionListener(e -> backButtonPressed = true);
        JButton submitButton = gP.getSubmitButton();
        submitButton.addActionListener(e -> submitButtonPressed = true);
        JButton storeButton = gP.getStorePasswordButton();
        storeButton.addActionListener(e -> storeButtonPressed = true);

        JLabel lengthError = gP.getLengthErrorLabel();
        JLabel capitalError = gP.getCapitalErrorLabel();
        JLabel specialError = gP.getSpecErrorLabel();
        JLabel digitError = gP.getDigitErrorLabel();

        JTextField usernameField = gP.getUsernameToStoreTF();
        JTextField lengthTF = gP.getLengthTF();
        JTextField capitalTF = gP.getCapitalTF();
        JTextField specialTF = gP.getSpecTF();
        JTextField digitTF = gP.getDigitTF();

        JTextArea displayedPassword = gP.getDisplayedPassword();

        this.add(genPanel);
        this.pack();
        this.setVisible(true);
        while (!backButtonPressed) {
            if (submitButtonPressed) {
                System.out.println("SUBMIT PRESSED.");
                String lengthText = lengthTF.getText();
                String capitalText = capitalTF.getText();
                String specialText = specialTF.getText();
                String digitText = digitTF.getText();
                boolean lengthIsNum = isNumeric(lengthText);
                lengthError.setVisible(!lengthIsNum);
                boolean capitalIsNum = isNumeric(capitalText);
                capitalError.setVisible(!capitalIsNum);
                boolean specIsNum = isNumeric(specialText);
                specialError.setVisible(!specIsNum);
                boolean digitIsNum = isNumeric(digitText);
                digitError.setVisible(!digitIsNum);
                if (lengthIsNum && capitalIsNum && specIsNum && digitIsNum) {
                    length = Integer.parseInt(lengthText);
                    capCount = Integer.parseInt(capitalText);
                    specialCharCount = Integer.parseInt(specialText);
                    numCount = Integer.parseInt(digitText);
                    boolean[] errorCodes = validateParams();
                    if (errorCodes[4]) {
                        for (int i = 0; i < errorCodes.length - 1; i++) {
                            switch (i) {
                                case 0 -> lengthError.setVisible(errorCodes[i]);
                                case 1 -> capitalError.setVisible(errorCodes[i]);
                                case 2 -> specialError.setVisible(errorCodes[i]);
                                case 3 -> digitError.setVisible(errorCodes[i]);
                            }
                        }
                    }
                    else {
                        lengthError.setVisible(false);
                        capitalError.setVisible(false);
                        specialError.setVisible(false);
                        digitError.setVisible(false);
                        String password = getUserInformation()[1];
                        displayedPassword.setText(password);
                    }
                }
                submitButtonPressed = false;
            }
            if (storeButtonPressed) {
                if (!displayedPassword.getText().isEmpty()) {
                    if (!usernameField.getText().isBlank()) {
                        System.out.println("Storing username and password.");
                        String[] userPassCombo = new String[] {usernameField.getText().toLowerCase(), displayedPassword.getText()};
                        try (Storage store = new Storage(logger)) {
                            store.storeData(userPassCombo);
                        }
                        catch (ClassNotFoundException e) {
                            logger.log(Level.SEVERE, "Error initializing database connection.");
                            shutdown();
                            System.exit(1);
                        }
                    }
                    else {
                        System.err.println("NO USERNAME TO STORE");
                    }
                }
                else {
                    System.err.println("NO PASSWORD TO STORE.");
                }
                storeButtonPressed = false;
            }
        }
        System.out.println("BACK PRESSED");
        backButtonPressed = false;
        genPanel.setVisible(false);
        this.remove(genPanel);
        genPanel.removeAll();
    }
    public void handleSearch() {
        SearchPanel sP = new SearchPanel();

        JPanel searchPanel = sP.getSearchPanel();
        JPanel displayPanel = sP.getDisplayPanel();

        JButton searchButton = sP.getSearchButton();
        searchButton.addActionListener(e -> searchButtonPressed = true);
        JButton backButton = sP.getBackButton();
        backButton.addActionListener(e -> backButtonPressed = true);

        JTextField enteredUsername = sP.getEnteredUsername();

        this.add(searchPanel);
        this.pack();
        this.setVisible(true);
        while (!backButtonPressed) {
            if (searchButtonPressed) {
                System.out.println("SEARCH PRESSED");
                if (!enteredUsername.getText().isBlank()) {
                    ArrayList<String> resultsOfQuery = new ArrayList<>();
                    try (Storage store = new Storage(logger)) {
                        resultsOfQuery = store.checkStoredDataForName(enteredUsername.getText().trim().toLowerCase());
                    }
                    catch (ClassNotFoundException e) {
                        logger.log(Level.SEVERE, "Error initializing database connection.");
                        shutdown();
                        System.exit(1);
                    }
                    if (!resultsOfQuery.isEmpty()) {
                        displayPanel.removeAll();
                        displayPanel.setLayout(new GridLayout(resultsOfQuery.size(), 1, 0, 2));
                        for (int i = 0; i < resultsOfQuery.size(); i++) {
                            JLabel value = getjLabel(resultsOfQuery, i);
                            displayPanel.add(value);
                        }
                        displayPanel.revalidate();
                        displayPanel.repaint();
                        searchPanel.revalidate();
                        searchPanel.repaint();
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "There were no matches to your search.\nTry a different username.",
                                "Notice",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "You must enter a name to search for.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                searchButtonPressed = false;
            }
        }
        System.out.println("BACK PRESSED");
        backButtonPressed = false;
        searchPanel.setVisible(false);
        this.remove(searchPanel);
        searchPanel.removeAll();
    }

    private static JLabel getjLabel(ArrayList<String> resultsOfQuery, int i) {
        String[] usernameAndPass = resultsOfQuery.get(i).split("~~SEPARATOR~~");
        JLabel value = new JLabel(
                String.format(
                        "[%d] Username: %s, Password: %s",
                        i + 1,
                        usernameAndPass[0],
                        usernameAndPass[1]
                )
        );
        value.setFont(new Font("Verdana", Font.PLAIN, 14));
        return value;
    }

    private void setButtonStyle(JButton p) {
        p.setForeground(Color.BLACK);
        p.setBorder(BorderFactory.createBevelBorder(1, Color.BLACK, Color.WHITE));
        p.setFocusPainted(false);
        p.setFont(new Font("Tahoma", Font.BOLD, 20));
    }
    @Override
    public void run() {
		int x = getOption();
		while (x != 7) {
			switch (x) {
				case 1 -> {
					passwordGenerate();
					x = getOption();
				}
				case 2 -> {
					handleSearch();
					findNamePassCombos(null);
					x = getOption();
				}
				case 3 -> {
					extractInfoFromList();
					x = getOption();
				}
				case 4 -> {
					strengthTest(true, null);
					x = getOption();
				}
				case 5 -> {
					boolean choice = getChangeOrRemoveDecision();
					if (choice) changeData();
					else removeData();
					x = getOption();
				}
				case 6 -> {
					String[] tempStr = getPasswordFromUser();
					getPassIdentifierFromUser(tempStr);
//                    handleAddition();
					x = getOption();
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
    private int getOption() {
		Arrays.fill(checker, false);
		home();
		Object[] testArrAndGetIndex = checkArr();
		boolean isPressed = (boolean) testArrAndGetIndex[0];
		int index = (int) testArrAndGetIndex[1];
		while (!isPressed) {
			testArrAndGetIndex = checkArr();
			isPressed = (boolean) testArrAndGetIndex[0];
			index = (int) testArrAndGetIndex[1];
		}
		if (index >= 0 && index <=6) {
			return index;
		}
        return 7;
    }
	private Object[] checkArr() {
		for (int i = 0; i < checker.length; i++) {
			if (checker[i]) {
				return new Object[] {true, i + 1};
			}
		}
		return new Object[] {false, -1};
	}
    @Override
    public void shutdown() {
		dispose();
    }

    private boolean[] validateParams() {
        boolean lengthError = false;
        boolean capitalError = false;
        boolean specError = false;
        boolean digitError = false;
        boolean hasError = false;
        if (length < 1) {
            lengthError = true;
            hasError = true;
            JOptionPane.showMessageDialog(
                    null,
                    "The length must be greater than one.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        if (length > 32) {
            lengthError = true;
            hasError = true;
            JOptionPane.showMessageDialog(
                    null,
                    "The length must be less than 33.\nTo generate a longer password, use the CLI.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        if (capCount < 0 || capCount > length) {
            capitalError = true;
            hasError = true;
        }
        if (specialCharCount < 0 || specialCharCount > length) {
            specError = true;
            hasError = true;
        }
        if (numCount < 0 || numCount > length) {
            digitError = true;
            hasError = true;
        }
        if (numCount + capCount + specialCharCount > length && !hasError) {
            capitalError = true;
            specError = true;
            digitError = true;
            hasError = true;
        }
        return new boolean[] {lengthError, capitalError, specError, digitError, hasError};
    }
    @Override
    public void findNamePassCombos(String passedName) {
		// TODO: wait on boolean JButton before trying to parse entry
        try (Storage store = new Storage(logger)) {
            String search = searchForPass();
            ArrayList<String> acceptedStrings = store.checkStoredDataForName(search);
            if (!acceptedStrings.isEmpty()) {
                acceptedStrings.replaceAll(string -> string.replace(",", ":"));
                // TODO: Create list of password-username combos for user
                displayResults(acceptedStrings);
            } else {
                // Prompt user to try a dif search because there were no matches
                noResults(search);
                String retryString = ""; // TODO: Make button for yes and no
                if (retryString.toLowerCase().trim().equals("y")) {
                    findNamePassCombos(null);
                }
            }
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error instantiating Storage object.");
        }
	}
    @Override
    public String[] getUserInformation() {
		Generator gen = new Generator(logger);
		String[] params = new String[2];
		params[1] = gen.generatePassword(length, specialCharCount, capCount, numCount);
        resetParams();
		return params;
	}
    @Override
    public boolean getChangeOrRemoveDecision() {
        String choice = cOrR();
        return choice.equals("c") || choice.equals("change");
    }
    @Override
    public void changeData() {
		finalizeChange();
    }
    @Override
    public void removeData() {
		finalizeRemove();
    }
    @Override
    public String getPassIdentifierForChangeOrRemove(int x) {
        return null;
    }
    @Override
    public String[] getPasswordFromUser() {
		String[] userPass = new String[2];
		userPass[1] = addExisting();
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
			userPass[1] = addExisting();
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
//		arr[0] = nameGetter.getText(); // TODO: Get val from textField
		boolean problem = false;
		char[] checker;
		if (!arr[0].equals("stop")) {
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
//				arr[0] = nameGetter.getText();
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
        try (Storage store = new Storage(logger)) {
            String[] transferable = new String[2];
            String encodedName = Base64.getEncoder().encodeToString(info[0].getBytes());
            String encodedPwd = Base64.getEncoder().encodeToString(info[1].getBytes());
            transferable[0] = encodedName;
            transferable[1] = encodedPwd;
            store.storeData(transferable);
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error instantiating Storage object.");
        }
	}
    @Override
    public void strengthTest(boolean isConsole, String pass) {
		Generator gen = new Generator(logger);
        // TODO: Prompt user for password to test
//		String password = toStrengthTest.getText();
//		int score = gen.passwordStrengthScoring(password);
//		strengthDisplay(score);
	}
    @Override
    public void extractInfoFromList() {
        try (Storage store = new Storage(logger)) {
            String[] combos = store.getUserPassCombosForUI();
            displayInfo(combos);
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error instantiating Storage object.");
        }
		// TODO: add values from combos to the interface to be displayed with scroll bar if needed
	}
	public void displayResults(ArrayList<String> found) {
        // TODO: Display results from search
    }
    public void noResults(String found) {
        // TODO: Display message stating no results were found from search
    }
    public String searchForPass() { // TODO: One method for found results, one for no results
        // Pull typed info and return it for UI
        return "";
    }
    public void displayInfo(String[] combos) {
        JPanel infoPanel = new JPanel();
        this.add(infoPanel);
        this.pack();
        this.setVisible(true);

        infoPanel.setVisible(false);
        this.remove(infoPanel);
    }
    public void strengthDisplay(int score) {
    }
    public String strengthText() {

        return "";
    }
    public void finalizeChange() {
        // TODO: Display message confirming change and see if they want to change another
    }
    public void finalizeRemove() {
        // TODO: Display message confirming removal and see if they want to remove another
    }
    public String cOrR() {
        JPanel cOrRPanel = new JPanel();
        String choice = "";
        this.add(cOrRPanel);
        this.pack();
        this.setVisible(true);

        // TODO: Make sure val is "change, c, r, or remove
        return choice;
    }
    public String addExisting() {
        JPanel additionPanel = new JPanel();
        String pass = "";
        this.add(additionPanel);
        this.pack();
        this.setVisible(true);

        additionPanel.setVisible(false);
        // TODO: Confirm completion and offer to go back to home screen
        return pass;
    }
}