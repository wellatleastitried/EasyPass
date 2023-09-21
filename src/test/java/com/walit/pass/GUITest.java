package com.walit.pass;

import org.junit.Test;

import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class GUITest {
    @Test
    public void checkFunctionCall() {
        String checker = new PasswordGUI(Logger.getLogger("temp")).temporaryMethod();
        assertThat(checker, is("Called."));
    }
}