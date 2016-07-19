package kr.prev.ndnd.data;

import android.util.Log;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Record Data
 */
public class RecordData {
	/**
	 * Record ID (unique)
	 */
	public int id;

	/**
	 * UserID
	 */
	public int userId;

	/**
	 * Record type
	 *
	 * 0: lend
	 * 1: loan
	 */
	public int type;

	/**
	 * 0: transaction not cleared
	 * 1: transaction cleared
	 */
	public int state;


	/**
	 * Amount of transaction
	 */
	public int amount;


	/**
	 * Note of transaction
	 * ex) 점심, 저녁 커피
	 */
	public String note;


	/**
	 * Date and time of transaction
	 * YYYY-mm-dd HH:ii:ss
	 */
	public String date;


	/**
	 * Location of transaction
	 */
	public String location;


	/**
	 * Target User Data
	 * Someone who has transaction with current user
	 */
	public UserData targetUser;


	@Override
	public String toString() {
		String str = "kr.prev.ndnd.UserData {\n";
		for (Field field : this.getClass().getDeclaredFields()) {
			try {
				if (field.get(this) != null)
					str += "\t" + field.getName() + ": " + field.get(this).toString() + "\n";
				else
					str += "\t" + field.getName() + ": null\n";

			}catch (IllegalAccessException e) {

			}
		}
		return str + "}";
	}
}
