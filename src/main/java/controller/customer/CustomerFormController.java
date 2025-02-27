package controller.customer;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import service.BoFactory;
import service.custom.CustomerBo;
import service.custom.impl.CustomerBoImpl;
import util.BoType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableView tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSalary;

    CustomerBo customerBo = BoFactory.getInstance().getBoType(BoType.CUSTOMER);

    @FXML
    void btnAddOnAction(ActionEvent event) {

        boolean isCustomerAdd = customerBo.addCustomer(
                new Customer(
                        txtId.getText(),
                        txtName.getText(),
                        txtAddress.getText(),
                        Double.parseDouble(txtSalary.getText())
                )
        );

        if (isCustomerAdd){
            new Alert(Alert.AlertType.INFORMATION,"Customer Added !").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Customer Not Added !").show();
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    private void loadTable(){

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

//        List<Customer> all = new CustomerContriller().getAll();
//        all.forEach(customer->{

//        new CustomerContriller().getAll().forEach(customer->{
//            customerObservableList.add(customer);
//        });
//
//        tblCustomer.setItems(customerObservableList);
//
//        System.out.println(new CustomerContriller().getAll());
    }

}
