package kr.prev.ndnd.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NdUtil {

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


	/**
	 * "어제 11시" 처럼 상대적인 시간을 출력한다
	 * @return String
	 */
	public static String getRelativeDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date now = new Date();
		Date yesterday = new Date(now.getDate() - 1);
		Date parsedDate;

		try {
			parsedDate = dateFormat.parse(date);

		}catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		String dateText;

		if (isSameDay(now, parsedDate))                 dateText = "오늘";
		else if (isSameDay(yesterday, parsedDate))      dateText = "어제";
		else                                            dateText = (parsedDate.getMonth() + 1) + "월 " + parsedDate.getDate() + "일";

		dateText += " " + parsedDate.getHours() + "시";

		return dateText;
	}

	private static boolean isSameDay(Date d1, Date d2) {
		return (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
	}


}
