package au.com.einsporn.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CategoryDialogBoxData {


    private ObservableList<String> addNewCategoryList;


    public CategoryDialogBoxData() {

        addNewCategoryList = FXCollections.observableArrayList();

    }

    private void addCategoryToList(String add) {

        addNewCategoryList.addAll(add);

    }

    public ObservableList<String> getAddNewCategoryList() {

        return addNewCategoryList;
    }



    public void retriveCategoryFromDB() {

        String SQL = "SELECT * FROM Category";

        try (Connection con = DBConnection.getconnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {


            while (rs.next()) {
                addCategoryToList(rs.getString("Category"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addCategoryToDB(String category) {


        try(Connection con = DBConnection.getconnection();
            PreparedStatement ps = con.prepareStatement("INSERT Category (Category) VALUES (?)")) {

            ps.setString(1, category);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteCategoryFromDB(String category) throws Exception {


        try (Connection con = DBConnection.getconnection();
             PreparedStatement ps = con.prepareStatement("DELETE Category where Category=?")) {

            ps.setString(1, category);
            ps.execute();

        } catch(SQLException e){
            e.printStackTrace();

        }

    }
}

    
