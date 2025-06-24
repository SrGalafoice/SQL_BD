package utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {
    private final static String DB_CLASS = "net.sourceforge.jtds.jdbc.Driver";
    private final static String DB_URL = "jdbc:jtds:sqlserver://LAPTOP-GDQT9ETG;instance=SQLEXPRESS;databaseName=mercado";
    private static DBConnection dbConnection = null;
    private final static String DB_USER = "mercadodb10";
    private final static String DB_PASS = "123456";
    private DBConnection() throws ClassNotFoundException {
        Class.forName(DB_CLASS);
    }

    public static DBConnection getInstance() throws Exception {
        if (dbConnection == null){
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_CLASS);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
