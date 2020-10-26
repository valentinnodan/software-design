package ru.akirakozov.sd.refactoring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductsDatabase {
    public void insertToDB(String name, long price) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllProductsFromDB() {
        List<String> res = new ArrayList<>();
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    res.add(name + "\t" + price + "</br>");
                }

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public List<String> selectFromDB(String sqlCommand, List<Integer> resColumns, boolean asInt) {
        List<String> res = new ArrayList<>();
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCommand);
                while (rs.next()) {
                    for (int columnNumber : resColumns)
                        if (asInt) {
                            res.add(rs.getInt(columnNumber) + "");
                        } else {
                            res.add(rs.getString(columnNumber));
                        }
                }
                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private String getStringRepresentationOfRow(List<String> row) {
        if (row.size() == 0) {
            return "";
        }
        return row.get(0) + "\t" + row.get(1) + "</br>";
    }

    public String max() {
        return getStringRepresentationOfRow(selectFromDB("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", Arrays.asList(2, 3), false));
    }

    public String min() {
        return getStringRepresentationOfRow(selectFromDB("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", Arrays.asList(2, 3), false));
    }

    public String sum() {
        return selectFromDB("SELECT SUM(price) FROM PRODUCT", Collections.singletonList(1), true).get(0);
    }

    public String count() {
        return selectFromDB("SELECT COUNT(*) FROM PRODUCT", Collections.singletonList(1), true).get(0);
    }
}
