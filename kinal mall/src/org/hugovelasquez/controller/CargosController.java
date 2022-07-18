/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.system.Principal;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import org.hugovelasquez.bean.Cargos;
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
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 10 jul. 2021
 * @time 23:39:03
 */
public class CargosController implements Initializable {

    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView tblCargos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    private Principal escenarioPrincipal;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Cargos> listaCargos;

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }
    
    public void habilitarCampos() {
        txtId.setDisable(false);
        txtNombre.setEditable(true);
    }
    public void limpiarCampos() {
        txtNombre.clear();
        txtId.clear();
    }
    public void desabilitarCampos() {
        txtNombre.setEditable(false);
        txtId.setDisable(false);
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
                Cargos registro = new Cargos();
                registro.setNombre(txtNombre.getText());

                if (registro.getNombre().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Llene el campo del nombre");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                    agregarCargos();
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
                Cargos registro = new Cargos();
                registro.setNombre(txtNombre.getText());

                if (registro.getNombre().equals("")) {
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

                    Optional <ButtonType> respuesta = alert.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        limpiarCampos();
                        elimiarCargos();
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
                Cargos registro = new Cargos();
                registro.setNombre(txtNombre.getText());

                if (registro.getNombre().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                    editarCargos();
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
            case NINGUNO : 
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCargos.jasper", "Reporte Cargos", parametros);
                
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
    @FXML
    private void mostrarMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarAdministracion();
    }

    @FXML
    private void seleccionarElemento(MouseEvent event) {
        if(existeElementoSeleccionado()){
        txtId.setText(String.valueOf(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getId()));
        txtNombre.setText(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getNombre());
           }
    }

    public void cargarDatos() {
        tblCargos.setItems(getCargos());
        colId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombre"));

    }
    

    public ObservableList<Cargos> getCargos() {
        ArrayList<Cargos> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Cargos(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
        listaCargos = FXCollections.observableArrayList(lista);
        return listaCargos;
    }

    public void agregarCargos() {
        Cargos registro = new Cargos();
//        registro.setId(Integer.valueOf(txtId.getText()));
        registro.setNombre(txtNombre.getText());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCargos(?)}");
            pstmt.setString(1, registro.getNombre());
            pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void editarCargos() {
        Cargos registro = new Cargos();
        registro.setId(Integer.valueOf(txtId.getText()));
        registro.setNombre(txtNombre.getText());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCargos(?, ?)}");
            pstmt.setInt(1, registro.getId());
            pstmt.setString(2, registro.getNombre());
            pstmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void elimiarCargos() {
        Cargos cargos = ((Cargos) tblCargos.getSelectionModel().getSelectedItem());
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCargos(?)}");
            pstmt.setInt(1, cargos.getId());
            pstmt.executeQuery();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kinal mall");
            alert.setHeaderText(null);
            alert.setContentText("Registro eliminado correctamente");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public boolean existeElementoSeleccionado() {
        if (tblCargos.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }
}
