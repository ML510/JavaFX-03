package service.custom.impl;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import repository.DaoFactory;
import repository.custom.CustomerDao;
import service.custom.CustomerBo;
import util.DaoType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerBoImpl implements CustomerBo {

        CustomerDao customerDao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);

        @Override
        public boolean addCustomer(Customer customer) {
            return customerDao.save(customer);

        }

        @Override
        public boolean updateCustomer(Customer customer) {
            return false;
        }

        @Override
        public boolean deleteCustomer(Customer customer) {
            return false;
        }

        // *************************** Search Customer *********************************
        @Override
        public Customer searchCustomer(String id) {

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM customer WHERE id=" +"'"+ id +"'");
                resultSet.next();

                System.out.println("111");

                return new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public List<Customer> getAll() {

            ArrayList<Customer> customerArrayList = new ArrayList<>();

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");

                while (resultSet.next()){
                    Customer customer = new Customer(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4)
                    );
                    customerArrayList.add(customer);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return customerArrayList;
        }

        public ObservableList<String> getCustomerIds(){
            List<Customer> all = getAll();
            ObservableList<String> customerIds = FXCollections.observableArrayList();

            all.forEach(customer -> {
                customerIds.add(customer.getId());
            });
            return customerIds;
        }
}
