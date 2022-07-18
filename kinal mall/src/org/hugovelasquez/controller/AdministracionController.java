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
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.system.Principal;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hugovelasquez.bean.Administracion;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.JOptionPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 26 may. 2021
 * @time 15:38:04
 */
public class AdministracionController implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @FXML
    private void mostrarVistaDepartamentos(ActionEvent event) {
        escenarioPrincipal.mostrarDepartamentos();
    }

    @FXML
    private void mostrarVistaCargos(ActionEvent event) {
        escenarioPrincipal.mostratCargos();
    }

    @FXML
    private void mostrarVistaTipoClientes(ActionEvent event) {
        escenarioPrincipal.mostrarTipoCliente();
    }

    @FXML
    private void mostrarVistaLocales(ActionEvent event) {
        escenarioPrincipal.mostrarLocales();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Administracion> listaAdministracion;

    @FXML
    private TableView tblAdministracion;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colDireccion;

    @FXML
    private TableColumn colTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

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

    @FXML
    public void mostrarVistaMenuPrincipal() {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Administracion(resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                )
                );

            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;

    }

    public void cargarDatos() {
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));
    }

    private void agregarAdministracion() {
        Administracion registro = new Administracion();
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarAdministracion(?, ?)}");
            stmt.setString(1, registro.getDireccion());
            stmt.setString(2, registro.getTelefono());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarAdministracion() {
        Administracion registro = (Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarAdministracion(?, ?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDireccion());
            stmt.setString(3, registro.getTelefono());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarAdministracion() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarAdministracion(?)}");

            stmt.setInt(1, ((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId());

            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
            txtDireccion.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
        }
    }

    private void habilitarCampos() {

        txtId.setDisable(false);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }

    private void desabilitarCampos() {
        txtId.setDisable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtDireccion.clear();
        txtTelefono.clear();
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
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtTelefono);
                listaTextField.add(txtDireccion);

                if (validar(listaTextField)) {
                    if (validarTelefono(txtTelefono.getText())) {
                        agregarAdministracion();
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
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("El numero de telefono no es valido");
                        alert.setHeaderText(null);
                        alert.setTitle("Alerta");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Por favor llene todos los campos de texto");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
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
                Administracion registro = new Administracion();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());

                if ((registro.getDireccion().equals("")) && (registro.getTelefono().equals(""))) {
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
                        eliminarAdministracion();
                        cargarDatos();

                    }
                }

                break;
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        System.out.println(operacion);

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
                Administracion registro = new Administracion();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());

                if ((registro.getDireccion().equals("")) && (registro.getTelefono().equals(""))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.setHeaderText(null);
                    alert.setTitle("Alerta");
                    alert.show();
                } else {

                    System.out.println(operacion);
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                    editarAdministracion();
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
       
        switch (operacion) {
            
            case NINGUNO: 
                if(existeElementoSeleccionado()){
                    Map parametros = new HashMap();
                    int idAdministracion = (((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId());
                    parametros.put("ID", idAdministracion);
                    GenerarReporte.getInstance().mostrarReporte("ReporteAdministracionEmpleadosPorId.jasper", "Reporte de administracion/empleados", parametros);
                }else{
                  Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper", "Reporte Administracion", parametros);
              }
                break;
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

    private boolean existeElementoSeleccionado() {
        if (tblAdministracion.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean validar(ArrayList<TextField> listadoCampos) {
        boolean respuesta = true;

        for (TextField campoTexto : listadoCampos) {
            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

}
