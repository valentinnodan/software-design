package ru.akirakozov.sd.refactoring.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductsDatabaseUtil {
    public static void insertToDB(String name, long price){
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

    public static List<String> getAllProductsFromDB() {
        List<String> res = new ArrayList<>();
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
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

    public static String selectFromDB(String command, String sqlCommand) {
        String res = "";
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCommand);

                if (command == "min" || command == "max") {
                    while (rs.next()) {
                        String  name = rs.getString("name");
                        int price  = rs.getInt("price");
                        res = name + "\t" + price + "</br>";
                    }
                } else {
                    if (rs.next()) {
                        res = rs.getInt(1) + "";
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

//    public static String max() {
//        return selectFromDB("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")
//    }
}
