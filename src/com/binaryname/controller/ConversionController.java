package com.binaryname.controller;

/**
 * The ConversionController is use to convert string to binary form
 */
public class ConversionController {

	/**
	 * The getBinaryName is use to convert string to binary form
	 * 
	 * @param name
	 *            to be converted
	 * @return converted binary name
	 */
	public String getBinaryName(String name) {

		if (name == null || name.isEmpty()) {
			return "";
		}

		String result = "";
		char[] messChar = name.toCharArray();

		for (int i = 0; i < messChar.length; i++) {
			result += Integer.toBinaryString(messChar[i]) + " ";
		}

		return result;

	}

}
