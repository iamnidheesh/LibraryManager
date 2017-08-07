package library.assistant.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.ui.main.MainController;
import library.assistant.database.DatabaseHandler;


public class MemberAddController implements Initializable {

    DatabaseHandler databaseHandler;

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }
    

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID = id.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();

        Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
            return;
        }
//         stmt.execute("CREATE TABLE " + TABLE_NAME + "("
//                        + "	id varchar(200) primary key,\n"
//                        + "	name varchar(200),\n"
//                        + "	mobile varchar(20),\n"
//                        + "	email varchar(100)\n"
//                        + " )");
        String st = "INSERT INTO MEMBER VALUES ("
                + "'" + mID + "',"
                + "'" + mName + "',"
                + "'" + mMobile + "',"
                + "'" + mEmail + "'"
                + ")";
        System.out.println(st);
        String str1 = "SELECT * FROM MEMBER WHERE id = '"+mID+"'";
        ResultSet rs = databaseHandler.execQuery(str1);
        try {
            if(!rs.next()) {
                if (databaseHandler.execAction(st)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Data added");

                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Insert Operation Failed \n");
                alert1.showAndWait();
            }
            }
            else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Member id already exists \n");
                alert1.showAndWait();
            }


        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(st);
    }
    
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
   

}
