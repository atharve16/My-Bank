import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Account {

    private Connection con;
    private Scanner scan;

    public Account(Connection con, Scanner scan){
        this.con = con;
        this.scan = scan;
    }

    public long open_account(String email){
        if(!account_exist(email)) {
            String open_account_query = "INSERT INTO accounts(accNum, name, email, balance, pin) VALUES(?, ?, ?, ?, ?)";
            scan.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scan.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scan.nextDouble();
            scan.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scan.nextLine();
            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = con.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");

    }

    public long getAccountNumber(String email){
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT accNum from accounts WHERE email = ?");
            stmt.setString(1,email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("accNum");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber() {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT accNum from accounts ORDER BY accNum DESC LIMIT 1");
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("accNum");
                return last_account_number+1;
            } else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email){
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT email from accounts WHERE email = ?");
            stmt.setString(1,email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}