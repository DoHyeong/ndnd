package kr.prev.ndnd.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	/**
	 * "어제 11시" 처럼 상대적인 시간을 출력한다
	 *
	 * @param date: Date 오브젝트
	 * @return String: 상대적 날짜-시
	 */
	public static String getRelativeDate(Date date) {
		Date now = new Date();
		Date yesterday = new Date(now.getDate() - 1);

		String dateText;

		if (isSameDay(now, date))                 dateText = "오늘";
		else if (isSameDay(yesterday, date))      dateText = "어제";
		else                                      dateText = (date.getMonth() + 1) + "월 " + date.getDate() + "일";

		dateText += " " + date.getHours() + "시";

		return dateText;
	}


	/**
	 * "어제 11시" 처럼 상대적인 시간을 출력한다
	 *
	 * @param date: String 형태의 날짜 (Y-m-d H:i:s 포멧)
	 * @return String: 상대적 날짜-시
	 */
	public static String getRelativeDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date parsedDate;

		try {
			parsedDate = dateFormat.parse(date);

		}catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return getRelativeDate(parsedDate);
	}


	/**
	 * M월 D일 H시 형태로 파싱하여 반환한다
	 *
	 * @param date: Date
	 * @return: String
	 */
	public static String parseAsMDH(Date date) {
		return (date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + "시";
	}


	/**
	 * 같은 날짜인지 체크
	 * @param d1: Date
	 * @param d2: Date
	 * @return: boolean
	 */
	private static boolean isSameDay(Date d1, Date d2) {
		return (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
	}

}
