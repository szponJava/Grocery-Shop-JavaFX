package au.com.einsporn.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UnitsDialogBoxData {


    private ObservableList<String> addNewUnitsListView;


    public UnitsDialogBoxData() {

        addNewUnitsListView = FXCollections.observableArrayList();

    }

    private void addUnitsToListView(String add) {

        addNewUnitsListView.addAll(add);


    }

    public ObservableList<String> getAddNewUnitsListView() {

        return addNewUnitsListView;
    }


    public void retriveUnitsFromDB() {

        String SQL = "SELECT * FROM Units";

        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement();ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                addUnitsToListView(rs.getString("Units"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addUnitsToDB(String units) throws Exception {


        try(Connection con = DBConnection.getconnection(); PreparedStatement ps =con.prepareStatement("INSERT Units (Units) VALUES (?)"))  {

            ps.setString(1, units);

            ps.execute();

        }
    }


    public void deleteUnitsFromDB(String units) throws Exception {



        try(Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement("DELETE Units where Units=?")) {

            ps.setString(1, units);

            ps.execute();

        }

    }
}

    
