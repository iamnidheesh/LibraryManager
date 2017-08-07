package library.assistant.ui.delete;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import library.assistant.ui.main.MainController;
public class DeleteController implements Initializable  {
     
    @FXML
    private HBox book_info;
    @FXML
    private HBox member_info;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    DatabaseHandler databaseHandler;
    @FXML
    private TextField memberIDInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberMobile;
    @FXML
    private ImageView deleteBButton;
    @FXML
    private ImageView deleteMButton;
    @FXML
    private JFXTextField bookID;
    @FXML
    private ListView<String> issueDataList;
    
    Boolean isReadyForSubmission = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);

        databaseHandler = DatabaseHandler.getInstance();
    }
    
     @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookCache();

        String id = bookIDInput.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        Boolean flag = false;
        try {
            while (rs.next()) {
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");

                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                String status = (bStatus) ? "Available" : "Not Available";
                bookStatus.setText(status);

                flag = true;
            }

            if (!flag) {
                bookName.setText("No Such Book Available");
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearBookCache() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    void clearMemberCache() {
        memberName.setText("");
        memberMobile.setText("");
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMemberCache();

        String id = memberIDInput.getText();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        Boolean flag = false;
        try {
            while (rs.next()) {
                String mName = rs.getString("name");
                String mMobile = rs.getString("mobile");

                memberName.setText(mName);
                memberMobile.setText(mMobile);

                flag = true;
            }

            if (!flag) {
                memberName.setText("No Such Member Available");
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void loadDeleteBookOp(ActionEvent event) {
        String bookID = bookIDInput.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to delete the book " + bookName.getText());

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String str = "DELETE FROM BOOK WHERE id = '" + bookID + "'";
            String str1 = "SELECT * FROM ISSUE WHERE bookID = '"+bookID+"'";
            System.out.println(str);
            ResultSet rs = databaseHandler.execQuery(str1);
            try {
            if(!rs.next()) {
                if (databaseHandler.execAction(str)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Delete Operation Complete");

                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Delete Operation Failed \n");
                alert1.showAndWait();
            }
            }
            else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Book already Issued \n");
                alert1.showAndWait();
            }


        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Delete Operation cancelled");
            alert1.showAndWait();
        }
    }
    
    @FXML
    private void loadDeleteMemberOp(ActionEvent event) {
        String memberID = memberIDInput.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to delete the member named  "  + memberName.getText() + " ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String str = "DELETE FROM MEMBER WHERE id = '" + memberID + "'" + "AND id NOT IN (SELECT memberID FROM ISSUE )";
            String str1 = "SELECT * FROM ISSUE WHERE memberID = '"+memberID+"'";
            System.out.println(str);
            ResultSet rs = databaseHandler.execQuery(str1);
            try {
            if(!rs.next()) {
                if (databaseHandler.execAction(str)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Delete Operation Complete");

                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Delete Operation Failed \n");
                alert1.showAndWait();
            }
            }
            else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed ");
                alert1.setHeaderText(null);
                alert1.setContentText("Member has issued some books\n");
                alert1.showAndWait();
            }


        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Delete Operation cancelled");
            alert1.showAndWait();
        }
    }

}
