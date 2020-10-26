package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.ProductsDatabaseUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        String res;
        response.getWriter().println("<html><body>");
        if ("max".equals(command)) {
            response.getWriter().println("<h1>Product with max price: </h1>");
            res = ProductsDatabaseUtil.selectFromDB(command, "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
            if (!res.equals("")) {
                response.getWriter().println(res);
            }
        } else if ("min".equals(command)) {
            response.getWriter().println("<h1>Product with min price: </h1>");
            res = ProductsDatabaseUtil.selectFromDB(command, "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
            if (!res.equals("")) {
                response.getWriter().println(res);
            }
        } else if ("sum".equals(command)) {
            response.getWriter().println("Summary price: ");
            res = ProductsDatabaseUtil.selectFromDB(command, "SELECT SUM(price) FROM PRODUCT");
            response.getWriter().println(res);
        } else if ("count".equals(command)) {
            response.getWriter().println("Number of products: ");
            res = ProductsDatabaseUtil.selectFromDB(command, "SELECT COUNT(*) FROM PRODUCT");
            response.getWriter().println(res);
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
        response.getWriter().println("</body></html>");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
