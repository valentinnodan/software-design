package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabase;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    ProductsDatabase database = new ProductsDatabase();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        String res;
        HtmlBuilder builder = new HtmlBuilder();
        if ("max".equals(command)) {
            builder.addH1("Product with max price: ");
            res = database.max();
            builder.addContent(res);
        } else if ("min".equals(command)) {
            builder.addH1("Product with min price: ");
            res = database.min();
            builder.addContent(res);
        } else if ("sum".equals(command)) {
            builder.addContent("Summary price: ");
            res = database.sum();
            builder.addContent(res);
        } else if ("count".equals(command)) {
            builder.addContent("Number of products: ");
            res = database.count();
            builder.addContent(res);
        } else {
            builder.addContent("Unknown command: " + command);
        }
        response.getWriter().println(builder.toString());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
