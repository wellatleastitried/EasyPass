package com.walit.pass;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import java.util.logging.Logger;

public class GUITest {
    @Test
    public void checkFunctionCall() {
        String checker = new PasswordGUI(Logger.getLogger("temp")).temporaryMethod();
        MatcherAssert.assertThat(checker, is("Called."));
    }
}