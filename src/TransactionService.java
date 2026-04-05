// ------------------------------------
// ----Transaction operations class----
// ------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionService {

    public static boolean recordTransaction(int accountId, double amount, String type){
        try(Connection con = DBConnection.getConnection()){

            // CHECK BALANCE BEFORE DEBIT (IMPORTANT FIX)
            if(type.equalsIgnoreCase("DEBIT")){
                String checkQuery = "SELECT balance FROM account WHERE account_id = ?";
                PreparedStatement checkPs = con.prepareStatement(checkQuery);
                checkPs.setInt(1, accountId);
                ResultSet rs = checkPs.executeQuery();

                if(rs.next()){
                    double currentBalance = rs.getDouble("balance");

                    if(currentBalance < amount){
                        System.out.println("Insufficient Balance");
                        return false;
                    }
                } else {
                    System.out.println("Account not found");
                    return false;
                }
            }

            //  Insert transaction record
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO transaction_table(account_id, amount, type_of_transaction) VALUES (?, ?, ?)"
            );
            ps.setInt(1, accountId);
            ps.setDouble(2, amount);
            ps.setString(3, type);

            int rows = ps.executeUpdate();
            if(rows == 0){
                return false;
            }

            // Update account balance
            String updateQuery;
            if(type.equalsIgnoreCase("DEBIT")){
                updateQuery = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
            } else {
                updateQuery = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
            }

            PreparedStatement ps2 = con.prepareStatement(updateQuery);
            ps2.setDouble(1, amount);
            ps2.setInt(2, accountId);
            ps2.executeUpdate();

            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Deposit
    public static boolean depositAddFunds(int accountId, double amount){
        return recordTransaction(accountId, amount, "CREDIT");
    }

    // Withdraw
    public static boolean withdrawDeductFunds(int accountId, double amount){
        return recordTransaction(accountId, amount, "DEBIT");
    }

    // Show Balance
    public static void showBalance(int accountId){
        try(Connection con = DBConnection.getConnection()){
            String query = "SELECT balance FROM account WHERE account_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Balance: ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found.");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    // Transfer Funds (SAFE VERSION)
    public static boolean transferFunds(int fromAccountId, int toAccountId, double amount){
        try(Connection con = DBConnection.getConnection()){
            con.setAutoCommit(false); // start transaction

            // Check balance first
            String check = "SELECT balance FROM account WHERE account_id=?";
            PreparedStatement psCheck = con.prepareStatement(check);
            psCheck.setInt(1, fromAccountId);
            ResultSet rs = psCheck.executeQuery();

            if(!rs.next() || rs.getDouble("balance") < amount){
                System.out.println("Insufficient Balance");
                return false;
            }

            // Debit
            PreparedStatement debit = con.prepareStatement(
                    "UPDATE account SET balance = balance - ? WHERE account_id=?"
            );
            debit.setDouble(1, amount);
            debit.setInt(2, fromAccountId);
            debit.executeUpdate();

            // Credit
            PreparedStatement credit = con.prepareStatement(
                    "UPDATE account SET balance = balance + ? WHERE account_id=?"
            );
            credit.setDouble(1, amount);
            credit.setInt(2, toAccountId);
            credit.executeUpdate();

            // Record transactions
            recordTransaction(fromAccountId, amount, "DEBIT");
            recordTransaction(toAccountId, amount, "CREDIT");

            con.commit(); // success
            return true;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Show Transactions
    public static void showTransactions(int customerID){
        String sql = "SELECT * FROM transaction_table ORDER BY transaction_id";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            System.out.println("\nID | AccID | Amount | Type | Risk");

            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                int accId = rs.getInt("account_id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("type_of_transaction");
                String risk = rs.getString("level_of_risk");

                System.out.printf("%d | %d | ₹%.2f | %s | %s%n",
                        id, accId, amount, type, risk);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    // Modify Password (NOW RETURNS BOOLEAN)
    public static boolean modifyPassword(int customerId, String newPassword){
        String sql = "UPDATE customer SET password = ? WHERE customer_id = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, newPassword);
            ps.setInt(2, customerId);

            int rows = ps.executeUpdate();
            return rows > 0;

        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Credit Card Request
    public static void requestCreditCard(int accountId){
        System.out.println("Congratulations! Credit Card Approved with ₹100000 Limit");
    }

    // Spend Credit
    public static void spendCreditCard(int accountId, double amount){
        try(Connection con = DBConnection.getConnection()){
            String sql = "UPDATE account SET used_credit = used_credit + ? WHERE account_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();

            System.out.println("Credit Used: ₹" + amount);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Repay Credit
    public static void creditRepayment(int accountId, double amount){
        try(Connection con = DBConnection.getConnection()){
            String sql = "UPDATE account SET used_credit = used_credit - ? WHERE account_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();

            System.out.println("₹" + amount + " successfully repaid on credit card.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}