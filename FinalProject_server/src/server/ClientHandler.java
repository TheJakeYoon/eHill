package server;

import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    protected Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    public Auction auction;
    private ItemDatabase itemDatabase;

    private JSONObject json;
    private int remainingAttempt = 10;
    private String userName;
    private boolean logInSuccessful = false;
    public boolean removeMe = false;


    public ClientHandler(Socket socket){
        this.socket = socket;
        auction = new Auction();

        try{
            objectInputStream = new ObjectInputStream(
                    socket.getInputStream()
            );
            objectOutputStream = new ObjectOutputStream(
                    socket.getOutputStream()
            );
        }catch(IOException i){
        }
    }

    public JSONObject getJSONInput(){
        JSONObject json = null;
        String s = null;
        try{
            s = (String)objectInputStream.readObject();
            json = new JSONObject(s);
        }catch (ClassNotFoundException c){
            //c.printStackTrace();
            System.out.println("End Client");
            removeMe = true;
            interrupt();
        }catch( IOException i){
            //i.printStackTrace();
            System.out.println("End Client");
            removeMe = true;
            interrupt();
        }
        return json;
    }

    public void sendJSONOutput(JSONObject json){
        try{
            objectOutputStream.writeObject(json.toString());
        }catch (IOException i){
            System.out.println("End Client");
            removeMe = true;
            interrupt();
        }
    }

    public void run(){
        System.out.println(socket.toString() + " Running");

        // Start

        // Log In
        json = getJSONInput();
        while(!logInSuccessful && json != null){
            if(json.has("userName")){
                LogIn(json);
            }
            else if(json.has("button")){
                if(json.get("button").equals("newAccount")){
                    while(newAccount()){}
                }
                else if(json.get("button").equals("forgotPassword")){
                    forgotPassword();
                }
            }
            if(!logInSuccessful){
                json = getJSONInput();
            }
        }

        // Main Page
        //System.out.println("Testing");
        initAuction();

        //System.out.println("Testing");

        while(isAlive()){

            System.out.println("Running auction");
            if(json.has("button")){
                if(json.get("button").equals("MyAccount")){

                }
            }
            synchronized (this){
                if(json.has("bid") || json.has("sold")){
                    auction.bid(json);
                    int itemid = auction.getItemID(json);
                    double finalprice = auction.getPrice(json);
                    itemDatabase.updateFinalPrice(itemid, finalprice);
                }
            }
            json = getJSONInput();
            if(json == null){
                interrupt();
                System.out.println("thread ended");
            }
        }
    }

    public void LogIn(JSONObject json){
        System.out.println(json.toString());
        LogIn logIn = new LogIn((String) json.get("userName"), (String) json.get("password"));
        if (logIn.exists() && logIn.passwordMatched()) {
            userName = (String)json.get("userName");
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("message", "success");
            sendJSONOutput(jsonObject1);
            logInSuccessful = true;
        } else {
            if(remainingAttempt == 1){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("button", "QUIT");
                sendJSONOutput(jsonObject1);
                interrupt();
            }
            else{
                remainingAttempt--;
                JSONObject jsonObject1 = new JSONObject();
                String message = "Try Again! " + String.valueOf(remainingAttempt) + " attempts remaining!";
                jsonObject1.put("message", message);
                sendJSONOutput(jsonObject1);
            }
        }
    }

    public boolean newAccount(){
        JSONObject jsonObject = new JSONObject();
        json = getJSONInput();
        LogIn logIn = new LogIn((String)json.get("userName"), (String)json.get("password"), (String)json.get("hint"));
        if(logIn.exists()){
            jsonObject.put("message", "exists");
            sendJSONOutput(jsonObject);
            return true;
        }
        else{
            userName = (String)json.get("userName");
            logIn.addUser();
            logIn.display();
            jsonObject.put("message", "success");
            sendJSONOutput(jsonObject);
            logInSuccessful = true;
            return false;
        }
    }

    public void forgotPassword(){
        JSONObject jsonObject = new JSONObject();

        json = getJSONInput();

        LogIn logIn = new LogIn((String)json.get("userName"), "");
        if(logIn.exists()){
            System.out.println(logIn.getHint());
            jsonObject.put("hint", logIn.getHint());
            sendJSONOutput(jsonObject);

            json = getJSONInput();
            LogIn newLogIn = new LogIn((String)json.get("userName"), (String)json.get("password"));
            if(!newLogIn.passwordMatched()){
                JSONObject temp = new JSONObject();
                temp.put("button", "QUIT");
                sendJSONOutput(temp);
                interrupt();
            }
            else{
                userName = (String)json.get("userName");
                JSONObject temp = new JSONObject();
                temp.put("message", "success");
                sendJSONOutput(temp);
                logInSuccessful = true;
            }
        }
        else{
            jsonObject.put("button", "QUIT");
            sendJSONOutput(jsonObject);
            interrupt();
        }
    }

    public void initAuction(){
        auction.addUserName(userName);
        itemDatabase = new ItemDatabase();
        //itemDatabase.displayAll();

        ArrayList<JSONObject> data = new ArrayList<>();
        data = itemDatabase.createJSONdata();
        for(JSONObject temp: data){
            sendJSONOutput(temp);
        }
    }

}
