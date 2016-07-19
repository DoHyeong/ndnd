package kr.prev.ndnd.data;

import java.util.List;

/**
 * Initial Data
 *
 * loaded in "/api/load"
 */

public class InitialData {
	/**
	 * Now logged user data
	 */
	public UserData user;


	/**
	 * List of RecordData (logged user's record)
	 */
	public List<RecordData> data;


	/**
	 * Summary of logged user data
	 * Include sum of loan, lend amount
	 */
	public SummarayData summary;

}
