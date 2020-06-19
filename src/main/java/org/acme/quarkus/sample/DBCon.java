package org.acme.quarkus.sample;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DBCon {

        private static Connection con;

        public DBCon(){
            if (con == null){
                con = getDBCon();
            }
        }

        public Connection getDBCon() {
            try {
                Class driver_class = Class.forName("com.mysql.cj.jdbc.Driver");
                Driver driver = (Driver) driver_class.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(driver);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Angular_Fokusarbeit", "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return con;
        }

    }
