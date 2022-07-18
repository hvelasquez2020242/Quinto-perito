/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Base64;
import javafx.event.ActionEvent;
import org.hugovelasquez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import org.hugovelasquez.bean.Usuario;
import org.hugovelasquez.db.Conexion;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 4 ago. 2021
 * @time 16:27:09
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private PasswordField pfPass;
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void validar(ActionEvent event) {
        String pass = pfPass.getText();
        String user = txtNombre.getText();

        if (escenarioPrincipal.getUsuario() != null) {
            if (!(txtNombre.getText().isEmpty() || pfPass.getText().isEmpty())) {
                if (pass.equals(getPassword(user))) {
// Bienvenido
                    escenarioPrincipal.mostrarMenuPrincipal();
                } else {
// Alert -> Usuario o contraseña es incorrecto
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Usuario o contraseña es incorrecto");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                }
            } else {
// Alert -> Campos están vacíos.
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Los campos estan vacios");
                alert.setHeaderText(null);
                alert.setTitle("Alerta");
                alert.show();
            }
        }

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private String getPassword(String user) {
        String passwordDecodificado = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarUsuario(?)");
            pstmt.setString(1, user);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                escenarioPrincipal.getUsuario().setUser(rs.getString("user"));
                escenarioPrincipal.getUsuario().setPass(rs.getString("pass"));
                escenarioPrincipal.getUsuario().setNombre(rs.getString("nombre"));
                escenarioPrincipal.getUsuario().setRol(rs.getInt("rol"));
                passwordDecodificado = new String(Base64.getDecoder().decode(escenarioPrincipal.getUsuario().getPass()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return passwordDecodificado;
    }

}
