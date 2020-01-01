package models;

import libraries.connectionInfo;
import libraries.matrices;

import java.sql.*;
import java.util.ArrayList;

public class model {

	Statement statement;
	ResultSet resultSet;

	/**
	 * @param connection
	 * @return database list
	 * @throws SQLException
	 */
	public ResultSet databaseList(Connection connection) throws SQLException {
		statement = connection.createStatement();
		resultSet= statement.executeQuery("SHOW DATABASES");
		return resultSet;
	}

	/**
	 * @param connection
	 * @return table names
	 * @throws SQLException
	 */
	public ResultSet tableList(Connection connection) throws SQLException {
		statement = connection.createStatement();
		statement.executeQuery("USE " + connectionInfo.getDatabase());
		resultSet= statement.executeQuery("SHOW TABLES");
		return resultSet;
	}

	/**
	 * @param connection connected database information
	 * @param table selected table name
	 * @return selected data tuples
	 * @throws SQLException
	 */
	public ResultSet tableData(Connection connection, String table) throws SQLException {
		statement = connection.createStatement();
		statement.executeQuery("USE " + connectionInfo.getDatabase());
		resultSet = statement.executeQuery("SELECT * FROM " + table);
		return resultSet;
	}

	/**
	 * @param connection connected database information
	 * @param table selected table name
	 * @param values values to set
	 * @param column columns to set on table
	 * @param id identifies the tuple to update
	 * @throws SQLException
	 */
	public void update(Connection connection, String table, ArrayList<matrices> values, String column, int id) throws SQLException {
		statement = connection.createStatement();
		statement.executeQuery("USE " + connectionInfo.getDatabase());
		for (matrices item: values) {
			statement.executeUpdate("UPDATE " + table + " SET `" + item.fieldName + "`='" + item.value + "' WHERE " + column + " = " + id);
		}
	}

	/**
	 * @param connection connected database information
	 * @param table selected table name
	 * @param values values to set
	 * @throws SQLException
	 */
	public void insert(Connection connection, String table, ArrayList<matrices> values) throws SQLException {
		statement = connection.createStatement();
		statement.executeQuery("USE " + connectionInfo.getDatabase());
		String fields = "";
		String marks = "";
		int index = 0;

		// prepares query strings
		for (matrices item: values) {
			fields += item.fieldName + ",";
			marks += "?,";
		}

		// deleting the comma end of strings
		fields = fields.substring(0, fields.length() - 1);
		marks = marks.substring(0, marks.length() - 1);

		// query string to be executed
		String sql = "INSERT INTO " + table + " (" + fields + ")" +
				             "VALUES (" + marks + ")";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		// binds data and ? marks on the query string
		for (matrices item: values) {
			preparedStatement.setString(++index, item.value);
		}

		preparedStatement.executeUpdate();
	}

	/**
	 * @param connection connected database information
	 * @param table selected table name
	 * @param column columns to set on table
	 * @param id identifies the tuple to delete
	 * @throws SQLException
	 */
	public void delete(Connection connection, String table, String column, int id) throws SQLException {
		statement = connection.createStatement();
		statement.executeQuery("USE " + connectionInfo.getDatabase());
		statement.executeUpdate("DELETE FROM " + table + " WHERE " + column + " = " + id);
	}

}
