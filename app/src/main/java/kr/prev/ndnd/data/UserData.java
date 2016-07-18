package kr.prev.ndnd.data;


import java.lang.reflect.Field;

public class UserData {
	public int id;
	public String type;
	public double social_uid;
	public String user_email;
	private String user_name;
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
