package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerContriller;
import controller.item.ItemController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import model.Customer;
import model.Item;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderFromController implements Initializable {


    @FXML
    private JFXComboBox cmboCustomerId;

    @FXML
    private TableColumn colDescriotion;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private JFXComboBox comboItemCode;

    @FXML
    private Label lblDay;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<?> tblOrder;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtOrderId;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtUnitPrice;

    public JFXTextField txtQty;

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

    }

    private void setDateAndTime(){

        //------------------ Set Date ------------------------------
        Date date = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = dataFormat.format(date);
        lblDay.setText(format);

        System.out.println(format);

        //------------------ Set Time ------------------------------
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,e->{
                    LocalTime now = LocalTime.now();
                    lblTime.setText(now.getHour()+":"+now.getMinute()+":"+now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

// ######################## Load Customer Ids #################################

    private void loadCustomerIds(){

        // ------------ Step 01 --------------------------------

//        CustomerContriller customerContriller = new CustomerContriller();
//        List<Customer> all = customerContriller.getAll();
//
//        ObservableList<String> customerIds = FXCollections.observableArrayList();
//
//        all.forEach(customer -> {
//            customerIds.add(customer.getId());
//        });
//        cmboCustomerId.setItems(customerIds);

        // ------------ Step 02 --------------------------------

        cmboCustomerId.setItems(new CustomerContriller().getCustomerIds());
    }

    private void searchCustomerDate(String customerId) {

        Customer customer = new CustomerContriller().searchCustomer(customerId);
        System.out.println(customer);

        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
    }

// ######################## Load Item Ids #################################

    private void loadItemcodes(){
        comboItemCode.setItems(new ItemController().getItemcodes());
    }

    private void searchItemDate(String itemCode) {
        Item item = new ItemController().searchItem(itemCode);
        System.out.println(item);

        txtDescription.setText(item.getDescription());
        txtStock.setText(item.getStock().toString());
        txtUnitPrice.setText(item.getUnitPrice().toString());
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDateAndTime();  // ----- Set Date And Set Time -----
        loadCustomerIds(); // ----- Set Customer ID -----

        // ----- Entering values according to customer ID -----
        cmboCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null){
                searchCustomerDate(newValue.toString());
            }
            System.out.println(newValue);
            System.out.println(oldValue);
        });

        loadItemcodes(); // ----- Set Item ID -----

        // ----- Entering values according to Item ID -----
        comboItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null){
                searchItemDate(newValue.toString());
            }
        });
    }




}
