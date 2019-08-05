package au.com.einsporn.DataModel;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerPageData {


    private ObservableList<Customer> customersDataList;
    private ObservableList<Customer> selectedCustomer;

    public CustomerPageData() {

        customersDataList = FXCollections.observableArrayList();
        selectedCustomer = FXCollections.observableArrayList();


    }

    private void addDataToCustomersList(Customer customers) {

        customersDataList.addAll(customers);

    }

    public ObservableList<Customer> getCustomersDataList() {
        return customersDataList;
    }


    private void addDataToSelectedCustomerList(Customer customer) {

        selectedCustomer.addAll(customer);

    }

    public ObservableList<Customer> getSelectedCustomerList()
    {

        return selectedCustomer;
    }



    public void retriveCustomersFromDB() {

        String SQL = "SELECT * FROM Customers";

        try (Connection con = DBConnection.getconnection();
             PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {
                addDataToCustomersList(new Customer(rs.getInt("CustomerNumber"),rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Address"), rs.getString("Suburb"), rs.getString("State"),rs.getString("PostCode")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void retriveSearchSaleProductTableItemsFromDB(String text, String radioBtnSelected) throws SQLException {


        String text1 =  "%" + text+ "%" ;

        ResultSet rs=null;
        PreparedStatement ps = null;

        try (Connection con = DBConnection.getconnection()) {

            // selection of the SQL statement depending on RadioButton clicked by the user
            if (radioBtnSelected.equals("firstName")) {

                ps = con.prepareStatement("SELECT * FROM Customers where Customers.FirstName like ? ");// Returns all rows where  first name contains the letters as in Text1.
                ps.setString(1,text1);
                rs = ps.executeQuery();


            } else if(radioBtnSelected.equals("lastName")) {


                ps = con.prepareStatement("SELECT * FROM Customers where Customers.LastName like ?");// Returns all rows where  first name contains the letters as in Text1.
                ps.setString(1,text1);
                rs = ps.executeQuery();


            }else if(radioBtnSelected.equals("address")) {

                ps = con.prepareStatement("SELECT * FROM Customers where Customers.Address like ?");// Returns all rows where  first name contains the letters as in Text1.
                ps.setString(1,text1);
                rs = ps.executeQuery();

            }

            while (rs.next()) {
                addDataToCustomersList(new Customer(rs.getInt("CustomerNumber"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Address"), rs.getString("Suburb"), rs.getString("State"),rs.getString("PostCode")));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            if(ps!=null){
                ps.close();
            }

            if(rs!=null){
                rs.close();
            }

        }
    }



    public Integer retriveLastCustomerNumberFromDB() {

        Integer lastCutomerNumber=null;

        String SQL = "SELECT MAX(Customers.CustomerNumber) as CustomerNumber FROM Customers";

        try (Connection con = DBConnection.getconnection();
             PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            if(rs.next()) {
                lastCutomerNumber = rs.getInt("CustomerNumber");
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }

        return lastCutomerNumber;
       }


    public void addEditCustomerToDB(Integer customerNumber, Customer customers, String addEdit) throws Exception {


        String firstName = customers.getFirstName();
        String lastName = customers.getLastName();
        String addreess = customers.getAddress();
        String suburb = customers.getSuburb();
        String state = customers.getState();
        String postCode = customers.getPostCode();


        Connection con = null;
        PreparedStatement stm = null;

        try {

            con = DBConnection.getconnection();


            if(addEdit.equals("add")) {
                stm = con.prepareStatement("INSERT INTO Customers(CustomerNumber,FirstName,LastName,Address,Suburb,State,PostCode) VALUES (?,?,?,?,?,?,?)");
                stm.setInt(1, customerNumber);
                stm.setString(2, firstName);
                stm.setString(3, lastName);
                stm.setString(4, addreess);
                stm.setString(5, suburb);
                stm.setString(6, state);
                stm.setString(7, postCode);
                stm.execute();

            } else {

                stm = con.prepareStatement("UPDATE Customers SET FirstName=?,LastName=?,Address=?,Suburb=?,State=?,PostCode=? where CustomerNumber=?");
                stm.setString(1, firstName);
                stm.setString(2, lastName);
                stm.setString(3, addreess);
                stm.setString(4, suburb);
                stm.setString(5, state);
                stm.setString(6, postCode);
                stm.setInt(7, customerNumber);
                stm.execute();
            }


        } finally {

            if (stm != null) {

                stm.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }



    public void deleteCustomerFromDB(Integer customerNumber){


        try ( Connection con = DBConnection.getconnection();
              PreparedStatement ps = con.prepareStatement("DELETE FROM Customers where CustomerNumber=?")) {

            ps.setInt(1, customerNumber);
            ps.execute();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }



    public void updateSelectedCutomerInDB(Integer customerNumber, String customerFirsName, String customerLastName){


        try(Connection con = DBConnection.getconnection();
            PreparedStatement ps = con.prepareStatement("UPDATE SelectedCustomer SET CustomerNumber=?,CustomerFirstName=?,CustomerLastName=?")) {

            ps.setInt(1, customerNumber);
            ps.setString(2,customerFirsName);
            ps.setString(3,customerLastName);
            ps.execute();


        }catch (SQLException e){
            e.printStackTrace();

        }
    }


    public void retriveSelectedCustomer() {

        String SQL = "SELECT * FROM SelectedCustomer";

        try (Connection con = DBConnection.getconnection();
             PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            if(rs.next()) {
                addDataToSelectedCustomerList(new Customer(rs.getInt("CustomerNumber"),rs.getString("CustomerFirstName"),rs.getString("CustomerLastName")));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
