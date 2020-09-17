package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControllerAuction {


    public void setLabel(String username){
        userName_Auction.setText(username);
    }

    @FXML
    private Button myAccountButton;

    @FXML
    private Label item1Price;

    @FXML
    private Label item1Time;

    @FXML
    private TextField item1NewPrice;

    @FXML
    private Button quitButton_Auction;

    @FXML
    private Label userName_Auction;

    @FXML
    private Button bidButton1;

    @FXML
    void quitButton_AuctionPressed(ActionEvent event) {

    }

    @FXML
    void bidButton1Pressed(ActionEvent event) {

    }

    @FXML
    void myAccountButtonPressed(ActionEvent event) {

    }

}
