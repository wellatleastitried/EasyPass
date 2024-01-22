package com.walit.pass;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class PasswordGenerationTest {

    private final Generator gen = new Generator(new CLI().getLogger());
    @Test
    public void checkPasswordGeneration() {
        int length = 10;
        int capitals = 2;
        int specialChars = 2;
        int numbers = 2;
        String passwordToCheck = gen.generatePassword(length, specialChars, capitals, numbers);
        MatcherAssert.assertThat(passwordToCheck.length(), is(10));
        MatcherAssert.assertThat(specialChars, is(checkSpecialCharCount(passwordToCheck)));
        MatcherAssert.assertThat(capitals, is(checkCapitalCharCount(passwordToCheck)));
        MatcherAssert.assertThat(numbers, is(checkNumberCharCount(passwordToCheck)));
    }
    public int checkSpecialCharCount(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (gen.isSpecialChar(c)) {
                count++;
            }
        }
        return count;
    }
    public int checkCapitalCharCount(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (gen.isUppercase(c)) {
                count++;
            }
        }
        return count;
    }
    public int checkNumberCharCount(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (gen.isNumber(c)) {
                count++;
            }
        }
        return count;
    }
}