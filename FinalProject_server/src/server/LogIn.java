package server;

import java.sql.*;

public class LogIn {

    private String url = "jdbc:mysql://localhost:3306/finalProject" +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String userName;
    private String password;
    private String hint;
    private Connection myConn;
    private Statement myStmt;

    public LogIn(String userName, String password){
        this.userName = userName;
        this.password = password;
        try{
            myConn = DriverManager.getConnection(url, "root", "TSi5PT6A6J76mac!");
            myStmt = myConn.createStatement();
        }catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public LogIn(String userName, String password, String hint){
        this.userName = userName;
        this.password = password;
        this.hint = hint;
        try{
            myConn = DriverManager.getConnection(url, "root", "TSi5PT6A6J76mac!");
            myStmt = myConn.createStatement();
        }catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public boolean exists(){
        try{
            ResultSet myResult = myStmt.executeQuery("select * from login");
            while(myResult.next()){
                if(myResult.getString("username").equals(userName)){
                    return true;
                }
            }
        }catch (SQLException s){

        }
        return false;
    }

    public void addUser(){
        try{
            myStmt.executeUpdate("INSERT INTO login (username, password, hint) " + "VALUES ('" + userName + "', '" + password + "', '" + hint + "')");
        }catch (SQLException s){
            s.printStackTrace();
        }
    }

    public boolean passwordMatched(){
        try{
            ResultSet myResult = myStmt.executeQuery("select * from login");
            while (myResult.next()){
                if(myResult.getString("password").equals(password)){
                    return true;
                }
            }
        }catch(SQLException s){

        }
        return false;
    }

    public String getHint(){
        String hint = "tryingtofind";
        try{
            ResultSet myResult = myStmt.executeQuery("select hint from login where username = '" + userName + "'");
            while(myResult.next()){
                hint = myResult.getString("hint");
            }
        }catch(SQLException s){
        }
        return hint;
    }

    public void display(){
        try{
            ResultSet myResult = myStmt.executeQuery("select * from login");
            while(myResult.next()){
                System.out.print("Username: " + myResult.getString("username"));
                System.out.print(" Password: " + myResult.getString("password"));
                System.out.println(" Hint: " + myResult.getString("hint"));
            }
        }catch (SQLException s){
        }
    }

    public void clear(){
        try{
            myStmt.executeUpdate("TRUNCATE TABLE login");
        }catch (SQLException s){
        }
    }
}
