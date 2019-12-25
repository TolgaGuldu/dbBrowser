package libraries;

public class connectionInfo {
	public static String username;
	public static String password;

	/**
	 * @param username keeps database username
	 */
	public static void setUsername(String username) {
		connectionInfo.username = username;
	}

	/**
	 * @param password keeps database password
	 */
	public static void setPassword(String password) {
		connectionInfo.password = password;
	}

	/**
	 * @return database username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @return database password
	 */
	public static String getPassword() {
		return password;
	}
}
