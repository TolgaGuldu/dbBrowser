package libraries;

public class connectionInfo {
	public static String username;
	public static String password;
	public static String database;

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
	 * @param database keeps database name
	 */
	public static void setDatabase(String database) { connectionInfo.database = database; }

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

	/**
	 * @return database name
	 */
	public static String getDatabase() { return database; }
}
