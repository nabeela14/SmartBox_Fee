package com.smartbox;
import java.sql.Connection;
import java.sql.DriverManager;

public   class DBconnection {
    public static Connection getConnection(){
        Connection conn =null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Smartbox","root","rootnabee");
        }catch (Exception e){
            e.printStackTrace();
        }
    return conn;
    }
}
