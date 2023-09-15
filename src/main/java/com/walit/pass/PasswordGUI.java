package com.walit.pass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

class PasswordGUI {

    private final Logger logger;

    protected PasswordGUI(Logger guiLog) {
        this.logger = guiLog;
    }
    protected int[] getSpecs() {
        int[] res = new int[4];
        String l = lengthField.getText();
        String cap = capField.geteText();
        String special = specField.getText();
        String nums = specField.getText();
        boolean lSet;
        boolean capSet;
        boolean specSet;
        boolean numSet;
        try {
            res[0] = Integer.parseInt(l);
            lSet = true;
            res[1] = Integer.parseInt(cap);
            capSet = true;
            res[2] = Integer.parseInt(special);
            specSet = true;
            res[3] = Integer.parseInt(nums);
            numSet = true;
        } catch (NumberFormatException nFE) {
            logger.log(Level.INFO, "Integers must be entered for the parameters.");
        }
        while (!lSet || !capSet || !specSet || !numSet) {
            if (!lSet) {
                //display error for user to see
                int[] temp = getSpecs();
                res[0] = temp[0];
            }
            if (!capSet) {
                
            }
            if (!specSet) {

            }
            if (!numSet) {

            }
        }
        return res;
    }

}
//Methods to create
/*
p.getSpecs();
p.GetLength();
p.getCaps();
p.getSpecChars();
p.getNumbers();
p.getInformation();
p.finalizeName();
p.completeScreen();
p.searchFor();
p.completeScreen();
p.extractInfoFromList();
p.completeScreen();
p.strengthTest();
p.completeScreen();
p.changeOrRem();
p.changeInfo();
p.removeInfo();
p.completeScreen();
p.getPassFromUser();
p.finalizeName(tempStr);
p.completeScreen();
*/