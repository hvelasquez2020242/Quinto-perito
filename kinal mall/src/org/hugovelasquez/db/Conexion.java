/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 27 may. 2021
 * @time 16:08:13
 */
public class Conexion {
    private Connection conexion; 
    private final String URL;
    private final String BD;
    private static Conexion instancia; 
    private final String SERVER; 
    private final String PUERTO; 
    private final String USER; 
    private final String PASS;
    
    
    private Conexion() {
    SERVER = "localhost";
    BD = "IN5BV_KinalMall";
    PUERTO = "3306";
    USER = "root";
    PASS = "admin";
            
URL = "jdbc:mysql://" + SERVER + ":" + PUERTO + "/" + BD + "?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false";
    try{

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
       
       conexion = DriverManager.getConnection(URL, USER , PASS);
            
       
        } catch (ClassNotFoundException e) {
            System.out.println("No se encuentra ninguna definicion para la clase");
           e.printStackTrace();
       } catch (InstantiationException e) {
            System.out.println("No se puede crear una instancia del objeto");
           e.printStackTrace();
       } catch (IllegalAccessException e) {
            System.out.println("No se tiene los permisos para acceder al paquete");
           e.printStackTrace();
       } catch (SQLException e) {
            System.out.println("Se produjo un error");
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       }
    } 

    public Connection getConexion() {
        return conexion;
    }
    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia; 
    }
    
}
