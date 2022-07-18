/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Proveedores;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.system.Principal;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.ButtonType;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 1 jul. 2021
 * @time 19:45:26
 */
public class ProveedoresController implements Initializable {

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
    private TableView tblProveedores;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colServicioPrestado;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSandoContra;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtServicioPrestado;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoContra;
    private Principal escenarioPrincipal;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

    private ObservableList<Proveedores> listaProveedores;
    @FXML
    private TextField txtSaldoLiquido;

    @FXML
    private void mostrarVistaCuentasPorPagar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorPagar();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        {
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
                    listaTextField.add(txtNit);
                    listaTextField.add(txtServicioPrestado);
                    listaTextField.add(txtSaldoContra);
                    listaTextField.add(txtSaldoFavor);
                    listaTextField.add(txtDireccion);
                    listaTextField.add(txtTelefono);

                    if (validar(listaTextField)) {
                        if (validarTelefono(txtTelefono.getText())) {
                            if (validarDatos(txtNit.getText())) {
                                if(validarSaldoContra(txtSaldoContra.getText())){
                                    if(validarSaldoFavor(txtSaldoFavor.getText())){
                                agregarProveedores();
                                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));

                                cargarDatos();
                                desabilitarCampos();
                                limpiarCampos();
                                btnNuevo.setText("Nuevo");
                                btnEliminar.setText("Eliminar");
                                btnEditar.setDisable(false);
                                btnReporte.setDisable(false);
                                operacion = Operaciones.NINGUNO;
                             }else{
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("El saldo a favor no es valido");
                                alert.setHeaderText(null);
                                alert.setTitle("Alerta");
                                alert.show();    
                                    }
                                    } else {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("El saldo en contra no es valido");
                                alert.setHeaderText(null);
                                alert.setTitle("Alerta");
                                alert.show();
                                }
                                } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Los datos no son validos");
                                alert.setHeaderText(null);
                                alert.setTitle("Alerta");
                                alert.show();

                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Los datos no son validos");
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

                if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Esta seguro que desea eliminar lo que selecciono?");

                    Optional<ButtonType> respuesta = alert.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        limpiarCampos();
                        eliminarProveedores();
                        cargarDatos();

                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea eliminar");
                    alert.show();
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
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                    break;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea editar");
                    alert.show();
                }
            case ACTUALIZAR:
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtNit);
                listaTextField.add(txtServicioPrestado);
                listaTextField.add(txtSaldoContra);
                listaTextField.add(txtSaldoFavor);
                listaTextField.add(txtDireccion);
                listaTextField.add(txtTelefono);

                if (validar(listaTextField)) {
                    if (validarTelefono(txtTelefono.getText())) {
                        if (validarDatos(txtNit.getText())) {
                            if(validarSaldoFavor(txtSaldoFavor.getText())){
                                if(validarSaldoContra(txtSaldoContra.getText())){
                            editarProveedores();
                            cargarDatos();
                            limpiarCampos();
                            desabilitarCampos();
                            btnNuevo.setDisable(false);
                            btnEliminar.setDisable(false);
                            btnEditar.setText("Editar");
                            btnReporte.setText("Reporte");
                            operacion = Operaciones.NINGUNO;
                            imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                            imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                        } else {
                                  Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("El saldo en contra no es valido");
                            alert.setHeaderText(null);
                            alert.setTitle("Alerta");
                            alert.show();    
                                }
                                }else {
                              Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("El saldo a favor no es valido");
                            alert.setHeaderText(null);
                            alert.setTitle("Alerta");
                            alert.show();  
                            }
                            } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Los datos no son validos");
                            alert.setHeaderText(null);
                            alert.setTitle("Alerta");
                            alert.show();

                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Los datos no son validos");
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

        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        System.out.println(operacion);
        switch (operacion) {
            case NINGUNO:
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteProveedores.jasper", PAQUETE_IMAGE, parametros);
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
    private void seleccionarElemento(MouseEvent event) {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
            txtNit.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit());
            txtDireccion.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
            txtServicioPrestado.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado());
            txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtTelefono.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono()));
            txtSaldoLiquido.setText(String.valueOf(calcularSaldoLiquido()));
        }
    }

    public void habilitarCampos() {
        txtId.setDisable(false);
        txtNit.setEditable(true);
        txtDireccion.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtTelefono.setEditable(true);
    }

    public void limpiarCampos() {
        txtId.clear();
        txtNit.clear();
        txtDireccion.clear();
        txtServicioPrestado.clear();
        txtSaldoContra.clear();
        txtSaldoFavor.clear();
        txtTelefono.clear();
    }

    public void desabilitarCampos() {
        txtId.setDisable(false);
        txtNit.setEditable(false);
        txtDireccion.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtSaldoFavor.setDisable(false);
        txtTelefono.setEditable(false);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores()}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Proveedores(
                        rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaProveedores = FXCollections.observableArrayList(lista);
        return listaProveedores;
    }

    public void cargarDatos() {

        tblProveedores.setItems(getProveedores());
        colId.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("id"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nit"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccion"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoFavor"));
        colSandoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoContra"));

    }

    public boolean existeElementoSeleccionado() {
        if (tblProveedores.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
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

    public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean validarDatos(String numero) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);
        

        return matcher.matches();
    }
    public boolean validarSaldoContra(String numero) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);
        

        return matcher.matches();
    }
    public boolean validarSaldoFavor(String numero) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);
        

        return matcher.matches();
    }

    public void agregarProveedores() {
        Proveedores registro = new Proveedores();

        registro.setNit(txtNit.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        registro.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarProveedores(?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, registro.getNit());
            pstmt.setString(2, registro.getServicioPrestado());
            pstmt.setString(3, registro.getTelefono());
            pstmt.setString(4, registro.getDireccion());
            pstmt.setBigDecimal(5, registro.getSaldoFavor());
            pstmt.setBigDecimal(6, registro.getSaldoContra());
            pstmt.executeQuery();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarProveedores() {

        ArrayList<TextField> listaTextField = new ArrayList<>();
        listaTextField.add(txtId);
        listaTextField.add(txtNit);
        listaTextField.add(txtServicioPrestado);
        listaTextField.add(txtTelefono);
        listaTextField.add(txtDireccion);
        listaTextField.add(txtSaldoFavor);
        listaTextField.add(txtSaldoContra);

        if (validar(listaTextField)) {
            Proveedores registro = new Proveedores();
            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNit(txtNit.getText());
            registro.setServicioPrestado(txtServicioPrestado.getText());
            registro.setTelefono(txtTelefono.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
            registro.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?)}");
                pstmt.setInt(1, registro.getId());
                pstmt.setString(2, registro.getNit());
                pstmt.setString(3, registro.getServicioPrestado());
                pstmt.setString(4, registro.getTelefono());
                pstmt.setString(5, registro.getDireccion());
                pstmt.setBigDecimal(6, registro.getSaldoFavor());
                pstmt.setBigDecimal(7, registro.getSaldoContra());
                pstmt.executeQuery();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Llene todos los campos de texto");
            alert.setHeaderText(null);
            alert.setTitle("Alerta");
            alert.show();
        }
    }

    public void eliminarProveedores() {
        Proveedores proveedores = ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem());

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarProveedores(?)}");
            pstmt.setInt(1, proveedores.getId());
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

    public BigDecimal calcularSaldoLiquido() {

        BigDecimal num1 = (((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra());
        BigDecimal num2 = (((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor());

        BigDecimal resultado = num2.subtract(num1);

        txtSaldoLiquido.setText(String.valueOf(resultado));

        return resultado;
    }
}
