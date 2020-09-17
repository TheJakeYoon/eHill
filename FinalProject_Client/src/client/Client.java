package client;

import java.net.*;
import java.io.*;
import org.json.JSONObject;

public class Client{

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Client(String address, int port){

        try{
            System.out.println("Starting Client");
            socket = new Socket(address, port);

            objectOutputStream = new ObjectOutputStream(
                    socket.getOutputStream()
            );
            objectInputStream = new ObjectInputStream(
                    socket.getInputStream()
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject getJSONInput(){
        JSONObject json = null;
        String s;
        try{
            s = (String) objectInputStream.readObject();
            json = new JSONObject(s);
        }catch (ClassNotFoundException c){
        }catch( IOException i){
        }
        return json;
    }

    public void sendJSONOutput(JSONObject json){
        try{
            objectOutputStream.writeObject(json.toString());
        }catch (IOException i){
        }
    }

    public void sendJSONOutput(String str1, String str2){
        try{
            JSONObject json = new JSONObject();
            json.put(str1, str2);
            objectOutputStream.writeObject(json.toString());
        }catch (IOException i){
        }
    }
}
