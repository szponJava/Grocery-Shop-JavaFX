package au.com.einsporn.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;

public class SalesPageData {

   private ObservableList<Sales> salesDataList;
   private ObservableList<String> selectFilterOptionsList;


    public SalesPageData() {

        salesDataList = FXCollections.observableArrayList();

        selectFilterOptionsList =FXCollections.observableArrayList();
        selectFilterOptionsList.add("Customer Name");
        selectFilterOptionsList.add("Product Name");
        selectFilterOptionsList.add("Category");

    }


    public ObservableList<String> getSelectFilterOptionsList(){

        return selectFilterOptionsList;

    }


    public ObservableList<Sales> getSalesDataList(){

        return salesDataList;

    }

    private void addSaleToTableList(Sales sales){

        salesDataList.addAll(sales);

    }


    public void retriveSalesFromDB() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String SQL = "SELECT * FROM Sales";

        try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement();ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                addSaleToTableList(new Sales(rs.getInt("SaleNumber"),simpleDateFormat.format(rs.getDate("Date")), rs.getString("Time"), rs.getString("Customer"),
                        rs.getString("Product"), rs.getString("Code"), rs.getString("Size"),
                        rs.getString("Units"),rs.getString("Category"),rs.getString("Quantity"),rs.getString("UnitPrice"),rs.getString("SalePrice")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addSaleToDB(Sales sale) throws Exception {


        Integer saleNumber = sale.getSaleNumber();
        String date = sale.getDate();
        String time = sale.getTime();
        String customer = sale.getCustomer();
        String product = sale.getProduct();
        String code = sale.getCode();
        String size = sale.getSize();
        String units = sale.getUnits();
        String category = sale.getCategory();
        String quantity = sale.getQuantity();
        String unitPrice = sale.getItemPrice();
        String salePrice = sale.getSalePrice();


        String SQL ="INSERT INTO Sales(SaleNumber,Date,Time,Customer,Product,Code,Size,Units,Category,Quantity,UnitPrice,SalePrice)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try(Connection con = DBConnection.getconnection(); PreparedStatement ps = con.prepareStatement(SQL)) {

                ps.setInt(1, saleNumber);
                ps.setString(2, date);
                ps.setString(3, time);
                ps.setString(4, customer);
                ps.setString(5, product);
                ps.setString(6, code);
                ps.setString(7, size);
                ps.setString(8, units);
                ps.setString(9, category);
                ps.setString(10, quantity);
                ps.setString(11, unitPrice);
                ps.setString(12, salePrice);
                ps.execute();

        }
    }


    // method retrives filtered data from DB
    public void retriveSearchSaleDataFromDB(String text1,String text2, String selection) throws SQLException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


        ResultSet rs = null;
        PreparedStatement ps=null;

        String searchWord = "%" + text1 + "%" ;


        try (Connection con = DBConnection.getconnection()) {

            // selction of the SQL statment depending on RadioButton clicked by the user
            if (selection.equals("SaleNumber")) {

                String SQL = "SELECT * FROM Sales where Sales.SaleNumber>=? AND Sales.SaleNumber<=?"; // filters rows by Sale Number
                ps = con.prepareStatement(SQL);
                ps.setString(1,text1);
                ps.setString(2,text2);
                rs = ps.executeQuery();


            } else if (selection.equals("dateSelected")) {

                String SQL = "SELECT * FROM Sales where Sales.Date between ? AND ?";// filters rows by selected date
                ps = con.prepareStatement(SQL);
                ps.setString(1,text1);
                ps.setString(2,text2);
                rs = ps.executeQuery();


            } else if (selection.equals("customerName")) {

                String SQL = "SELECT * FROM Sales where Sales.Customer like ?"; // filters rows by typed Customer Name
                ps = con.prepareStatement(SQL);
                ps.setString(1,searchWord);
                rs = ps.executeQuery();


            } else if (selection.equals("productName")) {

                String SQL = "SELECT * FROM Sales where Sales.Product like ? ";  // filters rows by typed Product Name
                ps = con.prepareStatement(SQL);
                ps.setString(1,searchWord);
                rs = ps.executeQuery();

            } else if (selection.equals("category")) {

                String SQL = "SELECT * FROM Sales where Sales.Category like ?";  // filters rows by typed Category
                ps = con.prepareStatement(SQL);
                ps.setString(1,searchWord);
                rs = ps.executeQuery();
            }

            while (rs.next()) {
                addSaleToTableList(new Sales(rs.getInt("SaleNumber"), simpleDateFormat.format(rs.getDate("Date")), rs.getString("Time"), rs.getString("Customer"),
                        rs.getString("Product"), rs.getString("Code"), rs.getString("Size"),
                        rs.getString("Units"), rs.getString("Category"), rs.getString("Quantity"), rs.getString("UnitPrice"), rs.getString("SalePrice")));

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

    public Integer retriveLastSaleNumberFromDB () throws SQLException {

            Integer lastSaleID = null;

            try (Connection con = DBConnection.getconnection(); Statement stmt = con.createStatement()) {
                String SQL = "SELECT MAX(Sales.SaleNumber) as SaleID FROM Sales";
                ResultSet rs = stmt.executeQuery(SQL);

                if (rs.next()) {
                    lastSaleID = rs.getInt("SaleID");
                }


            } catch (SQLException e) {
                e.printStackTrace();

            }

            return lastSaleID;
        }
    }

