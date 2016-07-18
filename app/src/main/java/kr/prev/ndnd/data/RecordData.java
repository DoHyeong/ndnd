package kr.prev.ndnd.data;

import android.util.Log;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordData {
	public int id;
	public int user_id;
	public int target_user_id;
	public int type;
	public int state;
	public int amount;
	public String note;
	public String date;
	public String location;



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
