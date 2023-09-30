package com.walit.pass;

import javax.swing.*;
import java.util.Arrays;

public class Stage extends JFrame {

    //TODO: Probably need a class for each of these panels, too much detail for them to all be put in this constructor.
    JPanel startPanel, homePanel, completePanel, genPanel, searchPanel, infoPanel, strengthPanel, cOrRPanel, addPanel;
    JTextField lengthField, capField, specField, numField, nameGetter, toStrengthTest;
    JButton genPass, search, displayInfo, strength, changeOrRem, addExist; // For home menu
    public boolean[] checker = new boolean[6];
    public Stage() {
        Arrays.fill(checker, false);
        this.setTitle("EasyPass");
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Initialize panels
    }
    public void start() {
        this.add(startPanel);
        this.pack();
        this.setVisible(true);

        startPanel.setVisible(false);
    }
    public void home() {
        // TODO: On button press, change corresponding val to true
        this.add(homePanel);
        this.pack();
        this.setVisible(true);

        homePanel.setVisible(false);
    }
    public void complete() {
        this.add(completePanel);
        this.pack();
        this.setVisible(true);

        completePanel.setVisible(false);
    }
    public void passwordGenerate() {
        this.add(genPanel);
        this.pack();
        this.setVisible(true);

        genPanel.setVisible(false);
    }
    public void searchForPass() { // TODO: One method for found results, one for no results
        this.add(searchPanel);
        this.pack();
        this.setVisible(true);

        searchPanel.setVisible(false);
    }
    public void displayInfo(String[] combos) {
        this.add(infoPanel);
        this.pack();
        this.setVisible(true);

        infoPanel.setVisible(false);
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
    public void cOrR() {
        this.add(cOrRPanel);
        this.pack();
        this.setVisible(true);

        cOrRPanel.setVisible(false);
    }
    public void addExisting() {
        this.add(addPanel);
        this.pack();
        this.setVisible(true);

        addPanel.setVisible(false);
    }
}
