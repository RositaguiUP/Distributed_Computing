/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Talkie.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.parser.ParseException;

import Talkie.Functions.Client;

/**
 * FXML Controller class
 *
 */
public class CreateGroupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private Button btnPlay;
    @FXML private Label  lblResult;
    @FXML private TextField txtGroupName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblResult.setVisible(false);
        txtGroupName.setText("Group");
    }
    
    public int createGroup(String username) throws IOException, ParseException{
        String groupname = txtGroupName.getText();

        
        try {
            Client client = new Client(); 
            char response = client.createGroup(username, groupname);
            lblResult.setVisible(true);
            if (response == '1') {
                lblResult.setText("Group created!");
            } else if (response == '0') {
                lblResult.setText("Group already exists");
            }
            return response - '0';
        } catch (Exception e) {
            lblResult.setVisible(true);
            lblResult.setText("Server not available");
            return -1;
        }
    }
}
