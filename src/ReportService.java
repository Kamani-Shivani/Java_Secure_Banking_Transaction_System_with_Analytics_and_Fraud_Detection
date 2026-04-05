// -----------------------------------------------------------------------
// ------Generates a summary of total transactions for each customer------
// -----------------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

public class ReportService {

    public static void generateCustomerTransactionReport(){
        String sql = """
                select c.customer_id, c.name, sum(t.amount) as total_amount 
                from customer c 
                join account a on c.customer_id = a.customer_id
                join transaction_table t on a.account_id = t.account_id
                group by c.customer_id, c.name
                """;

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            System.out.println("\nID | Name | Total Amount");

            while(rs.next()){
                System.out.printf("%d | %s | ₹%.2f%n",
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getDouble("total_amount"));
            }

        } catch(Exception e){
            System.out.println("Report generation failed");
        }
    }
}