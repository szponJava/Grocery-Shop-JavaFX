package au.com.einsporn.DataModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class ShopPageData {

    private ObservableList<Product> selectProductTableList;
    private ObservableList<CustomerBasketTable> customerBasketTableList;


    public ShopPageData() {

        selectProductTableList = FXCollections.observableArrayList();
        customerBasketTableList = FXCollections.observableArrayList();

    }


    public ObservableList<Product> getSelectProductTableList() {
        return selectProductTableList;
    }

    public ObservableList<CustomerBasketTable> getCustomerBasketTableList() {
        return customerBasketTableList;
    }


    public void addCustomerBasketTableList(CustomerBasketTable customerBasketTable) {

        this.customerBasketTableList.addAll(customerBasketTable);

    }

    private void addToSelectProductTableList(Product selectProductTable) {

        this.selectProductTableList.addAll(selectProductTable);

    }

    public void removeProductFromCustomerBasketTableList(CustomerBasketTable customerBasketTable){
        this.customerBasketTableList.remove(customerBasketTable);

    }





    public void retriveCustomerBasketItemsFromDB() {

        String SQL = "SELECT * FROM CustomerBasket";

        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                addCustomerBasketTableList(new CustomerBasketTable(rs.getInt("Id"),rs.getString("ProductName"), rs.getString("ProductCode"),
                        rs.getString("Size"), rs.getString("Units"), rs.getString("Category"),
                        rs.getString("Quantity"),rs.getString("Prise")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void retriveSearchSelectProductTableItemsFromDB(String text, String radioBtnSelection) throws SQLException {


        String text1 = "%" + text+ "%" ;
        ResultSet rs = null;
        PreparedStatement ps = null;


        try (Connection con = DBConnection.getconnection()) {

            if (radioBtnSelection.equals("description")) {

                ps = con.prepareStatement("SELECT * FROM Products where Products.ProductName LIKE ?"); // Returns all rows where the product name contains the letters as in Text1.
                ps.setString(1,text1);
                rs = ps.executeQuery();

            } else if (radioBtnSelection.equals("category")) {


                ps = con.prepareStatement("SELECT * FROM Products where Products.Category LIKE ?"); // Returns all rows where the product name contains the letters as in Text1.
                ps.setString(1,text1);
                rs = ps.executeQuery();

            }

            while (rs.next()) {
                addToSelectProductTableList(new Product(rs.getString("ProductName"),
                        rs.getString("ProductCode"), rs.getString("Size"), rs.getString("Units"),
                        rs.getString("Category"), rs.getString("StockLevel"),rs.getString("Price")));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {

            if(ps!=null){

                ps.close();
            }
        }
    }



    public void addCustomerBasketItemToDB(Integer id, String productName, String productCode, String size, String unit, String category, String quantity, String price) throws SQLException {


        String SQL = "INSERT INTO CustomerBasket(Id,ProductName,ProductCode,Size,Units,Category,Quantity,Prise) VALUES (?,?,?,?,?,?,?,?)";

        try(Connection con = DBConnection.getconnection();PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, id);
            ps.setString(2, productName);
            ps.setString(3, productCode);
            ps.setString(4, size);
            ps.setString(5, unit);
            ps.setString(6, category);
            ps.setString(7, quantity);
            ps.setString(8, price);
            ps.execute();

        }
    }



    public void editSelectedCustomerBasketIteminInDB(Integer id,Integer quantity, Float prise) throws SQLException {


        try( Connection con = DBConnection.getconnection();PreparedStatement ps = con.prepareStatement("UPDATE CustomerBasket set Quantity=?, Prise=? where Id=?")) {


            ps.setInt(1, quantity);
            ps.setFloat(2,prise);
            ps.setInt(3, id);
            ps.execute();

        }
    }



    public void removeSelectedCustomerBasketItemFromDB(Integer id) throws SQLException {

        try(Connection con = DBConnection.getconnection();PreparedStatement ps = con.prepareStatement("DELETE FROM CustomerBasket where id=?")) {

            ps.setInt(1, id);
            ps.execute();
        }
    }


    public void clearCustomerBasketItemsFromDB() throws SQLException {

        try(Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM CustomerBasket")) {

            ps.execute();

        }
    }
}









