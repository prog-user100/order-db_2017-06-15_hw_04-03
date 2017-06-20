<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
3. Создать проект «База данных заказов». Создать
таблицы «Товары» , «Клиенты» и «Заказы».
Написать код для добавления новых клиентов,
товаров и оформления заказов.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Add data to db</title>
  </head>
  <body>

    <form action="/add_customer" method="post">
      <h3>Add customer:</h3>
      <table>
        <tr>
          <td>Name:</td>
          <td><input type="text" name="name" required /></td>
        </tr>
        <tr>
          <td>Phone:</td>
          <td><input type="text" name="phone" required /></td>
        </tr>
        <tr>
          <td>Address:</td>
          <td><input type="text" name="address" required /></td>
        </tr>
        <tr>
          <td colspan="2"><input type="submit" value="Add customer"></td>
        </tr>
      </table>

    </form>

    <form action="/add_goods" method="post">
      <h3>Add goods:</h3>
      <table>
        <tr>
          <td>Name:</td>
          <td><input type="text" name="name" required /></td>
        </tr>
        <tr>
          <td>Price:</td>
          <td><input type="number" name="price" required min="0"/></td>
        </tr>
        <tr>
          <td colspan="2"><input type="submit" value="Add goods"></td>
        </tr>
      </table>

    </form>

    <form action="/add_order" method="post">
      <h3>Add order:</h3>
      Goods:
      <select name="goods">
        <c:forEach items="${goodsList}" var="goods">
          <option value="<c:out value="${goods.id}"/>" ><c:out value="${goods.name}" /></option>
        </c:forEach>
      </select>
      Customer:
      <select name="customer">
        <c:forEach items="${customerList}" var="customer">
          <option value="<c:out value="${customer.id}"/>" ><c:out value="${customer.name}" /> <c:out value="${customer.phone}" /></option>
        </c:forEach>
      </select>
      <br/>
      <input type="submit" value="Add order">

    </form>
    <br/>
    <h3>Existing orders:</h3>
    <select name="order">
      <c:forEach items="${orderList}" var="order">
        <option><c:out value="${order.customerName}" /> | <c:out value="${order.goodsName }" /> | <c:out value="${order.date}" /></option>
      </c:forEach>
    </select>

  </body>
</html>
