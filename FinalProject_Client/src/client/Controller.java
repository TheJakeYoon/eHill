package client;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.IOException;

public class Controller {

    private static Client client = null;
    private String userName;

    // Start Menu
    @FXML
    void startButtonPressed_Start(ActionEvent event) throws IOException {
        client = new Client("127.0.0.1.", 5000);
        Parent root = FXMLLoader.load(getClass().getResource("eHill.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void quitButtonPressed_Start(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    // Log In Menu
    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label logInMessage;

    @FXML
    void logInButtonClicked(ActionEvent event) throws IOException {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();

        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("password", password);

        client.sendJSONOutput(json);
        json = client.getJSONInput();

        if (json.has("message")) {
            if (json.get("message").equals("success")) {
                this.userName = username;
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("eHill_Auction.fxml"));
                //loader.setController(this);
                Parent root = FXMLLoader.load(getClass().getResource("eHill_Auction.fxml"));
                window.setScene(new Scene(root));
            } else {
                logInMessage.setText((String) json.get("message"));
            }
        } else if (json.has("button")) {
            if (json.get("button").equals("QUIT")) {
                Platform.exit();
            }
        }
    }

    @FXML
    void newAccountButtonClicked(ActionEvent event) throws IOException {
        client.sendJSONOutput("button", "newAccount");
        Parent root = FXMLLoader.load(getClass().getResource("eHill_NewAccount.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void forgotPasswordButtonClicked(ActionEvent event) throws IOException {
        client.sendJSONOutput("button", "forgotPassword");
        Parent root = FXMLLoader.load(getClass().getResource("eHill_ForgotPassword.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void quitButtonPressed(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    // New Account
    @FXML
    private TextField newUserName;

    @FXML
    private PasswordField newPassword;

    @FXML
    private TextField hintNewAccount;

    @FXML
    private Label label;

    @FXML
    void createButtonClicked(ActionEvent event) throws IOException {
        String username = newUserName.getText();
        String password = newPassword.getText();
        String hint = hintNewAccount.getText();

        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("password", password);
        json.put("hint", hint);

        client.sendJSONOutput(json);
        json = client.getJSONInput();

        if (json.has("message")) {
            if (json.get("message").equals("exists")) {
                label.setText("Username Exists! Try again!");
            } else if (json.get("message").equals("success")) {
                Parent root = FXMLLoader.load(getClass().getResource("eHill_Auction.fxml"));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(new Scene(root));
            }
        }
    }

    @FXML
    void quitButton_newAccountPressed(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    // Forgot Password
    @FXML
    private PasswordField password_forgotPassword;

    @FXML
    private TextField userName_forgotPassword;

    @FXML
    private Label hint;

    @FXML
    private Button getHintButton;

    @FXML
    void quitButton_forgotPasswordPressed(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logInButton_forgotPasswordClicked(ActionEvent event) throws IOException {
        String username = userName_forgotPassword.getText();
        String password = password_forgotPassword.getText();

        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("password", password);
        client.sendJSONOutput(json);

        json = client.getJSONInput();
        if (json.has("message")) {
            if (json.get("message").equals("success")) {
                Parent root = FXMLLoader.load(getClass().getResource("eHill_Auction.fxml"));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(new Scene(root));
            }
        } else if (json.has("button")) {
            if (json.get("button").equals("QUIT")) {
                Platform.exit();
            }
        }
    }

    @FXML
    void getHintButtonClicked(ActionEvent event) {
        String username = userName_forgotPassword.getText();
        JSONObject json = new JSONObject();
        json.put("userName", username);
        client.sendJSONOutput(json);

        json = client.getJSONInput();
        if (json.has("hint")) {
            hint.setText((String) json.get("hint"));
        } else if (json.has("button")) {
            if (json.get("button").equals("QUIT")) {
                Platform.exit();
            }
        }
        getHintButton.setDisable(true);

    }

    // Main
    private double buyprice1, buyprice2, buyprice3, buyprice4, buyprice5, buyprice6;
    private double startprice1, startprice2, startprice3, startprice4, startprice5, startprice6;
    private void getData(){
        JSONObject json = client.getJSONInput();
        item1Time.setText(((Integer)json.get("time")).toString());
        if(Integer.valueOf(item1Time.getText()) < 1){
            bidButton1.setDisable(true);
        }
        startprice1 = Double.valueOf(Integer.toString((Integer)json.get("startprice")));
        buyprice1 = Double.valueOf(Integer.toString((Integer)json.get("buyprice")));
        item1Price.setText(json.get("finalprice").toString());

        json = client.getJSONInput();
        item2Time.setText(((Integer)json.get("time")).toString());
        if(Integer.valueOf(item2Time.getText()) < 1){
            bidButton2.setDisable(true);
        }
        item2Price.setText(json.get("finalprice").toString());
        startprice2 = Double.valueOf(Integer.toString((Integer)json.get("startprice")));
        buyprice2 = Double.valueOf(Integer.toString((Integer)json.get("buyprice")));
        json = client.getJSONInput();
        item3Time.setText(((Integer)json.get("time")).toString());
        if(Integer.valueOf(item3Time.getText()) < 1){
            bidButton3.setDisable(true);
        }
        item3Price.setText(json.get("finalprice").toString());
        startprice3 = Double.valueOf(Integer.toString((Integer)json.get("startprice")));
        buyprice3 = Double.valueOf(Integer.toString((Integer)json.get("buyprice")));
        json = client.getJSONInput();
        item4Time.setText(((Integer)json.get("time")).toString());
        if(Integer.valueOf(item4Time.getText()) < 1){
            bidButton4.setDisable(true);
        }
        item4Price.setText(json.get("finalprice").toString());
        startprice4 = Double.valueOf(Integer.toString((Integer)json.get("startprice")));
        buyprice4 = Double.valueOf(Integer.toString((Integer)json.get("buyprice")));
        json = client.getJSONInput();
        item5Time.setText(((Integer)json.get("time")).toString());
        if(Integer.valueOf(item5Time.getText()) < 1){
            bidButton5.setDisable(true);
        }
        item5Price.setText(json.get("finalprice").toString());
        startprice5 = Double.valueOf(Integer.toString((Integer)json.get("startprice")));
        buyprice5 = Double.valueOf(Integer.toString((Integer)json.get("buyprice")));
        json = client.getJSONInput();
        item6Time.setText(((Integer)json.get("time")).toString());
        if(Integer.valueOf(item6Time.getText()) < 1){
            bidButton6.setDisable(true);
        }
        item6Price.setText(json.get("finalprice").toString());
        startprice6 = Double.valueOf(Integer.toString((Integer)json.get("startprice")));
        buyprice6 = Double.valueOf(Integer.toString((Integer)json.get("buyprice")));
    }
    private void runAuction() {
        System.out.println("Running update Task");

        Task task = new Task<Void>(){
            @Override
            public Void call(){
                while (true) {
                    JSONObject json = client.getJSONInput();
                    System.out.println("Received json");
                    System.out.println(json.toString());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(json.has("bid")) {
                                String newPrice = (String) json.get("price");
                                int itemid = (Integer) json.get("bid");
                                if (itemid == 1) {
                                    if(Double.valueOf(newPrice) > Double.valueOf(item1Price.getText())){
                                        item1Price.setText(newPrice);
                                    }
                                }
                                if(itemid == 2){
                                    if(Double.valueOf(newPrice) > Double.valueOf(item2Price.getText())){
                                        item2Price.setText(newPrice);
                                    }
                                }
                                if(itemid == 3){
                                    if(Double.valueOf(newPrice) > Double.valueOf(item3Price.getText())){
                                        item3Price.setText(newPrice);
                                    }
                                }
                                if(itemid == 4){
                                    if(Double.valueOf(newPrice) > Double.valueOf(item4Price.getText())){
                                        item4Price.setText(newPrice);
                                    }
                                }
                                if(itemid == 5){
                                    if(Double.valueOf(newPrice) > Double.valueOf(item5Price.getText())){
                                        item5Price.setText(newPrice);
                                    }
                                }
                                if(itemid == 6){
                                    if(Double.valueOf(newPrice) > Double.valueOf(item6Price.getText())){
                                        item6Price.setText(newPrice);
                                    }
                                }
                            }
                            else if(json.has("sold")){
                                String soldPrice = (String)json.get("price");
                                int item = (Integer)json.get("sold");
                                if(item == 1){
                                    item1Price.setText(soldPrice);
                                    bidButton1.setDisable(true);
                                }
                                if(item == 2){
                                    item2Price.setText(soldPrice);
                                    bidButton2.setDisable(true);
                                }
                                if(item == 3){
                                    item3Price.setText(soldPrice);
                                    bidButton3.setDisable(true);
                                }
                                if(item == 4){
                                    item4Price.setText(soldPrice);
                                    bidButton4.setDisable(true);
                                }
                                if(item == 5){
                                    item5Price.setText(soldPrice);
                                    bidButton5.setDisable(true);
                                }
                                if(item == 6){
                                    item6Price.setText(soldPrice);
                                    bidButton6.setDisable(true);
                                }
                            }
                        }
                    });
                }
            }
        };
        new Thread(task).start();
    }

    private void runTimer(){
        new Thread(){
            public void run(){
                for(int i = 0; i < 600; i++){
                    try{
                        sleep(1000);
                        int time1 = Integer.valueOf(item1Time.getText());
                        if(time1 > 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    item1Time.setText((String.valueOf(time1-1)));
                                }
                            });

                        }
                        else if(time1 == 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    if(Integer.valueOf(item1Time.getText()) < 1){
                                        bidButton1.setDisable(true);
                                    }
                                }
                            });
                        }
                        int time2 = Integer.valueOf(item2Time.getText());
                        if(time2 > 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    item2Time.setText((String.valueOf(time2-1)));
                                }
                            });

                        }
                        else if(time2 == 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    if(Integer.valueOf(item2Time.getText()) < 1){
                                        bidButton2.setDisable(true);
                                    }
                                }
                            });
                        }
                        int time3 = Integer.valueOf(item3Time.getText());
                        if(time3 > 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    item3Time.setText((String.valueOf(time3-1)));
                                }
                            });

                        }
                        else if(time3 == 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    if(Integer.valueOf(item3Time.getText()) < 1){
                                        bidButton3.setDisable(true);
                                    }
                                }
                            });
                        }
                        int time4 = Integer.valueOf(item4Time.getText());
                        if(time4 > 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    item4Time.setText((String.valueOf(time4-1)));
                                }
                            });

                        }
                        else if(time4 == 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    if(Integer.valueOf(item4Time.getText()) < 1){
                                        bidButton4.setDisable(true);
                                    }
                                }
                            });
                        }
                        int time5 = Integer.valueOf(item5Time.getText());
                        if(time5 > 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    item5Time.setText((String.valueOf(time5-1)));
                                }
                            });

                        }
                        else if(time5 == 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    if(Integer.valueOf(item5Time.getText()) < 1){
                                        bidButton5.setDisable(true);
                                    }
                                }
                            });
                        }
                        int time6 = Integer.valueOf(item6Time.getText());
                        if(time6 > 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    item6Time.setText((String.valueOf(time6-1)));
                                }
                            });

                        }
                        else if(time6 == 0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("update");
                                    if(Integer.valueOf(item6Time.getText()) < 1){
                                        bidButton6.setDisable(true);
                                    }
                                }
                            });
                        }

                    }catch (InterruptedException io){
                    }
                }
            }
        }.start();
    }

    @FXML
    private Label userName_Auction;

    @FXML
    private Label item1Price;

    @FXML
    private Label item1Time;

    @FXML
    private TextField item1NewPrice;

    @FXML
    private Label item6Price;

    @FXML
    private Label item3Time;

    @FXML
    private Label item3Price;

    @FXML
    private Label item4Price;

    @FXML
    private TextField item2NewPrice;

    @FXML
    private TextField item3NewPrice;

    @FXML
    private Label item4Time;

    @FXML
    private Label item6Time;

    @FXML
    private Label item2Price;

    @FXML
    private TextField item6NewPrice;

    @FXML
    private Label item5Price;

    @FXML
    private TextField item4NewPrice;

    @FXML
    private Label item5Time;

    @FXML
    private TextField item5NewPrice;

    @FXML
    private Label item2Time;

    @FXML
    private Button bidButton1;

    @FXML
    private Button bidButton2;

    @FXML
    private Button bidButton3;

    @FXML
    private Button bidButton4;

    @FXML
    private Button bidButton5;

    @FXML
    private Button bidButton6;

    @FXML
    private Button onlineButton;

    @FXML
    void onlineButtonPressed(ActionEvent event) {
        userName_Auction.setText(userName);

        onlineButton.setVisible(false);
        System.out.println("Testing");
        getData();
        runAuction();
        runTimer();
    }

    @FXML
    void quitButton_AuctionPressed(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void bidButton1Pressed(ActionEvent event) {
        System.out.println("pressed");
        String itemPrice = item1NewPrice.getText();
        JSONObject json = new JSONObject();
        if(Double.valueOf(itemPrice) > buyprice1){
            json.put("sold", 1);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        else if(Double.valueOf(itemPrice) > Double.valueOf(item1Price.getText())){
            json.put("bid", 1);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        client.sendJSONOutput(json);
    }

    @FXML
    void bidButton2Pressed(ActionEvent event) {
        String itemPrice = item2NewPrice.getText();
        JSONObject json = new JSONObject();
        if(Double.valueOf(itemPrice) > buyprice2){
            json.put("sold", 2);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        else if(Double.valueOf(itemPrice) > Double.valueOf(item2Price.getText())){
            json.put("bid", 2);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        client.sendJSONOutput(json);
    }

    @FXML
    void bidButton3Pressed(ActionEvent event) {
        String itemPrice = item3NewPrice.getText();
        JSONObject json = new JSONObject();
        if(Double.valueOf(itemPrice) > buyprice3){
            json.put("sold", 3);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        else if(Double.valueOf(itemPrice) > Double.valueOf(item3Price.getText())){
            json.put("bid", 3);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        client.sendJSONOutput(json);
    }

    @FXML
    void bidButton4Pressed(ActionEvent event) {
        String itemPrice = item4NewPrice.getText();
        JSONObject json = new JSONObject();
        if(Double.valueOf(itemPrice) > buyprice4){
            json.put("sold", 4);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        else if(Double.valueOf(itemPrice) > Double.valueOf(item4Price.getText())){
            json.put("bid", 4);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        client.sendJSONOutput(json);
    }

    @FXML
    void bidButton5Pressed(ActionEvent event) {
        String itemPrice = item5NewPrice.getText();
        JSONObject json = new JSONObject();
        if(Double.valueOf(itemPrice) > buyprice5){
            json.put("sold", 5);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        else if(Double.valueOf(itemPrice) > Double.valueOf(item5Price.getText())){
            json.put("bid", 5);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        client.sendJSONOutput(json);
    }

    @FXML
    void bidButton6Pressed(ActionEvent event){
        String itemPrice = item6NewPrice.getText();
        JSONObject json = new JSONObject();
        if(Double.valueOf(itemPrice) > buyprice6){
            json.put("sold", 6);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        else if(Double.valueOf(itemPrice) > Double.valueOf(item6Price.getText())){
            json.put("bid", 6);
            json.put("userName", userName);
            json.put("price", itemPrice);
        }
        client.sendJSONOutput(json);
    }

    @FXML
    void item1Detail(ActionEvent event){

    }
    @FXML
    void item2Detail(ActionEvent event){

    }
    @FXML
    void item3Detail(ActionEvent event){

    }
    @FXML
    void item4Detail(ActionEvent event){

    }
    @FXML
    void item5Detail(ActionEvent event){

    }
    @FXML
    void item6Detail(ActionEvent event){

    }
}

