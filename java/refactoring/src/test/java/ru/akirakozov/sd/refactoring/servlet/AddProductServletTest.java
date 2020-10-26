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
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        DatabaseTestsUtil.initTable();
    }

    @After
    public void after() {
        DatabaseTestsUtil.dropTable();
    }


    private void makeRequest(String name, int price) throws IOException {
        when(myRequest.getParameter("name")).thenReturn(name);
        when(myRequest.getParameter("price")).thenReturn(price + "");
        addServlet.doGet(myRequest, myResponse);
    }

    @Test
    public void addOneTest() throws IOException {
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        makeRequest("potato", 5);

        List<ProductItem> products = DatabaseTestsUtil.getProductsFromDatabase();
        assertEquals("OK\n", myWriter.toString());
        assertEquals(1, products.size());
        assertEquals("potato", products.get(0).getName());
        assertEquals(5, products.get(0).getPrice());
    }

    @Test
    public void addManyTest() throws IOException {
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        makeRequest("potato", 5);
        makeRequest("tomato", 10);
        makeRequest("onion", 15);

        List<ProductItem> products = DatabaseTestsUtil.getProductsFromDatabase();
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