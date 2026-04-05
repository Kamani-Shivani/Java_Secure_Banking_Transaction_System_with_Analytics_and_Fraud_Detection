// ------------------------------------
// ------------Main class--------------
// ------------------------------------

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        DBConnection.getConnection();

        Scanner sc = new Scanner(System.in);
        viewCustomerMenu(sc);
    }

    // ================= MAIN MENU =================
    private static void viewCustomerMenu(Scanner sc) {
        while (true) {
            System.out.println("\n====== Customer Menu ======");
            System.out.println("1. Account Operations Dashboard");
            System.out.println("2. Transaction Operations Dashboard");
            System.out.println("3. Analytics & Reports Dashboard");
            System.out.println("4. Card & Security Dashboard");
            System.out.println("5. Sign Out Safely");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewAccountMenu(sc);
                    break;
                case 2:
                    viewTransactionMenu(sc);
                    break;
                case 3:
                    viewReportMenu(sc);
                    break;
                case 4:
                    viewCardMenu(sc);
                    break;
                case 5:
                    System.out.println("Successfully Logged out!");
                    return;
                default:
                    System.out.println("Please select valid choice!");
            }
        }
    }

    // ================= ACCOUNT MENU =================
    private static void viewAccountMenu(Scanner sc) {
        while (true) {
            System.out.println("----- Account Operations Dashboard -----");
            System.out.println("1. View Accounts");
            System.out.println("2. View Balance");
            System.out.println("3. Return to Main Menu");

            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    viewAccounts();
                    break;
                case 2:
                    System.out.println("Enter Account ID to view balance:");
                    TransactionService.showBalance(sc.nextInt());
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= TRANSACTION MENU =================
    private static void viewTransactionMenu(Scanner sc) {
        while (true) {
            System.out.println("----- Transaction Operations Dashboard -----");
            System.out.println("1. Display Transactions");
            System.out.println("2. Add New Transaction");
            System.out.println("3. Deposit Funds");
            System.out.println("4. Withdraw Funds");
            System.out.println("5. Transfer Money Between Accounts");
            System.out.println("6. Return to Main Menu");

            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    TransactionService.showTransactions(0);
                    break;

                case 2:
                    recordTransaction(sc);
                    break;

                case 3:
                    System.out.println("Enter Account ID: ");
                    int acc1 = sc.nextInt();

                    System.out.println("Enter Amount: ");
                    double amt1 = sc.nextDouble();

                    boolean dep = TransactionService.depositAddFunds(acc1, amt1);

                    if(dep){
                        System.out.println("Deposit Successful");
                    } else {
                        System.out.println("Deposit Failed");
                    }
                    break;

                case 4:
                    System.out.println("Enter Account ID: ");
                    int acc2 = sc.nextInt();

                    System.out.println("Enter Amount: ");
                    double amt2 = sc.nextDouble();

                    boolean wit = TransactionService.withdrawDeductFunds(acc2, amt2);

                    if(wit){
                        System.out.println("Withdraw Successful");
                    } else {
                        System.out.println("Withdraw Failed");
                    }
                    break;

                case 5:
                    transferFunds(sc);
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= REPORT MENU =================
    private static void viewReportMenu(Scanner sc) {
        while (true) {
            System.out.println("\n----- Analytics & Reports Dashboard -----");
            System.out.println("1. Customer Transaction Overview");
            System.out.println("2. Return to Main Menu");

            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    ReportService.generateCustomerTransactionReport();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= CARD MENU =================
    private static void viewCardMenu(Scanner sc) {
        while (true) {
            System.out.println("\n----- Card & Security Dashboard -----");
            System.out.println("1. Change Account Password");
            System.out.println("2. Apply for Credit Card");
            System.out.println("3. Use Credit Card");
            System.out.println("4. Repay Credit");
            System.out.println("5. Return to Main Menu");

            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    try {
                        System.out.println("Enter Customer ID: ");
                        int id = sc.nextInt();

                        System.out.println("Enter New Password: ");
                        String pass = sc.next();

                        boolean upd = TransactionService.modifyPassword(id, pass);

                        if(upd){
                            System.out.println("Password Updated");
                        } else {
                            System.out.println("Update Failed");
                        }

                    } catch(Exception e){
                        System.out.println("Invalid Input (Customer ID must be number)");
                        sc.nextLine(); // clear wrong input
                    }
                    break;

                case 2:
                    System.out.println("Enter Account ID: ");
                    TransactionService.requestCreditCard(sc.nextInt());
                    break;

                case 3:
                    System.out.println("Enter Account ID: ");
                    int a3 = sc.nextInt();

                    System.out.println("Enter Amount: ");
                    TransactionService.spendCreditCard(a3, sc.nextDouble());
                    break;

                case 4:
                    System.out.println("Enter Account ID: ");
                    int a4 = sc.nextInt();

                    System.out.println("Enter Amount: ");
                    TransactionService.creditRepayment(a4, sc.nextDouble());
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= HELPER METHODS =================

    private static void viewAccounts() {
        try(Connection con = DBConnection.getConnection()){
            String query = "Select * from account order by account_id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nAccount ID | Customer ID | Type | Balance");
            System.out.println("-----------------------------------");

            while(rs.next()){
                System.out.println(
                        rs.getInt("account_id") + " | " +
                                rs.getInt("customer_id") + " | " +
                                rs.getString("type_of_account") + " | " +
                                rs.getDouble("balance")
                );
            }
        } catch (Exception e){
            System.out.println("Error fetching accounts");
        }
    }

    private static void recordTransaction(Scanner sc){
        System.out.println("Enter Account ID: ");
        int acc = sc.nextInt();

        System.out.println("Enter Amount: ");
        double amt = sc.nextDouble();

        System.out.println("Enter Type (DEBIT/CREDIT): ");
        String type = sc.next().toUpperCase();

        boolean success = TransactionService.recordTransaction(acc, amt, type);

        if(success){
            System.out.println("Transaction Successful");
        } else {
            System.out.println("Transaction Failed");
        }
    }

    private static void transferFunds(Scanner sc){
        System.out.print("Enter your Source Account ID: ");
        int from = sc.nextInt();

        System.out.print("Enter the Destination Account ID: ");
        int to = sc.nextInt();

        System.out.print("Enter the amount to transfer: ");
        double amt = sc.nextDouble();

        boolean success = TransactionService.transferFunds(from, to, amt);

        if(success){
            System.out.println("Money transferred successfully from Account " + from + " to Account " + to + ".");
        } else {
            System.out.println("Transfer Failed");
        }
    }
}