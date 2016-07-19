package kr.prev.ndnd.utils;



public class SessionManager {
	/**
	 * stand-alone
	 */
	private static SessionManager instance = new SessionManager();
	public static SessionManager getInstance() {
		return instance;
	}


	private String accessToken;
	private int userID;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public int getUserID() {
		return this.userID;
	}
}
