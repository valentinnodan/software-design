package ru.akirakozov.sd.refactoring.servlet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTestsUtil {

    public static void initTable() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addTestingProductsToDatabase(List<ProductItem> products) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();

            for (int i = 0; i < products.size(); i++) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + products.get(i).getName() + "\"," + products.get(i).getPrice() + ")";
                stmt.executeUpdate(sql);
            }

            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static List<ProductItem> getProductsFromDatabase() {
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

    public static void dropTable() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
