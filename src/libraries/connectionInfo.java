package libraries;

public class connectionInfo {
	public static String username;
	public static String password;

	/**
	 *
	 * @param password
	 */
	public static void setPassword(String password) {
		connectionInfo.password = password;
	}

	/**
	 *
	 * @param username
	 */
	public static void setUsername(String username) {
		connectionInfo.username = username;
	}

	/**
	 *
	 * @return
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 *
	 * @return
	 */
	public static String getPassword() {
		return password;
	}
}
