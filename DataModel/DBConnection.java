package au.com.einsporn.DataModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {


    private static final String URL ="jdbc:sqlserver://localhost\\SQLEXPRESS:1001;databaseName=GroceryShop";
    private static final String USER_Name = "Carlotta";
    private static final String PASSWORD = "123456";

    public static Connection getconnection() throws SQLException {

        return DriverManager.getConnection(URL, USER_Name, PASSWORD);

    }

}
