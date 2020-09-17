package server;

import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

// Server has Main function and combines all classes together

public class Server extends Thread implements Observer {

    public ArrayList<ClientHandler> clientHandlers;

    public Server(){
        clientHandlers = new ArrayList<>();
    }

    public static void main(String[] args) {

        ItemDatabase db = new ItemDatabase();
        db.clear();
        db.createDemoTable();
        db.displayAll();
        //db.updateFinalPrice(1,9999);
        //db.displayAll();

        Socket socket = null;
        ServerSocket serverSocket = null;
        int port = 5000;

        Server server = new Server();

        new Thread(){
            public void run(){
                try{
                    for(int i = 0; i < 600; i++){
                        //System.out.println("updating time");
                        sleep(1000);
                        for(int j = 0; j < 6; j++){
                            int time = db.getTime(j);
                            if(time > 0){
                                db.updateTime(j, db.getTime(j)-1);
                            }
                        }
                    }

                }catch (InterruptedException i){
                }
            }
        }.start();

        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException i){
        }
        while(true){
            try{
                socket = serverSocket.accept();
            }catch(IOException i){
            }
            server.init(socket);

            System.out.println("Number of Clients: " + server.clientHandlers.size());
        }
    }

    public void init(Socket socket){
        clientHandlers.add(new ClientHandler(socket));
        clientHandlers.get(clientHandlers.size()-1).auction.addObserver(this);
        clientHandlers.get(clientHandlers.size() - 1).start();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Updating all clients");
        for(ClientHandler client: clientHandlers){
            if(client.removeMe == true){
                System.out.println("Client removed");
                clientHandlers.remove(client);
            }
            System.out.println("Sending: " + ((JSONObject)arg).toString());
            client.sendJSONOutput((JSONObject)arg);
        }
    }
}
