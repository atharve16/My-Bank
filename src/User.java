import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection con;
    private Scanner scan;

    public User(Connection con, Scanner scan){
        this.con = con;
        this.scan = scan;
    }

    public void register(){
        scan.nextLine();
        System.out.print("Full Name: ");
        String name = scan.nextLine();
        System.out.print("Email: ");
        String email = scan.nextLine();
        System.out.print("Password: ");
        String password = scan.nextLine();

        if (user_exist(email)){
            System.out.println("USER ALREADY EXIST");
            return;
        }
        String query = "INSERT INTO user(name, email, password) VALUES(?, ?, ?)";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,name);
            stmt.setString(2,email);
            stmt.setString(3,password);
            int result = stmt.executeUpdate();
            if (result > 0) {
                System.out.println("Registration Successfull!");
            } else {
                System.out.println("Registration Failed!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        scan.nextLine();
        System.out.print("Email: ");
        String email = scan.nextLine();
        System.out.print("Password: ");
        String password = scan.nextLine();

        String query = "SELECT email FROM user WHERE email = ? AND password = ?";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,email);
            stmt.setString(2,password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return email;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email){
        String query = "SELECT email FROM user WHERE email = ?";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
