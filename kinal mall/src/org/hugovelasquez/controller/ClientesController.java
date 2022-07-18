/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import com.mysql.cj.jdbc.result.ResultSetImpl;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Administracion;
import org.hugovelasquez.bean.Clientes;
import org.hugovelasquez.bean.Locales;
import org.hugovelasquez.bean.TipoCliente;
import org.hugovelasquez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 9 jun. 2021
 * @time 14:52:08
 */
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.report.GenerarReporte;

public class ClientesController implements Initializable {

    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

    Principal escenarioPrincipal;
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
    private TextField txtApellido;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colTipoCliente;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox cmbTipoCliente;
    @FXML
    private TableView tblCliente;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @FXML
    private void mostrarTipoCliente(ActionEvent event) {
        escenarioPrincipal.mostrarTipoCliente();
    }

    @FXML
    private void mostrarVistaCuentasPorCobrar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorCobrar();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Clientes> listaCliente;
    private ObservableList<TipoCliente> listaTipoCLiente;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes()}");
            rs = pstmt.executeQuery();
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
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
        listaCliente = FXCollections.observableArrayList(lista);
        return listaCliente;

    }

    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCLiente()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
        listaTipoCLiente = FXCollections.observableArrayList(lista);
        return listaTipoCLiente;
    }

    public void cargarDatos() {
        tblCliente.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colTipoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idTipoCliente"));

        cmbTipoCliente.setItems(getTipoCliente());
    }

    private TipoCliente buscarTipoCliente(int id) {
        TipoCliente registro = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarTipoCliente(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new TipoCliente(rs.getInt("id"), rs.getString("descripcion"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }

        return registro;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
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
                listaTextField.add(txtNombre);
                listaTextField.add(txtApellido);
                listaTextField.add(txtDireccion);
                listaTextField.add(txtTelefono);
                listaTextField.add(txtEmail);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);

                if (validar(listaTextField, listaComboBox)) {

                    if (validarTelefono(txtTelefono.getText())) {
                        if (validarEmail(txtEmail.getText())) {
                            agregarClientes();
                            cargarDatos();
                            desactivarControles();
                            limpiarControles();
                            btnNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            btnEditar.setDisable(false);
                              imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                            btnReporte.setDisable(false);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Kinal mall");
                            alert.setHeaderText(null);
                            alert.setContentText("El correo electronico es invalido");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Kinal mall");
                        alert.setHeaderText(null);
                        alert.setContentText("El numero es invalido");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Llene todos los campos de texto");
                    alert.show();
                }

                break;

        }

    }

    public void habilitarCampos() {
        txtId.setDisable(false);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtEmail.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);

    }

    public void limpiarCampos() {
        txtId.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        cmbTipoCliente.valueProperty().set(null);

    }

    public void desabilitarCampos() {
        txtId.setDisable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtEmail.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
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
                Clientes registro = new Clientes();
                registro.setNombres(txtNombre.getText());
                registro.setApellidos(txtApellido.getText());
                registro.setTelefono(txtTelefono.getText());
                registro.setDireccion(txtDireccion.getText());
                registro.setEmail(txtEmail.getText());
                if (existeElementoSeleccionado()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Esta seguro que desea eliminar lo que selecciono?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarClietes();
                        limpiarCampos();
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
                    alert.setContentText("Seleccione un elemento");
                    alert.show();
                }
            case ACTUALIZAR:
          
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtNombre);
                listaTextField.add(txtApellido);
                listaTextField.add(txtDireccion);
                listaTextField.add(txtTelefono);
                listaTextField.add(txtEmail);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);

                if (validar(listaTextField, listaComboBox)) {
                   
                    if (validarTelefono(txtTelefono.getText())) {
                        if (validarEmail(txtEmail.getText())) {
                            editarClientes();
                            cargarDatos();
                            desactivarControles();
                            limpiarControles();
                            btnNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            btnEditar.setDisable(false);
                            imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                            btnEditar.setText("Editar");
                        btnReporte.setText("Reporte");
                            imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                            btnReporte.setDisable(false);
                             btnNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Kinal mall");
                            alert.setHeaderText(null);
                            alert.setContentText("El correo electronico es invalido");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Kinal mall");
                        alert.setHeaderText(null);
                        alert.setContentText("El numero es invalido");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Llene todos los campos de texto");
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
                GenerarReporte.getInstance().mostrarReporte("ReporteClientes.jasper", "Reporte Clientes", parametros);
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

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        PreparedStatement stmt = null;
        try {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;

    }

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Locales(rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtApellido.setEditable(false);
        txtEmail.setEditable(false);
        txtNombre.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        cmbTipoCliente.setDisable(true);
    }

    public void activarControles() {
        txtId.setEditable(true);
        txtApellido.setEditable(true);
        txtEmail.setEditable(true);
        txtNombre.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        cmbTipoCliente.setDisable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        cmbTipoCliente.valueProperty().set(null);
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getNombres());
            txtTelefono.setText(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getTelefono());
            txtEmail.setText(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getEmail());
            txtApellido.setText(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getApellidos());
            txtDireccion.setText(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getDireccion());
            cmbTipoCliente.getSelectionModel().select(buscarTipoCliente(((Clientes) tblCliente.getSelectionModel().getSelectedItem()).getIdTipoCliente()));
        }

    }

    public boolean existeElementoSeleccionado() {
        if (tblCliente.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void agregarClientes() {

        Clientes registro = new Clientes();
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellido.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setIdTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtApellido.getText());
            stmt.setString(3, txtTelefono.getText());
            stmt.setString(4, txtDireccion.getText());
            stmt.setString(5, txtEmail.getText());
            stmt.setInt(6, ((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1452) {
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }

    }

    public void editarClientes() {
        ArrayList<TextField> listaTextField = new ArrayList<>();
        listaTextField.add(txtId);
        listaTextField.add(txtNombre);
        listaTextField.add(txtApellido);
        listaTextField.add(txtDireccion);
        listaTextField.add(txtTelefono);
        listaTextField.add(txtEmail);

        ArrayList<ComboBox> listaComboBox = new ArrayList<>();
        listaComboBox.add(cmbTipoCliente);

        if (validar(listaTextField, listaComboBox)) {

            Clientes registro = new Clientes();

            registro.setId(Integer.parseInt(txtId.getText()));
            registro.setNombres(txtNombre.getText());
            registro.setApellidos(txtApellido.getText());
            registro.setTelefono(txtTelefono.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setEmail(txtEmail.getText());
            registro.setIdTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
            PreparedStatement pstmt = null;

            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
                pstmt.setInt(1, registro.getId());
                pstmt.setString(2, registro.getNombres());
                pstmt.setString(3, registro.getApellidos());
                pstmt.setString(4, registro.getTelefono());
                pstmt.setString(5, registro.getDireccion());
                pstmt.setString(6, registro.getEmail());
                pstmt.setInt(7, registro.getIdTipoCliente());

                pstmt.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
            }

        }

    }

    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox) {
        boolean respuesta = true;

        if (cmbTipoCliente.getSelectionModel().getSelectedItem() == null) {
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

    public void eliminarClietes() {

        Clientes clientes = ((Clientes) tblCliente.getSelectionModel().getSelectedItem());
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");
            pstmt.setInt(1, clientes.getId());
            pstmt.executeQuery();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kinal mall");
            alert.setHeaderText(null);
            alert.setContentText("Registro eliminado correctamente");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean validarDatos(String numero) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();

    }

}
