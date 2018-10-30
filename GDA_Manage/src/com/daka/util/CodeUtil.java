package com.daka.util;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

public class CodeUtil {
	public static void produceCodeWithId(String salt) {
		//return salt + getRandomChar(6);
	}

	public static final CopyOnWriteArraySet<String> activationCodes = new CopyOnWriteArraySet<>();

	public static String getRandomChar(int length) { // 生成随机字符串
		char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z'};
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();

		do {
			for (int i = 0; i < length; i++) {
				buffer.append(chr[random.nextInt(36)]);
			}
		}
		while (activationCodes.contains(buffer.toString()));
		activationCodes.add(buffer.toString());
		return buffer.toString();
	}

}
