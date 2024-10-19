package Modelo;

import java.sql.*;

public class Conexion {
    
    String url = "jdbc:mysql://localhost:3306/labdsi";
    String user = "root";
    String password = "";
    
    Connection con;
    
    public Connection getConnection(){
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return con;
    }
}
