package server;

import org.json.JSONObject;

import java.util.Observable;

public class Auction extends Observable {

    private String userName;
    private ItemDatabase db;

    public Auction (){
        db = new ItemDatabase();
    }
    public void addUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void bid(JSONObject json){
        System.out.println("bid accepted");
        //updateDatabase(json);
        setChanged();
        notifyObservers(json);
    }

    public int getItemID(JSONObject jsonObject){
        int itemid = 0;
        if(jsonObject.has("bid")){
            itemid = (Integer)jsonObject.get("bid");
        }
        else{
            itemid = (Integer)jsonObject.get("sold");
        }
        return itemid;
    }

    public double getPrice(JSONObject jsonObject){
        double price = Double.valueOf((String)jsonObject.get("price"));
        return price;
    }

    public void updateDatabase(JSONObject jsonObject){
        int itemid = getItemID(jsonObject);
        double finalprice = getPrice(jsonObject);
        db.updateFinalPrice(itemid, finalprice);
    }
}
