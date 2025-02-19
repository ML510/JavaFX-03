package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerContriller;
import controller.item.ItemController;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private TableView<CartTM> tblOrder;

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

    ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();

// ###################### Load Order Table ################################
    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

        String code = comboItemCode.getValue().toString();
        String description = txtDescription.getText();
        Integer qtyOnHand = Integer.parseInt(txtQty.getText());
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Double total = qtyOnHand * unitPrice;

        cartTMS.add(new CartTM(code,description,qtyOnHand,unitPrice,total));

        tblOrder.setItems(cartTMS);

        calcNetTotal();
    }

// --------------- Set Net Total -------------------------
    private void calcNetTotal(){
        Double netTotal = 0.0;

        for (CartTM tm : cartTMS){
            netTotal+=tm.getTotal();
        }

        lblNetTotal.setText(netTotal.toString());

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException {

        // ------ Create Order Object And OrderDetail Object ---------
        String orderId = txtOrderId.getText();
        String date = lblDay.getText();
        String customerId = cmboCustomerId.getValue().toString();

        List<OrderDetail> orderDetails = new ArrayList<>();

        cartTMS.forEach(cartTM -> {
            orderDetails.add(new OrderDetail(
                    orderId,
                    cartTM.getItemCode(),
                    cartTM.getQtyOnHand(),
                    cartTM.getUnitPrice()
            ));
        });

        Order order = new Order(orderId, date, customerId, orderDetails);

        System.out.println(order);

        // --------- Order Object Parsing -----------------------------
        if(new OrderController().placeOrder(order)){
            new Alert(Alert.AlertType.INFORMATION,"Order Placed !").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Order Not Placed !").show();
        }
    }

    private void setDateAndTime(){

        //------------------ Set Date ------------------------------
        Date date = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
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

        // --------------------------- Colum Binding ------------------------------------
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescriotion.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }


    public void btnCommitOnAction(ActionEvent actionEvent) throws SQLException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.commit();

    }
}
