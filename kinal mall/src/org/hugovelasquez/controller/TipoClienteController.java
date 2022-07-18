/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Clientes;
import org.hugovelasquez.bean.TipoCliente;
import org.hugovelasquez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 14 jun. 2021
 * @time 21:00:46
 */
public class TipoClienteController implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtId;
    @FXML
    private TableView tblTipoCliente;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescripcion;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

    private ObservableList<TipoCliente> listaTipoCLiente;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaClientes(MouseEvent event) {
        escenarioPrincipal.mostrarAdministracion();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                limpiarCampos();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));
                break;
            case GUARDAR:
                TipoCliente registro = new TipoCliente();
                registro.setDescripcion(txtDescripcion.getText());

                if (registro.getDescripcion().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Por favor llene todos los campos de texto");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                    agregarTipoCliente();
                    cargarDatos();
                    desabilitarCampos();
                    limpiarCampos();
                    imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                    imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    cargarDatos();
                    operacion = Operaciones.NINGUNO;
                }
                break;
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case GUARDAR:
                limpiarCampos();
                desabilitarCampos();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                break;
            case NINGUNO:
                TipoCliente registro = new TipoCliente();
                registro.setDescripcion(txtDescripcion.getText());

                if (registro.getDescripcion().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea eliminar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Esta seguro que desea eliminar lo que selecciono?");

                    Optional<ButtonType> respuesta = alert.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        limpiarCampos();
                        eliminarTipoCliente();
                        cargarDatos();

                    }
                }
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    habilitarCampos();
                    btnEditar.setText("Actualzar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                }
                break;
            case ACTUALIZAR:
                TipoCliente registro = new TipoCliente();
                registro.setDescripcion(txtDescripcion.getText());

                if (registro.getDescripcion().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                    editarTipoCliente();
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                    desabilitarCampos();
                    cargarDatos();
                    limpiarCampos();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    operacion = Operaciones.NINGUNO;
                }
                break;
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        System.out.println(operacion);
        switch (operacion) {
            case NINGUNO: 
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteTipoCliente.jasper", "Reporte TipoCliente", parametros);
            
            case ACTUALIZAR:
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                limpiarCampos();
                desabilitarCampos();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                operacion = Operaciones.NINGUNO;
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));

        }
    }

    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));
            }
            System.out.println("Conexion exitosa ");
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaTipoCLiente = FXCollections.observableArrayList(lista);
        return listaTipoCLiente;
    }

    public void cargarDatos() {
        tblTipoCliente.setItems(getTipoCliente());
        colId.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId()));
            txtDescripcion.setText(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        }

    }

    private void agregarTipoCliente() {
        TipoCliente tipoCliente = new TipoCliente();
        tipoCliente.setDescripcion(txtDescripcion.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarTipoCliente(?)}");
            stmt.setString(1, tipoCliente.getDescripcion());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarTipoCliente() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarTipoCliente(?)}");

            stmt.setInt(1, ((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desabilitarCampos() {
        txtDescripcion.setEditable(false);
        txtId.setDisable(false);
    }

    public void habilitarCampos() {
        txtId.setDisable(false);
        txtDescripcion.setEditable(true);
    }

    public void limpiarCampos() {
        txtDescripcion.clear();
        txtId.clear();
    }

    private void editarTipoCliente() {
        TipoCliente registro = (TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarTipoCliente(?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDescripcion());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existeElementoSeleccionado() {
        if (tblTipoCliente.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

}
