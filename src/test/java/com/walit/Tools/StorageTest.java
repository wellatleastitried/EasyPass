package com.walit.Tools;

import static org.mockito.Mockito.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageTest {

    private Storage storage;
    private Logger mockLogger;

    @Before
    public void setUp() throws ClassNotFoundException {
        storage = new Storage(mockLogger);
        mockLogger = mock(Logger.class);
    }
    @After
    public void tearDown() {
        if (storage != null) {
            storage.close();
        }
    }
    @Test
    public void testStoreAndRemoveData() {
        when(mockLogger.isLoggable(Level.WARNING)).thenReturn(true);
        String[] userInfo = {"testuser", "testpass"};
        storage.storeData(userInfo);
        storage.displayUserPassCombos();
        storage.removeRow("testuser", "testpass");
        verify(mockLogger, never()).warning(anyString());
    }
    @Test
    public void testDisplayUserPassCombos() {
        when(mockLogger.isLoggable(Level.SEVERE)).thenReturn(true);
        storage.displayUserPassCombos();
        verify(mockLogger, never()).severe(anyString());
    }
}
