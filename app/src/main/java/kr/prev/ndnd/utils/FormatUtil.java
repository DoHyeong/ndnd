package kr.prev.ndnd.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {

	/**
	 * "9,800 ￦" 처럼 규격화된 금액을 출력한다
	 * @return String
	 */
	public static String getFormattedAmount(int amount) {
		String formedAmout = "";
		String temp = Integer.toString(amount);

		for (int i = temp.length()-1, cnt = 1; i >= 0; i--, cnt++) {
			formedAmout = temp.charAt(i) + formedAmout;
			if (cnt % 3 == 0 && i != 0) formedAmout = "," + formedAmout;
		}

		return formedAmout + " ￦";
	}





}
