package e1262;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author nsp
 */
public class DatabaseHandler {

    //PASSWORD for l&thazira is VATMfFq4FWhOhdbc
    protected static String DB_NAME = "e1262";
//    protected static String DB_PASS = "";
//    protected static String DB_PASS = "flow350";
//    protected static String DB_USER = "root";
    protected static String DB_PASS = "abc";
    protected static String DB_USER = "gaju";
    protected static String DB_HOST = "localhost";
    
    

    Connection MakeConnection() {
        try {
            Connection conn;
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+":3306/" + DB_NAME + "?" + "user="+DB_USER+"&password="+DB_PASS);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {

        }
        return null;
    }

    public int execute(String query, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        if (st.executeUpdate(query) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int execute_sp(String sp, Connection conn) throws SQLException {
        CallableStatement st_sp = conn.prepareCall("{call " + sp + "}");
        if (st_sp.executeUpdate() > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    public ResultSet getData(String query, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery(query);
    }

    public ResultSet getData_sp(String sp, Connection conn) throws SQLException {
        CallableStatement st_sp = conn.prepareCall("{call " + sp + "}");
        st_sp.execute();
        return st_sp.getResultSet();
    }
}
