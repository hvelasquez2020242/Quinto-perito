/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Departamentos;
import org.hugovelasquez.system.Principal;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.sql.ResultSet;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 16 jun. 2021
 * @time 11:07:36
 */
public class DepartamentosController implements Initializable {

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
    private TextField txtNombre;
    @FXML
    private TextField txtId;
    @FXML
    private TableView tblDepartamentos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

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
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                Departamentos registro = new Departamentos();
                registro.setNombre(txtNombre.getText());

                if (registro.getNombre().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Llene todos los recuadros");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                    agregarDepartamentos();
                    cargarDatos();
                    desabilitarCampos();
                    limpiarCampos();
                    imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
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

    private void habilitarCampos() {

        txtId.setDisable(false);
        txtNombre.setEditable(true);
    }

    private void desabilitarCampos() {
        txtId.setDisable(false);
        txtNombre.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();

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
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                Departamentos registro = new Departamentos();
                registro.setNombre(txtNombre.getText());

                if (registro.getNombre().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que quiere eliminar");
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
                        eliminarDepartamentos();
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
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                }
                break;
            case ACTUALIZAR:
                Departamentos registro = new Departamentos();
                registro.setNombre(txtNombre.getText());

                if (registro.getNombre().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {
                    editarDepartamentos();
                    desabilitarCampos();
                    cargarDatos();
                    limpiarCampos();
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
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
                GenerarReporte.getInstance().mostrarReporte("ReporteDepartamentos.jasper", "Reporte Departamentos", parametros);
            case ACTUALIZAR:
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                limpiarCampos();
                desabilitarCampos();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                operacion = Operaciones.NINGUNO;

        }
    }

    @FXML
    private void mostrarMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarAdministracion();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Departamentos> listaDepartamentos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void cargarDatos() {
        tblDepartamentos.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombre"));

    }

    public ObservableList<Departamentos> getDepartamentos() {
        ArrayList<Departamentos> listado = new ArrayList<Departamentos>();

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDepartamentos()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                listado.add(new Departamentos(resultado.getInt("id"),
                        resultado.getString("nombre")));
            }
            resultado.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(listado);
        return listaDepartamentos;

    }

    private void agregarDepartamentos() {
        Departamentos registro = new Departamentos();
        registro.setNombre(txtNombre.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDepartamentos(?)}");
            stmt.setString(1, registro.getNombre());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarDepartamentos() {
        Departamentos registro = (Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombre(txtNombre.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarDepartamentos(?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNombre());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarDepartamentos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDepartamentos(?)}");
            stmt.setInt(1, ((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getId());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getNombre());

        }
    }

    public boolean existeElementoSeleccionado() {
        if (tblDepartamentos.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

}
