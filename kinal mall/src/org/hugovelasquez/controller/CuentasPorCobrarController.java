/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.hugovelasquez.bean.Administracion;
import org.hugovelasquez.bean.Clientes;
import org.hugovelasquez.bean.CuentasPorCobrar;
import org.hugovelasquez.bean.Locales;
import org.hugovelasquez.system.Principal;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 30 jun. 2021
 * @time 14:37:30
 */
public class CuentasPorCobrarController implements Initializable {

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
    private TableView tblCuentasPorCobrar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNumeroFactura;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colMes;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colIdAdministracion;
    @FXML
    private TableColumn colIdCliente;
    @FXML
    private TableColumn colIdLocal;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private Spinner<Integer> spnAnio;

    private SpinnerValueFactory<Integer> valueFactoryAnio;
    @FXML
    private Spinner<Integer> spnMes;

    private SpinnerValueFactory<Integer> valueFactoryMes;
    @FXML
    private ComboBox cmbIdAdministracion;
    @FXML
    private ComboBox cmbIdCliente;
    @FXML
    private ComboBox cmvidLocal;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Locales> listaLocales;
    private ObservableList<CuentasPorCobrar> listaCuentasPorCobrar;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";
    @FXML
    private ComboBox cmbEstadoPago;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        valueFactoryAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        spnAnio.setValueFactory(valueFactoryAnio);

        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1);
        spnMes.setValueFactory(valueFactoryMes);

        ObservableList<String> listaOpciones = FXCollections.observableArrayList("PENDIENTE", "PAGADO");
        cmbEstadoPago.getItems().addAll(listaOpciones);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
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
                    break;
                case GUARDAR:

                    ArrayList<TextField> listaTextField = new ArrayList<>();
                    listaTextField.add(txtNumeroFactura);
                    listaTextField.add(txtValorNeto);

                    ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                    listaComboBox.add(cmbIdAdministracion);
                    listaComboBox.add(cmvidLocal);
                    listaComboBox.add(cmbIdCliente);
                    listaComboBox.add(cmbEstadoPago);

                    ArrayList<Spinner> listaSpinner = new ArrayList<>();

                    listaSpinner.add(spnMes);
                    listaSpinner.add(spnAnio);
                    if (validar(listaTextField, listaComboBox, listaSpinner)) {
                        if (validarDatos(txtNumeroFactura.getText())) {
                            if (validarValorNeto(txtValorNeto.getText())) {
                                agregarCuentasPorCobrar();
                                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
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
                            alert.setContentText("Los datos no son validos");
                            alert.setHeaderText(null);
                            alert.setTitle("Alerta");
                            alert.show();
                        }

                    } else {
                        //  JOptionPane.showMessageDialog(null, "Por favor llene todos los campos de texto", "KINAL MALL", JOptionPane.WARNING_MESSAGE);
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

    private void habilitarCampos() {
        txtId.setEditable(false);
        cmbEstadoPago.setDisable(false);
        txtNumeroFactura.setEditable(true);
        txtValorNeto.setEditable(true);
        cmbIdAdministracion.setDisable(false);
        cmbIdCliente.setDisable(false);
        cmvidLocal.setDisable(false);
        spnAnio.setDisable(false);
        spnMes.setDisable(false);
    }

    private void desactivarControles() {
        txtId.setEditable(false);

        txtNumeroFactura.setEditable(false);
        txtValorNeto.setEditable(false);
        cmbIdAdministracion.setDisable(true);
        cmbIdCliente.setDisable(true);
        cmvidLocal.setDisable(true);
        spnAnio.setDisable(true);
        spnMes.setDisable(true);
        cmbEstadoPago.setEditable(false);
    }

    public void limpiarControles() {
        txtId.clear();

        txtNumeroFactura.clear();
        txtValorNeto.clear();

        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);

        cmbEstadoPago.valueProperty().set(null);
        cmbIdAdministracion.valueProperty().set(null);
        cmbIdCliente.valueProperty().set(null);
        cmvidLocal.valueProperty().set(null);
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
                        eliminarCuentasPorCobrar();
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

    private void eliminarCuentasPorCobrar() {
        CuentasPorCobrar cuentasPorCobrar = ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem());

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorCobrar(?)}");
            pstmt.setInt(1, cuentasPorCobrar.getId());
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
                    alert.setContentText("Seleccione un elemento");
                    alert.show();
                }
            case ACTUALIZAR:
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtNumeroFactura);
                listaTextField.add(txtValorNeto);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbIdAdministracion);
                listaComboBox.add(cmvidLocal);
                listaComboBox.add(cmbIdCliente);
                listaComboBox.add(cmbEstadoPago);

                ArrayList<Spinner> listaSpinner = new ArrayList<>();

                listaSpinner.add(spnMes);
                listaSpinner.add(spnAnio);
                if (validar(listaTextField, listaComboBox, listaSpinner)) {
                    if (validarDatos(txtNumeroFactura.getText())) {
                        if (validarValorNeto(txtValorNeto.getText())) {
                            editarCuentasPorCobrar();
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
                            alert.setContentText("El valor neto no es valido");
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
                    //  JOptionPane.showMessageDialog(null, "Por favor llene todos los campos de texto", "KINAL MALL", JOptionPane.WARNING_MESSAGE);
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
    private void reporte(ActionEvent event) {
        System.out.println(operacion);
        switch (operacion) {
              case NINGUNO: 
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorCobrar.jasper", "Reporte CuentasPorCobrar", parametros);
                break;
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
    private void seleccionarElemento(MouseEvent event) {
        if (existeElementoSeleccionado()) {
            cmvidLocal.getSelectionModel().select(buscarLocales(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdLocal()));
            cmbIdCliente.getSelectionModel().select(buscarClientes(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdCliente()));
            cmbIdAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdAdministracion()));
            txtId.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            //txtEstadoPago.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago()));
            txtValorNeto.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            spnAnio.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio());
            spnMes.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            cmbEstadoPago.setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());

        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    @FXML
    private void mostrarMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarClientes();
    }

    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar() {
        ArrayList<CuentasPorCobrar> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorCobrar()}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new CuentasPorCobrar(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getInt("anio"),
                        rs.getInt("mes"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getString("estadoPago"),
                        rs.getInt("idAdministracion"),
                        rs.getInt("idCliente"),
                        rs.getInt("idLocal")
                ));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        listaCuentasPorCobrar = FXCollections.observableArrayList(lista);
        return listaCuentasPorCobrar;
    }

    public void cargarDatos() {
        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("id"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, BigDecimal>("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idAdministracion"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idCliente"));
        colIdLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idLocal"));
        cmbIdAdministracion.setItems(getAdministracion());
        cmbIdCliente.setItems(getClientes());
        cmvidLocal.setItems(getLocales());
        cmbIdAdministracion.setOpacity(1.0);
        cmbIdCliente.setOpacity(1.0);
        cmvidLocal.setOpacity(1.0);
        spnAnio.setOpacity(1.0);
        spnMes.setOpacity(1.0);
        cmbEstadoPago.setOpacity(1.0);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;

    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes()}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Conexion exitosa");

                lista.add(new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("idTipoCliente")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;

    }

    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox, ArrayList<Spinner> listaSpinner) {
        boolean respuesta = true;
        if (spnAnio.getValue() == null) {
            return false;
        }
        for (Spinner spinner : listaSpinner) {
            if (spinner.getValue() == null) {
                return false;
            }
        }
        if (cmbIdAdministracion.getSelectionModel().getSelectedItem() == null) {
            return false;
        }
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

    public boolean existeElementoSeleccionado() {
        if (tblCuentasPorCobrar.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    private Clientes buscarClientes(int id) {
        Clientes registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarClientes(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Clientes(rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("idTipoCliente"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    private Locales buscarLocales(int idd) {
        Locales registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarLocales(?)}");
            pstmt.setInt(1, idd);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Locales(
                        rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Locales(rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")));
            }
            System.out.println("Conexion exitosa ");
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }

    public void agregarCuentasPorCobrar() {
        CuentasPorCobrar registro = new CuentasPorCobrar();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setAnio(spnAnio.getValue());
        registro.setMes(spnMes.getValue());
        registro.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        registro.setEstadoPago(cmbEstadoPago.getValue().toString());
        registro.setIdAdministracion(((Administracion) cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
        registro.setIdCliente(((Clientes) cmbIdCliente.getSelectionModel().getSelectedItem()).getId());
        registro.setIdLocal(((Locales) cmvidLocal.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, registro.getNumeroFactura());
            pstmt.setInt(2, registro.getAnio());
            pstmt.setInt(3, registro.getMes());
            pstmt.setBigDecimal(4, registro.getValorNetoPago());
            pstmt.setString(5, registro.getEstadoPago());
            pstmt.setInt(6, registro.getIdAdministracion());
            pstmt.setInt(7, registro.getIdCliente());
            pstmt.setInt(8, registro.getIdLocal());
            pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

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

    public void editarCuentasPorCobrar() {
        ArrayList<TextField> listaTextField = new ArrayList<>();
        listaTextField.add(txtId);
        listaTextField.add(txtNumeroFactura);
        listaTextField.add(txtValorNeto);

        ArrayList<ComboBox> listaComboBox = new ArrayList<>();
        listaComboBox.add(cmvidLocal);
        listaComboBox.add(cmbIdAdministracion);
        listaComboBox.add(cmbIdCliente);
        listaComboBox.add(cmbEstadoPago);
        ArrayList<Spinner> listaSpinner = new ArrayList<>();

        if (validar(listaTextField, listaComboBox, listaSpinner)) {
            CuentasPorCobrar registro = new CuentasPorCobrar();
            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNumeroFactura(txtNumeroFactura.getText());
            registro.setAnio(spnAnio.getValue());
            registro.setMes(spnMes.getValue());
            registro.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
            registro.setEstadoPago(cmbEstadoPago.getValue().toString());
            //registro.setEstadoPago(txtEstadoPago.getText());
            registro.setIdAdministracion(((Administracion) cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
            registro.setIdCliente(((Clientes) cmbIdCliente.getSelectionModel().getSelectedItem()).getId());
            registro.setIdLocal(((Locales) cmvidLocal.getSelectionModel().getSelectedItem()).getId());

            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                pstmt.setInt(1, registro.getId());
                pstmt.setString(2, registro.getNumeroFactura());
                pstmt.setInt(3, registro.getAnio());
                pstmt.setInt(4, registro.getMes());
                pstmt.setBigDecimal(5, registro.getValorNetoPago());
                pstmt.setString(6, registro.getEstadoPago());
                pstmt.setInt(7, registro.getIdAdministracion());
                pstmt.setInt(8, registro.getIdCliente());
                pstmt.setInt(9, registro.getIdLocal());
                pstmt.executeQuery();
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
    //  "^\\d{1,8}([.]\\d{1,2})?$" 

}
