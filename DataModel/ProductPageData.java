package au.com.einsporn.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
public class ProductPageData {


    private ObservableList<Product> productDataList;
    private ObservableList<String> comboBoxListCategory;
    private ObservableList<String> comboBoxListSupplier;
    private ObservableList<String> comboBoxListUnits;


    public ProductPageData() {

        productDataList = FXCollections.observableArrayList();
        comboBoxListCategory = FXCollections.observableArrayList();
        comboBoxListSupplier = FXCollections.observableArrayList();
        comboBoxListUnits = FXCollections.observableArrayList();

    }


    private void addToProductDataList(Product product) {
        this.productDataList.addAll(product);

    }

    public ObservableList<Product> getProductDataList() {
        return productDataList;

    }


    public ObservableList<String> getComboBoxListCategory() {
        return comboBoxListCategory;
    }

    private void addToComboBoxListCategory(String text) {
        this.comboBoxListCategory.addAll(text);

    }


    public ObservableList<String> getComboBoxListSupplier() {
        return comboBoxListSupplier;
    }

    private void addComboBoxListSupplier(String text) {
        this.comboBoxListSupplier.addAll(text);

    }


    public ObservableList<String> getComboBoxListUnits() {
        return comboBoxListUnits;
    }


    private void addComboBoxListUnits(String text) {
        this.comboBoxListUnits.addAll(text);

    }


    public void retriveProductsFromDB() {

        String SQL = "SELECT * FROM Products";
        try (Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement(SQL); ResultSet rs= ps.executeQuery()) {

            while (rs.next()) {
                addToProductDataList(new Product(rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getString("ProductCode"), rs.getString("Category"), rs.getString("Size"),
                        rs.getString("Units"), rs.getString("StockLevel"), rs.getString("Vendor"),
                        rs.getString("MainSuplier"), rs.getString("Price")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void retriveSearchProductFromDB(String searchWord, String radioBtnselection) throws SQLException {

        String text = "%"+ searchWord + "%";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection con = DBConnection.getconnection()) {

            // selction of the SQL statment depending on RadioButton clicked by the user
            if (radioBtnselection.equals("description")) {

                ps = con.prepareStatement("SELECT * FROM Products where Products.ProductName like ?");// Returns rows where Product name contains searchword.
                ps.setString(1,text);
                rs = ps.executeQuery();


            } else if (radioBtnselection.equals("category")) {

                ps = con.prepareStatement("SELECT * FROM Products where Products.Category like ?");// Returns rows where Category contains searchword.
                ps.setString(1,text);
                rs = ps.executeQuery();

            } else if (radioBtnselection.equals("vendor")) {


                ps = con.prepareStatement("SELECT * FROM Products where Products.Vendor like ?");// Returns rows where Vendor contains searchword.
                ps.setString(1,text);
                rs = ps.executeQuery();

            } else if (radioBtnselection.equals("supplier")) {


                ps = con.prepareStatement("SELECT * FROM Products where Products.MainSuplier like ?");// Returns rows where Supplier contains searchword.
                ps.setString(1,text);
                rs = ps.executeQuery();
            }
            else if (radioBtnselection.equals("stockLevel")) {


                ps = con.prepareStatement("SELECT * FROM Products where Products.StockLevel < ?");// Returns rows where Stock level is less than contains searchWord.
                ps.setString(1,searchWord);
                rs = ps.executeQuery();
                System.out.println("Stock Level" );
            }
            while (rs.next()) {
                addToProductDataList(new Product(rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getString("ProductCode"), rs.getString("Category"), rs.getString("Size"),
                        rs.getString("Units"), rs.getString("StockLevel"), rs.getString("Vendor"),
                        rs.getString("MainSuplier"), rs.getString("Price")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps!=null){
                ps.close();
            } if(rs!=null){
                rs.close();
            }
        }

    }


    public void retriveCategoryComboBoxListFromDB() {

        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement()) {
            String SQL = "SELECT * FROM Category";

            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                addToComboBoxListCategory(rs.getString("Category"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void retriveSupplierComboBoxListFromDB() {

        String SQL = "SELECT * FROM Supplier";

        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(SQL)) {


            while (rs.next()) {
                addComboBoxListSupplier(rs.getString("SupplierName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void retriveUnitsComboBoxListFromDB() {

        String SQL = "SELECT * FROM Units";
        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(SQL)) {


            while (rs.next()) {
                addComboBoxListUnits(rs.getString("Units"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addEditProductToDB(Product product, String addEditCustomer) throws Exception {


        Integer productId = product.getId();
        String description = product.getDescription();
        String code = product.getCode();
        String category = product.getCategory();
        String size = product.getSize();
        String unit = product.getUnit();
        String stockLevel = product.getStockLevel();
        String vendor = product.getVendor();
        String supplier = product.getMainSupplier();
        String price = product.getPrice();


        PreparedStatement ps = null;

        try( Connection con = DBConnection.getconnection()) {

            if (addEditCustomer.equals("addNewProduct")) {
                ps = con.prepareStatement("INSERT INTO Products(ProductID,ProductName,ProductCode,Category,Size,Units,StockLevel,Vendor,MainSuplier,Price) VALUES (?,?,?,?,?,?,?,?,?,?)");
                ps.setInt(1, productId);
                ps.setString(2, description);
                ps.setString(3, code);
                ps.setString(4, category);
                ps.setString(5, size);
                ps.setString(6, unit);
                ps.setString(7, stockLevel);
                ps.setString(8, vendor);
                ps.setString(9, supplier);
                ps.setString(10, price);
                ps.execute();


            } else if (addEditCustomer.equals("editProduct")) {

                ps = con.prepareStatement("UPDATE Products SET ProductName=?,ProductCode=?,Category=?,Size=?,Units=?,StockLevel=?,Vendor=?,MainSuplier=?,Price=? WHERE ProductID=?");

                ps.setString(1, description);
                ps.setString(2, code);
                ps.setString(3, category);
                ps.setString(4, size);
                ps.setString(5, unit);
                ps.setString(6, stockLevel);
                ps.setString(7, vendor);
                ps.setString(8, supplier);
                ps.setString(9, price);
                ps.setInt(10, productId);
                ps.execute();

            }

        } finally {

            if (ps != null) {

                ps.close();
            }
        }
    }


    public void deleteProductFromDB(Integer productId) throws Exception {


        try( Connection con = DBConnection.getconnection();PreparedStatement ps = con.prepareStatement("DELETE FROM Products WHERE ProductID=? ")) {

            ps.setInt(1, productId);
            ps.execute();

        }
    }


    public void updateProductStockLevelInDB(String productCode, String stockLevel) throws Exception {


        try( Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement("UPDATE Products SET Products.StockLevel=? WHERE ProductCode=? ")) {

            ps.setString(1, stockLevel);
            ps.setString(2, productCode);
            ps.execute();

        }
    }


    public String retriveProductStockLevelFromDB(String productCode) throws SQLException {

        String stockLevel=null;

        String text = "%" + productCode + "%";

        String SQL = "SELECT StockLevel FROM Products WHERE ProductCode=?";
        try (Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {

            ps.setString(1,text);

            while (rs.next()) {

               stockLevel = rs.getString("StockLevel");
            }

        }

        return stockLevel;
    }



    public Integer retriveLastProductIDFromDB() {

        Integer lastProductNumber=null;

        String SQL = "SELECT MAX(Products.ProductID) as ProductID FROM Products";
        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement();ResultSet  rs = stmt.executeQuery(SQL);) {

            if(rs.next()) {
                lastProductNumber = rs.getInt("ProductID");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return lastProductNumber;
    }



}



