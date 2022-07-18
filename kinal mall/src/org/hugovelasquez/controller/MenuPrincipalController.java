/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.hugovelasquez.system.Principal;
/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 5 may. 2021
 * @time 17:17:50
 */
public class MenuPrincipalController implements Initializable{
    
    private Principal escenarioPrincipal;
    @FXML
    private MenuItem btnautor;
    @FXML
    private MenuItem menuAdministracion;
    @Override
    
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaAutor(ActionEvent event) {
        escenarioPrincipal.mostrarAutor(); 
    }
    @FXML 
    private void mostrarVistaAdministracion(){ 
        escenarioPrincipal.mostrarAdministracion();
    }

    @FXML
    private void mostrarClientes(ActionEvent event) {
        escenarioPrincipal.mostrarClientes();
    }

     

    @FXML
    private void mostrarVistaProveedores(ActionEvent event) {
        escenarioPrincipal.mostrarProveedores();
    }

    

    @FXML
    private void mostrarVistaEmpleados(ActionEvent event) {
        escenarioPrincipal.mostrarEmpleados();
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        escenarioPrincipal.mostrarLogin();
    }

    public void validarPermisos(){
        if(escenarioPrincipal.getUsuario().getRol() != 1){
            menuAdministracion.setDisable(true);
        }
    }

}