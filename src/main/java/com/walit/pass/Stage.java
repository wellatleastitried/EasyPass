package com.walit.pass;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Stage extends JFrame {

    private final String resourceFolder = "resources\\images\\";
    JPanel startPanel, homePanel, genPanel, searchPanel, infoPanel, strengthPanel, cOrRPanel, additionPanel;
    JTextField lengthField, capField, specField, numField, nameGetter, toStrengthTest;
    JButton genPass, search, displayInfo, strength, changeOrRem, addExist, exitApp;
    String searchPass = "~Not~yet~entered~by~user";
    volatile boolean keyWasTyped = false;
    volatile boolean buttonPressed = false;
    Dimension dim = new Dimension(750, 750);
    public boolean[] checker = new boolean[7];

    public Stage() {
        // Array to handle choices, may change later
        Arrays.fill(checker, false);
        // Build frame
        this.setTitle("EasyPass");
        this.setResizable(false);
        this.setPreferredSize(dim);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(getApplicationIcon().getImage());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Look and feel error.");
        }
        // Build panels
        startPanel = buildStartPanel();
        homePanel = buildHomePanel();
        genPanel = buildGenPanel();
        searchPanel = buildSearchPanel();
        infoPanel = buildInfoPanel();
        strengthPanel = buildStrengthPanel();
        cOrRPanel = buildCOrRPanel();
        additionPanel = buildAdditionPanel();
    }
    protected ImageIcon getApplicationIcon() {
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(new File(resourceFolder + "lock.png"));
        }
        catch (IOException e) {
            System.err.println("Couldn't render image.");
        }
        assert icon != null;
        return new ImageIcon(icon);
    }
    protected JLabel getApplicationLogo() {
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File(resourceFolder + "logo.png"));
        }
        catch (IOException e) {
            System.err.println("Couldn't render images.");
        }
        assert logo != null;
        return new JLabel(new ImageIcon(new ImageIcon(logo).getImage().getScaledInstance(450, 450, Image.SCALE_DEFAULT)), SwingConstants.CENTER);
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
        genPass = new JButton("Generate password");
        setButtonStyle(genPass);
        genPass.addActionListener(e -> {
            buttonPressed = true;
            checker[0] = true;
        });
        buttonPanel.add(genPass);
        search = new JButton("Search for existing password");
        setButtonStyle(search);
        search.addActionListener(e -> {
            buttonPressed = true;
            checker[1] = true;
        });
        buttonPanel.add(search);
        displayInfo = new JButton("Display stored passwords");
        setButtonStyle(displayInfo);
        displayInfo.addActionListener(e -> {
            buttonPressed = true;
            checker[2] = true;
        });
        buttonPanel.add(displayInfo);
        strength = new JButton("Test password strength");
        setButtonStyle(strength);
        strength.addActionListener(e -> {
            buttonPressed = true;
            checker[3] = true;
        });
        buttonPanel.add(strength);
        changeOrRem = new JButton("Change or remove existing password");
        setButtonStyle(changeOrRem);
        changeOrRem.addActionListener(e -> {
            buttonPressed = true;
            checker[4] = true;
        });
        buttonPanel.add(changeOrRem);
        addExist = new JButton("Add existing password");
        setButtonStyle(addExist);
        addExist.addActionListener(e -> {
            buttonPressed = true;
            checker[5] = true;
        });
        buttonPanel.add(addExist);
        exitApp = new JButton("Exit");
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
    private void setButtonStyle(JButton p) {
        p.setForeground(Color.BLACK);
        p.setBorder(BorderFactory.createBevelBorder(1, Color.BLACK, Color.WHITE));
        p.setFocusPainted(false);
        p.setFont(new Font("Tahoma", Font.BOLD, 20));
    }
    public void home() {
        this.add(homePanel);
        this.pack();
        this.setVisible(true);
        while (!buttonPressed) {
            Thread.onSpinWait();
        }
        System.out.println("Button pressed.");
        this.remove(homePanel);
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
    private JPanel buildAdditionPanel() {
        /* TODO:
        Give the user username and password text fields to input their information, once they click a go button,
            confirm that the information is correct and store it.
         */
        return new JPanel();
    }
    private JPanel buildCOrRPanel() {
        /* TODO:
        Have two radio buttons at the top, depending which one is checked (c or r), change the below panel to fit the
            needs.
        For the c panel, there will need to be something similar to the search panel where the storage can check if
            the given search is even in the storage, if not re-prompt, else give the user a way to change it.
        For the r panel, there will need to be something similar to the above panel, but without the changing
            functionality at the end, instead removing it and displaying a confirmation.
         */
        return new JPanel();
    }
    private JPanel buildStrengthPanel() {
        /* TODO:
        Have a text field at the top, similar to the searchPanel search bar where the user can input a password to be
            assessed for its strength.
        Have a result panel, again, similar to the searchPanel, where the results are displayed.
         */
        return new JPanel();
    }
    private JPanel buildInfoPanel() {
        /* TODO:
        Display all user/pass combos in a scrollable (if needed) list for the user to look through.
         */
        return new JPanel();
    }
    private JPanel buildSearchPanel() {
        /* TODO:
        Search bar toward the top for the user to enter the associated name to search for its corresponding password.
        If found, display a list of user/pass combos that reflect the results, otherwise display message stating that
            there was not a username that fit the search.
         */
        return new JPanel();
    }
    private JPanel buildGenPanel() {
        /* TODO:
        Text fields for user to fill in the parameters in the top middle, labeled.
        Panel spanning the bottom 2/3 of the window that displays the generated password (once created) and
            how to store or discard it.
        Back button in the very top left that routes back to the main menu
         */
        JPanel panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.setBackground(Color.LIGHT_GRAY);



        panel.setVisible(true);
        return panel;
    }
    public void passwordGenerate() {
        this.add(genPanel);
        this.pack();
        this.setVisible(true);

        genPanel.setVisible(false);
        this.remove(genPanel);
    }
    public void displayResults(List<String> found) {
        // TODO: Display results from search
        searchPanel.setVisible(false);
    }
    public void noResults(String found) {
        // TODO: Display message stating no results were found from search
        searchPanel.setVisible(false);
    }
    public void searchHandle() {
        this.add(searchPanel);
        this.pack();
        this.setVisible(true);

        this.remove(searchPanel);
    }
    public String searchForPass() { // TODO: One method for found results, one for no results
        // Pull typed info and return it for UI
        return "";
    }
    public void displayInfo(String[] combos) {
        this.add(infoPanel);
        this.pack();
        this.setVisible(true);

        infoPanel.setVisible(false);
        this.remove(infoPanel);
    }
    public void strengthDisplay(int score) {

        strengthPanel.setVisible(false);
    }
    public String strengthText() {
        String pass = ""; // TODO
        this.add(strengthPanel);
        this.pack();
        this.setVisible(true);
        return pass;
    }
    public void finalizeChange() {
        // TODO: Display message confirming change and see if they want to change another
        cOrRPanel.setVisible(false);
    }
    public void finalizeRemove() {
        // TODO: Display message confirming removal and see if they want to remove another
        cOrRPanel.setVisible(false);
    }
    public String changeVal() { // TODO: Radio buttons near each choice, select the one to change
        String valToChange = ""; // TODO: Get choice of which to change
        return valToChange;
    }
    public String removeVal() { // TODO: Radio buttons near each choice, select the one to remove
        String valToRemove = ""; // TODO: Get choice of which to remove
        return valToRemove;
    }
    public String cOrR() {
        String choice = "";
        this.add(cOrRPanel);
        this.pack();
        this.setVisible(true);

        // TODO: Make sure val is "change, c, r, or remove
        return choice;
    }
    public String addExisting() {
        String pass = "";
        this.add(additionPanel);
        this.pack();
        this.setVisible(true);

        additionPanel.setVisible(false);
        // TODO: Confirm completion and offer to go back to home screen
        return pass;
    }
}
