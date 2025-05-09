import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import java.util.Scanner;

public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/mybank";
    private static final String username = "root";
    private static final String password = "Atharve@10";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner scan = new Scanner(System.in);
            User user = new User(con, scan);
            Account account = new Account(con, scan);
            Manager manager = new Manager(con, scan);

            String email;
            long account_number;

            while(true){
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scan.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!account.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(scan.nextInt() == 1) {
                                    account_number = account.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }

                            }
                            account_number = account.getAccountNumber(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Withdraw Money");
                                System.out.println("2. Deposit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scan.nextInt();
                                switch (choice2) {
                                    case 1:
                                        manager.withdraw_money(account_number);
                                        break;
                                    case 2:
                                        manager.deposit_money(account_number);
                                        break;
                                    case 3:
                                        manager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        manager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        }
                        else{
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}