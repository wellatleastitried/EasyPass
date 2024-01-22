package com.walit.pass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public interface Runner {
	String logFilePath = "resources\\utilities\\log\\PassMan.log";
    void shutdown();
    boolean getChangeOrRemoveDecision();
    void changeData();
    void removeData();
    String getPassIdentifierForChangeOrRemove(int x);
    String[] getPasswordFromUser();
    void getPassIdentifierFromUser(String[] arr);
    String[] getUserInformation();
    void storeInformation(String[] info);
    void extractInfoFromList();
    default void initializeMissingFilesForProgram() {
		String ls = System.getProperty("line.separator");
		File logDir = new File("resources\\utilities\\log");
		File storeDir = new File("resources\\utilities\\data");
		File wordLists = new File("resources\\WordLists");
		File[] dirs = new File[3];
		dirs[0] = storeDir;
		dirs[1] = logDir;
		dirs[2] = wordLists;
		try {
			for (File directory : dirs) {
				if (!(directory.exists())) {
					boolean checkDirCreation = directory.mkdirs();
					if (!checkDirCreation) {
						System.err.println("Error creating directory, please restart now.");
					}
				}
			}
		}
		catch (SecurityException sE) {
			System.err.println("IO exception while making directories.");
		}
		catch (NullPointerException nPE) {
			System.err.println("Null pointer exception while initializing directories.");
		}
		File passMan = new File(logFilePath);
		File inst = new File("resources\\utilities\\data\\vSTAH");
		File[] files = new File[2];
		files[0] = passMan;
		files[1] = inst;
		try {
			for (int i = 0; i < files.length; i++) {
				if (!(files[i].exists() && files[i].isFile())) {
					boolean checkFileCreation = files[i].createNewFile();
					if (!checkFileCreation) {
						System.err.println("Could not initialize files for program.");
					}
					if (i == 1 && files[i].length() == 0) {
						try {
							BufferedWriter bW = new BufferedWriter(new FileWriter(files[i]));
							String hex = "3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D22796573223F3E0A3C7061727365643E0A202020203C696E666F3E0A20202020202020203C70726F643E45617379506173733C2F70726F643E0A20202020202020203C76657273696F6E3E302E312E303C2F76657273696F6E3E0A20202020202020203C7061643E4145532F4342432F504B43533550414444494E473C2F7061643E0A20202020202020203C7374723E363544343142434145343436304235343138344335393034363644333030304645423742444246324138393841373436453745303642464535333538363846453C2F7374723E0A202020203C2F696E666F3E0A3C2F7061727365643E";
							bW.write(new String(deHex(hex), StandardCharsets.UTF_8));
							bW.write(ls);
							bW.flush();
							bW.close();
						}
						catch (IOException e) {
							System.err.println("Error initializing data file.");
						}
					}
				}
			}
		}
		catch (IOException e) {
			System.err.println("IO exception while creating new files for program.");
		}
	}
    private byte[] deHex(String string) {
        byte[] cipherText = new byte[string.length() / 2];
        for (int i = 0; i < cipherText.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(string.substring(index, index + 2), 16);
            cipherText[i] = (byte) val;
        }
        return cipherText;
    }
    void strengthTest();
    void findNamePassCombos();
    void run();
    void resetParams();
}