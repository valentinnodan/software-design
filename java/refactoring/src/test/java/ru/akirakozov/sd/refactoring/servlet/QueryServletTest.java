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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class QueryServletTest {
    QueryServlet queryServlet;
    @Mock
    HttpServletRequest myRequest;
    @Mock
    HttpServletResponse myResponse;
    Writer myWriter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        queryServlet = new QueryServlet();
        myWriter = new StringWriter();
        DatabaseTestsUtil.initTable();
    }

    @After
    public void after() {
        DatabaseTestsUtil.dropTable();
    }

    private void makeRequest(String command) throws IOException {
        when(myRequest.getParameter("command")).thenReturn(command);
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));

        queryServlet.doGet(myRequest, myResponse);
    }

    @Test
    public void minForEmptyTableTest() throws IOException {
        makeRequest("min");

        assertEquals("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void maxForEmptyTableTest() throws IOException {
        makeRequest("max");

        assertEquals("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void sumForEmptyTableTest() throws IOException {
        makeRequest("sum");

        assertEquals("<html><body>\n" +
                "Summary price: \n" +
                "0\n" +
                "</body></html>\n", myWriter.toString());

    }

    @Test
    public void countForEmptyTableTest() throws IOException {
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "Number of products: \n" +
                "0\n" +
                "</body></html>\n", myWriter.toString());

    }

    private void createSimpleTable() {
        DatabaseTestsUtil.addTestingProductsToDatabase(Arrays.asList(new ProductItem("potato", 5),
                new ProductItem("tomato", 10),
                new ProductItem("onion", 15)));
    }

    @Test
    public void minForSimpleTableTest() throws IOException {
        createSimpleTable();
        makeRequest("min");

        assertEquals("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "potato\t5</br>\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void maxForSimpleTableTest() throws IOException {
        createSimpleTable();
        makeRequest("max");

        assertEquals("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "onion\t15</br>\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void sumForSimpleTableTest() throws IOException {
        createSimpleTable();
        makeRequest("sum");

        assertEquals("<html><body>\n" +
                "Summary price: \n" +
                "30\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void countForSimpleTableTest() throws IOException {
        createSimpleTable();
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "Number of products: \n" +
                "3\n" +
                "</body></html>\n", myWriter.toString());
    }


    private void createTableWithRepetitions() {
        DatabaseTestsUtil.addTestingProductsToDatabase(Arrays.asList(new ProductItem("potato", 5),
                new ProductItem("tomato", 10),
                new ProductItem("onion", 15),
                new ProductItem("onion", 5),
                new ProductItem("potato", 15)));
    }

    @Test
    public void minForTableWithRepetitionsTest() throws IOException {
        createTableWithRepetitions();
        makeRequest("min");

        assertEquals("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "potato\t5</br>\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void maxForTableWithRepetitionsTest() throws IOException {
        createTableWithRepetitions();
        makeRequest("max");

        assertEquals("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "onion\t15</br>\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void sumForTableWithRepetitionsTest() throws IOException {
        createTableWithRepetitions();
        makeRequest("sum");

        assertEquals("<html><body>\n" +
                "Summary price: \n" +
                "50\n" +
                "</body></html>\n", myWriter.toString());
    }

    @Test
    public void countForTableWithRepetitionsTest() throws IOException {
        createTableWithRepetitions();
        makeRequest("count");

        assertEquals("<html><body>\n" +
                "Number of products: \n" +
                "5\n" +
                "</body></html>\n", myWriter.toString());
    }
}