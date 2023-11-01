package com.walit.pass;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
public class VersionTest {
    @Test
    public void checkVersion() throws Exception {
        MatcherAssert.assertThat(new CLI().getVersionInfo(), is(new Parsed().getVersion()));
    }
}