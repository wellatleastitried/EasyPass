package com.walit.pass;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class XMLTest {
    @Test
    public void checkStrings() throws Exception {
        Parsed p = new Parsed();
        String pad = p.getPad();
        assertThat(pad, is("AES/CBC/PKCS5PADDING"));
    }
}
