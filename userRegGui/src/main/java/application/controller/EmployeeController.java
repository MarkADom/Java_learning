package application.controller;

import application.dao.EmployeeDAO;
import application.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EmployeeController {

    @FXML
    private TextField empIdText;
    @FXML
    private TextArea resultArea;
    @FXML
    private TextField newEmailText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField surnameText;
    @FXML
    private TextField emailText;
    @FXML
    private TableView employeeTable;
    @FXML
    private TableColumn<Employee, Integer> empIdColumn;
    @FXML
    private TableColumn<Employee, String> empNameColumn;
    @FXML
    private TableColumn<Employee, String> empLastNameColumn;
    @FXML
    private TableColumn<Employee, String> empEmailColumn;
    @FXML
    private TableColumn<Employee, String> empPhoneNumberColumn;
    @FXML
    private TableColumn<Employee, Date> empHireDateColumn;

    //Search an employee
    @FXML
    private void searchEmployee(ActionEvent actionEvent)
            throws ClassNotFoundException, SQLException {
        try {
            //Get Employee information
            Employee emp = EmployeeDAO.searchEmployee(empIdText.getText());
            //Populate Employee on TableView and Display on TextArea
            populateAndShowEmployee(emp);
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error occurred while getting employee information from DB.\n" + e);
            throw e;
        }
    }

    //Search all employees
    @FXML
    private void searchEmployees(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException {
        try {
            //Get all Employees information
            ObservableList<Employee> empData = EmployeeDAO.searchEmployees();
            //Populate Employees on Table View
            populateEmployees(empData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting employees informatio from DB.\n" + e);
            throw e;
        }
    }

    //For MultiThreading
    private Executor exec;

    //Initializing the controller class.
    //This method is automatically called after the fxml has been load
    @FXML
    private void initialize() {
        /*
        The setCellValueFactory(...) that we set on the table columns are used to determine
        wich field inside the Employee objects should be used for the particular column.
        The arrow -> inicates that we're using a Java 8 feature called Lambdas.
        (Another option would be to use a PropertyValueFactory, but this is not type-safe).
        We're only using StringProperty values for our table columns in this example .
        When you want to use IntegerProperty or DoubleProperty, the setCellValueFactory(...)
        must have an additional asObject():
         */


        //For multithreading: Create executor that uses daemon threads

        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            return t;
        });



        empIdColumn.setCellValueFactory(cellData -> cellData.getValue().employee_idProperty().asObject());
        empNameColumn.setCellValueFactory(cellData -> cellData.getValue().first_nameProperty());
        empLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().last_nameProperty());
        empEmailColumn.setCellValueFactory(cellDta -> cellDta.getValue().emailProperty());
        empPhoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phone_numberProperty());
        empHireDateColumn.setCellValueFactory(cellData -> cellData.getValue().hire_dateProperty());
    }


    //populate Employees for TableView with MultiThreading
    private void fillEmployTable(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException {
        Task<List<Employee>> task = new Task<List<Employee>>() {
            @Override
            public ObservableList<Employee> call()
                    throws Exception {
                return EmployeeDAO.searchEmployees();
            }
        };
        task.setOnFailed(e -> task.getException().printStackTrace());
        task.setOnSucceeded(e -> employeeTable.setItems((ObservableList<Employee>) task.getValue()));
        exec.execute(task);
    }

    //Populate Employee
    @FXML
    private void populateEmployee(Employee emp)
            throws ClassNotFoundException {
        //Declare ObservableList fot table view
        ObservableList<Employee> empData = FXCollections.observableArrayList();
        //Add employee to the ObservableList
        empData.add(emp);
        employeeTable.setItems(empData);
    }

    //Set Employee information to Text Area
    @FXML
    private void setEmpInfoToTextArea(Employee emp) {
        resultArea.setText("First Name: " + emp.getFirst_name() + "\n" + "Last Name: " + emp.getLast_name());
    }

    //Populate Employee for TableView and Display Employee on Text Area
    @FXML
    private void populateAndShowEmployee(Employee emp)
            throws ClassNotFoundException {
        if (emp != null) {
            populateEmployee(emp);
            setEmpInfoToTextArea(emp);
        } else {
            resultArea.setText("This employee does not exist!\n");
        }
    }

    //Populate Employees for TableView
    @FXML
    private void populateEmployees(ObservableList<Employee> empData)
            throws ClassNotFoundException {
        //Set item to the employeeTable
        employeeTable.setItems(empData);
    }

    //Update employee's email with the email which is written on newEmailText field
    @FXML
    private void updateEmployeeEmail(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException {
        try {
            EmployeeDAO.updateEmpEmail(empIdText.getText(), newEmailText.getText());
            resultArea.setText("Email has been updated for, employee id: " + empIdText.getText() + "\n");
        } catch (SQLException e) {
            resultArea.setText("Problem occurred while updating email: " + e);
        }
    }

    //Insert an employee to the DB
    @FXML
    private void insertEmployee(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException {
        try {
            EmployeeDAO.insertEmp(nameText.getText(), surnameText.getText(), emailText.getText());
            resultArea.setText("Employee inserted! \n");
        } catch (SQLException e) {
            resultArea.setText("Problem ocurred while inserting employee " + e);
            throw e;
        }
    }

    //Delete and employee with a given employee Id from DB
    @FXML
    private void deleteEmployee(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException {
        try {
            EmployeeDAO.deleteEmpWithId(empIdText.getText());
            resultArea.setText("Employee deleted! Employee id: " + empIdText.getText() + "\n");
        } catch (SQLException e) {
            resultArea.setText("Problem ocurred while deleting employee " + e);
            throw e;
        }
    }
}
