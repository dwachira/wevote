package wevote_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;

/**
 * Provides functionality for working with MySQL
 * such as Select / Update / Insert / Delete
 *
 * Examples of use:
 *
 * MySQL foo = new MySQL();
 * 
 * ArrayList<ArrayList> result = foo.Select("SELECT title, question, pool_id FROM question WHERE id = 1 LIMIT 1");
 * result.get(0).get(0);
 *
 * foo.Update("UPDATE `user` SET age = 21 WHERE id = 2;");
 * foo.Update("INSERT INTO `user` VALUES (14, '+79219899375', 21, 1);");
 *
 * @author NorthernDemon
 */
public class MySQL {

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
        }

        return conn;
    }

    /**
     * @param Query contains SQL-string for retrieving info from database
     * @return result of Query 2-dimentional ArrayList
     */
    public ArrayList<ArrayList> Select(String Query) {
        ArrayList<ArrayList> resultS = new ArrayList<ArrayList>();

        try {
            Connection conn = this.Connection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery(Query);
            ResultSetMetaData meta = result.getMetaData();

            while (result.next()) {
                ArrayList row = new ArrayList();
                for (int i = 1; i <= meta.getColumnCount(); i++)
                    row.add(result.getString(i));
                resultS.add(row);
            }

            select.close();
            result.close();
            conn.close();
        } catch(SQLException e) {
            System.err.println("Mysql Statement Error: " + Query);
        }

        return resultS;
    }

    /**
     * @param Query contains SQL-string for updating/inserting/deleting info into database
     * @return 0 in case of error, Not 0 in case of success
     */
    public int Update(String Query) {
        int resultS = 0;

        try {
            Connection conn = this.Connection();
            Statement update = conn.createStatement();

            resultS = update.executeUpdate(Query);

            update.close();
            conn.close();
        } catch(SQLException e) {
            System.err.println("Mysql Statement Error: " + Query);
        }

        return resultS;
    }

}