package Modelo;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class PersonaDAO {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public PersonaDAO(){
        this.con = conectar.getConnection();
    }
    
    public Persona ultimo_registro(){
        
        String sql = "select id, nombre, correo, telefono from users order by id desc limit 1";
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                return new Persona(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                );
            }
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return new Persona();
    }
    
    public List listar(){
        List<Persona> datos = new ArrayList();
        
        String sql = "select id, nombre, correo, telefono from users";
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                datos.add(new Persona(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                ));
            }
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return datos;
    }
    
    public int Agregar(Persona p){
        String sql = "insert into users (nombre, correo, telefono) values (?,?,?)";
        
        try{
            ps = this.con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCorreo());
            ps.setString(3, p.getTelefono());
            
            return ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return 0;
    }
    
    public int Editar(Persona p){
        String sql = "update users set nombre = ?, correo = ?, telefono = ? where id = ?";
        
        try{
            ps = this.con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCorreo());
            ps.setString(3, p.getTelefono());
            ps.setInt(4, p.getId());
            
            return ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return 0;
    }
    
    public int Eliminar(int id){
        String sql = "delete from users where id = ?";
        
        try{
            ps = this.con.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return 0;
    }
}
