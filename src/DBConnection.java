// ----------------------------------
// ----Database Connection Class-----
// ----------------------------------

import java.sql.*;

public class DBConnection {

    public static Connection getConnection(){
        try{
            Class.forName("org.postgresql.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/Banking_system",
                    "your_postgres_username",   // replace with your PostgreSQL username
                    "your_postgres_password"    // replace with your PostgreSQL password
            );

            System.out.println("Database Connected Successfully");

            return con;

        } catch (Exception e){
            System.out.println("Database connection failed ");
            e.printStackTrace();
            return null;
        }
    }
}

