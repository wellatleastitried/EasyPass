package com.walit.pass;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class Stage extends JFrame {

    //TODO: Probably need a class for each of these panels, too much detail for them to all be put in this constructor.
    JPanel startPanel, homePanel, completePanel, genPanel, searchPanel, infoPanel, strengthPanel, cOrRPanel, additionPanel;
    JTextField lengthField, capField, specField, numField, nameGetter, toStrengthTest;
    JButton genPass, search, displayInfo, strength, changeOrRem, addExist; // For home menu
    public boolean[] checker = new boolean[6];
    public Stage() {
        Arrays.fill(checker, false); //on button click, becomes true, after operation, becomes false again.
        this.setTitle("EasyPass");
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Initialize panels
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

    private JPanel buildStartPanel() {
        JPanel panel = new JPanel();

        return panel;
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
