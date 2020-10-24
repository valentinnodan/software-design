package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AddProductServletTest {

    AddProductServlet addServlet;
    @Mock
    HttpServletRequest myRequest;
    @Mock
    HttpServletResponse myResponse;
    Writer myWriter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        addServlet = new AddProductServlet();
        myWriter = new StringWriter();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @After
    public void after() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<ProductItem> getProducts() {
        List<ProductItem> products = new ArrayList<>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                products.add(new ProductItem(name, price));
            }
            rs.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return products;
    }

    private void makeRequest(String name, int price) {
        when(myRequest.getParameter("name")).thenReturn(name);
        when(myRequest.getParameter("price")).thenReturn(price + "");
    }

    @Test
    public void addOneTest() throws IOException {
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        makeRequest("potato", 5);
        addServlet.doGet(myRequest, myResponse);

        List<ProductItem> products = getProducts();
        assertEquals("OK\n", myWriter.toString());
        assertEquals(1, products.size());
        assertEquals("potato", products.get(0).getName());
        assertEquals(5, products.get(0).getPrice());
    }

    @Test
    public void addManyTest() throws IOException {
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        makeRequest("potato", 5);
        addServlet.doGet(myRequest, myResponse);
        makeRequest("tomato", 10);
        addServlet.doGet(myRequest, myResponse);
        makeRequest("onion", 15);
        addServlet.doGet(myRequest, myResponse);

        List<ProductItem> products = getProducts();
        assertEquals("OK\nOK\nOK\n", myWriter.toString());
        assertEquals(3, products.size());
        assertEquals("potato", products.get(0).getName());
        assertEquals(5, products.get(0).getPrice());
        assertEquals("tomato", products.get(1).getName());
        assertEquals(10, products.get(1).getPrice());
        assertEquals("onion", products.get(2).getName());
        assertEquals(15, products.get(2).getPrice());
    }

}