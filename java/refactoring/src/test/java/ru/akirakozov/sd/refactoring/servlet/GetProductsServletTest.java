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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class GetProductsServletTest {
    GetProductsServlet getServlet;
    @Mock
    HttpServletRequest myRequest;
    @Mock
    HttpServletResponse myResponse;
    Writer myWriter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        getServlet = new GetProductsServlet();
        myWriter = new StringWriter();
        DatabaseTestsUtil.initTable();
    }

    @After
    public void after() {
        DatabaseTestsUtil.dropTable();
    }

    @Test
    public void getFromSimpleTableTest() throws IOException {
        DatabaseTestsUtil.addTestingProductsToDatabase(Arrays.asList(new ProductItem("potato", 5),
                new ProductItem("tomato", 10),
                new ProductItem("onion", 15)));
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        List<ProductItem> products = DatabaseTestsUtil.getProductsFromDatabase();

        getServlet.doGet(myRequest, myResponse);

        List<ProductItem> productsAfter = DatabaseTestsUtil.getProductsFromDatabase();
        assertEquals("<html><body>\n" +
                "potato\t5</br>\n" +
                "tomato\t10</br>\n" +
                "onion\t15</br>\n" +
                "</body></html>\n", myWriter.toString());
        assertEquals(products.size(), productsAfter.size());
    }

    @Test
    public void getFromTableWithRepetitionsTest() throws IOException {
        DatabaseTestsUtil.addTestingProductsToDatabase(Arrays.asList(new ProductItem("potato", 5),
                new ProductItem("tomato", 10),
                new ProductItem("onion", 15),
                new ProductItem("tomato", 100),
                new ProductItem("onion", 5)));
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        List<ProductItem> products = DatabaseTestsUtil.getProductsFromDatabase();

        getServlet.doGet(myRequest, myResponse);

        List<ProductItem> productsAfter = DatabaseTestsUtil.getProductsFromDatabase();
        assertEquals("<html><body>\n" +
                "potato\t5</br>\n" +
                "tomato\t10</br>\n" +
                "onion\t15</br>\n" +
                "tomato\t100</br>\n" +
                "onion\t5</br>\n" +
                "</body></html>\n", myWriter.toString());
        assertEquals(products.size(), productsAfter.size());
    }

    @Test
    public void getFromEmptyTest() throws IOException {
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        List<ProductItem> products = DatabaseTestsUtil.getProductsFromDatabase();

        getServlet.doGet(myRequest, myResponse);

        List<ProductItem> productsAfter = DatabaseTestsUtil.getProductsFromDatabase();
        assertEquals("<html><body>\n" +
                "</body></html>\n", myWriter.toString());
        assertEquals(products.size(), productsAfter.size());
    }

}