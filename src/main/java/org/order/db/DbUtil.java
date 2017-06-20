package org.order.db;

import org.order.entity.Customer;
import org.order.entity.Goods;
import org.order.entity.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {
    private static Connection connection;
    static {
        DbProperties props = new DbProperties();
        try {
            Class.forName(props.getDriver());
            connection = DriverManager.getConnection(props.getUrl(), props.getUser(), props.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException {
        String[] sqlArr = getCreateTableSQL();
        Statement stmt = connection.createStatement();
        for(int i = 0; i < sqlArr.length; i++) {
            stmt.executeUpdate(sqlArr[i]);
        }
        stmt.close();
    }

    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (name, phone, address) VALUES(?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getPhone());
        stmt.setString(3, customer.getAddress());
        stmt.executeUpdate();
        stmt.close();
    }

    public void addGoods(Goods goods) throws SQLException {
        String sql = "INSERT INTO goods (name, price) VALUES(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, goods.getName());
        stmt.setDouble(2, goods.getPrice());
        stmt.executeUpdate();
        stmt.close();
    }

    public void addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, goods_id, date) VALUES(?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, order.getCustomerId());
        stmt.setLong(2, order.getGoodsId());
        stmt.setDate(3, order.getDate());
        stmt.executeUpdate();
        stmt.close();
    }

    public List<Goods> getAllGoods() throws SQLException {
        List<Goods> list = new ArrayList<>();
        String sql = "SELECT * FROM goods";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Goods goods = new Goods();
            goods.setId(rs.getLong("id"));
            goods.setName(rs.getString("name"));
            goods.setPrice(rs.getDouble("price"));
            list.add(goods);
        }
        rs.close();
        stmt.close();
        return list;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setName(rs.getString("name"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));
            list.add(customer);
        }
        rs.close();
        stmt.close();
        return list;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.customer_id, o.goods_id, o.date, c.name, g.name " +
                "FROM orders o " +
                "INNER JOIN customer c ON o.customer_id=c.id " +
                "INNER JOIN goods g ON o.goods_id=g.id";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Order order = new Order();
            order.setCustomerId(rs.getLong("o.customer_id"));
            order.setCustomerName(rs.getString("c.name"));
            order.setGoodsId(rs.getLong("o.goods_id"));
            order.setGoodsName(rs.getString("g.name"));
            order.setDate(rs.getDate("o.date"));
            list.add(order);
        }
        rs.close();
        stmt.close();
        return list;
    }

    private String[] getCreateTableSQL() {
        String[] arr = new String[7];
        arr[0] = "SET foreign_key_checks = 0";
        arr[1] = "DROP TABLE IF EXISTS customer";
        arr[2] = "CREATE TABLE customer(" +
                "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "phone VARCHAR(100) NOT NULL UNIQUE," +
                "address VARCHAR(100) NOT NULL" +
                ")";
        arr[3] = "DROP TABLE IF EXISTS goods";
        arr[4] = "CREATE TABLE goods(" +
                "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL UNIQUE," +
                "price DOUBLE NOT NULL" +
                ")";
        arr[5] = "DROP TABLE IF EXISTS orders";
        arr[6] = "CREATE TABLE orders(" +
                "customer_id BIGINT NOT NULL," +
                "goods_id BIGINT NOT NULL," +
                "date DATE NOT NULL," +
                "FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE," +
                "FOREIGN KEY (goods_id) REFERENCES goods(id) ON DELETE CASCADE" +
                ")";
        return arr;
    }

}
