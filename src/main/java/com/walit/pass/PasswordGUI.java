package com.walit.pass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

class PasswordGUI {

    protected int length;
    protected int specialCharCount;
    protected int capCount;
    protected int numCount;
    // TODO: Initialize local buttons, text-fields, etc for Manager to access
    JTextField lengthField;
    JTextField capField;
    JTextField specField;
    JTextField numField;
    JButton genPass;
    JButton search;
    JButton displayInfo;
    JButton strength;
    JButton changeOrRem;
    JButton addExist;

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

}
//Methods to create
/*
p.getInformation();
p.finalizeName(tempStr);
p.searchFor();
p.extractInfoFromList();
p.strengthTest();
p.changeOrRem();
p.changeInfo();
p.removeInfo();
p.getPassFromUser();
*/