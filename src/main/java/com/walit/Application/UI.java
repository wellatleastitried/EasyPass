package com.walit.Application;

import com.walit.Interface.*;
import com.walit.Tools.Generator;
import com.walit.Tools.Storage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

import javax.imageio.ImageIO;

import javax.swing.*;

public non-sealed class UI extends JFrame implements Runner {

    protected int length = -1;
    protected int specialCharCount = -1;
    protected int capCount = -1;
    protected int numCount = -1;
    private final Logger logger = Logger.getLogger("ManagerLog");
	private final String resourceFolder = "resources" + fs + "images" + fs;
	private final JPanel startPanel;
    volatile boolean keyWasTyped = false;
    volatile boolean buttonPressed = false;
    volatile boolean backButtonPressed = false;
    volatile boolean submitButtonPressed = false;
    volatile boolean storeButtonPressed = false;
    volatile boolean searchButtonPressed = false;
    Dimension dim = new Dimension(750, 750);
    public boolean[] checker = new boolean[7];
    public UI() {
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
//        panel.setBackground(new Color(12, 196, 43));
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
    private void handleUpdate() {
        try {
            URL url = new URL("https://api.github.com/repos/wellatleastitried/EasyPass/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.err.printf("Http response code : %d%n", responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                // TODO: parse the JSON response and extract the version information from it
                String jsonResponse = response.toString();
                String latestVersion = "";
                String currentVersion = breakVersionString(new CLI().getVersionInfo());
                System.out.println(currentVersion);
                if (!latestVersion.equals(currentVersion)) {
                    JOptionPane.showConfirmDialog(
                            null,
                            "An update is available!\nWould you like to download it now?",
                            "Update",
                            JOptionPane.YES_NO_OPTION
                    );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Your application is up to date.",
                            "Update",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
            else {
                notYetAvailable();
            }
        }
        catch (MalformedURLException mUE) {
            logger.log(Level.WARNING, "Error reaching URL.");
        } catch (ProtocolException e) {
            logger.log(Level.WARNING, "ProtocolException while getting response from Github.");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error receiving data from http connection.");
        }
    }

    private String breakVersionString(String fullString) {
        return fullString.substring(fullString.indexOf("-") + 1);
    }

    private void handleEncryptionSetup() {
        /*
        TODO: Take a user input, generate a key with it, save it to a config file,
         restart program and use it to encrypt and decrypt entries in db
         */
        notYetAvailable();
    }
	private JPanel buildHomePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.setBackground(Color.LIGHT_GRAY);
//        panel.setBackground(new Color(12, 196, 43));
        JMenuBar menuBar = getjMenuBar();
        this.setJMenuBar(menuBar);
        int ROW = 7;
        int COL = 1;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
//        buttonPanel.setBackground(new Color(12, 196, 43));
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
//        titlePanel.setBackground(new Color(12, 196, 43));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(750, 150));
        JLabel title = new JLabel("Manage your passwords here!");
        title.setFont(title.getFont().deriveFont(36.0f));
        titlePanel.add(title, BorderLayout.CENTER);
        panel.add(titlePanel, BorderLayout.PAGE_START);
        return panel;
    }

    private JMenuBar getjMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu settings = new JMenu("Settings");
        JMenuItem update = new JMenuItem("Check for updates");
        update.addActionListener(e -> handleUpdate());
        JMenuItem encryptionSetup = new JMenuItem("Setup password for encryption");
        encryptionSetup.addActionListener(e -> handleEncryptionSetup());
        settings.add(update);
        settings.add(encryptionSetup);
        menuBar.add(settings);
        return menuBar;
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
            Thread.onSpinWait();
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
                        int choice = JOptionPane.showConfirmDialog(
                                null,
                                String.format("Is the following information correct?\nUsername: %s\nPassword: %s", userPassCombo[0], userPassCombo[1]),
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (choice == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(null, "Username and password have been saved.");
                            storeInformation(userPassCombo);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Your information has been discarded.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "You must enter a username to store.",
                                "Notice",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "You must generate a password to be stored.",
                            "Notice",
                            JOptionPane.INFORMATION_MESSAGE
                    );
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
            Thread.onSpinWait();
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

    public void handleAddition() {
        AddExisting aE = new AddExisting();

        JPanel addExistingPanel = aE.getAddExistingPanel();

        JTextField usernameTF = aE.getUsernameTF();
        JTextField passwordTF = aE.getPasswordTF();

        JButton submitButton = aE.getSubmitButton();
        submitButton.addActionListener(e -> submitButtonPressed = true);
        JButton backButton = aE.getBackButton();
        backButton.addActionListener(e -> backButtonPressed = true);

        this.add(addExistingPanel);
        this.pack();
        this.setVisible(true);
        while (!backButtonPressed) {
            Thread.onSpinWait();
            if (submitButtonPressed) {
                System.out.println("SUBMIT PRESSED");
                if (!passwordTF.getText().isBlank()) {
                    if (!usernameTF.getText().isBlank()) {
                        String[] userPassCombo = new String[] {usernameTF.getText(), passwordTF.getText()};
                        int choice = JOptionPane.showConfirmDialog(
                                null,
                                String.format("Confirm the following information is correct:\nUsername: %s\nPassword: %s", userPassCombo[0], userPassCombo[1]),
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (choice == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(null, "Information will be saved.");
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
                            JOptionPane.showMessageDialog(null, "Operation has been cancelled.");
                        }
                    }
                    else {
                        System.out.println("No username provided.");
                        JOptionPane.showMessageDialog(
                                null,
                                "You must provide a username to store.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                else {
                    System.out.println("No password provided.");
                    JOptionPane.showMessageDialog(
                            null,
                            "You must provide a password to store.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                submitButtonPressed = false;
            }
        }
        System.out.println("BACK PRESSED");
        backButtonPressed = false;
        addExistingPanel.setVisible(false);
        this.remove(addExistingPanel);
        addExistingPanel.removeAll();
    }

    public void handleStrength() {
        StrengthPanel sP = new StrengthPanel();

        JPanel strengthPanel = sP.getStrengthPanel();
        JPanel resultPanel = sP.getResultPanel();

        JButton submitButton = sP.getSubmitButton();
        submitButton.addActionListener(e -> submitButtonPressed = true);
        JButton backButton = sP.getBackButton();
        backButton.addActionListener(e -> backButtonPressed = true);

        JTextField passToCheck = sP.getPasswordTF();

        JLabel strengthComment = sP.getStrengthComment();

        JProgressBar strengthResult = sP.getPasswordStrength();

        this.add(strengthPanel);
        this.pack();
        this.setVisible(true);
        while (!backButtonPressed) {
            Thread.onSpinWait();
            if (submitButtonPressed) {
                System.out.println("SUBMIT PRESSED");
                if (!passToCheck.getText().isBlank()) {
                    strengthResult.setIndeterminate(true);
                    strengthComment.setText("Searching through word lists to ensure the security of the password...");
                    strengthComment.setVisible(true);
                    strengthPanel.revalidate();
                    strengthPanel.repaint();
                    resultPanel.revalidate();
                    resultPanel.repaint();
                    int score = strengthTest(passToCheck.getText());
                    strengthResult.setIndeterminate(false);
                    strengthResult.setValue(100);
                    strengthResult.setValue(score * 10);
                    String comment = switch (score) {
                        case -1 -> String.format("Your password exists in previous data breaches, you may want to change it: %d/10", 0);
                        case 0 -> String.format("Your password is very weak: %d/10", score);
                        case 1, 2, 3, 4 -> String.format("Your password is somewhat weak: %d/10", score);
                        case 5, 6, 7, 8 -> String.format("Your password is somewhat strong: %d/10", score);
                        case 9, 10 -> String.format("Your password is very strong: %d/10", score);
                        default -> "Error";
                    };
                    if (!comment.equals("Error")) {
                        strengthComment.setText(comment);
                        strengthComment.setVisible(true);
                        strengthPanel.revalidate();
                        strengthPanel.repaint();
                        resultPanel.revalidate();
                        resultPanel.repaint();
                    }
                    else {
                        strengthComment.setVisible(false);
                        JOptionPane.showMessageDialog(
                                null,
                                "An error has occurred while testing your password.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        logger.log(Level.WARNING, "Error in strengthTest(), ensure correct values");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "You must enter a password to test the strength of.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                submitButtonPressed = false;
            }

        }
        System.out.println("BACK PRESSED");
        backButtonPressed = false;
        strengthPanel.setVisible(false);
        this.remove(strengthPanel);
        strengthPanel.removeAll();
    }

    public void handleDisplayAll() {
        DisplayAllPanel dAP = new DisplayAllPanel();

        JPanel displayPanel = dAP.getDisplayPanel();

        JList<String> listOfQueries = getListOfQueries();
        JScrollPane scrollPane = dAP.getScrollPane();
        scrollPane.setViewportView(listOfQueries);

        JButton backButton = dAP.getBackButton();
        backButton.addActionListener(e -> backButtonPressed = true);

        this.add(displayPanel);
        this.pack();
        this.setVisible(true);
        while (!backButtonPressed) {
            Thread.onSpinWait();
        }
        System.out.println("BACK PRESSED");
        backButtonPressed = false;
        displayPanel.setVisible(false);
        this.remove(displayPanel);
        displayPanel.removeAll();
    }

    public JList<String> getListOfQueries() {
        try (Storage store = new Storage(logger)) {
            String[] resultOfQueries = store.getUserPassCombosForUI();
            for (int i = 0; i < resultOfQueries.length; i++) {
                String[] split = resultOfQueries[i].split("~~SEPARATOR~~");
                resultOfQueries[i] = String.format("Username: %s, Password: %s", split[0], split[1]);
            }
            return new JList<>(resultOfQueries);
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Unable to connect to database.");
        }
        return null;
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
					x = getOption();
				}
				case 3 -> {
                    handleDisplayAll();
					x = getOption();
				}
				case 4 -> {
					handleStrength();
					x = getOption();
				}
				case 5 -> {
//                    notYetAvailable();
					int choice = getChangeOrRemoveDecision();
					if (choice == 0) System.out.println("Change pressed.");//changeData();
					else if (choice == 1) System.out.println("Remove pressed.");//removeData();
					x = getOption();
				}
				case 6 -> {
                    handleAddition();
					x = getOption();
				}
			}
		}
		shutdown();
		logger.log(Level.INFO, "Successful termination.");
		System.exit(0);
    }

    public void notYetAvailable() {
        JOptionPane.showMessageDialog(
                null,
                "This feature is coming soon!",
                "Alert",
                JOptionPane.ERROR_MESSAGE
        );
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
		this.dispose();
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
        if (length > 20) {
            lengthError = true;
            hasError = true;
            JOptionPane.showMessageDialog(
                    null,
                    "The length must be less than 20.\nTo generate a longer password, use the CLI.",
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
    public String[] getUserInformation() {
		Generator gen = new Generator(logger);
		String[] params = new String[2];
		params[1] = gen.generatePassword(length, specialCharCount, capCount, numCount);
        resetParams();
		return params;
	}
    public int getChangeOrRemoveDecision() {
        AtomicInteger getButtonPress = new AtomicInteger(-1);
        ChangeRemovePanel cRP = new ChangeRemovePanel();
        JPanel changeRemovePanel = cRP.getQuestionPanel();

        JButton removeButton = cRP.getRemoveButton();
        removeButton.addActionListener(e -> getButtonPress.set(1));
        JButton changeButton = cRP.getChangeButton();
        changeButton.addActionListener(e -> getButtonPress.set(0));
        JButton backButton = cRP.getBackButton();
        backButton.addActionListener(e -> getButtonPress.set(2));

        this.add(changeRemovePanel);
        this.pack();
        this.setVisible(true);
        while (getButtonPress.get() == -1) {
            Thread.onSpinWait();
        }
        changeRemovePanel.setVisible(false);
        this.remove(changeRemovePanel);
        changeRemovePanel.removeAll();
        return getButtonPress.get();
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
    public void storeInformation(String[] info) {
        try (Storage store = new Storage(logger)) {
            store.storeData(info);
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error initializing database connection.");
            shutdown();
            System.exit(1);
        }
	}
    public int strengthTest(String pass) {
		return new Generator(logger).passwordStrengthScoring(pass);
	}
    public void finalizeChange() {
        // TODO: Display message confirming change and see if they want to change another
    }
    public void finalizeRemove() {
        // TODO: Display message confirming removal and see if they want to remove another
    }
}