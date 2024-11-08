package application.dao;

import application.model.Employee;
import application.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO {

    //*******************************
    //SELECT an Employee
    //*******************************
    public static Employee searchEmployee(String empId)
            throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM employees WHERE employee_id" + empId;

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

            //Send Result to the getEmployeeFromResultSet method and get employee object
            Employee employee = getEmployeeFromResultSet(rsEmp);

            //Return employee object
            return employee;
        } catch (SQLException e) {
            System.out.println("While searching an employee with "
                    + empId
                    + "id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    //Use ResultSet from DB as a parameter and set Employee Object's attributes and return object
    private static Employee getEmployeeFromResultSet(ResultSet rs)
            throws SQLException {
        Employee emp = null;
        if (rs.next()) {
            emp = new Employee();
            emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
            emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
            emp.setFirst_name(rs.getString("FIRST_NAME"));
            emp.setLast_name(rs.getString("LAST_NAME"));
            emp.setEmail(rs.getString("EMAIL"));
            emp.setPhone_number(rs.getString("PHONE_NUMBER"));
            emp.setHire_date(rs.getDate("HIRE_DATE"));
            emp.setJob_id(rs.getString("JOB_ID"));
            emp.setSalary(rs.getInt("SALARY"));
            emp.setCommission_pct(rs.getDouble("COMMISSION_PCT"));
            emp.setManager_id(rs.getInt("MANAGER_ID"));
            emp.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
        }
        return emp;
    }

    //*******************************
    //SELECT Employees
    //*******************************
    public static ObservableList<Employee> searchEmployees()
            throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM employees";

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmplyeeList method and get employee object
            ObservableList<Employee> empList = getEmployeeList(rsEmps);

            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    //Select * from employees operation
    private static ObservableList<Employee> getEmployeeList(ResultSet rs)
            throws SQLException, ClassNotFoundException {
        //Declare a observable list witch consists of Employee objects
        ObservableList<Employee> empList = FXCollections.observableArrayList();

        while (rs.next()) {
            Employee emp = new Employee();
            emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
            emp.setFirst_name(rs.getString("FIRST_NAME"));
            emp.setLast_name(rs.getString("LAST_NAME"));
            emp.setEmail(rs.getString("EMAIL"));
            emp.setPhone_number(rs.getString("PHONE_NUMBER"));
            emp.setHire_date(rs.getDate("HIRE_DATE"));
            emp.setJob_id(rs.getString("JOB_ID"));
            emp.setSalary(rs.getInt("SALARY"));
            emp.setCommission_pct(rs.getDouble("COMMISSION_PCT"));
            emp.setManager_id(rs.getInt("MANAGER_ID"));
            emp.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
            //Add employee to the ObservableList
            empList.add(emp);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }

    //*************************************
    //UPDATE an employee's email address
    //*************************************
    public static void updateEmpEmail(String empId, String empEmail)
            throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt = "BEGIN\n"
                + " UPDATE employees\n"
                + " SET EMAIL = '"
                + empEmail
                + "'\n"
                + " WHERE EMPLOYEE_ID = "
                + empId
                + ";\n"
                + "COMMIT;\n"
                + "END;";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.println("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE an employee
    //*************************************
    public static void deleteEmpWithId(String empid)
            throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt = "BEGIN\n"
                + " DELETE FROM employees\n"
                + " WHERE employee_id ="
                + empid
                + ";\n"
                + " COMMIT;\n"
                + "END;";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.println("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //INSERT an employee
    //*************************************
    public static void insertEmp(String name, String lastname, String email)
            throws SQLException, ClassNotFoundException {
        //Declare a INSERT statement
        String updateStmt = "BEGIN\n"
                + " INSERT INTO employees\n"
                + " (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, HIRE_DATE, JOB_ID);\n"
                + " VALUES\n"
                + " (sequence_employee.nextval, '" + name + "', '" + lastname + "','" + email + "', SYSDATE, 'IT_PROG');\n"
                + " END;";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.println("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
}
