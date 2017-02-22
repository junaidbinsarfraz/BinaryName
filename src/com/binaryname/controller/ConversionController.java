package com.binaryname.controller;

public class ConversionController {
	
	public String getBinaryName(String name) {
		
		if(name == null || name.isEmpty()) {
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
