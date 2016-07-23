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
}
