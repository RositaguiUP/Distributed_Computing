package Talkie.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import Talkie.Elements.User;
import Talkie.Functions.Client;

/**
 * FXML Controller class
 *
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblError.setVisible(false);
        txtPass.setText("pass1");
        txtUser.setText("user1");
    }

    @FXML
    private void login(ActionEvent event) throws IOException, ParseException {
        String username = txtUser.getText();
        String password = txtPass.getText();
        
        try {
            Client client = new Client();
            char response = client.login(username, password);
        
            if (response == '1') {
                Stage loginScreen = (Stage) txtUser.getScene().getWindow();
                loginScreen.hide();
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("./../UI/Main.fxml"));
                Parent root = null;

                User user = new User(username);
                
                try {
                    root = loader.load();
                    MainController controller = loader.getController();
                    controller.initData(user);
                    
                    Scene lobbyScene = new Scene(root);
                    Stage gameStage = new Stage();
                    gameStage.setTitle("Talkie");
                    gameStage.getIcons().add(new Image("file:./../img/Icon.png"));
                    gameStage.setResizable(false);
                    gameStage.setScene(lobbyScene);
                    gameStage.show();
                } catch (IOException ex) {
                    Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (response == '0') {
                lblError.setVisible(true);
                lblError.setText("User or passsword incorrect, try again");
            }
        } catch (Exception e) {
            lblError.setVisible(true);
            lblError.setText("Server not available");
        }
    }
}