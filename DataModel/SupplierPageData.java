package au.com.einsporn.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SupplierPageData {

    private ObservableList<Supplier> suppliersDataList;


    public SupplierPageData() {

        suppliersDataList = FXCollections.observableArrayList();
    }


    public ObservableList<Supplier> getSuppliersDataList(){

        return suppliersDataList;

    }


    private void addToSuppliersDataList(Supplier suppliers){

        suppliersDataList.addAll(suppliers);

    }


    public void retriveSupplierFromDB() {

        String SQL = "SELECT * FROM Supplier";

        try (Connection con = DBConnection.getconnection(); PreparedStatement ps= con.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                addToSuppliersDataList(new Supplier(rs.getInt("SupplierID"),
                        rs.getString("SupplierName"), rs.getString("Email"), rs.getString("Phone"),
                        rs.getString("Address"),rs.getString("Notes")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void retriveSearchSupplierFromDB(String searchWord, String radioBtnSelected) throws SQLException {


        String text = "%"+ searchWord+ "%";
        PreparedStatement ps =null;
        ResultSet rs= null;


        try (Connection con = DBConnection.getconnection()) {

            if (radioBtnSelected.equals("searchByID")) {

                ps = con.prepareStatement("SELECT * FROM Supplier where Supplier.SupplierID LIKE ?");// Returns all rows where last name contains the letters as in Text1.
                ps.setString(1,text);
                rs = ps.executeQuery();


            }else if(radioBtnSelected.equals("searchByName")) {

                ps = con.prepareStatement("SELECT * FROM Supplier where Supplier.SupplierName LIKE ?");// Returns all rows where last name contains the letters as in Text1.
                ps.setString(1,text);
                rs = ps.executeQuery();



        }else if(radioBtnSelected.equals("searchByAddress")) {

                ps = con.prepareStatement("SELECT * FROM Supplier where Supplier.SupplierAddress LIKE ?");// Returns all rows where last name contains the letters as in Text1.
                ps.setString(1,text);
                rs = ps.executeQuery();
        }

            while (rs.next()) {
                addToSuppliersDataList(new Supplier(rs.getInt("SupplierID"),
                        rs.getString("SupplierName"), rs.getString("Email"), rs.getString("Phone"),
                        rs.getString("Address"), rs.getString("Notes")));

            }

        } catch (SQLException e) {
            e.printStackTrace();


        } finally {

            if(ps!=null){

                ps.close();
            }
        } if(rs!=null){

            rs.close();
        }

    }

    public void addEditSupplierToDB(Supplier supplier, String addOrEddit) throws Exception {


        Integer id = supplier.getSupplierId();
        String name = supplier.getName();
        String email = supplier.getEmail();
        String phone = supplier.getPhone();
        String address = supplier.getAddress();
        String notes = supplier.getNotes();


        PreparedStatement stm = null;

        try(Connection con = DBConnection.getconnection()) {

            if(addOrEddit.equals("addSupplier")) {
                stm = con.prepareStatement("INSERT INTO Supplier(SupplierID,SupplierName,Email,Phone,Address,Notes) VALUES (?,?,?,?,?,?)");
                stm.setInt(1, id);
                stm.setString(2,name);
                stm.setString(3, email);
                stm.setString(4, phone);
                stm.setString(5, address);
                stm.setString(6, notes);

                stm.execute();


            } else if(addOrEddit.equals("editSupplier")) {

                stm = con.prepareStatement("UPDATE Supplier SET SupplierName=?,Email=?,Phone=?,Address=?, Notes=? where SupplierID=?");
                stm.setString(1,name);
                stm.setString(2, email);
                stm.setString(3, phone);
                stm.setString(4, address);
                stm.setString(5, notes);
                stm.setInt(6, id);
                stm.execute();
            }

        } finally {

            if (stm != null) {

                stm.close();
            }

        }
    }



    public Integer retriveLastSupplierNumberFromDB(){

        Integer lastCutomerNumber=null;
        String SQL = "SELECT MAX(Supplier.SupplierID) as SupplierNumber FROM Supplier";

        try (Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {


            if(rs.next()) {
                lastCutomerNumber = rs.getInt("SupplierNumber");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return lastCutomerNumber;
    }



    public void deleteSupplierFromDB(Integer supplierId) {

        String SQL = "DELETE FROM Supplier WHERE SupplierID=?";

        try(Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement(SQL)){

            ps.setInt(1, supplierId);

            } catch ( SQLException e){

            e.printStackTrace();
        }
        }
    }

