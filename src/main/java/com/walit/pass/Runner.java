package com.walit.pass;

public interface Runner {
    void shutdown();
    boolean changeOrRem();

    void changeInfo();
    void removeInfo();
    String getUserNameForAlter(int x);
    String[] getPassFromUser();
    void finalizeName(String[] arr);
    String[] getInformation();
    void storeInformation(String[] info);
    void extractInfoFromList();
    void initializeFilesForProgram();
    void strengthTest();
    void searchFor();
    void run();
    void resetParams();
}
