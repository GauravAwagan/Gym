import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static void main(String[] args) {
        // Database URL with database name 'gaurav'
        String url = "jdbc:mysql://localhost:3306/gaurav";
        String user = "root";       // Your DB username
        String password = "";       // Your DB password

        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database 'gaurav' successfully!");

            // Close the connection
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e);
        }
    }
}
