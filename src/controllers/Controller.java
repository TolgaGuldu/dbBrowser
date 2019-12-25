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

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
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
	 * Initializes the list variables on start up
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tablesInDataBase = FXCollections.observableArrayList();
		data = FXCollections.observableArrayList();
	}

	/**
	 * Where connection action happens
	 * @param actionEvent
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@FXML
	private void connectToDatabse(ActionEvent actionEvent) throws ClassNotFoundException
                                                                    ,InstantiationException
                                                                    ,IllegalAccessException {
		connectionInfo.setUsername(username.getText());
		connectionInfo.setPassword(password.getText());
		checkIfEmpty(username.getText(), password.getText());
	}

	/**
	 * takes the table from the combo box and triggers the dynamic table populate function
	 * @param actionEvent
	 * @throws SQLException
	 */
	@FXML
	private void selectedTable(ActionEvent actionEvent) throws SQLException {
		fillTable(tableList.getSelectionModel().getSelectedItem());
	}

	/**
	 * first clears the field
	 * creates required number of textfield dynamically
	 * fills the textfields with the data selected on tableview
	 * also creates button dynamically and waits for click action to that button
	 * takes the variables calls update function from model
	 * lastly refreshes table
	 * @param actionEvent
	 */
	@FXML
	private void updateRecord(ActionEvent actionEvent) {
		textfieldArea.getChildren().clear();
		try{
			// initialization of required fields and datas
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

			// creating text fields dynamically
			for(int i=0;i<rowCount-1;i++) {
				textField[i] = new TextField();
				textField[i].setText(vals[i+1]);
				textField[i].setPromptText(resultSet.getMetaData().getColumnName(i+2));
				textfieldArea.getChildren().add(textField[i]);
			}
			dynamicFieldButton.setText(val);
			textfieldArea.getChildren().add(dynamicFieldButton);

			// this waits for click action of dynamicly created button
			dynamicFieldButton.setOnAction((EventHandler<ActionEvent>) e -> {
				try {
					ArrayList<matrices> values = new ArrayList<matrices>();
					for(int i=1;i<rowCount;i++) {
						values.add(new matrices(resultSet.getMetaData().getColumnName(i+1)
								,textField[i-1].getText().trim()));
					}
					// call the update function
					model.update(connection,table,values,column,id);

					// refresh table
					fillTable(table);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			});
		}catch (Exception e){
			showDialog("You didn't choose a table or logged in!", Alert.AlertType.WARNING);
		}
	}

	/**
	 * does same as before
	 * this time it inserts the data
	 * @param actionEvent
	 */
	@FXML
	private void insertRecord(ActionEvent actionEvent) {
		textfieldArea.getChildren().clear();
		try{
			dynamicFieldButton = new Button("btn");
			String table = tableList.getSelectionModel().getSelectedItem();

			int rowCount = resultSet.getMetaData().getColumnCount();
			String val = pressedButton(actionEvent);
			TextField textField[] = new TextField[rowCount];
			for(int i=0;i<rowCount;i++) {
				textField[i] = new TextField();
				textField[i].setPromptText(resultSet.getMetaData().getColumnName(i+1));
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
					try{
						model.insert(connection,table,values);
					} catch (Exception ex) {
						showDialog("Please Fill the Required Fields!", Alert.AlertType.WARNING);
					}

					fillTable(table);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			});
		}catch (Exception e){
			showDialog("You didn't choose a table or logged in!", Alert.AlertType.WARNING);
		}
	}

	/**
	 * deletes seletcted item on tableview
	 * then refreshes the tableview
	 * @param actionEvent
	 * @throws SQLException
	 */
	@FXML
	private void deleteFromTable(ActionEvent actionEvent) throws SQLException {
		Optional<ButtonType> respond = showDialog("Are You Sure You Want to Delete This Record"
															,Alert.AlertType.CONFIRMATION);

		if(respond.get() != ButtonType.OK) return;

		String table = tableList.getSelectionModel().getSelectedItem();
		String column = resultSet.getMetaData().getColumnName(1);
		String rowValue = String.valueOf(tableView.getSelectionModel().getSelectedItem());
		String[] vals = rowValue.split(",");
		int  id = Integer.parseInt(vals[0].replace("[",""));

		// calls the delete function from model
		model.delete(connection,table,column,id);

		// refreshes the tableview
		fillTable(table);
	}

	/**
	 * error handling for empty fields
	 * checks the login information
	 * @param username database username
	 * @param password database password
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void checkIfEmpty(String username, String password) throws ClassNotFoundException
	                                                                   ,InstantiationException
	                                                                   ,IllegalAccessException {
		if(username.isEmpty() || password.isEmpty()){
			showDialog("Please Fill The Required Fields!", Alert.AlertType.WARNING);
		} else {
			makeTheConnection();
		}
	}

	/**
	 * creates database connection if everything is set
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void makeTheConnection() throws ClassNotFoundException
	                                        ,IllegalAccessException
	                                        ,InstantiationException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbBrowser"
														,connectionInfo.getUsername()
														,connectionInfo.getPassword());
			changeButton();
			fillCombos();
		} catch (Exception e){
			showDialog("Invalid username or password! ", Alert.AlertType.WARNING);
		}
	}

	/**
	 * changes the button color to green when successfully connected
	 * @throws SQLException
	 */
	private void changeButton() throws SQLException {
		if (connection.isValid(0)) {
			connect.setText("Connected");
			connect.setStyle("-fx-background-color: #00ff00; ");
		}
	}

	/**
	 * takes the table names from database and fills the combobox
	 * @throws SQLException
	 */
	private void fillCombos() throws SQLException {
		// returns the data names form databaser
		ResultSet resultSet = model.tableList(connection);

		// populates the combobox
		while(resultSet.next()){
			tablesInDataBase.add(resultSet.getString(1));
		}
		tableList.setItems(tablesInDataBase);
	}

	/**
	 * takes data from selected table and sends to tableview functions
	 * @param selectedTable
	 * @throws SQLException
	 */
	private void fillTable(String selectedTable) throws SQLException {
		resultSet = model.tableData(connection, selectedTable);
		prepareTableView(resultSet);
		fillTableView(resultSet);
	}

	/**
	 * dynamically creates table column
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
	 * populates the table according to desired tables data
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
	 * on every change of table
	 * we need to clear tableview and observablelist
	 */
	private void clearAllData(){
		data.clear();
		tableView.getItems().clear();
		tableView.getColumns().clear();
	}

	/**
	 * handles navigation button such as first, last, previous, next
	 * gives ability to navigations via buttons on top of the window
	 * @param actionEvent
	 */
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

	/**
	 * takes the index from and moves back and forth on tableview rows
	 * @param index
	 */
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
	 * according to message fills the dialog box
	 * @param message
	 * @return
	 */
	private Optional<ButtonType> showDialog(String message, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setContentText(message);
		return alert.showAndWait();
	}
}
