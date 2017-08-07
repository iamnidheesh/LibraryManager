/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author nidheesh
 */
public class UserController implements Initializable {

    DatabaseHandler databaseHandler;
    @FXML
    private JFXTextField user;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton loginButton;
    @FXML
    private AnchorPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance(); // TODO
    }  
    
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void checkPassword(ActionEvent event) {
        
        String str1 = "SELECT USERNAME FROM USERS WHERE USERNAME ='"+user.getText()+"'"+"AND PASSWORD ='"+password.getText()+"'";
        ResultSet rs = databaseHandler.execQuery(str1);
        try {
            if(rs.next()) {
                loadWindow("/library/assistant/ui/main/main.fxml", "Library manager");
                 Stage stage = (Stage) rootPane.getScene().getWindow();
                  stage.close();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Wrong Userid and password. \n");
                alert1.showAndWait();
            }


        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
