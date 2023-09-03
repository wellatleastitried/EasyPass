package com.walit.pass;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XMLTest {
    @Test
    public void checkStrings() throws Exception {
        Parsed p = new Parsed();
        String str = p.getStr();
        String pad = p.getPad();
        assertThat(str, is("Eb12p50KRBhsJTRpH59PQ58Stp/R1OgKk4kGkyoUJ7I="));
        assertThat(pad, is("AES/CBC/PKCS5PADDING"));
    }
}
