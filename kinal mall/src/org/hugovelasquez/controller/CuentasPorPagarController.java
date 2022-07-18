/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.CuentasPorPagar;
import org.hugovelasquez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hugovelasquez.db.Conexion;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.util.converter.LocalDateStringConverter;
import org.hugovelasquez.bean.Administracion;
import org.hugovelasquez.bean.Proveedores;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 7 jul. 2021
 * @time 22:59:56
 */
public class CuentasPorPagarController implements Initializable {

    private Principal escenarioPrincipal;
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
    private TableColumn colId;
    @FXML
    private TableColumn colNumeroFactura;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colIdAdministracion;
    @FXML
    private TableColumn colIdProveedor;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private ComboBox cmbIdAdministracion;
    @FXML
    private ComboBox cmbIdProveedor;
    @FXML
    private JFXDatePicker tpFechaLimitePago;
    @FXML
    private TableView tblCuentasPorPagar;

    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedores> listaProveedores;
    @FXML
    private TableColumn colFechaLimitePago;
    @FXML
    private TextField txtEstadoPago;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        tpFechaLimitePago.setEditable(false);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void habilitarCampos() {
        txtId.setEditable(false);
        txtEstadoPago.setEditable(true);
        txtNumeroFactura.setEditable(true);
        txtValorNeto.setEditable(true);
        cmbIdAdministracion.setDisable(false);
        cmbIdProveedor.setDisable(false);
        tpFechaLimitePago.setDisable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtEstadoPago.clear();
        txtNumeroFactura.clear();
        txtValorNeto.clear();
        tpFechaLimitePago.getEditor().clear();
        cmbIdAdministracion.valueProperty().set(null);
        cmbIdProveedor.valueProperty().set(null);
    }

    public void desactivarControles() {
        tpFechaLimitePago.setDisable(true);
        txtId.setEditable(false);
        txtEstadoPago.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtValorNeto.setEditable(false);
        cmbIdAdministracion.setDisable(true);
        cmbIdProveedor.setDisable(true);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        {
            switch (operacion) {
                case NINGUNO:
                    habilitarCampos();
                    limpiarControles();
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
                    listaTextField.add(txtNumeroFactura);
                    listaTextField.add(txtValorNeto);
                    listaTextField.add(txtEstadoPago);

                    ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                    listaComboBox.add(cmbIdAdministracion);
                    listaComboBox.add(cmbIdProveedor);

                    ArrayList<JFXDatePicker> listaJFXDatePicker = new ArrayList<>();
                    listaJFXDatePicker.add(tpFechaLimitePago);

                    if (validar(listaTextField, listaComboBox, listaJFXDatePicker)) {
                        if (validarDatos(txtNumeroFactura.getText())) {
                            if (validarValorNeto(txtValorNeto.getText())) {
                                agregarCuentasPorPagar();
                                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));

                                cargarDatos();
                                desactivarControles();
                                limpiarControles();
                                btnNuevo.setText("Nuevo");
                                btnEliminar.setText("Eliminar");
                                btnEditar.setDisable(false);
                                btnReporte.setDisable(false);
                                operacion = Operaciones.NINGUNO;
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("El valor neto no es valido");
                                alert.setHeaderText(null);
                                alert.setTitle("Alerta");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("La factura no es valida");
                            alert.setHeaderText(null);
                            alert.setTitle("Alerta");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Por favor llene todos los campos de texto y fecha");
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
                limpiarControles();
                desactivarControles();
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
                        limpiarControles();
                        eliminarCuentasPorPagar();
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
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));
                    break;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo un elemento");
                    alert.show();
                }
            case ACTUALIZAR:
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtId);
                listaTextField.add(txtEstadoPago);
                listaTextField.add(txtNumeroFactura);
                listaTextField.add(txtValorNeto);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbIdProveedor);
                listaComboBox.add(cmbIdAdministracion);

                ArrayList<JFXDatePicker> listaJFXDatePicker = new ArrayList<>();
                listaJFXDatePicker.add(tpFechaLimitePago);
                if (existeElementoSeleccionado()) {
                    if (validar(listaTextField, listaComboBox, listaJFXDatePicker)) {
                        editarCuentasPorPagar();
                        cargarDatos();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                        btnEditar.setText("Editar");
                        btnReporte.setText("Reporte");
                        operacion = Operaciones.NINGUNO;
                        imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                        imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Kinal mall");
                        alert.setHeaderText(null);
                        alert.setContentText("No puede dejar ningun texto vacio");
                        alert.show();

                    }
                }
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        System.out.println(operacion);
        switch (operacion) {
            case NINGUNO: 
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorPagar.jasper", "Reporte  CuentasPorPagar", parametros);
            case ACTUALIZAR:
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                operacion = Operaciones.NINGUNO;
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));

        }
    }

    @FXML
    private void mostrarMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarProveedores();
    }

    @FXML
    private void seleccionarElemento(MouseEvent event) {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            tpFechaLimitePago.setValue(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago().toLocalDate());
            txtValorNeto.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            txtEstadoPago.setText(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago());

            cmbIdAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getIdAdministracion()));
            cmbIdProveedor.getSelectionModel().select(buscarProveedores(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getIdProveedor()));
        }
    }

    public void cargarDatos() {

        cmbIdAdministracion.setItems(getAdministracion());
        cmbIdProveedor.setItems(getProveedores());
        tblCuentasPorPagar.setItems(getCuentasPorPagar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("id"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("estadoPago"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, BigDecimal>("valorNetoPago"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("idAdministracion"));
        colIdProveedor.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("idProveedor"));
    }

    public ObservableList<CuentasPorPagar> getCuentasPorPagar() {
        ArrayList<CuentasPorPagar> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorPagar()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new CuentasPorPagar(rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getDate("fechaLimitePago"),
                        rs.getString("estadoPago"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getInt("idAdministracion"),
                        rs.getInt("idProveedor")));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        listaCuentasPorPagar = FXCollections.observableArrayList(lista);
        return listaCuentasPorPagar;
    }

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();

        try {
            PreparedStatement stmt;
            //CallableStatement stmt;  
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;

    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Proveedores(rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra")));
            }
            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        listaProveedores = FXCollections.observableArrayList(lista);
        return listaProveedores;
    }

    private Administracion buscarAdministracion(int id) {
        Administracion registro = null;

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Administracion(rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("telefono"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

    private Proveedores buscarProveedores(int id) {
        Proveedores registro = null;

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarProveedores(?)");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                registro = new Proveedores(rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"));
            }
            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox, ArrayList<JFXDatePicker> listaDatePicker) {

        boolean respuesta = true;

        /*if(tpFechaLimitePago.getValue() == null) {
            return false;
        }*/
        for (JFXDatePicker jFXDatePicker : listaDatePicker) {
            if (jFXDatePicker.getValue() == null) {
                return false;
            }
        }
        /*if (cmbIdAdministracion.getSelectionModel().getSelectedItem() == null) {
            return false;
        }*/
        for (ComboBox combobox : listaComboBox) {
            if (combobox.getSelectionModel().getSelectedItem() == null) {
                return false;
            }
        }
        for (TextField campoTexto : listadoCampos) {
            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

    public boolean validarDatos(String numero) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();

    }

    public boolean validarValorNeto(String numero) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();

    }

    public boolean existeElementoSeleccionado() {
        if (tblCuentasPorPagar.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void agregarCuentasPorPagar() {
        CuentasPorPagar registro = new CuentasPorPagar();

        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(Date.valueOf(tpFechaLimitePago.getValue()));
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        registro.setIdAdministracion(((Administracion) cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
        registro.setIdProveedor(((Proveedores) cmbIdProveedor.getSelectionModel().getSelectedItem()).getId());
        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorPagar(?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, registro.getNumeroFactura());
            pstmt.setDate(2, registro.getFechaLimitePago());
            pstmt.setString(3, registro.getEstadoPago());
            pstmt.setBigDecimal(4, registro.getValorNetoPago());
            pstmt.setInt(5, registro.getIdAdministracion());
            pstmt.setInt(6, registro.getIdProveedor());
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

    public void editarCuentasPorPagar() {

        CuentasPorPagar registro = new CuentasPorPagar();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(Date.valueOf(tpFechaLimitePago.getValue()));

        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        registro.setIdAdministracion(((Administracion) cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
        registro.setIdProveedor(((Proveedores) cmbIdProveedor.getSelectionModel().getSelectedItem()).getId());
        PreparedStatement pstmt = null;

        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorPagar(?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setInt(1, registro.getId());
            pstmt.setString(2, registro.getNumeroFactura());
            pstmt.setDate(3, registro.getFechaLimitePago());
            pstmt.setString(4, registro.getEstadoPago());
            pstmt.setBigDecimal(5, registro.getValorNetoPago());
            pstmt.setInt(6, registro.getIdAdministracion());
            pstmt.setInt(7, registro.getIdProveedor());
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

    private void eliminarCuentasPorPagar() {
        CuentasPorPagar cuentasPorPagar = ((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorPagar(?)}");
            pstmt.setInt(1, cuentasPorPagar.getId());
            pstmt.executeQuery();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kinal mall");
            alert.setHeaderText(null);
            alert.setContentText("Registro eliminado correctamente");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
            }
        }

    }
}
