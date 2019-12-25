package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import libraries.connectionInfo;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;
import libraries.matrices;
import models.model;

import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	@FXML private TextField username;
	@FXML private TextField password;
	@FXML private Button connect;
	@FXML private ComboBox<String> tableList;
	@FXML private TableView tableView;
	@FXML private VBox textfieldArea;

	private Button dynamicFieldButton;
	private Connection connection;
	private ResultSet resultSet;
	private ObservableList<String> tablesInDataBase;
	private ObservableList<ObservableList> data;
	private model model = new model();

	/**
	 *
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tablesInDataBase = FXCollections.observableArrayList();
		data = FXCollections.observableArrayList();
	}

	/**
	 *
	 * @param actionEvent
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@FXML
	private void connectToDatabse(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		connectionInfo.setUsername(username.getText());
		connectionInfo.setPassword(password.getText());
		checkIfEmpty(username.getText(), password.getText());
	}

	@FXML
	private void selectedTable(ActionEvent actionEvent) throws SQLException {
		fillTable(tableList.getSelectionModel().getSelectedItem());
	}

	/**
	 *
	 * @param actionEvent
	 * @throws SQLException
	 */
	@FXML
	private void updateRecord(ActionEvent actionEvent) throws SQLException {
		textfieldArea.getChildren().clear();
		try{
			dynamicFieldButton = new Button("btn");
			String table = tableList.getSelectionModel().getSelectedItem();
			String rowValue = String.valueOf(tableView.getSelectionModel().getSelectedItem());
			String column = resultSet.getMetaData().getColumnName(1);
			String[] vals = rowValue.split(",");
			vals[vals.length-1] = vals[vals.length-1].replace("]","");
			int  id = Integer.parseInt(vals[0].replace("[",""));

			int rowCount = resultSet.getMetaData().getColumnCount();
			String val = pressedButton(actionEvent);
			TextField textField[] = new TextField[rowCount];
			for(int i=0;i<rowCount-1;i++) {
				textField[i] = new TextField();
				textField[i].setText(vals[i+1]);
				textfieldArea.getChildren().add(textField[i]);
			}
			dynamicFieldButton.setText(val);
			textfieldArea.getChildren().add(dynamicFieldButton);

			dynamicFieldButton.setOnAction((EventHandler<ActionEvent>) e -> {
				try {
					ArrayList<matrices> values = new ArrayList<matrices>();
					for(int i=1;i<rowCount;i++) {
						values.add(new matrices(resultSet.getMetaData().getColumnName(i+1)
								,textField[i-1].getText().trim()));
					}
					model.update(connection,table,values,column,id);

					fillTable(table);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			});
		}catch (Exception e){
			showDialog("You didn't choose a table or logged in!");
		}
	}

	/**
	 *
	 * @param actionEvent
	 * @throws SQLException
	 */
	@FXML
	private void insertRecord(ActionEvent actionEvent) throws SQLException {
		textfieldArea.getChildren().clear();
		try{
			dynamicFieldButton = new Button("btn");
			String table = tableList.getSelectionModel().getSelectedItem();

			int rowCount = resultSet.getMetaData().getColumnCount();
			String val = pressedButton(actionEvent);
			TextField textField[] = new TextField[rowCount];
			for(int i=0;i<rowCount;i++) {
				textField[i] = new TextField();
				textfieldArea.getChildren().add(textField[i]);
			}
			dynamicFieldButton.setText(val);
			textfieldArea.getChildren().add(dynamicFieldButton);

			dynamicFieldButton.setOnAction((EventHandler<ActionEvent>) e -> {
				try {
					ArrayList<matrices> values = new ArrayList<matrices>();
					for(int i=0;i<rowCount;i++) {
						values.add(new matrices(resultSet.getMetaData().getColumnName(i+1)
								,textField[i].getText().trim()));
					}
					model.insert(connection,table,values);

					fillTable(table);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			});
		}catch (Exception e){
			showDialog("You didn't choose a table or logged in!");
		}
	}

	/**
	 *
	 * @param actionEvent
	 * @throws SQLException
	 */
	@FXML
	private void deleteFromTable(ActionEvent actionEvent) throws SQLException {
		String table = tableList.getSelectionModel().getSelectedItem();
		String column = resultSet.getMetaData().getColumnName(1);
		String rowValue = String.valueOf(tableView.getSelectionModel().getSelectedItem());
		String[] vals = rowValue.split(",");
		int  id = Integer.parseInt(vals[0].replace("[",""));
		model.delete(connection,table,column,id);
		fillTable(table);
	}

	/**
	 *
	 * @param username
	 * @param password
	 */
	private void checkIfEmpty(String username, String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		if(username.isEmpty() || password.isEmpty()){
			showDialog("Please Fill The Required Fields!");
		} else {
			makeTheConnection();
		}
	}

	/**
	 *
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void makeTheConnection() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbBrowser", connectionInfo.getUsername(), connectionInfo.getPassword());
			changeButton();
			fillCombos();
		} catch (Exception e){
			showDialog("Invalid username or password! ");
		}
	}

	/**
	 *
	 * @throws SQLException
	 */
	private void changeButton() throws SQLException {
		if (connection.isValid(0)) {
			connect.setText("Connected");
			connect.setStyle("-fx-background-color: #00ff00; ");
		}
	}

	/**
	 *
	 * @throws SQLException
	 */
	private void fillCombos() throws SQLException {
		ResultSet resultSet = model.tableList(connection);
		while(resultSet.next()){
			tablesInDataBase.add(resultSet.getString(1));
		}
		tableList.setItems(tablesInDataBase);
	}

	/**
	 *
	 * @param selectedTable
	 * @throws SQLException
	 */
	private void fillTable(String selectedTable) throws SQLException {
		resultSet = model.tableData(connection, selectedTable);
		prepareTableView(resultSet);
		fillTableView(resultSet);
	}

	/**
	 *
	 * @param resultSet
	 * @throws SQLException
	 */
	private void prepareTableView(ResultSet resultSet) throws SQLException {
		clearAllData();

		for(int i=0 ; i<resultSet.getMetaData().getColumnCount(); i++){
			final int j = i;
			TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
			col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(j).toString());
				}
			});
			tableView.getColumns().addAll(col);
		}
	}

	/**
	 *
	 * @param resultSet
	 * @throws SQLException
	 */
	private void fillTableView(ResultSet resultSet) throws SQLException {
		while(resultSet.next()){
			ObservableList<String> row = FXCollections.observableArrayList();
			for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
				row.add(resultSet.getString(i));
			}
			data.add(row);
		}
		tableView.setItems(data);
	}

	/**
	 *
	 */
	private void clearAllData(){
		data.clear();
		tableView.getItems().clear();
		tableView.getColumns().clear();
	}

	@FXML
	private void navigation(ActionEvent actionEvent) {
		String val = pressedButton(actionEvent);
		System.out.println(val);
		int sizeOftable = tableView.getItems().size();
		int currentIndex = tableView.getSelectionModel().getFocusedIndex();

		// @TODO: fix navigation
		if (val.equals("First"))
			moveInTable(0);
		else if (val.equals("Last"))
			moveInTable(sizeOftable);
		else if (sizeOftable!=currentIndex && val.equals("Next"))
			moveInTable(currentIndex++);
		else if (currentIndex!=0 && val.equals("Previous"))
			moveInTable(currentIndex--);
	}

	private void moveInTable(int index) {
		tableView.requestFocus();
		tableView.getSelectionModel().select(index);
		tableView.getFocusModel().focus(index);
	}

	/**
	 * seeks for the button event
	 * and returns the value in
	 * pressed button
	 * @param event
	 * @return
	 */
	public String pressedButton(ActionEvent event){
		return ((Button)event.getSource()).getText();
	}
	/**
	 *
	 * @param message
	 */
	private void showDialog(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
