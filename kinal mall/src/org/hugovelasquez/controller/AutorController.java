/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.controller;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import org.hugovelasquez.system.Principal;

/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 12 may. 2021
 * @time 14:33:24
 */
public class AutorController implements Initializable{

    private Principal escenarioPrincipal;
    @FXML
    private ImageView regresar;
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
    private void regresar(MouseEvent event){
        escenarioPrincipal.mostrarMenuPrincipal();
    }
    @FXML 
    private void mostrarVistaAnterior() {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

    

}
