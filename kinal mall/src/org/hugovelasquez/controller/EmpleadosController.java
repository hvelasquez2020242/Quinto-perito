/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Empleados;
import org.hugovelasquez.system.Principal;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.hugovelasquez.db.Conexion;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.hugovelasquez.bean.Administracion;
import org.hugovelasquez.bean.Cargos;
import org.hugovelasquez.bean.Departamentos;
import org.hugovelasquez.bean.Horarios;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 14 jul. 2021
 * @time 11:53:54
 */
public class EmpleadosController implements Initializable {

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
    private TableColumn colId;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colFechadeContratacion;
    @FXML
    private TableColumn colSueldo;
    @FXML
    private TableColumn colIdDepartamento;
    @FXML
    private TableColumn colIdHorario;
    @FXML
    private TableColumn colIdAdministracion;
    @FXML
    private TableColumn colIdCargos;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtSueldo;
    @FXML
    private ComboBox cmbIdDepartamento;
    @FXML
    private ComboBox cmbHorario;
    @FXML
    private ComboBox cmbIdAdministracion;
    @FXML
    private ComboBox cmbCargos;
    @FXML
    private TableView tblEmpleados;
    private ObservableList<Empleados> listaEmpleados;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Cargos> listaCargos;
    private ObservableList<Horarios> listaHorarios;
    private ObservableList<Departamentos> listaDepartamentos;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";
    @FXML
    private JFXDatePicker drFechaContratacion;

    @FXML
    private void mostrarMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    @FXML
    private void mostrarVistaHorarios(ActionEvent event) {
        escenarioPrincipal.mostrarHorarios();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;
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
                drFechaContratacion.setEditable(false);

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
                    imgEliminar.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));

                    break;
                case GUARDAR:

                    ArrayList<TextField> listaTextField = new ArrayList<>();
                    listaTextField.add(txtNombre);
                    listaTextField.add(txtApellidos);
                    listaTextField.add(txtEmail);
                    listaTextField.add(txtTelefono);
                    listaTextField.add(txtSueldo);

                    ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                    listaComboBox.add(cmbIdAdministracion);
                    listaComboBox.add(cmbCargos);
                    listaComboBox.add(cmbHorario);
                    listaComboBox.add(cmbIdDepartamento);
                    
                    ArrayList<JFXDatePicker> listaDatePicker = new ArrayList<>();
                    listaDatePicker.add(drFechaContratacion);

                    if (validar(listaTextField, listaComboBox, listaDatePicker)) {
                        if (validarDatos(txtSueldo.getText())) {
                            if (validarTelefono(txtTelefono.getText())) {
                                if (validarEmail(txtEmail.getText())) {
                                    agregarEmpleados();
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
                                    alert.setContentText("El email no es valido");
                                    alert.setHeaderText(null);
                                    alert.setTitle("Alerta");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("El numero de telefono no es valido");
                                alert.setHeaderText(null);
                                alert.setTitle("Alerta");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("El sueldo no es valido");
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
                        eliminarEmpleados();
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
                listaTextField.add(txtNombre);
                listaTextField.add(txtApellidos);
                listaTextField.add(txtEmail);
                listaTextField.add(txtTelefono);
                listaTextField.add(txtSueldo);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbIdAdministracion);
                listaComboBox.add(cmbCargos);
                listaComboBox.add(cmbHorario);
                listaComboBox.add(cmbIdDepartamento);
                
                ArrayList<JFXDatePicker> listaDatePicker = new ArrayList<>();
                listaDatePicker.add(drFechaContratacion);

                //ArrayList<JFXDatePicker> listaJFXDatePicker = new ArrayList<>();
                //listaJFXDatePicker.add(tpFechaLimitePago);
                if (existeElementoSeleccionado()) {
                    if (validar(listaTextField, listaComboBox, listaDatePicker)) {
                        if (validarEmail(txtEmail.getText())) {
                            if (validarTelefono(txtTelefono.getText())) {
                                if (validarDatos(txtSueldo.getText())) {
                                    editarEmpleados();
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
                                    alert.setContentText("El sueldo no es valido");
                                    alert.setHeaderText(null);
                                    alert.setTitle("Alerta");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("El numero de telefono no es valido");
                                alert.setHeaderText(null);
                                alert.setTitle("Alerta");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("El email no es valido");
                            alert.setHeaderText(null);
                            alert.setTitle("Alerta");
                            alert.show();
                        }
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
                GenerarReporte.getInstance().mostrarReporte("ReporteEmpleados.jasper", "Reporte Empleados", parametros);
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

    public boolean existeElementoSeleccionado() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void desactivarControles() {
        drFechaContratacion.setDisable(true);
        txtId.setEditable(false);
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtEmail.setEditable(false);
        txtTelefono.setEditable(false);
        txtSueldo.setEditable(false);
        cmbCargos.setDisable(true);
        cmbHorario.setDisable(true);
        cmbIdAdministracion.setDisable(true);
        cmbIdDepartamento.setDisable(true);

    }

    public void habilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        drFechaContratacion.setDisable(false);
        txtEmail.setEditable(true);
        txtTelefono.setEditable(true);
        txtSueldo.setEditable(true);
        cmbCargos.setDisable(false);
        cmbHorario.setDisable(false);
        cmbIdAdministracion.setDisable(false);
        cmbIdDepartamento.setDisable(false);

    }

    public void limpiarControles() {
        txtId.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtSueldo.clear();
        drFechaContratacion.getEditor().clear();
        cmbCargos.valueProperty().set(null);
        cmbHorario.valueProperty().set(null);
        cmbIdAdministracion.valueProperty().set(null);
        cmbIdDepartamento.valueProperty().set(null);

    }

    @FXML
    private void seleccionarElemento(MouseEvent event) {
        if(existeElementoSeleccionado()){
        cmbIdAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdAdministracion()));
        cmbCargos.getSelectionModel().select(buscarCargos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdCargo()));
        cmbHorario.getSelectionModel().select(buscarHorarios(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdHorario()));
        cmbIdDepartamento.getSelectionModel().select(buscarDepartamentos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdDepartamento()));
        txtId.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
        txtNombre.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidos());
        txtEmail.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmail());
        txtTelefono.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefono());
        txtSueldo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        drFechaContratacion.setValue(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getFechaContratacion().toLocalDate());
    }
    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> listado = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarEmpleados()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listado.add(new Empleados(rs.getInt("id"), rs.getString("nombres"),
                        rs.getString("apellidos"), rs.getString("email"), rs.getString("telefono"),
                        rs.getDate("fechaContratacion"), rs.getBigDecimal("sueldo"), rs.getInt("idDepartamento"),
                        rs.getInt("idCargo"), rs.getInt("idHorario"), rs.getInt("idAdministracion")));
            }

        } catch (Exception e) {
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
        listaEmpleados = FXCollections.observableArrayList(listado);
        return listaEmpleados;
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidos"));
        colFechadeContratacion.setCellValueFactory(new PropertyValueFactory<Empleados, Date>("fechaContratacion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Empleados, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefono"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, BigDecimal>("sueldo"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idAdministracion"));
        colIdCargos.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idCargo"));
        colIdDepartamento.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idDepartamento"));
        colIdHorario.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idHorario"));
        cmbIdAdministracion.setItems(getAdministracion());
        cmbCargos.setItems(getCargos());
        cmbHorario.setItems(getHorarios());
        cmbIdDepartamento.setItems(getDepartamentos());
        drFechaContratacion.setOpacity(1.0);
        cmbCargos.setOpacity(1.0);
        cmbHorario.setOpacity(1.0);
        cmbIdAdministracion.setOpacity(1.0);
        cmbIdDepartamento.setOpacity(1.0);
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }
        }
        listaCargos = FXCollections.observableArrayList(lista);
        return listaCargos;
    }

    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios()}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Horarios(rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaHorarios = FXCollections.observableArrayList(lista);
        return listaHorarios;
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

    private Departamentos buscarDepartamentos(int id) {
        Departamentos registro = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarDepartamentos(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Departamentos(rs.getInt("id"),
                        rs.getString("nombre"));
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
        return registro;
    }

    private Cargos buscarCargos(int id) {
        Cargos registro = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarCargos(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Cargos(rs.getInt("id"), rs.getString("nombre"));
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
        return registro;
    }

    private Horarios buscarHorarios(int id) {
        Horarios registro = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarHorarios(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes"));
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
        return registro;

    }

    public void agregarEmpleados() {
        PreparedStatement pstmt = null;
        Empleados registro = new Empleados();
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setEmail(txtEmail.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setFechaContratacion(Date.valueOf(drFechaContratacion.getValue()));
        registro.setSueldo(new BigDecimal(txtSueldo.getText()));
        registro.setIdDepartamento(((Departamentos) cmbIdDepartamento.getSelectionModel().getSelectedItem()).getId());
        registro.setIdCargo(((Cargos) cmbCargos.getSelectionModel().getSelectedItem()).getId());
        registro.setIdHorario(((Horarios) cmbHorario.getSelectionModel().getSelectedItem()).getId());
        registro.setIdAdministracion(((Administracion) cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarEmpleados(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, registro.getNombres());
            pstmt.setString(2, registro.getApellidos());
            pstmt.setString(3, registro.getEmail());
            pstmt.setString(4, registro.getTelefono());
            pstmt.setDate(5, registro.getFechaContratacion());
            pstmt.setBigDecimal(6, registro.getSueldo());
            pstmt.setInt(7, registro.getIdDepartamento());
            pstmt.setInt(8, registro.getIdCargo());
            pstmt.setInt(9, registro.getIdHorario());
            pstmt.setInt(10, registro.getIdAdministracion());
            pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox, ArrayList<JFXDatePicker> listaDatePicker) {
        boolean respuesta = true;
        for (JFXDatePicker jFXDatePicker : listaDatePicker) {
            if (jFXDatePicker.getValue() == null) {
                return false;
            }
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

    public boolean validarDatos(String numero) {

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(numero);

        return matcher.matches();
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

    public void editarEmpleados() {
        Empleados registro = new Empleados();
        registro.setId(Integer.valueOf(txtId.getText()));
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setEmail(txtEmail.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setFechaContratacion(Date.valueOf(drFechaContratacion.getValue()));
        registro.setSueldo(new BigDecimal(txtSueldo.getText()));
        registro.setIdDepartamento(((Departamentos) cmbIdDepartamento.getSelectionModel().getSelectedItem()).getId());
        registro.setIdCargo(((Cargos) cmbCargos.getSelectionModel().getSelectedItem()).getId());
        registro.setIdHorario(((Horarios) cmbHorario.getSelectionModel().getSelectedItem()).getId());
        registro.setIdAdministracion(((Administracion) cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarEmpleados(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setInt(1, registro.getId());
            pstmt.setString(2, registro.getNombres());
            pstmt.setString(3, registro.getApellidos());
            pstmt.setString(4, registro.getEmail());
            pstmt.setString(5, registro.getTelefono());
            pstmt.setDate(6, registro.getFechaContratacion());
            pstmt.setBigDecimal(7, registro.getSueldo());
            pstmt.setInt(8, registro.getIdDepartamento());
            pstmt.setInt(9, registro.getIdCargo());
            pstmt.setInt(10, registro.getIdHorario());
            pstmt.setInt(11, registro.getIdAdministracion());
            pstmt.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarEmpleados() {
        Empleados empleados = ((Empleados) tblEmpleados.getSelectionModel().getSelectedItem());
        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ELiminarEmpleados(?)}");
            pstmt.setInt(1, empleados.getId());
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
}
