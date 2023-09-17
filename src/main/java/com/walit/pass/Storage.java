package com.walit.pass;

import org.xml.sax.SAXException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

/**
 * Storage serves as a helper class to PasswordManager by handling the storage and calling from specific files
 * used by the program.
 *
 * @author Jackson Swindell
 */
abstract class Storage {

	private static Logger logger;
	public static final String ls = getProperty("line.separator");
	private static final byte[] initialize = new byte[16];
	public static final String bSlash = File.separator;
	private static final int[] res1 = {85, 74, 78, 78, 90, 45, 48, 45};

	/**
	 * Sets the logger for program the duration of the program's runtime and initializes a new iv.
	 */
	protected Storage(Logger storeLog) {
		logger = storeLog;
		SecureRandom sR = new SecureRandom();
		sR.nextBytes(initialize);
	}

	/**
	 * @return Returns the key to be used in the encryption of passwords.
	 */
	private static String calcStr() throws ParserConfigurationException, IOException, SAXException {
		Parsed par = new Parsed();
		return getVal(par.getStr());
	}

	/**
	 * Calculates string to return.
	 * @param change String to pass through operations.
	 * @return Resulting string after operations.
	 */
	private static String getVal(String change) {
		StringBuilder sB = new StringBuilder();
		for (int n : res1) {
			n += 31;
			sB.append((char) n);
		}
		byte[] kB = deHex(change);
		byte[] w = sB.toString().getBytes();
		byte[] res = new byte[kB.length];
		for (int i = 0; i < kB.length; i++) {
			res[i] = (byte) (kB[i] ^ w[i % w.length]);
		}
		return hex(res);
	}

	/**
	 * Gets the previously used initialization vector from its file, if it doesn't exist, creates a new one to be stored.
	 * @return Returns the old initialization vector if present, returns a new one if not.
	 */
	private static byte[] getIVSpec() {
		byte[] iv = new byte[16];
		File vecFil = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "iVSTAH");
		try {
			BufferedReader bR = new BufferedReader(new FileReader(vecFil));
			String line = bR.readLine();
			if (line == null) {
				SecureRandom sR = new SecureRandom();
				sR.nextBytes(iv);
				BufferedWriter bW = new BufferedWriter(new FileWriter(vecFil, false));
				bW.write(hex(iv));
				bW.flush();
				bW.close();
			} else {
				byte[] oldIV;
				oldIV = deHex(line);
				return oldIV;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to retrieve end bytes!");
		} catch (NullPointerException nPE) {
			logger.log(Level.SEVERE, "Null pointer in getIVSpec().");
		}
		return iv;
	}

	/**
	 * Method to handle encrypting and decrypting the store file.
	 * @param num If num is '0' the file will be decrypted, if it is '1' the file will be encrypted.
	 */
	private static void unlockLock(int num) {
		try {
			String x = calcStr();
			byte[] bytesOfKVector = deHex(x);
			SecretKey y = new SecretKeySpec(bytesOfKVector, 0, bytesOfKVector.length, "AES");
			File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
			IvParameterSpec ivPS = new IvParameterSpec(initialize);
			String ls = getProperty("line.separator");
			String pad = new Parsed().getPad();
			Cipher cipher = Cipher.getInstance(pad);
			if (num == 0) {
				byte[] oldIVBytes;
				oldIVBytes = getIVSpec();
				IvParameterSpec oldIV = new IvParameterSpec(oldIVBytes);
				StringBuilder sB = new StringBuilder();
				try {
					String line;
					BufferedReader bR = new BufferedReader(new FileReader(file));
					while((line = bR.readLine()) != null) {
						sB.append(line);
					}
					bR.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Could not read elements from file.");
				}
				String cipherTextToDeHex = sB.toString();
				if (!cipherTextToDeHex.equals("~#WARNING#~DO NOT EDIT THIS FILE~#WARNING#~")) {
					byte[] cipherText = deHex(cipherTextToDeHex);
					cipher.init(Cipher.DECRYPT_MODE, y, oldIV);
					byte[] plainBytes = cipher.doFinal(cipherText);
			        String plainText = new String(plainBytes);
			        try {
			            BufferedWriter bW = new BufferedWriter(new FileWriter(file));
			            bW.write(plainText);
						bW.flush();
			            bW.close();
			        } catch (IOException e) {
			            logger.log(Level.SEVERE, "Could not write elements to file.");
			        }
			    }
			} else if (num == 1) {
				cipher.init(Cipher.ENCRYPT_MODE, y, ivPS);
				StringBuilder sB = new StringBuilder();
				try {
					String line;
					BufferedReader bR = new BufferedReader(new FileReader(file));
			            while ((line = bR.readLine()) != null) {
				            sB.append(line);
				            sB.append(ls);
			            }
			            bR.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Could not read elements from file.");
				}
				String plainText = sB.toString();
					byte[] cipherText = cipher.doFinal(plainText.getBytes());
		        try {
		            BufferedWriter bW = new BufferedWriter(new FileWriter(file));
		            bW.write(hex(cipherText));
					bW.flush();
		            bW.close();
		        } catch (IOException e) {
		            logger.log(Level.SEVERE, "Could not write elements to file.");
		        }
			}
		} catch (InvalidKeyException iKE) {
		   	logger.log(Level.SEVERE, "Invalid key exception in unlockLock() method.");
		} catch (NoSuchAlgorithmException nSAE) {
		   	logger.log(Level.SEVERE, "No such algorithm exception in unlockLock() method.");
		} catch (IllegalBlockSizeException iBSE) {
		   	logger.log(Level.SEVERE, "Illegal block size exception in unlockLock() method.");
		} catch (NoSuchPaddingException nSPE){
		   	logger.log(Level.SEVERE, "No such padding exception in unlockLock() method.");
		} catch (InvalidAlgorithmParameterException iAPE) {
		   	logger.log(Level.SEVERE, "Invalid algorithm parameter exception in unlockLock() method.");
		} catch (BadPaddingException bPE) {
		   	logger.log(Level.SEVERE, "Bad padding exception in unlockLock() method.");
		} catch (Exception e) {
            logger.log(Level.SEVERE, "Exception parsing xml.");
        }

		try {
			File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "iVSTAH");
			if (file.exists() && file.isFile()) {
				BufferedWriter app = new BufferedWriter(new FileWriter(file, false));
				app.write(hex(initialize));
				app.flush();
				app.close();
			} else {
				boolean checkFileCreation = file.createNewFile();
				if (!checkFileCreation) logger.log(Level.WARNING, "File unable to be created.");
				BufferedWriter app = new BufferedWriter(new FileWriter(file, false));
				app.write(hex(initialize));
				app.flush();
				app.close();
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO exception in unlockLock() method.");
		}
		//get this done
	}

	/**
	 * Stores the name and password for the user.
	 * @param transferable Encoded user information to be stored.
	 */
	protected static void storeInfo(String[] transferable) {
		String[] info = new String[2];
		File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
		try {
			if (!(file.exists() && file.isFile())) {
				boolean checkFileCreation = file.createNewFile();
				if (!checkFileCreation) logger.log(Level.WARNING, "Issue creating file.");
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "File could not be found/created");
		}
		for (int i = 0; i < transferable.length; i++) {
			byte[] decodedBytes = Base64.getDecoder().decode(transferable[i]);
			info[i] = new String(decodedBytes);
		}
		//decrypt csv file contents
		unlockLock(0);

		//store name and password into csv file
		try {
			BufferedWriter bW = new BufferedWriter(new FileWriter(file, true));
			bW.write(info[0]);
			bW.write(", ");
			bW.write(info[1]);
			bW.newLine();
			bW.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not write elements to file.");
		}
		//encrypt csv file contents
		unlockLock(1);
	}

	/**
	 * Displays all passwords and associated names for the user.
	 */
	protected static void getInfo() {
		File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
		try {
			if (!(file.exists() && file.isFile())) {
				boolean checkFileCreation = file.createNewFile();
				if (!checkFileCreation) logger.log(Level.WARNING, "Issue creating file.");
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "File could not be found/created");
		}
		//decrypt
		unlockLock(0);
		String placeholder;
		try {
			if (!(file.exists() && file.isFile())) {
				boolean checkFileCreation = file.createNewFile();
				if (!checkFileCreation) logger.log(Level.WARNING, "Issue creating file.");
			}
			BufferedReader bR = new BufferedReader(new FileReader(file));
			out.println();
			while ((placeholder = bR.readLine()) != null) {
				if (hasComma(placeholder)) {
					String[] temp = placeholder.split(", ", 2);
					out.println(temp[0] + ": " + temp[1]);
				}
			}
			bR.close();
		} catch (IOException e) {
			logger.log(Level.WARNING, ".csv file unable to be read.");
		} catch (ArrayIndexOutOfBoundsException aE) {
			logger.log(Level.SEVERE, "Array index out of bound exception in getInfo() method, please restart.");
			exit(1);
		}
		//encrypt
		unlockLock(1);
	}
	// TODO: Create getInfoGUI() -> protected String[] getInfoGUI() {}

	/**
	 * Searches through store file for a specific name.
	 * @param name The specific name to find in the file's contents.
	 * @return Returns a list of all lines that have a name matching the given one.
	 */
	protected static ArrayList<String> findInfo(String name) {
		File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
		ArrayList<String> acceptedStrings = new ArrayList<>();
		try {
			if (!(file.exists() && file.isFile())) {
				boolean checkFileCreation = file.createNewFile();
				if (!checkFileCreation) logger.log(Level.WARNING, "Issue creating file.");
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "File could not be found/created");
		}
		unlockLock(0);
		String line;
		try {
			BufferedReader bR = new BufferedReader(new FileReader(file));
			while ((line = bR.readLine()) != null) {
				String[] splitValues = line.split(", ", 2);
				if ((splitValues[0]).equalsIgnoreCase(name)) {
					acceptedStrings.add(line);
				}
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error searching for name in file.");
		}
		unlockLock(1);
		return acceptedStrings;
	}

	/**
	 * Checks to see if the given string has a comma in it.
	 * @param str The string to search through.
	 * @return Returns 'true' if there is a comma found and 'false' if there is not a comma.
	 */
	private static boolean hasComma(String str) {
		char[] chars = str.toCharArray();
		for (char character : chars) {
			if (character == ',') return true;
		}
		return false;
	}

	/**
	 * Converts bytes to hexadecimal.
	 * @param bytes The byte array that will be converted to hex.
	 * @return Returns the hex values in the form of a string.
	 */
	private static String hex(byte[] bytes) {
		StringBuilder end = new StringBuilder();
		for (byte x : bytes) {
			end.append(String.format("%02X", x));
		}
		return end.toString();
	}

	/**
	 * Converts hex values back to bytes.
	 * @param string The string representation of the hex value.
	 * @return Returns the byte array representation of the value after being converted from hex.
	 */
	private static byte[] deHex(String string) {
		byte[] cipherText = new byte[string.length() / 2];
		for (int i = 0; i < cipherText.length; i++) {
        	int index = i * 2;
	        int val = Integer.parseInt(string.substring(index, index + 2), 16);
	        cipherText[i] = (byte) val;
        }
        return cipherText;
	}

	/**
	 * Searches the store file for all contents.
	 * @return Returns the contents of the file for the Manager to search through for a specific name.
	 */
	protected static List<String> findNameToAlter() {
		unlockLock(0);
		File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
		List<String> stringLines = new ArrayList<>();
		String line;
		try {
			BufferedReader bR = new BufferedReader(new FileReader(file));
			while ((line = bR.readLine()) != null) {
				stringLines.add(line);
			}
			unlockLock(1);
			return stringLines;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error reading strings from file.");
		}
		unlockLock(1);
		return stringLines;
	}

	/**
	 * Stores the password and name associated with it into the store file.
	 * @param infoToStore The list containing the necessary information to store.
	 */
	protected static void storeNameFromLists(List<String> infoToStore) {
		File file = new File("resources" + bSlash + "utilities" + bSlash + "data" + bSlash + "pSAH");
		unlockLock(0);
		try {
			BufferedWriter bW = new BufferedWriter(new FileWriter(file, false));
			for (String line : infoToStore) {
				bW.write(line);
				bW.write(ls);
			}
			bW.flush();
			bW.close();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error writing new data to info");
		}
		unlockLock(1);
	}
}