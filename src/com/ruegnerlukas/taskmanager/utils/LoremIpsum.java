package com.ruegnerlukas.taskmanager.utils;

import java.util.Random;

public class LoremIpsum {


	private static final String LOREM_IPSUM_200W = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet.";
	private static final String[] LOREM_IPSUM_WORDS = LOREM_IPSUM_200W.split(" ");




	/**
	 * @return a string with the given amount of words. The created string will always start with the same words if randomOffset is set to false.
	 */
	public static String get(int nWords, boolean randomOffset) {
		StringBuilder sb = new StringBuilder();
		int offset = new Random().nextInt(LOREM_IPSUM_WORDS.length - nWords);
		for (int i = 0; i < nWords - 1; i++) {
			sb.append(LOREM_IPSUM_WORDS[i + offset]).append(' ');
		}
		sb.append(LOREM_IPSUM_WORDS[nWords - 1 + offset]).append('.');
		return sb.toString();
	}




	/**
	 * @return a string with a random amount of words in the given range. The created string will always start with the same words if randomOffset is set to false.
	 */
	public static String get(int nWordsMin, int nWordsMax, boolean randomOffset) {
		int nWords = (int) ((nWordsMax - nWordsMin) * Math.random()) + nWordsMin;
		return get(nWords, randomOffset);
	}


}
