package models;

import libraries.matrices;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class model {

	Statement statement;
	ResultSet resultSet;


	/**
	 *
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public ResultSet tableList(Connection connection) throws SQLException {
		statement = connection.createStatement();
		resultSet= statement.executeQuery("SHOW TABLES");
		return resultSet;
	}

	/**
	 *
	 * @param connection
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public ResultSet tableData(Connection connection, String table) throws SQLException {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT * FROM " + table);
		return resultSet;
	}

	/**
	 *
	 * @param connection
	 * @param table
	 * @param values
	 * @param column
	 * @param id
	 * @throws SQLException
	 */
	public void update(Connection connection, String table, ArrayList<matrices> values, String column, int id) throws SQLException {
		statement = connection.createStatement();
		for (matrices item: values) {
			statement.executeUpdate("UPDATE " + table + " SET `" + item.fieldName + "`='" + item.value + "' WHERE " + column + " = " + id);
		}
	}

	public void insert(Connection connection, String table, ArrayList<matrices> values) throws SQLException {
		statement = connection.createStatement();
		String fields = "";
		String marks = "";
		int index = 0;

		for (matrices item: values) {
			fields += item.fieldName + ",";
			marks += "?,";
		}

		fields = fields.substring(0, fields.length() - 1);
		marks = marks.substring(0, marks.length() - 1);
		String sql = "INSERT INTO " + table + " (" + fields + ")" +
				             "VALUES (" + marks + ")";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		for (matrices item: values) {
			preparedStatement.setString(++index, item.value);
		}

		preparedStatement.executeUpdate();
	}

	/**
	 *
	 * @param connection
	 * @param table
	 * @param column
	 * @param id
	 * @throws SQLException
	 */
	public void delete(Connection connection, String table, String column, int id) throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM " + table + " WHERE " + column + " = " + id);
	}

}
