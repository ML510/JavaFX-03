package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RejisterFromController {

    @FXML
    private JFXTextField txtCPasswoed;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPasswoed;

    @FXML
    void btnRejisterOnAction(ActionEvent event) throws SQLException {

        String SQL = "INSERT INTO users (username,email,password) VALUES(?,?,?)";

        if (txtPasswoed.getText().equalsIgnoreCase(txtCPasswoed.getText())){


            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users WHERE email="+"'"+txtEmail.getText()+"'");

            System.out.println("email 1");

            if (!resultSet.next()){
                User user = new User(
                        txtName.getText(),
                        txtEmail.getText(),
                        txtPasswoed.getText()

                );
                System.out.println("email 2");

                PreparedStatement psTm = connection.prepareStatement(SQL);

                psTm.setString(1,user.getUserName());
                psTm.setString(2,user.getEmail());
                psTm.setString(3, user.getPassword());

                System.out.println("email 3");

                psTm.executeUpdate();

            }else {
                System.out.println("email 4");
                new Alert(Alert.AlertType.ERROR,"USER FOUND!").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Not Same Password!").show();
        }
    }

}
