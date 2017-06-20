package org.order.servlet;

import org.order.db.DbUtil;
import org.order.entity.Customer;
import org.order.entity.Goods;
import org.order.entity.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/page", "/add_customer", "/add_goods", "/add_order"})
public class MainServlet extends HttpServlet {
    private static DbUtil dbUtil = new DbUtil();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAttributes(req);
        req.getRequestDispatcher("page.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if(req.getRequestURI().equals("/add_customer")) {
                addCustomer(req);
            } else if (req.getRequestURI().equals("/add_goods")) {
                addGoods(req);
            } else if (req.getRequestURI().equals("/add_order")) {
                addOrder(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAttributes(req);
        req.getRequestDispatcher("page.jsp").forward(req, resp);
    }

    private void addCustomer(HttpServletRequest req) throws SQLException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        if(name == null || name.isEmpty() ||
                phone == null || phone.isEmpty() ||
                address == null || address.isEmpty()
                ) {
            throw new NullPointerException();
        } else {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setAddress(address);
            dbUtil.addCustomer(customer);
        }
    }

    private void addGoods(HttpServletRequest req) throws SQLException {
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        if(name == null || name.isEmpty() ||
                price == null || price.isEmpty()
                ) {
            throw new NullPointerException();
        } else {
            Goods goods = new Goods();
            goods.setName(name);
            goods.setPrice(Double.parseDouble(price));
            dbUtil.addGoods(goods);
        }
    }

    private void addOrder(HttpServletRequest req) throws SQLException {
        String customerId = req.getParameter("customer");
        String goodsId = req.getParameter("goods");
        if(customerId == null || customerId.isEmpty() ||
                goodsId == null || goodsId.isEmpty()
                ) {
            throw new NullPointerException();
        } else {
            Order order = new Order();
            order.setCustomerId(Integer.parseInt(customerId));
            order.setGoodsId(Integer.parseInt(goodsId));
            dbUtil.addOrder(order);
        }
    }

    private void setAttributes(HttpServletRequest req) {
        List<Customer> customerList = null;
        List<Goods> goodsList = null;
        List<Order> orderList = null;
        try {
            customerList = dbUtil.getAllCustomers();
            goodsList = dbUtil.getAllGoods();
            orderList = dbUtil.getAllOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("customerList", customerList);
        req.setAttribute("goodsList", goodsList);
        req.setAttribute("orderList", orderList);
    }
}
