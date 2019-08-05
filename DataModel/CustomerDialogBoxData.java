package au.com.einsporn.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerDialogBoxData {

    private ObservableList<Customer> customerDialogBoxDataTableView;

    public CustomerDialogBoxData() {

        customerDialogBoxDataTableView = FXCollections.observableArrayList();

    }


    private void addCustomerDialogBoxDataTableView(Customer customer) {

        customerDialogBoxDataTableView.addAll(customer);

    }


    public ObservableList<Customer> getCustomerDialogBoxDataTableView() {

        return customerDialogBoxDataTableView;

    }


    public void retriveCustomersFromDB(){

        String SQL = "SELECT * FROM Customers";

        try (Connection con = DBConnection.getconnection();
             PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                addCustomerDialogBoxDataTableView(new Customer(rs.getInt("CustomerNumber"), rs.getString("FirstName"), rs.getString("LastName")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


