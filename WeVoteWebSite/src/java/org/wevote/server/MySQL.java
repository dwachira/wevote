package org.wevote.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import java.util.Vector;

import javax.servlet.http.HttpServlet;

/**
 * Supporting class for Chart & Menu implementation
 * Provides Connect & Select operations with database
 *
 * @author NorthernDemon
 */

public class MySQL extends HttpServlet {

    /**
     * @return conn creates connection to MySQL
     */
    public Connection Connection() {
        Connection conn     = null;

        String url          = "jdbc:mysql://localhost:3306/";
        String db           = "wevote";
        String user         = "root";
        String pass         = "";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url + db, user, pass);
        } catch (Exception e) {
            System.err.println("Mysql Connection Error:");
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * @param Query contains SQL-string for retrieving info from database
     * @return result of Query 2-dimentional Vector
     */
    public Vector<Vector> Select(String Query) {
        Vector<Vector> resultS = new Vector<Vector>();

        try {
            Connection conn = this.Connection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery(Query);
            ResultSetMetaData meta = result.getMetaData();

            while (result.next()) {
                Vector row = new Vector();
                for (int i = 1; i <= meta.getColumnCount(); i++)
                    row.add(result.getString(i));
                resultS.add(row);
            }

            select.close();
            result.close();
            conn.close();
        } catch(SQLException e) {
            System.err.println("Mysql Statement Error: " + Query);
            e.printStackTrace();
        }

        return resultS;
    }

}