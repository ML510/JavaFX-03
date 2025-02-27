package controller.item;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController implements ItemService{
    @Override
    public boolean addItem(Item item) {
        return false;
    }

    @Override
    public boolean update(Item item) {
        return false;
    }

    @Override
    public boolean delete(Item item) {
        return false;
    }

    @Override
    public Item searchItem(String code) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM item WHERE code=" + "'"+ code +"'");
            resultSet.next();

            System.out.println("111");

            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> getAll() {

        List<Item> itemArrayList = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM item");

            while (resultSet.next()){
                Item item = new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3),
                        resultSet.getInt(4)
                );
                itemArrayList.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemArrayList;
    }
    public ObservableList<String> getItemcodes(){
        List<Item> all = getAll();
        ObservableList<String> itemcodes = FXCollections.observableArrayList();

        all.forEach(item ->{
            itemcodes.add(item.getCode());
        });
        return itemcodes;
    }


    public boolean updateStock(List<OrderDetail> orderDetails){
        for (OrderDetail od : orderDetails){
            boolean isUpdateStock = updateStock(od);

            if (!isUpdateStock){
                return false;
            }
        }
        return true;
    }

    public boolean updateStock(OrderDetail orderDetail){

        String SQL ="UPDATE item SET qtyOnHand = qtyOnHand-? WHERE code=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,orderDetail.getQty());
            psTm.setObject(2,orderDetail.getItemCode());
            return psTm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
