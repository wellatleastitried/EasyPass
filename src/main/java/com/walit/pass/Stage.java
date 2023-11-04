package com.walit.pass;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Stage extends JFrame {

    JPanel startPanel, homePanel, completePanel, genPanel, searchPanel, infoPanel, strengthPanel, cOrRPanel, additionPanel;
    JTextField lengthField, capField, specField, numField, nameGetter, toStrengthTest;
    JButton genPass, search, displayInfo, strength, changeOrRem, addExist; // For home menu
    volatile boolean keyWasTyped = false;
    Dimension dim = new Dimension(750, 750);
    public boolean[] checker = new boolean[6];

    public Stage() {
        // Array to handle choices, may change later
        Arrays.fill(checker, false);

        // Build frame
        this.setTitle("EasyPass");
        this.setResizable(false);
        this.setPreferredSize(dim);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(getApplicationIcon().getImage());

        // Build panels
        startPanel = buildStartPanel();
        homePanel = buildHomePanel();
        completePanel = buildCompletePanel();
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
            icon = ImageIO.read(new File("resources\\images\\lock.png"));
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
            logo = ImageIO.read(new File("resources\\images\\logo.png"));
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
            public void keyTyped(KeyEvent e) {
                keyWasTyped = true;
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        JLabel startText = new JLabel("Press any key to continue...");
        startText.setFont (startText.getFont ().deriveFont (36.0f));
        panel.add(startText, BorderLayout.PAGE_END);
        JLabel logo = getApplicationLogo();
        logo.setPreferredSize(new Dimension(500, 500));
        panel.add(logo);
        panel.setVisible(true);
        return panel;
    }

    public void start() {
        this.add(startPanel);
        this.pack();
        this.setVisible(true);
        while (!keyWasTyped) {
            Thread.onSpinWait();
        }
        System.out.println("Key was pressed: Application opening.");
        home();
        //startPanel.setVisible(false);
    }

    private JPanel buildAdditionPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildCOrRPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildStrengthPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildInfoPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildSearchPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildGenPanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildCompletePanel() {
        JPanel panel = new JPanel();

        return panel;
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel();

        return panel;
    }


    public void home() {
        // TODO: On button press, change corresponding val to true
        this.add(homePanel);
        this.pack();
        //p.setVisible(false);
        this.setVisible(true);

        homePanel.setVisible(false);
    }
    public void complete() {
        this.add(completePanel);
        this.pack();
        this.setVisible(true);
        // TODO: Message that operation is complete with back button to go back to home screen.
        completePanel.setVisible(false);
    }
    public void passwordGenerate() {
        this.add(genPanel);
        this.pack();
        this.setVisible(true);

        genPanel.setVisible(false);
        complete();
    }
    public void displayResults(List<String> found) {
        // TODO: Display results from search
        searchPanel.setVisible(false);
        complete();
    }
    public void noResults(String found) {
        // TODO: Display message stating no results were found from search
        searchPanel.setVisible(false);
        complete();
    }
    public String searchForPass() { // TODO: One method for found results, one for no results
        String search = "";
        this.add(searchPanel);
        this.pack();
        this.setVisible(true);

        return search;
    }
    public void displayInfo(String[] combos) {
        this.add(infoPanel);
        this.pack();
        this.setVisible(true);

        infoPanel.setVisible(false);
        complete();
    }
    public void strengthDisplay(int score) {

        strengthPanel.setVisible(false);
        complete();
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
        complete();
    }
    public void finalizeRemove() {
        // TODO: Display message confirming removal and see if they want to remove another
        cOrRPanel.setVisible(false);
        complete();
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
        complete();
        return pass;
    }
}
