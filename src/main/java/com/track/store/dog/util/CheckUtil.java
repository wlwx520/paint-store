package com.track.store.dog.util;

import java.util.List;

public class CheckUtil {
	public static boolean checkStrIsEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	public static boolean checkListOne(List<?> list) {
		return list != null && list.size() == 1;
	}
	
	public static boolean checkListZero(List<?> list) {
		return list != null && list.size() == 0;
	}
}
