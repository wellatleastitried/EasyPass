package com.walit.pass;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import java.util.logging.Logger;

public class UITest {
    @Test
    public void checkFunctionCall() {
        String checker = new UI(Logger.getLogger("temp")).temporaryMethod();
        MatcherAssert.assertThat(checker, is("Called."));
    }
}