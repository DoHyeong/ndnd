package kr.prev.ndnd.data;

import java.lang.reflect.Field;

/**
 * User Data
 */

public class UserData {

	/**
	 * UserID (unique)
	 */
	public int id;

	/**
	 * User type
	 * (currently, only facebook is supported)
	 */
	public String type = "";

	/**
	 * Social UID
	 * (currently, only facebbok is supported)
	 */
	public String socialUid;

	/**
	 * Email address
	 */
	public String userEmail = "";

	/**
	 * User Name
	 */
	public String userName;

	/**
	 * Registered Date
	 * YYYY-mm-dd HH:ii:ss
	 */
	public String regdate;


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
