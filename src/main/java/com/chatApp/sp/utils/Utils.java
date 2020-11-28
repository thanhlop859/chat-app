package com.chatApp.sp.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {
	
	public static String covertToString(String value) {
	      try {
	            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
	            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "").replaceAll("đ", "d");
	       } catch (Exception ex) {
	            ex.printStackTrace();
	      }
	      return null;
	}

}
