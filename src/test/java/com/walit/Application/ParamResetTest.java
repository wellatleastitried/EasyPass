package com.walit.Application;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ParamResetTest {

    private final CLI c = new CLI();
    @Test
    public void lengthTest() {
        c.resetParams();
        MatcherAssert.assertThat(c.lengthOfPassword, is(-1));
    }
    @Test
    public void capitalTest() {
        c.resetParams();
        MatcherAssert.assertThat(c.capitals, is(-1));
    }
    @Test
    public void specialCharTest() {
        c.resetParams();
        MatcherAssert.assertThat(c.specialChars, is(-1));
    }
    @Test
    public void numberTest() {
        c.resetParams();
        MatcherAssert.assertThat(c.numbers, is(-1));
    }
}
