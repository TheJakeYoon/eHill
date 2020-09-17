package server;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class ItemDatabase {

    private String url = "jdbc:mysql://localhost:3306/finalProject" +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private int itemID;
    private int time;
    private double startPrice;
    private double buyPrice;
    private double finalPrice;

    private Connection myConn;
    private Statement myStmt;

    public ItemDatabase() {
        try {
            myConn = DriverManager.getConnection(url, "root", "TSi5PT6A6J76mac!");
            myStmt = myConn.createStatement();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public ArrayList<JSONObject> createJSONdata(){
        ArrayList<JSONObject> data = new ArrayList<>();
        JSONObject json = new JSONObject();
        try {
            ResultSet myResult = myStmt.executeQuery("select * from items");
            while(myResult.next()){
                json.put("itemid", myResult.getInt("itemid"));
                json.put("time", myResult.getInt("time"));
                json.put("startprice", myResult.getInt("startprice"));
                json.put("buyprice", myResult.getInt("buyprice"));
                json.put("finalprice", myResult.getInt("finalprice"));
                data.add(json);
                json = new JSONObject();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }

    public void updateFinalPrice(int itemid, double finalPrice) {
        try{
            myStmt.executeUpdate("update items set finalprice=" + finalPrice
                    + " where itemid=" + itemid);
        }catch (SQLException s){s.printStackTrace();}

        displayAll();
    }

    public void updateTime(int itemid, int time){
        try{
            myStmt.executeUpdate("update items set time =" + time
            + " where itemid=" + itemid);
        }catch (SQLException s){
        }
    }

    public int getTime(int itemid){
        int time = 0;
        try{
            ResultSet resultSet = myStmt.executeQuery("select time from items where itemid=" + itemid);
            while(resultSet.next()){
                time = resultSet.getInt("time");
            }
        }catch (SQLException s){

        }
        return time;
    }
    public void displayAll(){
        try {
            ResultSet myResult = myStmt.executeQuery("select * from items");
            while (myResult.next()) {
                System.out.print("ItemID: " + myResult.getInt("itemid"));
                System.out.print(" Time: " + myResult.getInt("time"));
                System.out.print(" Start Price: " + myResult.getDouble("startprice"));
                System.out.print(" Buy Now Price: " + myResult.getDouble("buyprice"));
                System.out.println(" Final Price: " + myResult.getDouble("finalprice"));
            }
        } catch (SQLException s) {
        }
    }

    public void createDemoTable(){
        try{
            myStmt.executeUpdate("insert into items (itemid, time, startprice, buyprice, finalprice)" +
                    " values (1, 120, 9000, 45000, 9000)");
            myStmt.executeUpdate("insert into items (itemid, time, startprice, buyprice, finalprice)" +
                    " values (2, 30, 50, 150, 50)");
            myStmt.executeUpdate("insert into items (itemid, time, startprice, buyprice, finalprice)" +
                    " values (3, 300, 100000, 500000, 100000)");
            myStmt.executeUpdate("insert into items (itemid, time, startprice, buyprice, finalprice)" +
                    " values (4, 15, 300, 900, 300)");
            myStmt.executeUpdate("insert into items (itemid, time, startprice, buyprice, finalprice)" +
                    " values (5, 5, 0, 10, 0)");
            myStmt.executeUpdate("insert into items (itemid, time, startprice, buyprice, finalprice)" +
                    " values (6, 60, 100, 300, 100)");
        }catch(SQLException s){
            s.printStackTrace();
        }
    }

    public void clear(){
        try{
            myStmt.executeUpdate("TRUNCATE TABLE items");
        }catch (SQLException s){
        }
    }
}