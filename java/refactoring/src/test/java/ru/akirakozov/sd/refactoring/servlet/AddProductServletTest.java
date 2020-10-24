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
    List<String> products;
    List<Integer> prices;
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
        products = new ArrayList<>();
        prices = new ArrayList<>();
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
    public void after(){
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void addOneTest() throws IOException {
        when(myRequest.getParameter("name")).thenReturn("potato");
        when(myRequest.getParameter("price")).thenReturn("5");
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        addServlet.doGet(myRequest, myResponse);

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                products.add(name);
                prices.add(price);
            }
            rs.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertEquals("OK\n", myWriter.toString());
        assertEquals(1, prices.size());
        assertEquals(1, products.size());
        assertEquals("potato", products.get(0));
        assertEquals((Integer)5, prices.get(0));
    }

}