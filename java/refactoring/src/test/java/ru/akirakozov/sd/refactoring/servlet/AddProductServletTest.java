package ru.akirakozov.sd.refactoring.servlet;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AddProductServletTest {

    AddProductServlet addServlet = new AddProductServlet();
    @Mock
    HttpServletRequest myRequest;
    @Mock
    HttpServletResponse myResponse;
    Writer myWriter;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        myWriter = new StringWriter();
    }

    @Test
    public void addOneTest() throws IOException {
        when(myRequest.getParameter("name")).thenReturn("potato");
        when(myRequest.getParameter("price")).thenReturn("5");
        when(myResponse.getWriter()).thenReturn(new PrintWriter(myWriter));
        addServlet.doGet(myRequest, myResponse);
        assertEquals("OK\n", myWriter.toString());
    }

}