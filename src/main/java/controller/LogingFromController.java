package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.User;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogingFromController {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, IOException {
        String SQL = "SELECT * FROM users WHERE email="+"'"+txtEmail.getText()+"'";

        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(SQL);

//------------------------------------ Password decryptor --------------------------------------------
        String key = "##123##";

        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        if (resultSet.next()){
            User user = new User(
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            System.out.println(user);

            //------------------------------------ Password decryptor --------------------------------------------
            if (basicTextEncryptor.decrypt(user.getPassword()).equals(txtPassword.getText())){
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
                stage.show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Check Your Password").show();
            }

        }else {
            new Alert(Alert.AlertType.ERROR,"User Not Found!").show();
        }
    }

    @FXML
    void hyperRegisterOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/rejister_form.fxml"))));
        stage.show();



    }

}
