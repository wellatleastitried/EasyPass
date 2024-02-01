package com.walit.Application;

import com.walit.Tools.Generator;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class QuickGenerateTest {


    private final CLI c = new CLI();
    private final Generator gen = new Generator(c.getLogger());

    @Test
    public void quickPasswordTest() {
        c.lengthOfPassword = 10;
        c.capitals = 2;
        c.specialChars = 2;
        c.numbers = 2;
        while (c.lengthOfPassword < 10000) {
            String passwordToCheck = c.quickGenerate();
            MatcherAssert.assertThat(passwordToCheck.length(), is(c.lengthOfPassword));
            MatcherAssert.assertThat(c.specialChars, is(checkSpecialCharCount(passwordToCheck)));
            MatcherAssert.assertThat(c.capitals, is(checkCapitalCharCount(passwordToCheck)));
            MatcherAssert.assertThat(c.numbers, is(checkNumberCharCount(passwordToCheck)));
            c.lengthOfPassword *= 2;
            c.capitals *= 2;
            c.specialChars *= 2;
            c.numbers *= 2;
        }
        c.resetParams();
    }
    private int checkSpecialCharCount(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (gen.isSpecialChar(c)) {
                count++;
            }
        }
        return count;
    }
    private int checkCapitalCharCount(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (gen.isUppercase(c)) {
                count++;
            }
        }
        return count;
    }
    private int checkNumberCharCount(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (gen.isNumber(c)) {
                count++;
            }
        }
        return count;
    }
}
